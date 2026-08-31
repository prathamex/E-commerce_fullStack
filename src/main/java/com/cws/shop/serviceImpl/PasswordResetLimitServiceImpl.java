package com.cws.shop.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cws.shop.exception.ResetLimitExceededException;
import com.cws.shop.model.PasswordResetLimit;
import com.cws.shop.repository.PasswordResetLimitRepository;
import com.cws.shop.service.PasswordResetLimitService;

@Service
public class PasswordResetLimitServiceImpl implements PasswordResetLimitService{
	
	 private PasswordResetLimitRepository repository;
	 private static final int MAX_RESET_LIMIT = 3;
	 
	 

	public PasswordResetLimitServiceImpl(PasswordResetLimitRepository repository) {
		this.repository = repository;
	}

	@Override
    public void validateResetLimit(String email) {

        LocalDate today = LocalDate.now();

        Optional<PasswordResetLimit> optional =
                repository.findByEmailAndResetDate(email, today);

        if (optional.isPresent()) {

            PasswordResetLimit limit = optional.get();

            if (limit.getResetCount() >= MAX_RESET_LIMIT) {

                throw new ResetLimitExceededException(
                        "You have reached the maximum password reset requests. Please try again after 24 hours.");
            }
        }
    }

    @Override
    public void incrementResetCount(String email) {

        LocalDate today = LocalDate.now();

        Optional<PasswordResetLimit> optional =
                repository.findByEmailAndResetDate(email, today);

        if (optional.isPresent()) {

            PasswordResetLimit limit = optional.get();

            limit.setResetCount(limit.getResetCount() + 1);

            repository.save(limit);

        } else {

            PasswordResetLimit limit = new PasswordResetLimit();

            limit.setEmail(email);
            limit.setResetDate(today);
            limit.setResetCount(1);

            repository.save(limit);
        }
    }
	}


