package employee_management.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class APIResponse<T> {

    private String message;
    private int statusCode;
    private String statusMessage;
    private String errorMessage;
    private T data;

}