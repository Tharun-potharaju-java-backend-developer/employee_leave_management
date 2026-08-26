package employee_management.demo.DTO;

public record Attachment(
        String fileName,
        byte[] data,
        String contentType
) {
}