package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.dto.UserDTO;
import org.ifsul.games4todos.model.DisabilityType;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.model.UserDisability;
import org.ifsul.games4todos.repository.DisabilityTypeRepository;
import org.ifsul.games4todos.repository.UserDisabilityRepository;
import org.ifsul.games4todos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DisabilityTypeRepository disabilityTypeRepository;
    private final UserDisabilityRepository userDisabilityRepository;

    @Transactional
    public User create(UserDTO dto){
        User user = User.builder()
                        .nickname(dto.nickname())
                        .email(dto.email())
                        .password(passwordEncoder.encode(dto.password()))
                        .build();


        List<Integer> ids = Optional.ofNullable(dto.disabilityTypeIds()).orElse(List.of());

        List<DisabilityType> types = disabilityTypeRepository.findAllById(ids);

        if (types.size() != new HashSet<>(ids).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de deficiência desconhecido");
        }
        long distinctCategories = types.stream()
                .map(t -> t.getCategory().getId()).distinct().count();
        if (distinctCategories != types.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas um tipo por categoria");
        }

        userRepository.save(user);

        types.forEach(t -> userDisabilityRepository.save(
                new UserDisability(user, t.getCategory().getId(), t.getId())));

        return user;
    }
    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public void delete(User User){
        this.userRepository.delete(User);
    }
    public Optional<User> findById(Integer id){
        return this.userRepository.findById(id);
    }

}
