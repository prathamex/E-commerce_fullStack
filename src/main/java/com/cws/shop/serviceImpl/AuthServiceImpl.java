package com.cws.shop.serviceImpl;

import java.time.LocalDateTime;

import java.util.UUID;

import com.cws.shop.dto.request.ForgotPassRequestDto;
import com.cws.shop.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cws.shop.dto.request.CreateUserRequestDto;
import com.cws.shop.dto.request.LoginRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.LoginResponse;
import com.cws.shop.exception.InvalidTokenException;
import com.cws.shop.exception.TokenAlreadyUsedException;
import com.cws.shop.exception.TokenExpiredException;
import com.cws.shop.exception.UnauthorizedException;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.Role;
import com.cws.shop.model.Token;
import com.cws.shop.model.TokenType;
import com.cws.shop.model.User;
import com.cws.shop.model.UserStatus;
import com.cws.shop.repository.TokenRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.security.JwtService;
import com.cws.shop.service.AuthService;
import com.cws.shop.service.EmailService;
import com.cws.shop.service.NotificationService;
import com.cws.shop.model.NotificationType;
import java.util.List;

import com.cws.shop.service.PasswordResetLimitService;



@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    
    private final PasswordResetLimitService passwordResetLimitService;
    
    private final EmailService emailService;
    private TokenService tokenService;
    private final NotificationService notificationService;

    
    private static final long EMAIL_VERIFICATION_HOURS = 24;
    private static final long PASSWORD_RESET_HOURS = 1;

    
    
   



public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			TokenRepository tokenRepository, PasswordResetLimitService passwordResetLimitService,
			EmailService emailService, TokenService tokenService, NotificationService notificationService) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.tokenRepository = tokenRepository;
		this.passwordResetLimitService = passwordResetLimitService;
		this.emailService = emailService;
       this.tokenService = tokenService;
       this.notificationService = notificationService;

   }



//	@Override
//    public ApiResponse<LoginResponse> login(LoginRequest request) {
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new UserNotFoundException("user not found"));
//
//        if (user.getRole() == Role.ADMIN) {
//        	 if (!passwordEncoder.matches(
//                     request.getPassword(),
//                     user.getPassword())) {
//
//                 throw new RuntimeException("Invalid Password");
//             }
//
//             String token = jwtService.generateToken(user.getEmail());
//
//             user.setActiveToken(token);
//
//             userRepository.save(user);
//
//             LoginResponse loginResponse = new LoginResponse(
//                     token,
//                     user.getName(),
//                     user.getEmail(),
//                     user.getRole().name()
//             );
//
//             return new ApiResponse<>(
//                     true,
//                     "Login Successful",
//                     loginResponse
//             );
//        }
//        
//        else {
//       	  if (!passwordEncoder.matches(
//               request.getPassword(),
//               user.getPassword())) {
//
//                       throw new UnauthorizedException("Invalid Password");
//             }
//
//              String token = jwtService.generateToken(user.getEmail());
//
//              user.setActiveToken(token);
//
//              userRepository.save(user);
//
//              LoginResponse loginResponse = new LoginResponse(
//              token,
//              user.getName(),
//              user.getEmail(),
//              user.getRole().name()
//            );
//
//           return new ApiResponse<>(
//           true,
//           "Login Successful",
//           loginResponse
//            );
//}
//        	
//        }
   
   @Override
   public ApiResponse<LoginResponse> login(LoginRequest request) {

       User user = userRepository.findByEmail(request.getEmail())
               .orElseThrow(() ->
                       new UserNotFoundException("User not found"));
       
       if (user.getStatus() != UserStatus.ACTIVE) {
           throw new UnauthorizedException(
                   "Please verify your email before login");
       }

       if (!passwordEncoder.matches(
               request.getPassword(),
               user.getPassword())) {

           throw new UnauthorizedException("Invalid Password");
       }

       String token = jwtService.generateToken(
               user.getEmail());

       user.setActiveToken(token);
       user.setLastLogin(LocalDateTime.now());

       userRepository.save(user);

       LoginResponse loginResponse = new LoginResponse(
    		   user.getId(),
               token,
               user.getName(),
               user.getEmail(),
               user.getRole().name()
       );

       return new ApiResponse<>(
               true,
               "Login Successful",
               loginResponse
       );
   }

       
    
