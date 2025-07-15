package lightning_productivity;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;

public class PngToPdfConverter {

    public static void convertToPdf(File pngFile, File pdfFile) throws IOException {
        float widthInches = 4.25f;
        float heightInches = 6.0f;

        float widthPoints = widthInches * 72f;
        float heightPoints = heightInches * 72f;

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(widthPoints, heightPoints));
            doc.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromFileByContent(pngFile, doc);

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                contentStream.drawImage(pdImage, 0, 0, widthPoints, heightPoints);
            }

            doc.save(pdfFile);
        }
    }
}
