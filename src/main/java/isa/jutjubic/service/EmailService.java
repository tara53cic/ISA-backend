package isa.jutjubic.service;

import isa.jutjubic.model.User;

public interface EmailService{


    public void sendVerificationEmail(User user, String token);
}
