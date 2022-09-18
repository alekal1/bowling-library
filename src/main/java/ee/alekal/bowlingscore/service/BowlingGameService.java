package ee.alekal.bowlingscore.service;

import ee.alekal.bowlingscore.dto.api.BowlingRollRequest;
import org.springframework.http.ResponseEntity;

public interface BowlingGameService {

    ResponseEntity<?> makeFirstRoll(BowlingRollRequest roll);
    ResponseEntity<?> makeSecondRoll(BowlingRollRequest roll);
}
