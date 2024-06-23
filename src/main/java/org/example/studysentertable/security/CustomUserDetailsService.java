package org.example.studysentertable.security;

import lombok.RequiredArgsConstructor;
import org.example.studysentertable.repository.StudentRepository;
import org.example.studysentertable.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhone(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}