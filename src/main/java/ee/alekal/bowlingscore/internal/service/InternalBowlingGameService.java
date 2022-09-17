package ee.alekal.bowlingscore.internal.service;

import ee.alekal.bowlingscore.dto.BowlingRoll;
import ee.alekal.bowlingscore.dto.type.FrameRollQueueType;
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

    @Override
    public ResponseEntity<?> makeFirstRoll(BowlingRoll bowlingRoll) {
        log.info("makeFirstRoll, bowlingRoll {}", bowlingRoll);

        var nickname = bowlingRoll.getPlayerNickname();
        var frameId = bowlingRoll.getFrameId();
        var score = bowlingRoll.getScore();

        ValidationService
                .withNickNameAndFrameId(nickname, frameId)
                .playerIsRegisteredInInternalStorage()
                .frameExistsInStorage()
                .isValidScore(score)
                .canMakeRoll(FrameRollQueueType.FIRST_ROLL);

        val player = InternalBowlingStorage.getPlayer(nickname);
        player.setFrameFirstRollScore(frameId, convertStringScore(score));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> makeSecondRoll(BowlingRoll bowlingRoll) {
        log.info("makeSecondRoll, bowlingRoll {}", bowlingRoll);

        var nickname = bowlingRoll.getPlayerNickname();
        var frameId = bowlingRoll.getFrameId();
        var score = bowlingRoll.getScore();

        ValidationService
                .withNickNameAndFrameId(nickname, frameId)
                .playerIsRegisteredInInternalStorage()
                .frameExistsInStorage()
                .isValidScore(score)
                .canMakeRoll(FrameRollQueueType.SECOND_ROLL);

        val player = InternalBowlingStorage.getPlayer(nickname);
        player.setFrameSecondRollScore(frameId, convertStringScore(score));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
