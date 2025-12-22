package isa.jutjubic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.jutjubic.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

