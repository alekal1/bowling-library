package ee.alekal.bowlingscore.util;

public class Constants {
    public static final String BASE_PATH = "/api/v1";
    public static final String BOWLING_PATH = BASE_PATH + "/bowling";

    // Exceptions
    public static final String ERR_PLAYER_ALREADY_REGISTERED = "Player %s already registered!";
    public static final String ERR_PLAYER_NOT_REGISTERED = "Player %s is not registered!";
    public static final String ERR_FRAME_DOES_NOT_EXISTS = "Frame with id %s does not exists!";
}
