package com.cws.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.ContactMessage;



public interface ContactRepository  extends JpaRepository<ContactMessage, Long>{
	
}




