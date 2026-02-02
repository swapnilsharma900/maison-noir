package in.maisonnoir.backend.api.account.service;

import in.maisonnoir.backend.api.account.model.dto.user.UserRegistrationDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserResponseDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserUpdateDTO;

import java.util.List;

public interface UserService {

    // CUSTOMER SERVICES
    UserResponseDTO createUser(UserRegistrationDTO userDTO);

    UserResponseDTO getUser();

    UserResponseDTO updateUser(UserUpdateDTO userDTO);

    void deleteUser();


    // ADMIN SERVICES
    UserResponseDTO getUserById(Long userId);

    List<UserResponseDTO> getAllUsers();

    void deleteUserById(Long userId);
}
