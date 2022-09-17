package ee.alekal.bowlingscore.internal.blogic;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_BOARD_SIZE;

public class GameBehaviour {

    @Getter
    private Integer currentFrame = 0;
    @Getter
    private final Map<Integer, List<Player>> framePlayerQueue;

    private static GameBehaviour instance;

    private GameBehaviour() {
        framePlayerQueue = new HashMap<>();

        for (int i = 0; i < BOWLING_BOARD_SIZE; i++) {
            framePlayerQueue.put(i, List.of());
        }
    }

    public static GameBehaviour getInstance() {
        if (instance == null) {
            instance = new GameBehaviour();
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

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
}
