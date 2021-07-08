package pl.marczuk.bookstore.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
