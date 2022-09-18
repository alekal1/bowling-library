package ee.alekal.bowlingscore.exception.player;

import ee.alekal.bowlingscore.exception.game.BowlingValidationException;

import static ee.alekal.bowlingscore.constants.Constants.ERR_PLAYER_ALREADY_REGISTERED;

public class PlayerAlreadyRegisteredException extends BowlingValidationException {

    public PlayerAlreadyRegisteredException(String nickname) {
        super(String.format(ERR_PLAYER_ALREADY_REGISTERED, nickname));
    }
}
