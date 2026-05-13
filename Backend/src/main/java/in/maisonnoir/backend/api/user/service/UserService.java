package in.maisonnoir.backend.api.user.service;

import in.maisonnoir.backend.api.user.model.dto.UpdatePasswordDTO;
import in.maisonnoir.backend.api.user.model.dto.UserResponseDTO;
import in.maisonnoir.backend.api.user.model.dto.UserUpdateDTO;

import java.util.List;

public interface UserService {

    // CUSTOMER SERVICES
    UserResponseDTO getUser();

    UserResponseDTO updateUser(UserUpdateDTO userDTO);

    void updatePassword(UpdatePasswordDTO dto);

    void deleteUser();

    // ADMIN SERVICES
    UserResponseDTO getUserById(Long userId);

    List<UserResponseDTO> getAllUsers();

    void deleteUserById(Long userId);
}
