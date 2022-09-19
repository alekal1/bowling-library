package ee.alekal.bowlingscore.internal;

import ee.alekal.bowlingscore.dto.api.BowlingRollRequest;
import ee.alekal.bowlingscore.utils.InternalBowlingBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InternalValidationTest extends InternalBowlingBaseTest {

    @Test
    public void verifyCannotDeleteUnregisteredPlayer() {
        assertThrowsBowlingValidationException(() ->
                bowlingManagementService.removePlayerByNickname("verifyCannotDeleteUnregisteredPlayer"));
    }

    @Test
    public void verifyThrowsValidationExceptionUserAlreadyRegistered() {
        var nickNamePrefix = "verifyThrowsValidationExceptionUserAlreadyRegistered";

        addPlayer(nickNamePrefix);

        assertThrowsBowlingValidationException(() -> addPlayer(nickNamePrefix));
    }

    @Test
    public void verifyThrowsValidationExceptionPlayerShouldMakeFirstRoll() {
        var player = addPlayer("verifyThrowsValidationExceptionPlayerShouldMakeFirstRoll");

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeSecondRoll(
                        BowlingRollRequest.builder()
                                .playerNickname(player.getNickname())
                                .score("1")
                                .build()));
    }

    @Test
    public void verifyThrowsValidationExceptionFrameRollResultAlreadyReported() {
        var player = addPlayer("verifyThrowsValidationExceptionFrameRollResultAlreadyReported");

        var firstBowlingRoll = BowlingRollRequest.builder()
                .playerNickname(player.getNickname())
                .score("1")
                .build();

        bowlingGameService.makeFirstRoll(firstBowlingRoll);

        var firstRollResponse =
                bowlingManagementService.getPlayerFrameScore(
                        player.getNickname(),
                        gameBehaviour.getCurrentFrame());
        assertEquals(HttpStatus.OK, firstRollResponse.getStatusCode());
        assertTrue(firstRollResponse.hasBody());

        assertEquals(1, (Integer) firstRollResponse.getBody());

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(firstBowlingRoll));
    }

    @Test
    public void verifyThrowsValidationExceptionFrameDoesNotExists() {
        var player = addPlayer("verifyThrowsValidationExceptionFrameDoesNotExists");

        assertThrowsBowlingValidationException(() ->
                bowlingManagementService.getPlayerFrameScore(player.getNickname(), -1));


        assertThrowsBowlingValidationException(() ->
                bowlingManagementService.getPlayerFrameScore(player.getNickname(), 11));
    }

    @Test
    public void verifyThrowsValidationExceptionInvalidScoreValue() {
        var player = addPlayer("verifyThrowsValidationExceptionInvalidScoreValue");

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRollRequest.builder()
                                .playerNickname(player.getNickname())
                                .score("abc")
                                .build()
                ));

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRollRequest.builder()
                                .playerNickname(player.getNickname())
                                .score("-1")
                                .build()));

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRollRequest.builder()
                                .playerNickname(player.getNickname())
                                .score("22")
                                .build()));
    }

    @Test
    public void verifyThrowsValidationExceptionInvalidTotalScore() {
        var player = addPlayer("verifyThrowsValidationExceptionInvalidTotalScore");

        bowlingGameService.makeFirstRoll(
                BowlingRollRequest.builder()
                        .playerNickname(player.getNickname())
                        .score("7")
                        .build());

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRollRequest.builder()
                                .playerNickname(player.getNickname())
                                .score("7")
                                .build()));

    }

    @Test
    public void verifyCannotMakeSecondRollOnCurrentFrameIfFirstRollScoreIsStrike() {
        var player = addPlayer("verifyCannotMakeSecondRollOnCurrentFrameIfFirstRollScoreIsStrike");

        bowlingGameService.makeFirstRoll(
                BowlingRollRequest.builder()
                        .playerNickname(player.getNickname())
                        .score("10")
                        .build());

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeSecondRoll(
                        BowlingRollRequest.builder()
                                .playerNickname(player.getNickname())
                                .score("1")
                                .build()));

    }

    @Test
    public void verifyThrowsValidationExceptionGameIsEnded() {
        var player = addPlayer("verifyThrowsValidationExceptionGameIsEnded");
        var request = BowlingRollRequest.builder()
                .playerNickname(player.getNickname())
                .score("10")
                .build();

        for (int i = 0; i < BOWLING_BOARD_SIZE; i++) {
            bowlingGameService.makeFirstRoll(request);
        }

        assertThrowsBowlingValidationException(() -> bowlingGameService.makeFirstRoll(request));
    }
}
