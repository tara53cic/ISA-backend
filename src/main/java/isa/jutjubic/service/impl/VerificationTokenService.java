package isa.jutjubic.service.impl;

import isa.jutjubic.model.User;
import isa.jutjubic.model.VerificationToken;
import isa.jutjubic.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public VerificationToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken(user, token);
        return tokenRepository.save(vt);
    }

    public VerificationToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void delete(VerificationToken token) {
        tokenRepository.delete(token);
    }
}
