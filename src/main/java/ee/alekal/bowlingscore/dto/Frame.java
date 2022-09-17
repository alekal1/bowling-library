package ee.alekal.bowlingscore.dto;

import lombok.Data;

@Data
public class Frame {
    private Integer firstRollScore;
    private Integer secondRollScore;

    public Integer getFrameTotalScore() {
        if (firstRollScore == null && secondRollScore == null) {
            return 0;
        } else if (firstRollScore != null && secondRollScore == null) {
            return firstRollScore;
        }
        return firstRollScore == null ? secondRollScore : firstRollScore + secondRollScore;
    }
}
