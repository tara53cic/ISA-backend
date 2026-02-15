package isa.jutjubic.controller;

import isa.jutjubic.model.VerificationToken;
import isa.jutjubic.service.EmailService;
import isa.jutjubic.service.LoginAttemptService;
import isa.jutjubic.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import isa.jutjubic.dto.JwtAuthenticationRequest;
import isa.jutjubic.dto.UserRequest;
import isa.jutjubic.dto.UserTokenState;

import isa.jutjubic.model.User;
import isa.jutjubic.service.UserService;
import isa.jutjubic.util.TokenUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;


//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@Autowired
	private EmailService emailService;

    @Autowired
    private LoginAttemptService loginAttemptService;
	
	// Prvi endpoint koji pogadja korisnik kada se loguje.
	// Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request) {
        String ip = getClientIP(request);

        if (loginAttemptService.isBlocked(ip)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();

            loginAttemptService.resetAttempts(ip);

            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));

        } catch (AuthenticationException ex) {
            loginAttemptService.recordFailedAttempt(ip);
            throw ex;
        }
	}

	@PostMapping("/signup")
	public ResponseEntity<Map<String,String>> addUser(@RequestBody UserRequest userRequest) {

		if (userService.findByUsername(userRequest.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Username already exists"));
		}

		User user = userService.save(userRequest);

		VerificationToken token = verificationTokenService.createToken(user);

		emailService.sendVerificationEmail(user, token.getToken());

		return ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Collections.singletonMap("message", "Registration successful. Check your email."));
	}

	@GetMapping("/verify")
	public void verifyAccount(@RequestParam String token, HttpServletResponse response) throws IOException {

		VerificationToken vt = verificationTokenService.findByToken(token);

		if (vt == null || vt.getExpiryDate().isBefore(LocalDateTime.now())) {
			response.sendRedirect("http://localhost:4200/activation-error");
			return;
		}

		User user = vt.getUser();
		user.setEnabled(true);
		userService.update(user);

		verificationTokenService.delete(vt);

		response.sendRedirect("http://localhost:4200/login");
	}

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

}