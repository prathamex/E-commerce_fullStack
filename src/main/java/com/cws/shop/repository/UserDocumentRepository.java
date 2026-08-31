package com.cws.shop.repository;

import com.cws.shop.model.User;
import com.cws.shop.model.UserDocumentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDocumentRepository extends JpaRepository<UserDocumentMapping,Long> {
    List<UserDocumentMapping> findByUser(User user);
}
