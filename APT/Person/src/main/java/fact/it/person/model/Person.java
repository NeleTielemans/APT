package fact.it.person.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "person")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
