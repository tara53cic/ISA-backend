package isa.jutjubic.service;

import java.util.List;

import isa.jutjubic.dto.UserRequest;
import isa.jutjubic.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
	User save(UserRequest userRequest);
    User update(User user);
}
