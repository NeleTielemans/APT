package fact.it.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DogResponse {
    private String id;
    private String chipnr;
    private String name;
    private String breed;
    private String gender;
    private String ownerId;
}
