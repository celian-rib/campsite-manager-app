package pt4.flotsblancs.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import javax.swing.filechooser.FileSystemView;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import pt4.flotsblancs.database.model.Client;
import pt4.flotsblancs.database.model.Reservation;
import pt4.flotsblancs.scenes.utils.PriceUtils;

import java.awt.Desktop;

public class PDFGenerator {

    /**
     * Télécharge la facture de la réservation donnée (Si il y en a une) et l'ouvre
     * via
     * l'explorateur de fichier de l'os La facture est stocké dans le répertoire
     * d'accueil de
     * l'ordinateur
     * 
     * @param reservation réservation contenant la facture à ouvrir
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void openFile(Reservation reservation) throws FileNotFoundException, IOException {
        if (reservation.getBill() == null) {
            System.out.println(
                    "Impossible d'ouvrir le fichier de facture une réservation sans facture");
            return;
        }

        System.out.println("Exportation PDF : " + reservation);

        var fileName = "facture_" + reservation.getClient().getName() + "_" + reservation.getId() + ".pdf";
        var home = FileSystemView.getFileSystemView().getDefaultDirectory();

        File outputFile = new File(home.getPath() + "/" + fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(reservation.getBill());
        }

        System.out.println("Ouverture PDF : " + reservation);
        Desktop desktop = Desktop.getDesktop();
        desktop.open(outputFile);
    }

    /**
     * Permet de générer une facture à partir d'une réservation
     * 
     * @param reservation réservation
     * @return Stream de byte correspondant au fichier PDF de la facture
     * @throws SQLException
     * @throws DocumentException
     * @throws MalformedURLException
     * @throws IOException
     */
    public static ByteArrayOutputStream generateReservationBillPDF(Reservation reservation)
            throws SQLException, DocumentException, MalformedURLException, IOException {
        System.out.println("Génération PDF : " + reservation);

        Document document = new Document();

        var outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);
        document.open();

        Image img = Image.getInstance("src/main/resources/logo_dark.png");
        img.scaleAbsolute(40, 40);
        img.setAlignment(Element.ALIGN_CENTER);
        document.add(img);

        var title = new Paragraph("Camping des Flots Blancs");
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        var line = new Chunk(new LineSeparator());
        document.add(line);

        var paragraph = new Paragraph(
                "Cher(e) Client(e),\n Nous avons le plaisir de vous adresser votre facture acquitée. Veuillez vérifier l'exactitude des élèments du dossier. En cas de désaccord, contactez-nous à l'adresse indiquée sur ce document.");
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setIndentationLeft(50);
        paragraph.setIndentationRight(50);
        document.add(paragraph);

        document.add(line);

        document.add(createClientTable(reservation.getClient()));
        document.add(createReservationTable(reservation));
        document.add(createPriceTable(reservation));
        document.add(line);
        document.add(createFooter());
        document.close();

        System.out.println("PDF généré");
        MailSender.createSession();
        return outputStream;
    }

    private static PdfPCell createCell(String content) {
        var c = new PdfPCell(new Paragraph(content));
        c.setBorder(2);
        return c;
    }

    private static PdfPTable createClientTable(Client client) {
        PdfPTable table = new PdfPTable(2);
        table.addCell(createCell("Nom client : "));
        table.addCell(createCell(client.getName()));

        table.addCell(createCell("Prénom client : "));
        table.addCell(createCell(client.getFirstName()));

        table.addCell(createCell("Adresse : "));
        table.addCell(createCell(client.getAddresse()));

        table.addCell(createCell("Tel : "));
        table.addCell(createCell(client.getPhone()));

        table.addCell(createCell("Email : "));
        table.addCell(createCell(client.getEmail()));
        table.setSpacingAfter(40);

        return table;
    }

    private static PdfPTable createReservationTable(Reservation reservation) {
        PdfPTable table = new PdfPTable(2);

        table.addCell(createCell("Date de début : "));
        table.addCell(createCell(reservation.getStartDate().toString()));

        table.addCell(createCell("Date de fin : "));
        table.addCell(createCell(reservation.getEndDate().toString()));

        table.addCell(createCell("Nombre de jours facturés"));
        table.addCell(createCell(reservation.getDayCount() + ""));

        table.addCell(createCell("Equipements"));
        table.addCell(createCell(reservation.getEquipments().getName()));

        table.addCell(createCell("Services"));
        table.addCell(createCell(reservation.getSelectedServices().getName()));

        table.addCell(createCell("Nombre de personnes"));
        table.addCell(createCell(reservation.getNbPersons() + ""));

        table.addCell(createCell("Emplacement"));
        table.addCell(createCell("#" + reservation.getCampground().getId()));
        table.setSpacingAfter(40);

        return table;
    }

    private static PdfPTable createPriceTable(Reservation reservation) {
        PdfPTable table = new PdfPTable(2);

        table.addCell(createCell("Prix services (jours) : "));
        table.addCell(createCell(PriceUtils.priceToString(reservation.getSelectedServices().getPricePerDay()) + "€"));

        table.addCell(createCell("Prix emplacement (jours) : "));
        table.addCell(createCell(PriceUtils.priceToString(reservation.getCampground().getPricePerDays()) + "€"));

        table.addCell(createCell("Prix acompte : "));
        table.addCell(createCell(PriceUtils.priceToString(reservation.getDepositPrice()) + "€"));

        table.addCell(createCell("Prix payé après acompte : "));
        table.addCell(createCell(
                PriceUtils.priceToString(reservation.getTotalPrice() - reservation.getDepositPrice()) + "€"));

        var font = FontFactory.getFont("Arial", 12, Font.BOLD);
        table.addCell(new PdfPCell(new Paragraph("Prix TTC", font)));
        table.addCell(new PdfPCell(new Paragraph(PriceUtils.priceToString(reservation.getTotalPrice()) + "€", font)));

        return table;
    }

    private static Paragraph createFooter() {

        var paragraph = new Paragraph(
                "Numéro de téléphone : 05 55 55 55 55 \nAdresse de facturation : 1 Ruben de la plage 130008\nEmail : campoforever@campo.fr\nIBAN : FR12 1234 5678\nSWIFT/BIC : ABCDFRP1XXX");
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setIndentationLeft(50);
        paragraph.setIndentationRight(50);
        paragraph.setSpacingBefore(20);
        paragraph.setSpacingAfter(20);

        return paragraph;
    }
}
