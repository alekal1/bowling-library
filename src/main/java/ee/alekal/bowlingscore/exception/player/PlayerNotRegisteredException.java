package ee.alekal.bowlingscore.exception.player;

import static ee.alekal.bowlingscore.util.Constants.ERR_PLAYER_NOT_REGISTERED;

public class PlayerNotRegisteredException extends IllegalArgumentException {

    public PlayerNotRegisteredException(String nickname) {
        super(String.format(ERR_PLAYER_NOT_REGISTERED, nickname));
    }
}
