package in.maisonnoir.backend.api.common.response;

public record ApiResponse(boolean success, String message, Object data) {}
