package ee.alekal.bowlingscore.service;

import org.springframework.http.ResponseEntity;

public interface BowlingService {

    ResponseEntity<?> addPlayer(String nickname);
    ResponseEntity<?> getPlayer(String nickname);
    ResponseEntity<?> getAllPlayers();
    ResponseEntity<?> getPlayerFrameScore(String nickname, Integer frameId);
    ResponseEntity<?> getPlayerTotalScore(String nickname);
}
