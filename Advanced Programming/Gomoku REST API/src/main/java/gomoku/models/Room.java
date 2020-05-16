package gomoku.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "rooms")
@NoArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long identifier;

    @Column(name = "random_id", nullable = false)
    private String randomIdentifier;

    @Column(name = "first_player")
    private String firstPlayer;

    @Column(name = "second_player")
    private String secondPlayer;

    @Column(name = "winner")
    private String winner;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
