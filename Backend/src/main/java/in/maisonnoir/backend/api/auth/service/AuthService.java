package in.maisonnoir.backend.api.auth.service;

import in.maisonnoir.backend.api.auth.model.dto.AuthResponseDTO;
import in.maisonnoir.backend.api.auth.model.dto.LoginDTO;
import in.maisonnoir.backend.api.auth.model.dto.RegisterDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterDTO registerDTO);
    AuthResponseDTO login(LoginDTO loginDTO);
}
