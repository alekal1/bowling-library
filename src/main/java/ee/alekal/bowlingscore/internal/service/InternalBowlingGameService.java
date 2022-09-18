package ee.alekal.bowlingscore.internal.service;

import ee.alekal.bowlingscore.dto.api.BowlingRollRequest;
import ee.alekal.bowlingscore.dto.type.RollQueueType;
import ee.alekal.bowlingscore.internal.blogic.GameBehaviour;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.internal.validation.ValidationService;
import ee.alekal.bowlingscore.service.BowlingGameService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static ee.alekal.bowlingscore.internal.utils.InternalUtils.convertStringScore;

@Slf4j
@Service
public class InternalBowlingGameService implements BowlingGameService {

    private static final Integer CURRENT_FRAME_ID = GameBehaviour.getInstance().getCurrentFrame();

    @Override
    public ResponseEntity<?> makeFirstRoll(BowlingRollRequest bowlingRoll) {
        log.info("makeFirstRoll, bowlingRoll {}", bowlingRoll);

        var nickname = bowlingRoll.getPlayerNickname();
        var score = bowlingRoll.getScore();

        ValidationService
                .withNickNameAndFrameId(nickname, CURRENT_FRAME_ID)
                .playerIsRegisteredInInternalStorage()
                .frameExistsInInternalStorage()
                .isValidScore(score)
                .canMakeRoll(RollQueueType.FRAME_FIRST_ROLL);

        val player = InternalBowlingStorage.getPlayerByNickname(nickname);
        player.setFrameFirstRollScore(CURRENT_FRAME_ID, convertStringScore(score));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> makeSecondRoll(BowlingRollRequest bowlingRoll) {
        log.info("makeSecondRoll, bowlingRoll {}", bowlingRoll);

        var nickname = bowlingRoll.getPlayerNickname();
        var score = bowlingRoll.getScore();

        ValidationService
                .withNickNameAndFrameId(nickname, CURRENT_FRAME_ID)
                .playerIsRegisteredInInternalStorage()
                .frameExistsInInternalStorage()
                .isValidScore(score)
                .canMakeRoll(RollQueueType.FRAME_SECOND_ROLL);

        val player = InternalBowlingStorage.getPlayerByNickname(nickname);
        player.setFrameSecondRollScore(CURRENT_FRAME_ID, convertStringScore(score));

        GameBehaviour.getInstance().addPlayerToFrameQueue(player);
        GameBehaviour.getInstance().updateCurrentFrameIfAllPlayersRolledOnCurrent();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
