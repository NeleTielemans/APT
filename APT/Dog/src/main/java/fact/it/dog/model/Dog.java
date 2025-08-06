package fact.it.dog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "dog")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Dog {
    private String id;
    private String chipnr;
    private String name;
    private String breed;
    private String gender;
    private String ownerId;
}
