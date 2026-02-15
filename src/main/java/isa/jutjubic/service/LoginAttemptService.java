package isa.jutjubic.service;

public interface LoginAttemptService {

    public boolean isBlocked(String ip);


    public void recordFailedAttempt(String ip);

    public void resetAttempts(String ip);
}
