package br.com.aligenigena.pdf2img.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testConvertImages() {
        // Configurar os dados de teste
        Map<String, String> requestBody = new HashMap<>();
        // pdf
        requestBody.put("img1", "put_your_base_64_pdf");

        //png
        requestBody.put("img2", "put_your_base_64_pdf_or_png");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Enviar a requisição e obter a resposta
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/convert-images", request, String.class);

        // Verificar o resultado
        assertEquals(200, response.getStatusCodeValue());
        // Adicione mais asserções conforme necessário para verificar a resposta
    }
}
