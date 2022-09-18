package ee.alekal.bowlingscore.internal.service;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.dto.api.BowlingRollRequest;
import ee.alekal.bowlingscore.dto.type.RollQueueType;
import ee.alekal.bowlingscore.internal.blogic.GameBehaviour;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.internal.validation.ValidationService;
import ee.alekal.bowlingscore.service.BowlingGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static ee.alekal.bowlingscore.internal.utils.InternalUtils.convertStringScore;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalBowlingGameService implements BowlingGameService {

    private final GameBehaviour gameBehaviour;

    @Override
    public ResponseEntity<?> makeFirstRoll(BowlingRollRequest bowlingRoll) {
        log.info("makeFirstRoll, bowlingRoll {}", bowlingRoll);

        var nickname = bowlingRoll.getPlayerNickname();
        var score = bowlingRoll.getScore();

        ValidationService
                .withNickNameAndFrameId(nickname, gameBehaviour.getCurrentFrame())
                .playerIsRegisteredInInternalStorage()
                .frameExistsInInternalStorage()
                .isValidScore(score)
                .canMakeRoll(RollQueueType.FRAME_FIRST_ROLL);

        val player = InternalBowlingStorage.getPlayerByNickname(nickname);
        player.setFrameFirstRollScore(gameBehaviour.getCurrentFrame(), convertStringScore(score));

        if (player.getFrameTotalScore(gameBehaviour.getCurrentFrame()).equals(10)) {
            addPlayerToFrameUpdateCurrentIfNeeded(player);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> makeSecondRoll(BowlingRollRequest bowlingRoll) {
        log.info("makeSecondRoll, bowlingRoll {}", bowlingRoll);

        var nickname = bowlingRoll.getPlayerNickname();
        var score = bowlingRoll.getScore();

        ValidationService
                .withNickNameAndFrameId(nickname, gameBehaviour.getCurrentFrame())
                .playerIsRegisteredInInternalStorage()
                .frameExistsInInternalStorage()
                .isValidScore(score)
                .canMakeRoll(RollQueueType.FRAME_SECOND_ROLL);

        val player = InternalBowlingStorage.getPlayerByNickname(nickname);
        player.setFrameSecondRollScore(gameBehaviour.getCurrentFrame(), convertStringScore(score));

        addPlayerToFrameUpdateCurrentIfNeeded(player);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addPlayerToFrameUpdateCurrentIfNeeded(Player player) {
        gameBehaviour.addPlayerToFrameQueue(player);
        gameBehaviour.updateCurrentFrameIfAllPlayersRolledOnCurrent();
    }
}
