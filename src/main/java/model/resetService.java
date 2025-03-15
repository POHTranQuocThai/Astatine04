/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class resetService {

    private final int LIMIT_MINUS = 10;
    static final String from = "locma2004@gmail.com";
    static final String password = "irru rtqu fkhd dazt";

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(LIMIT_MINUS);
    }

    public boolean isExpireTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    public boolean sendEmail(String to, String link, String name) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        MimeMessage msg = new MimeMessage(session);

        try {
            msg.addHeader("Content-type", "text/html; charset=UTF-8");
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("Reset Password", "UTF-8");
            String content = "<!DOCTYPE html>"
                    + "<html lang='vi'>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Đặt lại mật khẩu</title>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }"
                    + ".email-container { max-width: 500px; background: #fff; padding: 20px; margin: 30px auto; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }"
                    + "h2 { color: #333; text-align: center; }"
                    + "p { font-size: 16px; color: #555; text-align: center;}"
                    + ".btn { display: block; background: #21A691; color: #fff !important; text-align: center; padding: 16px; border-radius: 8px; text-decoration: none; font-size: 16px; font-weight: bold; }"
                    + ".btn:hover { background: #fff !important; color: #21A691 !important; border: 0.2px solid #21A691 !important}"
                    + ".footer { text-align: center; font-size: 12px; color: #777; margin-top: 20px; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='email-container'>"
                    + "<img src='https://drive.google.com/uc?export=view&id=1XICuRbb6ddxISheuuZZWPHpmbMMd0Y3M' alt='logo' width='150' style='display:block;margin: 0 auto;'/>"
                    + "<h2>Hello, " + name + "!</h2>"
                    + "<p>You have requested a password reset. Click the button below to do so:</p>"
                    + "<a href='" + link + "' class='btn'>Reset your Password</a>"
                    + "<div class='footer'>"
                    + "<p>&copy; 2025 All rights reserved | This template is made with Astatine04.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            msg.setContent(content, "text/html; charset=UTF-8");
            Transport.send(msg);
            System.out.println("Send successfully");
            return true;
        } catch (Exception e) {
            System.out.println("Send error");
            System.out.println(e);
            return false;
        }
    }
}
