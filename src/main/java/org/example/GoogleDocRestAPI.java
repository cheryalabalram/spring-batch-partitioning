package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;

@SpringBootApplication
@Slf4j
public class GoogleDocRestAPI {

    @RestController
    public class InnerCtrl{

        @Autowired
        ProcessDocument processDocument;

        @GetMapping(value = "processJob")
        @SneakyThrows
//        public void actualJob(@RequestParam MultipartFile file){
        public void actualJob(){
            //FIXME - Direct
//            byte[] bytes = file.getInputStream().readAllBytes();
            String document = "C:\\Users\\balra\\Downloads\\invoice.pdf";

            // Get the file name
//            String fileName = file.getName();
//            log.info("File name - {}", fileName);

            // Get the MIME type based on the last string
//            String mimeType = Files.probeContentType(file.getResource().getFile().toPath());

            // Print the MIME type
//            System.out.println(mimeType);

            String mimeType = "application/pdf"; // Disable if you enable the MultiPartFile
            processDocument.processDocument(document, mimeType);
//            processDocument.processDocument(document);// Change with bytes
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GoogleDocRestAPI.class);
    }
}