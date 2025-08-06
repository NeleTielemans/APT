package fact.it.history.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String competitionId;
    private String program;
    private String personId;
    private String dogId;
    private int score;

}
