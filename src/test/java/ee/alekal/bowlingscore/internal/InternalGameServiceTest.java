package ee.alekal.bowlingscore.internal;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.dto.api.BowlingRollRequest;
import ee.alekal.bowlingscore.utils.InternalBowlingBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InternalGameServiceTest extends InternalBowlingBaseTest {

    @Test
    public void verifyCurrentFrameIsUpdatedAfterAllPlayersRoll() {
        assertEquals(0, gameBehaviour.getCurrentFrame());

        var bowlingPlayers = List.of(
                addPlayer("verifyCurrentFrameIsUpdatedAfterAllPlayersRoll-1"),
                addPlayer("verifyCurrentFrameIsUpdatedAfterAllPlayersRoll-2"),
                addPlayer("verifyCurrentFrameIsUpdatedAfterAllPlayersRoll-3")
        );

        for (Player player : bowlingPlayers) {
            var firstRollResponse = bowlingGameService.makeFirstRoll(
                    BowlingRollRequest.builder()
                            .playerNickname(player.getNickname())
                            .score("3")
                            .build());

            assertEquals(HttpStatus.OK, firstRollResponse.getStatusCode());

            var secondRollResponse = bowlingGameService.makeSecondRoll(
                    BowlingRollRequest.builder()
                            .playerNickname(player.getNickname())
                            .score("3")
                            .build());

            assertEquals(HttpStatus.OK, secondRollResponse.getStatusCode());
        }

        assertEquals(1, gameBehaviour.getCurrentFrame());
    }

    @Test
    public void verifyUpdatesCurrentFrameIfFirstRollOnCurrentFrameWasStrike() {
        var player = addPlayer();

        assertEquals(0, gameBehaviour.getCurrentFrame());

        bowlingGameService.makeFirstRoll(
                BowlingRollRequest.builder()
                        .playerNickname(player.getNickname())
                        .score("10")
                        .build());

        assertEquals(1, gameBehaviour.getCurrentFrame());
    }
}
