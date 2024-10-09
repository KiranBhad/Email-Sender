package com.example.emailSenderApp;

import com.example.emailSenderApp.services.EmailService;
import helper.CustomResponse;
import helper.EmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test")
    public void Test(){
        System.out.println("Running ");
    }

    @PostMapping("/send/email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmailWithHtml(request.getTo(), request.getSubject(), request.getMessage());

        return ResponseEntity.ok(
                CustomResponse.builder().message("Email sent..").httpStatus(HttpStatus.OK).success(true).build()
        );
    }

    @PostMapping("/send-with-file")
    public ResponseEntity<CustomResponse> sendWithFile(@RequestPart EmailRequest request, @RequestPart MultipartFile file) throws IOException {
        emailService.sendEmailWithFile(request.getTo(), request.getSubject(), request.getMessage(), file.getInputStream());
        return ResponseEntity.ok(
                CustomResponse.builder().message("Email sent..").httpStatus(HttpStatus.OK).success(true).build()
        );

    }
}


