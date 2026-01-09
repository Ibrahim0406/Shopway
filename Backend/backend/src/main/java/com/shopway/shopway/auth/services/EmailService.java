package com.shopway.shopway.auth.services;

import com.shopway.shopway.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/*
 * Servis za slanje email poruka korisnicima.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /*
     * Šalje email sa verifikacionim kodom korisniku nakon registracije.
     * Email sadrži personalizovanu poruku sa verifikacionim kodom.
     *
     * @param user User objekat koji sadrži email adresu i verifikacioni kod
     * @return "Email sent" ako je uspešno poslato, "Error while Sending Mail" ako je došlo do greške
     */
    public String sendMail(User user){
        String subject = "Verify your email";
        String senderName = "ShopWay Team";
        String mailContent = "Hello " + user.getUsername() + ",\n";
        mailContent += "Your verification code is: " + user.getVerificationCode() + "\n";
        mailContent += "Please enter this code to verify your email.";
        mailContent +="\n";
        mailContent+= senderName;

        try{
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getEmail());
            mailMessage.setText(mailContent);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
        }
        catch (Exception e){
            return "Error while Sending Mail";
        }
        return "Email sent";
    }

}
