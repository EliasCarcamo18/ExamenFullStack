package cl.duoc.review.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationResponseDTO {

    private Long id;
    private String name;
    private String country;
    private String city;
    private Boolean active;
}
