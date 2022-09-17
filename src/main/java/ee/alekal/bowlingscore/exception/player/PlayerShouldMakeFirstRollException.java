package ee.alekal.bowlingscore.exception.player;

import ee.alekal.bowlingscore.exception.BowlingValidationException;

import static ee.alekal.bowlingscore.constants.Constants.ERR_PLAYER_SHOULD_MAKE_FIRST_ROLL;

public class PlayerShouldMakeFirstRollException extends BowlingValidationException {

    public PlayerShouldMakeFirstRollException(String nickname, Integer frameId) {
        super(String.format(ERR_PLAYER_SHOULD_MAKE_FIRST_ROLL, nickname, frameId));

    }
}
