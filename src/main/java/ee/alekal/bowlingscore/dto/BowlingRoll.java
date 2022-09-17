package ee.alekal.bowlingscore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BowlingRoll {
    String playerNickname;
    String score;
    Integer frameId;

}
