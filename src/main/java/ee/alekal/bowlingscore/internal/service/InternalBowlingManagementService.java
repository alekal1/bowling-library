package ee.alekal.bowlingscore.internal.service;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.internal.blogic.GameBehaviour;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.service.BowlingManagementService;
import ee.alekal.bowlingscore.internal.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalBowlingManagementService implements BowlingManagementService {

    private final GameBehaviour gameBehaviour;

    @Override
    public ResponseEntity<Player> addPlayerByNickname(String nickname) {
        log.info("addPlayerByNickname, nickname {}", nickname);

        ValidationService
                .withNickName(nickname)
                .canRegisterToInternalStorage();

        val player = new Player(nickname);
        InternalBowlingStorage.addPlayer(player);

        return ResponseEntity.ok(player);
    }

    @Override
    public ResponseEntity<?> getPlayerByNickname(String nickname) {
        log.info("getPlayerByNickname, nickname {}", nickname);

        ValidationService
                .withNickName(nickname)
                .playerIsRegisteredInInternalStorage();

        val player = InternalBowlingStorage.getPlayerByNickname(nickname);

        return ResponseEntity.ok(player);
    }

    @Override
    public ResponseEntity<List<Player>> getAllPlayers() {
        log.info("getAllPlayers");
        return ResponseEntity.ok(InternalBowlingStorage.getPlayers());
    }

    @Override
    public ResponseEntity<Integer> getPlayerTotalScore(String nickname) {
        log.info("getPlayersTotalScore, nickname {}", nickname);

        ValidationService
                .withNickName(nickname)
                .playerIsRegisteredInInternalStorage();

        val player = InternalBowlingStorage.getPlayerByNickname(nickname);

        return ResponseEntity.ok(player.getTotalScore());
    }

    @Override
    public ResponseEntity<?> removePlayerByNickname(String nickname) {
        log.info("removePlayerByNickname, nickname {}", nickname);

        ValidationService
                .withNickName(nickname)
                .playerIsRegisteredInInternalStorage();

        InternalBowlingStorage.removePlayerByNickname(nickname);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeAllPlayers() {
        InternalBowlingStorage.removeAllPlayers();
        gameBehaviour.afterPropertiesSet();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getPlayerFrameScore(String nickname, Integer frameId) {
        log.info("getPlayersFrameScore, nickname {}, frameId {}", nickname, frameId);

        ValidationService
                .withNickNameAndFrameId(nickname, frameId)
                .playerIsRegisteredInInternalStorage()
                .frameExistsInInternalStorage();

        val player = InternalBowlingStorage.getPlayerByNickname(nickname);

        val frameScore = player.getFrameTotalScore(frameId);

        return ResponseEntity.ok(frameScore);
    }
}
