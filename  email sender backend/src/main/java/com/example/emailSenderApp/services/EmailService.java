package com.example.emailSenderApp.services;

import helper.Message;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface EmailService {

    // void send email to single person
    void sendEmail(String to, String subject, String message);

    // void send email to multiple person
    void sendEmail(String[] to, String subject, String message);

    // send email with HTML
    void sendEmailWithHtml(String to, String subject, String htmlContent);

    // send email with file
    void sendEmailWithFile(String to, String subject, String message, File file);

    void sendEmailWithFile(String to, String subject, String message, InputStream is);


    List<Message > getInboxMessages();
}
