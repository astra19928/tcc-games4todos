package org.ifsul.games4todos.security;

import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class G4TUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public G4TUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));

        return new G4TUserDetails(user);
    }
}
