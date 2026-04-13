package in.maisonnoir.backend.api.account.service;

import in.maisonnoir.backend.api.account.model.dto.user.UpdatePasswordDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserResponseDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserUpdateDTO;

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
