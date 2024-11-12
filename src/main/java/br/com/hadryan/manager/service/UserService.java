package br.com.hadryan.manager.service;

import br.com.hadryan.manager.mapper.UserMapper;
import br.com.hadryan.manager.mapper.request.UserPostRequest;
import br.com.hadryan.manager.mapper.request.UserPutRequest;
import br.com.hadryan.manager.mapper.response.UserResponse;
import br.com.hadryan.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponse save(UserPostRequest request) {
        log.info("Saving user: {}", request.getUsername());
        var userToSave = userMapper.postToUser(request);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        var savedUser = userRepository.save(userToSave);
        return userMapper.userToResponse(savedUser);
    }

    public UserResponse findById(Long id) {
        log.info("Finding user by id: {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.userToResponse(user);
    }

    public void update (UserPutRequest request) {
        log.info("Updating user by id: {}", request.getId());
        var userToUpdate = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userToUpdate.setUsername(request.getUsername());
        userToUpdate.setPassword(request.getPassword());
        userToUpdate.setEmail(request.getEmail());
        userRepository.save(userToUpdate);
    }

    public void delete(Long id) {
        log.info("Deleting user by id: {}", id);
        var userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(userToDelete);
    }

}
