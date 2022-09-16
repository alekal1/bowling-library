package ee.alekal.bowlingscore.exception.player;

import static ee.alekal.bowlingscore.util.Constants.ERR_PLAYER_ALREADY_REGISTERED;

public class PlayerAlreadyRegisteredException extends IllegalArgumentException {

    public PlayerAlreadyRegisteredException(String nickname) {
        super(String.format(ERR_PLAYER_ALREADY_REGISTERED, nickname));
    }
}
