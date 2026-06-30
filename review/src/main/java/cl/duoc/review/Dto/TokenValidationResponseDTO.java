package cl.duoc.review.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponseDTO {

    private Boolean valid;
    private Long userId;
    private String username;
    private String role;
}
