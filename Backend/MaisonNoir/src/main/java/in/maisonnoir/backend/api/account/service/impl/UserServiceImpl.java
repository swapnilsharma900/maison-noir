package in.maisonnoir.backend.api.account.service.impl;

import in.maisonnoir.backend.api.account.model.dto.UserDTO;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.account.mapper.UserMapper;
import in.maisonnoir.backend.api.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity entity = UserMapper.toEntity(userDTO);
        UserEntity saved = userDAO.save(entity);
        return UserMapper.toDTO(saved);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        UserEntity entity = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        return UserMapper.toDTO(entity);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        UserEntity entity = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        entity.setName(userDTO.getName());
        entity.setEmail(userDTO.getEmail());
        entity.setPassword(userDTO.getPassword());
        entity.setRole(userDTO.getRole());

        UserEntity updated = userDAO.save(entity);
        return UserMapper.toDTO(updated);

    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity entity = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        userDAO.delete(entity);

    }
}
