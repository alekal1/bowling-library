package ee.alekal.bowlingscore.internal;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.exception.player.PlayerAlreadyRegisteredException;
import ee.alekal.bowlingscore.internal.service.InternalBowlingService;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = InternalBowlingServiceTest.Config.class)
public class InternalBowlingServiceTest {

    private static final String TEST_PLAYER_NICK_NAME = "test-%s:nickname";

    @Autowired
    InternalBowlingService internalBowlingService;

    @RepeatedTest(3)
    public void verifyCanAddPlayer(RepetitionInfo repetitionInfo) {
        addPlayer(repetitionInfo.getCurrentRepetition());
    }

    @Test
    public void verifyCannotAddPlayerWithSameNickname() {
        var nickNamePrefix = "exception";

        addPlayer(nickNamePrefix);
        assertThrows(PlayerAlreadyRegisteredException.class, () -> addPlayer(nickNamePrefix));
    }

    private void addPlayer(Object nickNamePrefix) {
        var response = internalBowlingService.addPlayer(
                String.format(TEST_PLAYER_NICK_NAME, nickNamePrefix));

        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Import(InternalBowlingService.class)
    static class Config {

    }
}
