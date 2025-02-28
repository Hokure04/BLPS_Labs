package org.example.blps_lab1.export.certificate;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CertificateExporter {


    public File generateCertificate(String courseName, String userName, String signaturePath) {
        try {
            Path tempFile = Files.createTempFile("certificate_", ".pdf");
            File file = tempFile.toFile();
            
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            // Document document = new Document(pdf, PageSize.A4);
            Document document = new Document(pdf, PageSize.A4.rotate()); 


            document.add(new Paragraph("Congratulations on completing the course on our platform, " + userName + "!")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.BLUE)
            );
            
            document.add(new Paragraph("Course: " + courseName)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
            );
            
            document.add(new Paragraph("We look forward to seeing you again on our platform!")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
            );
            
            File signatureFile = new File(signaturePath);
            if (signatureFile.exists()) {
                ImageData imageData = ImageDataFactory.create(signaturePath);
                Image signature = new Image(imageData);
                signature.setWidth(200);
                signature.setHeight(50);
                signature.setFixedPosition(PageSize.A4.getWidth() / 2 + 150, 150); 
                document.add(signature);
            }
            
            document.close();
            return file;
        } catch (IOException e) {
            log.error("Error while generating the certificate", e);
            return null;
        }
    }
}