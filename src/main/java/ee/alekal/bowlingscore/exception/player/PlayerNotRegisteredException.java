package ee.alekal.bowlingscore.exception.player;

import ee.alekal.bowlingscore.exception.game.BowlingValidationException;

import static ee.alekal.bowlingscore.constants.Constants.ERR_PLAYER_NOT_REGISTERED;

public class PlayerNotRegisteredException extends BowlingValidationException {

    public PlayerNotRegisteredException(String nickname) {
        super(String.format(ERR_PLAYER_NOT_REGISTERED, nickname));
    }
}
