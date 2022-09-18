package ee.alekal.bowlingscore.exception.score;

import ee.alekal.bowlingscore.exception.BowlingValidationException;

import static ee.alekal.bowlingscore.constants.Constants.ERR_INVALID_TOTAL_SCORE_VALUE;

public class InvalidTotalScoreValueException extends BowlingValidationException {

    public InvalidTotalScoreValueException(String boardSize) {
        super(String.format(ERR_INVALID_TOTAL_SCORE_VALUE, boardSize));
    }
}
