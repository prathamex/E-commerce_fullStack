package com.cws.shop.serviceImpl;

import java.time.LocalDate;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.request.ContactDTO;
import com.cws.shop.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String userName, String verificationLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("pratik.cws1@gmail.com");
            message.setTo(toEmail);
            System.out.println(toEmail);
            message.setSubject("Verify Your Email");
            message.setText("Hello " + userName + "\n" + verificationLink);

            mailSender.send(message);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendResetPasswordEmail(
            String toEmail,
            String userName,
            String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Reset Password");
        message.setFrom("pratik.cws1@gmail.com");

        message.setText(
                "Hello " + userName + ",\n\n"
              + "Click below link to reset password:\n\n"
              + resetLink
        );

        mailSender.send(message);
    }

    @Override
    public void sendLicenseEmail(String toEmail, String customerName, String productName, 
    							String licenseKey, String licenseType, String licensePlan, 
    							LocalDate activationDate, LocalDate expiryDate, Integer activationLimit) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("pratik.cws1@gmail.com"); //Used this address cause it's configured in application.properties
            message.setTo(toEmail);
            message.setSubject("Your License Has Been Generated");
            message.setText(
                    "Hello " + customerName + ",\n\n"
                  + "Your license has been generated successfully.\n\n"
                  + "Product: " + productName + "\n"
                  + "License Key: " + licenseKey + "\n"
                  + "License Type: " + licenseType + "\n"
                  + "License Plan: " + licensePlan + "\n"
                  + "Activation Date: " + activationDate + "\n"
                  + "Expiry Date: " + expiryDate + "\n"
                  + "Number of Devices Allowed: " + activationLimit + "\n\n"
                  + "Please keep this license key safe.\n\n"
                  + "Regards,\n"
                  + "CWS Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Added by harshada
    @Override
    public void sendContactUsEmail(ContactDTO contactDTO) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("pratik.cws1@gmail.com");

            message.setTo("pratik.cws1@gmail.com");

            message.setSubject("New Contact Us Message");

            message.setText(
                    "First Name : " + contactDTO.getFirstName()
                    + "\nLast Name : " + contactDTO.getLastName()
                    + "\nEmail : " + contactDTO.getEmail()
                    + "\nMobile : " + contactDTO.getMobile()
                    + "\nMessage : " + contactDTO.getMessage()
            );

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	

}
