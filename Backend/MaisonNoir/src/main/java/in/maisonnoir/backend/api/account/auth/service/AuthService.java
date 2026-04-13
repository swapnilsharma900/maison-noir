package in.maisonnoir.backend.api.account.auth.service;

import in.maisonnoir.backend.api.account.auth.model.dto.AuthResponseDTO;
import in.maisonnoir.backend.api.account.auth.model.dto.LoginDTO;
import in.maisonnoir.backend.api.account.auth.model.dto.RegisterDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterDTO registerDTO);
    AuthResponseDTO login(LoginDTO loginDTO);
}
