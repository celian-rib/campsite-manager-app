package pt4.flotsblancs.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.text.StringEscapeUtils;
import pt4.flotsblancs.database.model.Reservation;

public class MailSender {

    static Properties props;
    static Session session;
    static Dotenv dotenv;
    static final String FALLBACK_EMAIL = "camping.les.flots.blancs@gmail.com";

    static Properties createProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }

    static Session createSession() {
        props = props == null ? createProperties() : props;
        dotenv = Dotenv.load();
        Session newS = Session.getInstance(
                props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                dotenv.get("MAIL_USERNAME", FALLBACK_EMAIL),
                                dotenv.get("MAIL_PASSWORD"));
                    }
                });
        return newS;
    }

    public static void sendMail(Reservation reservation) throws MessagingException {
        session = session == null ? createSession() : session;
        if (reservation.getBill() == null) {
            System.out.println(
                    "Impossible d'ouvrir le fichier de facture une réservation sans facture");
            return;
        }

        File outputFile = getFactureFile(reservation);

            Multipart multipart = new MimeMultipart();

            Message msg = createMimeMessage(reservation);

            MimeBodyPart content = writeContent(reservation);

            MimeBodyPart logo = inlineLogo();
            MimeBodyPart facturePJ = attachBill(outputFile);

            multipart.addBodyPart(content);
            multipart.addBodyPart(logo);
            multipart.addBodyPart(facturePJ);

            msg.setContent(multipart);

            Transport.send(msg);
    }

    private static MimeBodyPart inlineLogo() {
        try {
            MimeBodyPart imgBodyPart = new MimeBodyPart();
            imgBodyPart.setFileName("logo_dark.png");
            DataSource logoDS = new FileDataSource(
                    "src/main/resources/logo_dark.png");
            imgBodyPart.setDataHandler(new DataHandler(logoDS));
            imgBodyPart.setHeader("Content-ID", "<logo>");
            imgBodyPart.setDisposition(MimeBodyPart.INLINE);
            return imgBodyPart;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static MimeBodyPart attachBill(File outputFile) {
        try {
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(outputFile);
            return attachmentBodyPart;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static MimeBodyPart writeContent(Reservation reservation) {
        String resIDStr = StringEscapeUtils.escapeHtml4(
                "Réservation #" + reservation.getId());
        String helloStr = StringEscapeUtils.escapeHtml4(
                "Bonjour " +
                        reservation.getClient().getDisplayName() +
                        ", merci d'avoir réservé au camping Les Flots Blancs");
        String seeAttachment = StringEscapeUtils.escapeHtml4(
                "Vous trouverez votre facture en pièce-jointe.");

        String msgStyled = "<div style=\"display:flex;flex-direction:column;align-items:center;background-color:#F4F4F4;padding:50px;font-family:sans-serif;min-height:800px\">"
                +
                "<img src=\"cid:logo\" alt=\"logo les flots blancs\" style=\"height:150px;\"/>" +
                "<h1 style=\"color:#333B61;\">Les Flots Blancs</h1>" +
                "<h2 style=\"color:#333B61;\">" +
                resIDStr +
                "</h2>" +
                "<p style=\"color:#333B61;\">" +
                helloStr +
                "<b>Les Flots Blancs</b></p>" +
                "<p style=\"color:#333B61;font-style:italic;\">" +
                seeAttachment +
                "</p>" +
                "</div>";

        MimeBodyPart content = new MimeBodyPart();
        try {
            content.setContent(msgStyled, "text/html");
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Message createMimeMessage(Reservation reservation) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(
                    new InternetAddress(
                            dotenv.get("MAIL_USERNAME", FALLBACK_EMAIL),
                            "Camping Les Flots Blancs"));

            message.addRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(reservation.getClient().getEmail()));
            if (dotenv.get("MAIL_CC", null) != null) {
                message.addRecipients(
                        Message.RecipientType.CC,
                        InternetAddress.parse(dotenv.get("MAIL_CC")));
            }
            message.setSubject(
                    "Facture #" + reservation.getId() + " - Les Flots Blancs");
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static File getFactureFile(Reservation reservation) {
        var fileName = "facture_" + reservation.getClient().getName() + "_" + reservation.getId() + ".pdf";
        var home = FileSystemView.getFileSystemView().getDefaultDirectory();

        File outputFile = new File(home.getPath() + "/" + fileName);

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(reservation.getBill());
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
