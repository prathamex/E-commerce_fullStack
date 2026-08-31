package com.cws.shop.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.PasswordResetLimit;

public interface PasswordResetLimitRepository extends JpaRepository<PasswordResetLimit, Long>{
	Optional<PasswordResetLimit>
    findByEmailAndResetDate(String email, LocalDate resetDate);

}
