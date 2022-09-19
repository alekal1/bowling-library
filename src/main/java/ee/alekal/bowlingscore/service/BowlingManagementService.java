package ee.alekal.bowlingscore.service;

import org.springframework.http.ResponseEntity;

public interface BowlingManagementService {

    ResponseEntity<?> addPlayerByNickname(String nickname);

    ResponseEntity<?> getPlayerByNickname(String nickname);

    ResponseEntity<?> getAllPlayers();

    ResponseEntity<?> getPlayerFrameScore(String nickname, Integer frameId);

    ResponseEntity<?> getPlayerTotalScore(String nickname);

    ResponseEntity<?> removePlayerByNickname(String nickname);

    ResponseEntity<?> removeAllPlayers();
}
