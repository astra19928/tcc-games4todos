package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.dto.UserDTO;
import org.ifsul.games4todos.dto.UserPatchDTO;
import org.ifsul.games4todos.model.DisabilityType;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.model.UserDisability;
import org.ifsul.games4todos.repository.DisabilityTypeRepository;
import org.ifsul.games4todos.repository.UserDisabilityRepository;
import org.ifsul.games4todos.repository.UserRepository;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
    private final FileSystemStorageService fileService;


    private final String fileDir = "users/";


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

        storeImage(dto.image(), user.getId());

        return user;
    }

    public User fetch(User user){
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
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

    public void storeImage(MultipartFile image, Integer id){
        if(image == null || image.isEmpty())
            return;

        String newFileDir = fileDir + id;
        fileService.store(image, newFileDir, "icon");
    }

    @Transactional
    public void patch(UserPatchDTO dto, Authentication authentication, Integer id) {
        G4TUserDetails details = (G4TUserDetails) authentication.getPrincipal();
        if(details == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Você deve estar autenticado para alterar sua conta!");

        User user = details.getUser();

        if(!user.getId().equals(id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Você não pode alterar detalhes de contas de outros usuários!");

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

        if(dto.password() != null && !dto.password().isBlank()){

            if(!passwordEncoder.matches(dto.oldPassword(), user.getPassword()))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Senha antiga inválida!");

            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        userDisabilityRepository.deleteById_IdUser(user.getId());

        types.forEach(t -> userDisabilityRepository.save(
                new UserDisability(user, t.getCategory().getId(), t.getId())));

        user.setNickname(dto.nickname());

        storeImage(dto.image(), user.getId());

        userRepository.save(user);
    }

    public void delete(Integer id, Authentication authentication) {
        G4TUserDetails details = (G4TUserDetails) authentication.getPrincipal();
        if(details == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Você deve estar autenticado para deletar sua conta!");

        User user = details.getUser();
        if(!user.getId().equals(id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode deletar a conta de outros usuários!");

        userRepository.delete(user);
    }
}
