package ee.alekal.bowlingscore.exception.game;


import static ee.alekal.bowlingscore.constants.Constants.ERR_GAME_IS_ENDED;

public class BowlingGameIsEndedException extends BowlingValidationException {

    public BowlingGameIsEndedException(String s) {
        super(ERR_GAME_IS_ENDED);
    }
}
