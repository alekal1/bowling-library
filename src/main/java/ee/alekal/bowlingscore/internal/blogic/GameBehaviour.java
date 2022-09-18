package ee.alekal.bowlingscore.internal.blogic;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_BOARD_SIZE;

@Getter
@Component
public class GameBehaviour implements InitializingBean {

    private Integer currentFrame;
    private Map<Integer, List<Player>> framePlayerQueue;

    public void addPlayerToFrameQueue(Player player) {
        var currentQueue = framePlayerQueue.get(currentFrame);
        var newQueue = new ArrayList<>(currentQueue);
        newQueue.add(player);

        framePlayerQueue.put(currentFrame, newQueue);
    }

    public void updateCurrentFrameIfAllPlayersRolledOnCurrent() {
        if (framePlayerQueue.get(currentFrame).size() == InternalBowlingStorage.getPlayers().size()) {
            currentFrame++;
        }
    }

    @Override
    public void afterPropertiesSet() {
        currentFrame = 0;
        framePlayerQueue = new HashMap<>();

        for (int i = 0; i < BOWLING_BOARD_SIZE; i++) {
            framePlayerQueue.put(i, List.of());
        }
    }
}
