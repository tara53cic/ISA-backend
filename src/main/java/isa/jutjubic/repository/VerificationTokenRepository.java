package isa.jutjubic.repository;

import isa.jutjubic.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
}

