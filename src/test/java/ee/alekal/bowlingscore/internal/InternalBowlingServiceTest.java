package ee.alekal.bowlingscore.internal;

import ee.alekal.bowlingscore.dto.BowlingRollRequest;
import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.exception.BowlingValidationException;
import ee.alekal.bowlingscore.internal.blogic.GameBehaviour;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.internal.service.InternalBowlingGameService;
import ee.alekal.bowlingscore.internal.service.InternalBowlingManagementService;
import ee.alekal.bowlingscore.service.BowlingGameService;
import ee.alekal.bowlingscore.service.BowlingManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = InternalBowlingServiceTest.Config.class)
public class InternalBowlingServiceTest {

    protected static final String TEST_PLAYER_RAW_NICK_NAME = "test-%s:internal";

    @Autowired
    BowlingManagementService bowlingManagementService;

    @Autowired
    BowlingGameService bowlingGameService;

    @BeforeEach
    public void init() {
        InternalBowlingStorage.removeAllPlayers();
        GameBehaviour.clearInstance();
    }

    @Test
    public void verifyAllPlayersReturnedSuccessfully() {
        addPlayer("verifyAllPlayersReturnedSuccessfully-1");
        addPlayer("verifyAllPlayersReturnedSuccessfully-2");
        addPlayer("verifyAllPlayersReturnedSuccessfully-3");

        assertEquals(3, InternalBowlingStorage.getPlayers().size());
    }

    @RepeatedTest(3)
    public void verifyCanAddPlayers(RepetitionInfo repetitionInfo) {
        var player = addPlayer(repetitionInfo.getCurrentRepetition());
        assertEquals(BOWLING_BOARD_SIZE, player.getFrames().size());
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

        var response = bowlingManagementService.getPlayerByNickname(
                player.getNickname());

        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(player, response.getBody());
    }

    @Test
    public void verifyPlayerTotalFrameScoreReturnedSuccessfully() {
        var player = addPlayer("verifyPlayerTotalFrameScoreReturnedSuccessfully");

        bowlingGameService.makeFirstRoll(
                BowlingRollRequest.builder()
                        .playerNickname(player.getNickname())
                        .score("1")
                        .build());

        var firstRollResponse =
                bowlingManagementService.getPlayerFrameScore(
                        player.getNickname(),
                        GameBehaviour.getInstance().getCurrentFrame());

        assertEquals(HttpStatus.OK, firstRollResponse.getStatusCode());
        assertTrue(firstRollResponse.hasBody());

        assertEquals(1, (Integer) firstRollResponse.getBody());

        bowlingGameService.makeSecondRoll(
                BowlingRollRequest.builder()
                        .playerNickname(player.getNickname())
                        .score("1")
                        .build());

        // NB! Current frame is updated after second shot, do not use getCurrentFrame()
        var secondRollResponse =
                bowlingManagementService.getPlayerFrameScore(
                        player.getNickname(), 0);
        assertEquals(HttpStatus.OK, secondRollResponse.getStatusCode());
        assertTrue(secondRollResponse.hasBody());

        assertEquals(2, (Integer) secondRollResponse.getBody());
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
        var player = addPlayer("verifyThrowsValidationExceptionFrameRollResultAlreadyReported");;

        var firstBowlingRoll = BowlingRollRequest.builder()
                .playerNickname(player.getNickname())
                .score("1")
                .build();

        bowlingGameService.makeFirstRoll(firstBowlingRoll);

        var firstRollResponse =
                bowlingManagementService.getPlayerFrameScore(
                        player.getNickname(),
                        GameBehaviour.getInstance().getCurrentFrame());
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
    public void verifyCanRemovePlayer() {
        var player = addPlayer("verifyCanRemovePlayer");

        assertEquals(1, InternalBowlingStorage.getPlayers().size());
        assertNotNull(InternalBowlingStorage.getPlayerByNickname(player.getNickname()));

        bowlingManagementService.removePlayerByNickname(player.getNickname());

        assertEquals(0, InternalBowlingStorage.getPlayers().size());
        assertNull(InternalBowlingStorage.getPlayerByNickname(player.getNickname()));
    }

    @Test
    public void verifyCannotDeleteUnregisteredPlayer() {
        assertThrowsBowlingValidationException(() -> {
            bowlingManagementService.removePlayerByNickname("verifyCannotDeleteUnregisteredPlayer");
        });
    }


    @Test
    public void verifyCurrentFrameIsUpdatedAfterAllPlayersRoll() {
        assertEquals(0, GameBehaviour.getInstance().getCurrentFrame());

        var bowlingPlayers = List.of(
                addPlayer("verifyCurrentFrameIsUpdatedAfterAllPlayersRoll-1"),
                addPlayer("verifyCurrentFrameIsUpdatedAfterAllPlayersRoll-2"),
                addPlayer("verifyCurrentFrameIsUpdatedAfterAllPlayersRoll-3")
        );

        for (Player player : bowlingPlayers) {
            bowlingGameService.makeFirstRoll(
                    BowlingRollRequest.builder()
                            .playerNickname(player.getNickname())
                            .score("3")
                            .build());

            bowlingGameService.makeSecondRoll(
                    BowlingRollRequest.builder()
                            .playerNickname(player.getNickname())
                            .score("3")
                            .build());
        }

        assertEquals(1, GameBehaviour.getInstance().getCurrentFrame());
    }

    @Test
    public void verifyThrowsValidationExceptionInvalidCurrentFrame() {

    }


    private Player addPlayer(Object nickNamePrefix) {
        var response = bowlingManagementService.addPlayerByNickname(
                String.format(TEST_PLAYER_RAW_NICK_NAME, nickNamePrefix));

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
