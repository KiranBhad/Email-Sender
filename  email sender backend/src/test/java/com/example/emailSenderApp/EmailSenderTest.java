package com.example.emailSenderApp;

import helper.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import com.example.emailSenderApp.services.EmailService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private EmailService emailService;

    @Test
    void emailSendTest() {
        System.out.println("Sending email");
        // Here you might want to actually call the sendEmail method to test its behavior
        emailService.sendEmail("striverkb1804@gmail.com", "regarding cricket","Hey Kiran Bhad , You are selected for indian team as an opener batsmen,best luck for your future.");
    }


    @Test
    void sendEmailWithHtml() {
        String html = "" +
                "<h1 style='color:red; border:2px solid red;'>This message is for testing purpose only for spring boot application</h1>";
        emailService.sendEmailWithHtml("striverkb1804@gmail.com", "Spring Boot Message", html);
    }

    @Test
    void sendEmailWithFileWithStream(){

        File file = new File("/home/kiran/Desktop/SpringBoot/emailSenderApp/src/main/resources/bappa.jpg");
        try {
            InputStream  is = new FileInputStream(file);

            emailService.sendEmailWithFile("striverkb1804@gmail.com",
                    "spring boot application",
                    "This email  contains file",is
                    );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void getInbox(){
        List<Message> inboxMessages = emailService.getInboxMessages();
        inboxMessages.forEach(item->{
            System.out.println(item.getSubjects());
//            System.out.println(item.getContent());
//            System.out.println(item.getFiles());
            System.out.println("-------------------------------" +
                    "" +
                    "" +
                    "");
        });
    }
}
