package ee.alekal.bowlingscore.constants;

public class Constants {
    // TODO: Could be configurable?
    public static final Integer BOWLING_BOARD_SIZE = 10;

    // Controllers
    public static final String BASE_PATH = "/api/v1";
    public static final String BOWLING_MANAGEMENT_PATH = BASE_PATH + "/bowling-management";
    public static final String BOWLING_GAME_PATH = BASE_PATH + "/bowling-game";

    // Exceptions
    public static final String ERR_PLAYER_ALREADY_REGISTERED = "Player %s already registered!";
    public static final String ERR_PLAYER_NOT_REGISTERED = "Player %s is not registered!";
    public static final String ERR_FRAME_DOES_NOT_EXISTS = "Frame with id %s does not exists!";
    public static final String ERR_FRAME_ROLL_RESULT_ALREADY_REPORTED
            = "Frame with id %s already has result in %s roll!";
    public static final String ERR_NOT_VALID_SCORE = "Score value %s is not valid!";
    public static final String ERR_PLAYER_SHOULD_MAKE_FIRST_ROLL = "Player %s should make first roll in frame %s";
    public static final String ERR_GAME_IS_ENDED = "Cannot perform roll on next frame due to game is ended!";
    public static final String ERR_INVALID_TOTAL_SCORE_VALUE
            = "Total score value per frame could to be greater than %s !";


}
