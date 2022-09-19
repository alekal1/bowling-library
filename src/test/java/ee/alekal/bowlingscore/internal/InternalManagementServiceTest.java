package ee.alekal.bowlingscore.internal;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.dto.api.BowlingRollRequest;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.utils.InternalBowlingBaseTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InternalManagementServiceTest extends InternalBowlingBaseTest {

    @RepeatedTest(3)
    public void verifyCanAddPlayers(RepetitionInfo repetitionInfo) {
        addPlayer(repetitionInfo.getCurrentRepetition());
    }

    @Test
    public void verifyFramesAreInitializedWhenPlayerIsAdded() {
        var player = addPlayer();
        assertEquals(BOWLING_BOARD_SIZE, player.getFrames().size());
    }

    @Test
    public void verifyAllPlayersAreSavedInBowlingStorage() {
        addPlayer("verifyAllPlayersAreSavedInBowlingStorage-1");
        addPlayer("verifyAllPlayersAreSavedInBowlingStorage-2");
        addPlayer("verifyAllPlayersAreSavedInBowlingStorage-3");

        assertEquals(3, InternalBowlingStorage.getPlayers().size());
    }

    @Test
    public void verifyCanRemoveAllPlayers() {
        addPlayer("verifyCouldRemoveAllPlayers-1");
        addPlayer("verifyCouldRemoveAllPlayers-2");

        assertEquals(2, InternalBowlingStorage.getPlayers().size());

        var response = bowlingManagementService.removeAllPlayers();
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(0, InternalBowlingStorage.getPlayers().size());
    }

    @Test
    public void verifyCanGetAllPlayers() {
        addPlayer("verifyCouldGetAllPlayers-1");
        addPlayer("verifyCouldGetAllPlayers-2");

        assertEquals(2, InternalBowlingStorage.getPlayers().size());

        var response = bowlingManagementService.getAllPlayers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        @SuppressWarnings("unchecked")
        var responseBody = (List<Player>) response.getBody();
        assertEquals(InternalBowlingStorage.getPlayers().size(),
                responseBody.size());
    }

    @Test
    public void verifySpecificPlayerReturnedSuccessfully() {
        var player = addPlayer();

        var response = bowlingManagementService.getPlayerByNickname(
                player.getNickname());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        assertEquals(player, response.getBody());
    }

    @Test
    public void verifyCanRemovePlayer() {
        var player = addPlayer();

        assertEquals(1, InternalBowlingStorage.getPlayers().size());
        assertNotNull(InternalBowlingStorage.getPlayerByNickname(player.getNickname()));

        bowlingManagementService.removePlayerByNickname(player.getNickname());

        assertEquals(0, InternalBowlingStorage.getPlayers().size());
        assertNull(InternalBowlingStorage.getPlayerByNickname(player.getNickname()));
    }

    @Test
    public void verifyPlayerTotalFrameScoreReturnedSuccessfully() {
        var player = addPlayer();

        bowlingGameService.makeFirstRoll(
                BowlingRollRequest.builder()
                        .playerNickname(player.getNickname())
                        .score("1")
                        .build());

        var firstRollResponse =
                bowlingManagementService.getPlayerFrameScore(
                        player.getNickname(),
                        gameBehaviour.getCurrentFrame());

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
    public void verifyPlayerTotalScoreReturnedCorrectly() {
        var player = addPlayer();

        var request = BowlingRollRequest.builder()
                .playerNickname(player.getNickname())
                .score("1")
                .build();

        for (int i = 0; i < 2; i++) {
            bowlingGameService.makeFirstRoll(request);
            bowlingGameService.makeSecondRoll(request);
        }

        var response = bowlingManagementService.getPlayerTotalScore(player.getNickname());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        var body = (Integer) response.getBody();

        assertEquals(4, body);
    }
}
