package isa.jutjubic.controller;

import isa.jutjubic.model.VerificationToken;
import isa.jutjubic.service.impl.EmailService;
import isa.jutjubic.service.impl.VerificationTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import isa.jutjubic.dto.JwtAuthenticationRequest;
import isa.jutjubic.dto.UserRequest;
import isa.jutjubic.dto.UserTokenState;

import isa.jutjubic.model.User;
import isa.jutjubic.service.UserService;
import isa.jutjubic.util.TokenUtils;

import java.io.IOException;
import java.time.LocalDateTime;


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
	
	// Prvi endpoint koji pogadja korisnik kada se loguje.
	// Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
		// Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
		// AuthenticationException
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		// Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
		// kontekst
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token za tog korisnika
		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();

		// Vrati token kao odgovor na uspesnu autentifikaciju
		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
	}

	@PostMapping("/signup")
	public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest) {

		if (userService.findByUsername(userRequest.getUsername()) != null) {
			//throw new ResourceConflictException(userRequest.getId(), "Username already exists");
		}

		User user = userService.save(userRequest);

		VerificationToken token = verificationTokenService.createToken(user);

		emailService.sendVerificationEmail(user, token.getToken());

		return ResponseEntity.ok("Registration successful. Check your email.");
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

		response.sendRedirect("http://localhost:4200/activation-success");
	}
}