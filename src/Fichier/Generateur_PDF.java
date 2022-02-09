package Fichier;

import Modele.LigneTicketCaisse;
import Modele.Paiement;
import Modele.TicketCaisse;
import java.io.FileOutputStream;
//import java.io.IOException;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import java.sql.Timestamp;

public class Generateur_PDF {
    
//    private static String FILE = "c:/temp/FirstPdf.pdf";
    private static TicketCaisse ticket;
    private static final Font remerciements = new Font(Font.FontFamily.COURIER, 16);
    private static final Font catFont = new Font(Font.FontFamily.COURIER, 11);
    private static final Font catFont2 = new Font(Font.FontFamily.COURIER, 12);
    private static final Font redFont = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL, BaseColor.RED);
    private static final Font subFont = new Font(Font.FontFamily.COURIER, 17, Font.BOLD);
    private static final Font smallBold = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
    private static final Font smallerBold = new Font(Font.FontFamily.COURIER, 12, Font.BOLDITALIC);
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
    private static final String dateString = format.format(new Date());
    private static final String enseigne = "LA CAVERNE D'ALIBABA";
    private static final String code_postal = "7000";
    private static final String localite = "MONS";
    private static final String rue = "Avenue du Jackpot 777";
    private static final String telephone = "069.65.54.87";
    private static final String tirets = "-----------------------------------------------------------------";
    private static String numero;
    private static String date;
    private static String employe;

    public static void generer(TicketCaisse t){
        try 
        {
            ticket = t;
            numero = String.valueOf(ticket.getId());
            date = format.format(ticket.getDateDeCreation());
            employe = String.valueOf(ticket.getlEmploye().getPrenom()); 

            String nomFichier = "temp/Ticket_" + ticket.getId() + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document);
            document.close(); 
            
            
            Desktop.getDesktop().open(new File(nomFichier));
            JOptionPane.showMessageDialog(null, "Le PDF a été généré avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la création du PDF !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } 
    }
    
    private static void addMetaData(Document document){
        String title = "Ticket_" + 0;
        document.addTitle(title);
//        document.addSubject("En utilisant iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Hotak");
        document.addCreator("Hotak");
    }
    
    private static void addTitlePage(Document document) throws DocumentException{
        Paragraph preface = new Paragraph();
       
        Paragraph a = new Paragraph(enseigne, subFont);
        a.setAlignment(Element.ALIGN_CENTER);
        Paragraph b = new Paragraph(rue, catFont);
        b.setAlignment(Element.ALIGN_CENTER);
        Paragraph c = new Paragraph(code_postal + " " + localite, catFont);
        c.setAlignment(Element.ALIGN_CENTER);
        Paragraph d = new Paragraph("Tél. : " + telephone, catFont);
        d.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(d, 1);
        Paragraph e = new Paragraph("NUMERO DE TICKET : " + numero, catFont2);
        e.setAlignment(Element.ALIGN_CENTER);
        Paragraph f = new Paragraph("DATE : " + date, catFont2);
        f.setAlignment(Element.ALIGN_CENTER);
        Paragraph g = new Paragraph("VOUS AVEZ ETE SERVI PAR : " + employe, catFont2); 
        g.setAlignment(Element.ALIGN_CENTER);
        Paragraph h = new Paragraph(tirets);
        h.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(h, 1);
        
        preface.add(a);
        preface.add(b);
        preface.add(c);
        preface.add(d);
        preface.add(e);
        preface.add(f);
        preface.add(g);
        preface.add(h);
        
        document.add(preface);

    }
    
    private static void addContent (Document document) throws DocumentException {
//        Anchor anchor = new Anchor("Premier chapitre", catFont);
//        anchor.setName("Premier chapitre");
//        
//        // Le deuxième paramètre est le numéro du chapitre
//        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
//        
//        Paragraph subPara = new Paragraph("Sous-catégorie 1", subFont);
//        Section subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("Bonjour"));
//        
//        subPara = new Paragraph("Sous-catégorie 2", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("Paragraphe 1"));
//        subCatPart.add(new Paragraph("Paragraphe 2"));
//        subCatPart.add(new Paragraph("Paragraphe 3"));
//        
//        // ajouter une liste
//        createList(subCatPart);
//        Paragraph paragraph = new Paragraph();
//        addEmptyLine(paragraph, 5);
//        subCatPart.add(paragraph);
        
        Paragraph contenu = new Paragraph();
        // ajouter un tableau
        createTable(contenu);
        
        // maintenant ajoutez tout cela au document
//        document.add(catPart);
//        
//        // section suivante
//        anchor = new Anchor("Deuxième chapitre", catFont);
//        anchor.setName("Deuxième chapitre");
//        
//        // Le deuxième paramètre est le numéro du chapitre
//        catPart = new Chapter(new Paragraph(anchor), 1);
//        
//        subPara = new Paragraph("Sous-catégorie", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("C'est un message très important"));
//        
//        // maintenant ajoutez tout cela au document
        document.add(contenu); 
    }
    
    private static void createTable (Paragraph p) throws BadElementException {

        Paragraph table = lignesListeEnTableau();
        p.add(table);
        Paragraph h = new Paragraph(); 
        addEmptyLine(p, 1);
        h = new Paragraph(tirets);
        h.setAlignment(Element.ALIGN_CENTER);
        
        p.add(h);
        
        addEmptyLine(p, 1);
        
        BigDecimal[] tab = new BigDecimal[3];
        tab[0] = new BigDecimal(0.0);
        tab[1] = new BigDecimal(0.0);
        tab[2] = new BigDecimal(0.0);
        String modeMontant = "";


        for (Paiement paiement : ticket.getListePaiements().getListePaiements()) {
            switch (paiement.getMode_paiement().getId()){
                case 1 : tab[0] = tab[0].add(paiement.getMontant()).setScale(2, RoundingMode.HALF_EVEN);
                    break;
                case 2 : tab[1] = tab[1].add(paiement.getMontant()).setScale(2, RoundingMode.HALF_EVEN);
                    break;
                case 3 : tab[2] = tab[2].add(paiement.getMontant()).setScale(2, RoundingMode.HALF_EVEN);
                    break;
            }
        }

        if (tab[0].compareTo(BigDecimal.ZERO) > 0) modeMontant += "Espèces : " + tab[0] + " €\n";
        if (tab[1].compareTo(BigDecimal.ZERO) > 0) modeMontant += "Bancontact : " + tab[1] + " €\n";
        if (tab[2].compareTo(BigDecimal.ZERO) > 0) modeMontant += "MasterCard/Visa : " + tab[2] + " €\n";
        
        BigDecimal tot = tab[0].add(tab[1].add(tab[2]));
        
        if (!tot.equals(ticket.getMontantTotal())) modeMontant += "\nMontant restant : " + ticket.getMontantTotal().subtract(tot).setScale(2, RoundingMode.HALF_EVEN);
        
        if (ticket.isStatutPaye()) {
            h = new Paragraph("Paiement effectué !\n" + modeMontant, catFont2);
        }else {
            h = new Paragraph("En attente de paiement ...\n" + modeMontant, catFont2);
        }
        h.setAlignment(Element.ALIGN_CENTER);
        
        p.add(h);
        
        addEmptyLine(p, 1);
        h = new Paragraph(tirets);
        h.setAlignment(Element.ALIGN_CENTER);
        
        p.add(h);
        
        if (ticket.isStatutPaye())
        {
            addEmptyLine(p, 1);
            h = new Paragraph("MERCI BEAUCOUP !", remerciements);
            h.setAlignment(Element.ALIGN_CENTER);
        
            p.add(h);
        }   
    }
    

    
    private static void addEmptyLine (Paragraph paragraph, int number){
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));          
        }
    } 
    
    private static Paragraph lignesListeEnTableau()
    {
        Paragraph tableau = new Paragraph();
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(85);
        PdfPCell c1;
        BigDecimal total = new BigDecimal(0.0);
                
        c1 = new PdfPCell(new Phrase("Quantité", smallerBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(8);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Nom", smallerBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(8);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Prix Unitaire", smallerBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(8);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Total", smallerBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(8);
        table.addCell(c1);
        table.setHeaderRows(1);
        
        for (LigneTicketCaisse ltc : ticket.getListeLigneTicketCaisse()) {
            BigDecimal qt = new BigDecimal(ltc.getQuantiteAchetee());
            BigDecimal pxq = (ltc.getPrixUnitaire().multiply(qt)).setScale(2, RoundingMode.HALF_EVEN);
            
            c1 = new PdfPCell(new Phrase("x" + String.valueOf(qt), catFont2));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding(3);
            table.addCell(c1);
         
            c1 = new PdfPCell(new Phrase(String.valueOf(ltc.getLeProduit().getNom()), catFont2));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding(3);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(String.valueOf(ltc.getPrixUnitaire().setScale(2, RoundingMode.HALF_EVEN)) + " €", catFont2));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding(3);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase((String.valueOf(pxq)) + " €", catFont2));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding(3);
            table.addCell(c1);
            
            total = total.add(pxq);
        }
        
        PdfPTable table2 = new PdfPTable(4);
        table2.setWidthPercentage(85);
        
        c1 = new PdfPCell(new Phrase());
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(3);
        table2.addCell(c1);
        
        c1 = new PdfPCell(new Phrase());
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(3);
        table2.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("TOTAL", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(3);
        table2.addCell(c1);
        
        c1 = new PdfPCell(new Phrase(String.valueOf(total) + " €", smallBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(3);
        table2.addCell(c1);
        
        tableau.add(table);
        addEmptyLine(tableau, 1);
        tableau.add(table2);

        return tableau;
    }
}
