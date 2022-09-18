package ee.alekal.bowlingscore.exception.score;

import ee.alekal.bowlingscore.exception.game.BowlingValidationException;

import static ee.alekal.bowlingscore.constants.Constants.ERR_NOT_VALID_SCORE;

public class InvalidScoreValueException extends BowlingValidationException {

    public InvalidScoreValueException(String score) {
        super(String.format(ERR_NOT_VALID_SCORE, score));
    }
}