//    @Override
//    public ApiResponse<String> logout(Authentication authentication) {
//
//        if (authentication == null || authentication.getPrincipal() == null) {
//            throw new UnauthorizedException("User not authenticated");
//        }
//
//        User user = (User) authentication.getPrincipal();
//        
//        
//     
//
//        user.setActiveToken(null);
//        userRepository.save(user);
//
//        SecurityContextHolder.clearContext();
//
//        return new ApiResponse<>(
//                true,
//                "Logout successful",
//                "User logged out"
//        );
//    }

   
//   @Override
//   public ApiResponse<String> logout(Authentication authentication) {
//
//      User user = (User) authentication.getPrincipal();
//        
//        System.out.println(user.getEmail());
//        
//        String email = user.getEmail();
//        User user1 = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UserNotFoundException("User not found"));
//
//        user1.setActiveToken(null);
//        userRepository.save(user1);
//
//       Object principal = authentication.getPrincipal();
//
//       //String email;
//
//       if (principal instanceof User) {
//
//           email = ((User) principal).getEmail();
//
//       } else {
//
//           email = principal.toString();
//       }
//       
//       System.out.println(authentication.getPrincipal());
//       System.out.println(authentication.getName());
//       
//       System.out.println(authentication);
//       System.out.println(authentication.getClass());
//
//       User user = userRepository.findByEmail(email)
//               .orElseThrow(() ->
//                       new UserNotFoundException(
//                               "User not found"));
//
//       user.setActiveToken(null);
//
//       userRepository.save(user);
//
//       SecurityContextHolder.clearContext();
//
//       return new ApiResponse<>(
//               true,
//               "Logout successful",
//               "User logged out"
//       );
//   }
   
   //pratik 29-05-26
   @Override
   public ApiResponse<String> logout(Authentication authentication) {

       Object principal = authentication.getPrincipal();

       String email;

       if (principal instanceof User) {
           email = ((User) principal).getEmail();
       } else {
           email = principal.toString();
       }

       System.out.println(email);

       User existingUser = userRepository.findByEmail(email)
               .orElseThrow(() ->
                       new UserNotFoundException("User not found"));

       existingUser.setActiveToken(null);

       userRepository.save(existingUser);

       SecurityContextHolder.clearContext();

       return new ApiResponse<>(
               true,
               "Logout successful",
               "User logged out"
       );
   }
    @Override
    public ApiResponse<String> registerBuyer(
            CreateUserRequestDto request) {

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists");
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setMobileNumber(
                request.getMobileNumber());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole(Role.BUYER);

        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
        
        List<User> admins =
                userRepository.findByRole(Role.ADMIN);
        for (User admin : admins) {

            notificationService.createNotification(
                    admin,
                    "New User Registered",
                    "New user registered with email: " + user.getEmail(),
                    NotificationType.SYSTEM
            );
        }



        String token = UUID.randomUUID().toString();

        Token userToken = new Token();

        userToken.setToken(token);

        userToken.setTokenType(
                TokenType.EMAIL_VERIFICATION);

        userToken.setExpiryDate(
                LocalDateTime.now().plusHours(24));

        userToken.setUsed(false);

        userToken.setUser(user);

        tokenRepository.save(userToken);

        String verificationLink =
                "http://localhost:8081/auth/verify?token="
                        + token;

        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getName(),
                verificationLink
        );

        return new ApiResponse<>(
                true,
                "Registration successful. Please verify your email.",
                null
        );
    }

//    @Override
//    public ApiResponse<?> forgotPassword(ForgotPassRequestDto request)
//    {
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        Token token = tokenService.createToken(user, TokenType.PASSWORD_RESET, PASSWORD_RESET_HOURS);
//
//        // ✅ FRONTEND LINK (IMPORTANT)
//        String resetLink = "http://localhost:5501/Reset_Password.html?token=" + token.getToken();
//
//        emailService.sendResetPasswordEmail(
//                user.getEmail(),
//                "Password Reset",
//                "Click here to reset your password: " + resetLink
//        );
//
//        return new ApiResponse<>(true, "Password reset link sent to email", null);
//    }

    
    @Override
    public ApiResponse<?> forgotPassword(ForgotPassRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check daily limit
        passwordResetLimitService.validateResetLimit(user.getEmail());

        Token token = tokenService.createToken(
                user,
                TokenType.PASSWORD_RESET,
                PASSWORD_RESET_HOURS);

        // Increment count after successful request
        passwordResetLimitService.incrementResetCount(user.getEmail());

        String resetLink =
                "http://localhost:5501/resetpassword.html?token="
                        + token.getToken();

        emailService.sendResetPasswordEmail(
                user.getEmail(),
                "Password Reset",
                "Click here to reset your password: " + resetLink
        );

        return new ApiResponse<>(
                true,
                "Password reset link sent to email",
                null
        );
    }
    
    @Override
    public ApiResponse<String> verifyEmail(String token) {

        Token verificationToken = tokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new InvalidTokenException("Invalid verification token"));

        if (verificationToken.getUsed()) {
            throw new TokenAlreadyUsedException("Token already used");
        }

        if (verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new TokenExpiredException("Token expired");
        }

        User user = verificationToken.getUser();

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        verificationToken.setUsed(true);

        tokenRepository.save(verificationToken);

        return new ApiResponse<>(
                true,
                "Email verified successfully",
                null
        );
    }
}
