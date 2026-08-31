package com.cws.shop.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.dto.request.ContactDTO;
import com.cws.shop.service.EmailService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/contact")
@CrossOrigin("*")
public class ContactController {
	
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendContact(
            @Valid @RequestBody ContactDTO contactDTO) {

        emailService.sendContactUsEmail(contactDTO);

        return ResponseEntity.ok("Message Sent Successfully");
    }

}
