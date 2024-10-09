package com.example.emailSenderApp.services.Impl;

import com.example.emailSenderApp.services.EmailService;
import helper.Message;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${mail.store.protocol}")
    private String protocol;

    @Value("${mail.imap.host}")
    private String host;

    @Value("${mail.imaps.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private Session session;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Initialize Session once after bean creation
    @PostConstruct
    public void initialize() {
        Properties configurations = new Properties();
        configurations.setProperty("mail.store.protocol", protocol);
        configurations.setProperty("mail.imap.host", host);
        configurations.setProperty("mail.imap.port", port);
        configurations.setProperty("mail.imap.ssl.enable", "true"); // Enable SSL if required

        session = Session.getInstance(configurations);
        logger.info("Mail session initialized successfully.");
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("bhadkiran1804@gmail.com");

        logger.info("Sending simple email to {}", to);
        mailSender.send(simpleMailMessage);
        logger.info("Email sent successfully.");
    }

    @Override
    public void sendEmail(String[] to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("bhadkiran1804@gmail.com");

        logger.info("Sending simple email to multiple recipients.");
        mailSender.send(simpleMailMessage);
        logger.info("Email sent successfully.");
    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("bhadkiran1804@gmail.com");
            helper.setText(htmlContent, true);

            logger.info("Sending HTML email to {}", to);
            mailSender.send(mimeMessage);
            logger.info("HTML Email sent successfully.");
        } catch (MessagingException e) {
            logger.error("Error sending HTML email to {}", to, e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    public void sendEmailWithFile(String to, String subject, String message, File file) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("bhadkiran1804@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);

            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(), file);

            logger.info("Sending email with attachment to {}", to);
            mailSender.send(mimeMessage);
            logger.info("Email with attachment sent successfully.");
        } catch (MessagingException e) {
            logger.error("Error sending email with attachment to {}", to, e);
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }

    @Override
    public void sendEmailWithFile(String to, String subject, String message, InputStream is) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("bhadkiran1804@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);

            // Define the directory to save attachments
            String attachmentDirPath = "src/main/resources/email/";
            File attachmentDir = new File(attachmentDirPath);
            if (!attachmentDir.exists()) {
                boolean dirsCreated = attachmentDir.mkdirs();
                if (!dirsCreated) {
                    throw new IOException("Failed to create directories for attachments.");
                }
            }

            // Create a temporary file for the attachment
            File tempFile = File.createTempFile("attachment_", ".tmp", attachmentDir);
            Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FileSystemResource fileSystemResource = new FileSystemResource(tempFile);
            helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);

            logger.info("Sending email with attachment to {}", to);
            mailSender.send(mimeMessage);
            logger.info("Email with attachment sent successfully.");
        } catch (MessagingException | IOException e) {
            logger.error("Error sending email with file input stream to {}", to, e);
            throw new RuntimeException("Failed to send email with file input stream", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                    logger.info("InputStream closed successfully.");
                }
            } catch (IOException e) {
                logger.error("Error closing InputStream", e);
            }
        }
    }

    @Override
    public List<Message> getInboxMessages() {
        List<Message> messagesList = new ArrayList<>();
        Store store = null;
        Folder inbox = null;

        try {
            store = session.getStore("imap");
            store.connect(username, password);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            jakarta.mail.Message[] messages = inbox.getMessages();
            logger.info("Number of messages in inbox: {}", messages.length);

            for (jakarta.mail.Message message : messages) {
                String content = getContentFromEmailMessage(message);
                List<String> files = getFilesFromEmailMessage(message);
                messagesList.add(Message.builder()
                        .subjects(message.getSubject())
                        .content(content)
                        .files(files)
                        .build());
            }

            logger.info("Fetched {} messages from inbox.", messagesList.size());
        } catch (MessagingException | IOException e) {
            logger.error("Error fetching inbox messages", e);
            throw new RuntimeException("Failed to fetch inbox messages", e);
        } finally {
            // Ensure resources are closed to prevent leaks
            if (inbox != null && inbox.isOpen()) {
                try {
                    inbox.close(false);
                } catch (MessagingException e) {
                    logger.warn("Failed to close inbox folder", e);
                }
            }
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    logger.warn("Failed to close mail store", e);
                }
            }
        }

        return messagesList;
    }

    private List<String> getFilesFromEmailMessage(jakarta.mail.Message message) throws MessagingException, IOException {
        List<String> files = new ArrayList<>();
        if (message.isMimeType("multipart/*")) {
            Multipart content = (Multipart) message.getContent();
            for (int i = 0; i < content.getCount(); i++) {
                BodyPart bodyPart = content.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
                        (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {

                    // Define the directory to save attachments
                    String attachmentDirPath = "src/main/resources/email/";
                    File attachmentDir = new File(attachmentDirPath);
                    if (!attachmentDir.exists()) {
                        boolean dirsCreated = attachmentDir.mkdirs();
                        if (!dirsCreated) {
                            throw new IOException("Failed to create directories for attachments.");
                        }
                    }

                    // Clean filename to prevent path traversal attacks
                    String fileName = new File(bodyPart.getFileName()).getName();
                    File file = new File(attachmentDir, fileName);

                    try (InputStream inputStream = bodyPart.getInputStream()) {
                        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        files.add(file.getAbsolutePath());
                        logger.info("Saved attachment: {}", file.getAbsolutePath());
                    } catch (IOException e) {
                        logger.error("Error saving attachment: {}", file.getAbsolutePath(), e);
                        // Continue processing other attachments
                    }
                }
            }
        }
        return files;
    }

    private String getContentFromEmailMessage(jakarta.mail.Message message) throws MessagingException, IOException {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if (message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.isMimeType("multipart/*")) {
            StringBuilder result = new StringBuilder();
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    result.append(bodyPart.getContent());
                } else if (bodyPart.isMimeType("text/html")) {
                    result.append(bodyPart.getContent());
                }
                // You can add more conditions to handle other MIME types if needed
            }
            return result.toString();
        }
        return "";
    }
}
