package isa.jutjubic.service;

import isa.jutjubic.model.User;
import isa.jutjubic.model.VerificationToken;


public interface VerificationTokenService {

    public VerificationToken createToken(User user);

    public VerificationToken findByToken(String token);

    public void delete(VerificationToken token);
}
