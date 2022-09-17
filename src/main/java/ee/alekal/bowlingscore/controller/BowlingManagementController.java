package ee.alekal.bowlingscore.controller;

import ee.alekal.bowlingscore.service.BowlingManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_MANAGEMENT_PATH;

@RestController
@RequestMapping(BOWLING_MANAGEMENT_PATH)
@RequiredArgsConstructor
public class BowlingManagementController {

    private final BowlingManagementService bowlingService;

    @PostMapping("/{nickname}")
    public void addPlayer(@PathVariable("nickname") String nickname) {
        bowlingService.addPlayer(nickname);
    }

    @GetMapping
    public ResponseEntity<?> getAllPlayers() {
        return bowlingService.getAllPlayers();
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<?> getPlayer(@PathVariable("nickname") String nickname) {
        return bowlingService.getPlayer(nickname);
    }

    @GetMapping("/{nickname}/{frameId}")
    public ResponseEntity<?> getPlayerFrameScore(@PathVariable("nickname") String nickname,
                                                 @PathVariable("frameId") Integer frameId) {
        return bowlingService.getPlayerFrameScore(nickname, frameId);
    }

    @GetMapping("/{nickname}/total")
    public ResponseEntity<?> getPlayerTotalScore(@PathVariable("nickname") String nickname) {
        return bowlingService.getPlayerTotalScore(nickname);
    }
}
