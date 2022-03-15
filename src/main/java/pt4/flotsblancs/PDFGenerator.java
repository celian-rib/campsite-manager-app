package pt4.flotsblancs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
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
import pt4.flotsblancs.database.model.ConstraintException;
import pt4.flotsblancs.database.model.Reservation;

public class PDFGenerator {

    private static String FILE = "facture.pdf";

    public static void main(String args[]) throws DocumentException, MalformedURLException,
            IOException, SQLException, ConstraintException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
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

        var client = new Client();
        client.setAddresse("15 rue Naudet, Gradignan, 33170");
        client.setFirstName("Michel");
        client.setName("Soulas");
        client.setPhone("+33 07 69 66 65 41");
        client.setEmail("unemail.super@gmail.com");
        client.setPreferences("Camping car avec famille");

        var reservation = new Reservation(client);
        reservation.setNbPersons(4);

        document.add(createClientTable(client));
        document.add(createReservationTable(reservation));
        document.add(createPriceTable(reservation));
        document.close();
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

        table.addCell(createCell("Prix acompte : "));
        table.addCell(createCell(reservation.getDepositPrice() + "€"));

        table.addCell(createCell("Prix services (jours) : "));
        table.addCell(createCell(reservation.getSelectedServices().getPricePerDay() + "€"));

        table.addCell(createCell("Prix emplacement (jours) : "));
        table.addCell(createCell(reservation.getCampground().getPricePerDays() + "€"));

        var font = FontFactory.getFont("Arial", 12, Font.BOLD);
        table.addCell(new PdfPCell(new Paragraph("Prix total", font)));
        table.addCell(new PdfPCell(new Paragraph(reservation.getTotalPrice() + "€", font)));

        return table;
    }
}
