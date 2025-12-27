package isa.jutjubic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.jutjubic.dto.UserRequest;
import isa.jutjubic.model.Role;
import isa.jutjubic.model.User;
import isa.jutjubic.repository.UserRepository;
import isa.jutjubic.service.RoleService;
import isa.jutjubic.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	public User findById(Long id) throws AccessDeniedException {
		return userRepository.findById(id).orElseGet(null);
	}

	public List<User> findAll() throws AccessDeniedException {
		return userRepository.findAll();
	}

	@Override
	public User save(UserRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());
		u.setEmail(userRequest.getEmail());

		u.setEnabled(false);
		u.setAddress(userRequest.getAddress());

		List<Role> roles = roleService.findByName("ROLE_USER");
		u.setRoles(roles);

		return userRepository.save(u);
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}


}
