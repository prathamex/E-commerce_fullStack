package com.cws.shop.repository;

import java.util.List;

import java.util.Optional;

import com.cws.shop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cws.shop.model.User;
import com.cws.shop.model.UserStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String string);

	Optional<User> findByEmail(String email);
	
	List<User> findByStatus(UserStatus status);

    Optional<User> findByIdAndRole(Long id , Role role);
    
    //kajal added
//    @Query("SELECT u FROM User u WHERE " +
//    	       "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//    	       "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    	List<User> searchUsers(@Param("keyword") String keyword);
    
    @Query("""
    	    SELECT u
    	    FROM User u
    	    WHERE u.role <> com.cws.shop.model.Role.ADMIN
    	      AND (
    	            LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	         OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	      )
    	    """)
    List<User> searchUsers(@Param("keyword") String keyword);
    

    List<User> findByDeletedFalse();
    
    
  //pratik added 29-05-26
    List<User> findByStatusAndDeletedFalse(UserStatus status);

    //For "Assign to user" drop down
	List<User> findByRole(Role role);
	
    List<User> findByNameContainingIgnoreCaseAndRole(String keyword, Role role);
}
