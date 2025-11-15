package in.maisonnoir.backend.service.Impl;

import in.maisonnoir.backend.DTO.UserDTO;
import in.maisonnoir.backend.Repository.UserRepository;
import in.maisonnoir.backend.entity.UserEntity;
import in.maisonnoir.backend.exception.ResourceNotFoundException;
import in.maisonnoir.backend.mapper.UserMapper;
import in.maisonnoir.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity entity = UserMapper.toEntity(userDTO);
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDTO(saved);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        return UserMapper.toDTO(entity);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        entity.setName(userDTO.getName());
        entity.setEmail(userDTO.getEmail());
        entity.setPassword(userDTO.getPassword());
        entity.setRole(userDTO.getRole());

        UserEntity updated = userRepository.save(entity);
        return UserMapper.toDTO(updated);

    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        userRepository.delete(entity);

    }
}
