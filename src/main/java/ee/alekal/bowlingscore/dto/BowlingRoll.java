package ee.alekal.bowlingscore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BowlingRoll {
    String playerNickname;
    String score;
    Integer frameId;

}
