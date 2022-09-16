package ee.alekal.bowlingscore.internal.service;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import ee.alekal.bowlingscore.service.BowlingService;
import ee.alekal.bowlingscore.internal.validation.ValidationService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InternalBowlingService implements BowlingService {

    @Override
    public ResponseEntity<Player> addPlayer(String nickname) {
        log.info("addPlayer, nickname {}", nickname);

        ValidationService
                .withNickName(nickname)
                .canRegisterToInternalStorage();

        val player = new Player(nickname);
        InternalBowlingStorage.addPlayer(player);

        return ResponseEntity.ok(player);
    }

    @Override
    public ResponseEntity<?> getPlayer(String nickname) {
        log.info("getPlayer, nickname {}", nickname);

        ValidationService
                .withNickName(nickname)
                .playerIsRegisteredInInternalStorage();

        val player = InternalBowlingStorage.getPlayer(nickname);

        return ResponseEntity.ok(player);
    }

    @Override
    public ResponseEntity<?> getAllPlayers() {
        log.info("getAllPlayers");

        return ResponseEntity.ok(InternalBowlingStorage.getPlayers());
    }

    @Override
    public ResponseEntity<Integer> getPlayerTotalScore(String nickname) {
        log.info("getPlayersTotalScore, nickname {}", nickname);

        ValidationService
                .withNickName(nickname)
                .playerIsRegisteredInInternalStorage();

        val player = InternalBowlingStorage.getPlayer(nickname);

        return ResponseEntity.ok(player.getTotalScore());
    }

    @Override
    public ResponseEntity<?> getPlayerFrameScore(String nickname, Integer frameId) {
        log.info("getPlayersFrameScore, nickname {}, frameId {}", nickname, frameId);

        ValidationService
                .withNickNameAndFrameId(nickname, frameId)
                .playerIsRegisteredInInternalStorage()
                .frameExistsInStorage();

        val player = InternalBowlingStorage.getPlayer(nickname);

        val frameScore = player.getFrames().get(frameId).getFrameScore();

        return ResponseEntity.ok(frameScore);
    }
}
