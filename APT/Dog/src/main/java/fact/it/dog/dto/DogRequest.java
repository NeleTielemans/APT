package fact.it.dog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DogRequest {
    private String chipnr;
    private String name;
    private String breed;
    private String gender;
    private String ownerId;
}
