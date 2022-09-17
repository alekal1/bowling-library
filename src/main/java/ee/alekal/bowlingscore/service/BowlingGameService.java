package ee.alekal.bowlingscore.service;

import ee.alekal.bowlingscore.dto.BowlingRoll;
import org.springframework.http.ResponseEntity;

public interface BowlingGameService {

    ResponseEntity<?> makeFirstRoll(BowlingRoll roll);
    ResponseEntity<?> makeSecondRoll(BowlingRoll roll);
}
