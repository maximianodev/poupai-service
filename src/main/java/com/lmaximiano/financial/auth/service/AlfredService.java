package com.lmaximiano.financial.auth.service;

import com.lmaximiano.financial.auth.exception.BadRequestException;
import com.lmaximiano.financial.auth.repository.UserRepository;
import com.lmaximiano.financial.auth.dto.UserDTO;
import com.lmaximiano.financial.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.lmaximiano.financial.auth.utils.Constants.ERROR_BAD_REQUEST;

@Service
public class AlfredService {
  @Autowired private UserRepository userRepository;

  public void registerUser(UserDTO userDTO) throws BadRequestException {

    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }
    User user = new User();
    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());
    String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
    user.setPassword(encryptedPassword);

    userRepository.save(user);
  }
}
