package org.ifsul.games4todos.service;

import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository UserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        UserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.UserRepository.save(user);
    }

    public void delete(User User){
        this.UserRepository.delete(User);
    }

    public List<User> findAll() {
        return this.UserRepository.findAll();
    }

    public User findById(Integer id){
        return this.UserRepository.findById(id).orElse(null);
    }
}
