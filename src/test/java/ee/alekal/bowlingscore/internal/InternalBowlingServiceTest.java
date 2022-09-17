package ee.alekal.bowlingscore.internal;

import ee.alekal.bowlingscore.dto.BowlingRoll;
import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.exception.BowlingValidationException;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.internal.service.InternalBowlingGameService;
import ee.alekal.bowlingscore.internal.service.InternalBowlingManagementService;
import ee.alekal.bowlingscore.service.BowlingGameService;
import ee.alekal.bowlingscore.service.BowlingManagementService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static ee.alekal.bowlingscore.constants.Constants.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = InternalBowlingServiceTest.Config.class)
public class InternalBowlingServiceTest {

    protected static final String TEST_PLAYER_NICK_NAME = "test-%s:internal";

    @Autowired
    BowlingManagementService bowlingManagementService;

    @Autowired
    BowlingGameService bowlingGameService;

    @Test
    @Order(1) // This method should be executed first
    public void verifyAllPlayersReturnedSuccessfully() {
        addPlayer("verifyAllPlayersReturnedSuccessfully-1");
        addPlayer("verifyAllPlayersReturnedSuccessfully-2");
        addPlayer("verifyAllPlayersReturnedSuccessfully-3");

        assertEquals(3, InternalBowlingStorage.getPlayers().size());
    }

    @RepeatedTest(3)
    public void verifyCanAddPlayers(RepetitionInfo repetitionInfo) {
        var player = addPlayer(repetitionInfo.getCurrentRepetition());
        assertEquals(BOARD_SIZE, player.getFrames().size());
    }

    @Test
    public void verifyThrowsValidationExceptionUserAlreadyRegistered() {
        var nickNamePrefix = "verifyThrowsValidationExceptionUserAlreadyRegistered";

        addPlayer(nickNamePrefix);

        assertThrowsBowlingValidationException(() -> addPlayer(nickNamePrefix));
    }

    @Test
    public void verifySpecificPlayerReturnedSuccessfully() {
        var nickNamePrefix = "verifySpecificPlayerReturnedSuccessfully";

        var player = addPlayer(nickNamePrefix);

        var response = bowlingManagementService.getPlayer(
                String.format(TEST_PLAYER_NICK_NAME, nickNamePrefix));

        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(player, response.getBody());
    }

    @Test
    public void verifyPlayerTotalFrameScoreReturnedSuccessfully() {
        var player = addPlayer("verifyPlayerTotalFrameScoreReturnedSuccessfully");
        var firstFrameId = 0;

        bowlingGameService.makeFirstRoll(
                BowlingRoll.builder()
                        .playerNickname(player.getNickname())
                        .frameId(firstFrameId)
                        .score("1")
                        .build());

        var firstRollResponse =
                bowlingManagementService.getPlayerFrameScore(player.getNickname(), firstFrameId);
        assertEquals(HttpStatus.OK, firstRollResponse.getStatusCode());
        assertTrue(firstRollResponse.hasBody());

        assertEquals(1, (Integer) firstRollResponse.getBody());

        bowlingGameService.makeSecondRoll(
                BowlingRoll.builder()
                        .playerNickname(player.getNickname())
                        .frameId(firstFrameId)
                        .score("1")
                        .build());

        var secondRollResponse =
                bowlingManagementService.getPlayerFrameScore(player.getNickname(), firstFrameId);
        assertEquals(HttpStatus.OK, secondRollResponse.getStatusCode());
        assertTrue(secondRollResponse.hasBody());

        assertEquals(2, (Integer) secondRollResponse.getBody());
    }

    @Test
    public void verifyThrowsValidationExceptionPlayerShouldMakeFirstRoll() {
        var player = addPlayer("verifyThrowsValidationExceptionPlayerShouldMakeFirstRoll");

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeSecondRoll(
                        BowlingRoll.builder()
                                .playerNickname(player.getNickname())
                                .frameId(0)
                                .score("1")
                                .build()));
    }

    @Test
    public void verifyThrowsValidationExceptionFrameRollResultAlreadyReported() {
        var player = addPlayer("verifyThrowsValidationExceptionFrameRollResultAlreadyReported");
        var firstFrameId = 0;

        var firstBowlingRoll = BowlingRoll.builder()
                .playerNickname(player.getNickname())
                .frameId(firstFrameId)
                .score("1")
                .build();

        bowlingGameService.makeFirstRoll(firstBowlingRoll);

        var firstRollResponse =
                bowlingManagementService.getPlayerFrameScore(player.getNickname(), firstFrameId);
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
                bowlingGameService.makeFirstRoll(
                        BowlingRoll.builder()
                                .playerNickname(player.getNickname())
                                .frameId(-1)
                                .score("1")
                                .build()));


        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRoll.builder()
                        .playerNickname(player.getNickname())
                        .frameId(11)
                        .score("1")
                        .build()));
    }

    @Test
    public void verifyThrowsValidationExceptionInvalidScoreValue() {
        var player = addPlayer("verifyThrowsValidationExceptionInvalidScoreValue");

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRoll.builder()
                                .playerNickname(player.getNickname())
                                .frameId(0)
                                .score("abc")
                                .build()
                ));

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRoll.builder()
                        .playerNickname(player.getNickname())
                        .frameId(0)
                        .score("-1")
                        .build()));

        assertThrowsBowlingValidationException(() ->
                bowlingGameService.makeFirstRoll(
                        BowlingRoll.builder()
                        .playerNickname(player.getNickname())
                        .frameId(0)
                        .score("22")
                        .build()));
    }


    private Player addPlayer(Object nickNamePrefix) {
        var response = bowlingManagementService.addPlayer(
                String.format(TEST_PLAYER_NICK_NAME, nickNamePrefix));

        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return (Player) response.getBody();
    }

    private void assertThrowsBowlingValidationException(Executable executable) {
        assertThrows(BowlingValidationException.class, executable);
    }

    @Import({InternalBowlingManagementService.class, InternalBowlingGameService.class})
    static class Config {

    }
}
