package br.com.aligenigena.pdf2img.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;

@RestController
public class ImageController {

    @PostMapping("/convert-images")
    public ResponseEntity<Map<String, String>> convertImages(@RequestBody Map<String, String> images) {
        try {
            String img1Base64 = images.get("img1");
            String img2Base64 = images.get("img2");

            // bytes das imagens convertidas em png
            String img1Base64Final = getPngOfBase64(img1Base64);
            String img2Base64Final = getPngOfBase64(img2Base64);

            // base64 de retorno.
            Map<String, String> imagesResponse = new HashMap<>();
            imagesResponse.put("img1", img1Base64Final);
            imagesResponse.put("img2", img2Base64Final);

            return ResponseEntity.ok(imagesResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private String getPngOfBase64(String base64) throws IOException {
        var inicio = System.currentTimeMillis();
        if (isPdf(base64)) {
            System.out.println("getPngOfBase64");
            System.out.println(System.currentTimeMillis() - inicio);
            return convertToBase64(convertPdfToPng(base64));
        }

        return base64;
    }

    public static String convertToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    private byte[] convertPdfToPng(String base64Pdf) throws IOException {
        var inicio = System.currentTimeMillis();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Pdf);

        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(decodedBytes))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 300); // DPI pode ser ajustado conforme a necessidade

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(image, "jpg", baos);
                System.out.println("convertPdfToPng");
                System.out.println(System.currentTimeMillis() - inicio);
                return baos.toByteArray();
            }
        }
    }

    private boolean isPdf(String base64Data) {
        byte[] data = Base64.getDecoder().decode(base64Data);
        try (InputStream is = new ByteArrayInputStream(data)) {
            return is.read() == '%' && is.read() == 'P' && is.read() == 'D' && is.read() == 'F';
        } catch (Exception e) {
            return false;
        }
    }
}

