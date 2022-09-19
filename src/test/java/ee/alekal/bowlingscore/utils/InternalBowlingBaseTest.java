package ee.alekal.bowlingscore.utils;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.exception.game.BowlingValidationException;
import ee.alekal.bowlingscore.internal.blogic.GameBehaviour;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.service.BowlingGameService;
import ee.alekal.bowlingscore.service.BowlingManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = InternalBowlingBaseTest.Config.class)
public class InternalBowlingBaseTest {

    protected static final String TEST_PLAYER_RAW_NICK_NAME = "test-%s:internal";

    @Autowired
    protected BowlingManagementService bowlingManagementService;

    @Autowired
    protected BowlingGameService bowlingGameService;

    @Autowired
    protected GameBehaviour gameBehaviour;

    @BeforeEach
    public void init() {
        InternalBowlingStorage.removeAllPlayers();
        gameBehaviour.afterPropertiesSet();
    }

    protected Player addPlayer() {
        return addPlayer("");
    }

    protected Player addPlayer(Object nickNamePrefix) {
        var response = bowlingManagementService.addPlayerByNickname(
                String.format(TEST_PLAYER_RAW_NICK_NAME, nickNamePrefix));

        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return (Player) response.getBody();
    }

    protected void assertThrowsBowlingValidationException(Executable executable) {
        assertThrows(BowlingValidationException.class, executable);
    }

    @ComponentScan(basePackages = "ee.alekal.bowlingscore.internal")
    static class Config {
    }
}
