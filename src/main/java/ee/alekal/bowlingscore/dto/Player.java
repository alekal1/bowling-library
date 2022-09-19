package ee.alekal.bowlingscore.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_BOARD_SIZE;

@Getter
public class Player {

    private final String nickname;
    private final List<Frame> frames = new ArrayList<>();

    public Player(String nickname) {
        this.nickname = nickname;
        initFrames();
    }

    private void initFrames() {
        for (int i = 0; i < BOWLING_BOARD_SIZE; i++) {
            this.frames.add(new Frame());
        }
    }

    public Integer getFrameFirstRollScore(Integer frameId) {
        return frames.get(frameId).getFirstRollScore();
    }

    public void setFrameFirstRollScore(Integer frameId, Integer score) {
        frames.get(frameId).setFirstRollScore(score);
    }

    public Integer getFrameSecondRollScore(Integer frameId) {
        return frames.get(frameId).getSecondRollScore();
    }

    public void setFrameSecondRollScore(Integer frameId, Integer score) {
        frames.get(frameId).setSecondRollScore(score);
    }

    public Integer getFrameTotalScore(Integer frameId) {
        return frames.get(frameId).getFrameTotalScore();
    }

    public Integer getTotalScore() {
        return frames.stream().mapToInt(Frame::getFrameTotalScore).sum();
    }
}
