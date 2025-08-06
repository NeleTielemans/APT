package fact.it.competition.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "competition")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Competition {
    private String id;
    private String name;
    private String location;
    private String description;
}
