package in.maisonnoir.backend.payload;

public record ApiResponse(boolean success, String message, Object data) {}
