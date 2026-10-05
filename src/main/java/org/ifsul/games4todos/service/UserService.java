package org.ifsul.games4todos.service;

import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository UserRepository;

    public UserService(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    public User salvar(User User){
        return this.UserRepository.save(User);
    }

    public void excluir(User User){
        this.UserRepository.delete(User);
    }

    public List<User> buscarTodos() {
        return this.UserRepository.findAll();
    }

    public User buscarPorId(Long id){
        return this.UserRepository.findById(id).orElse(null);
    }
}
