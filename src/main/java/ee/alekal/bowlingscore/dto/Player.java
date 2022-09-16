package ee.alekal.bowlingscore.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player {

    private final String nickname;

    private List<Frame> frames = new ArrayList<>();
    private Integer totalScore;

}
