package ee.alekal.bowlingscore.controller;

import ee.alekal.bowlingscore.dto.BowlingRollRequest;
import ee.alekal.bowlingscore.service.BowlingGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ee.alekal.bowlingscore.constants.Constants.BOWLING_GAME_PATH;

@RestController
@RequestMapping(BOWLING_GAME_PATH)
@RequiredArgsConstructor
public class BowlingGameController {

    private final BowlingGameService bowlingGameService;

    @PostMapping("/firstRoll")
    public ResponseEntity<?> makeFirstRoll(@RequestBody BowlingRollRequest roll) {
        return bowlingGameService.makeFirstRoll(roll);
    }

    @PostMapping("/secondRoll")
    public ResponseEntity<?> makeSecondRoll(@RequestBody BowlingRollRequest roll) {
        return bowlingGameService.makeSecondRoll(roll);
    }
}
