package ee.alekal.bowlingscore.internal.handler;

import ee.alekal.bowlingscore.dto.api.ErrorResponse;
import ee.alekal.bowlingscore.exception.game.BowlingGameIsEndedException;
import ee.alekal.bowlingscore.exception.game.BowlingValidationException;
import ee.alekal.bowlingscore.exception.frame.FrameDoesNotExistException;
import ee.alekal.bowlingscore.exception.frame.FrameRollResultAlreadyReportedException;
import ee.alekal.bowlingscore.exception.score.InvalidScoreValueException;
import ee.alekal.bowlingscore.exception.player.PlayerAlreadyRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerNotRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerShouldMakeFirstRollException;
import ee.alekal.bowlingscore.exception.score.InvalidTotalScoreValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final String ERR_PLAYER_CLASSIFIER = "player:err";
    private static final String ERR_FRAME_CLASSIFIER = "frame:err";
    private static final String ERR_SCORE_CLASSIFIER = "score:err";
    private static final String ERR_GAME_CLASSIFIER = "game:err";

    @ExceptionHandler({
            PlayerAlreadyRegisteredException.class,
            PlayerNotRegisteredException.class,
            PlayerShouldMakeFirstRollException.class})
    public ResponseEntity<ErrorResponse> handlePlayerExceptions(BowlingValidationException e) {
        return new ResponseEntity<>(new ErrorResponse(ERR_PLAYER_CLASSIFIER, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            FrameDoesNotExistException.class,
            FrameRollResultAlreadyReportedException.class})
    public ResponseEntity<ErrorResponse> handleFrameExceptions(BowlingValidationException e) {
        return new ResponseEntity<>(new ErrorResponse(ERR_FRAME_CLASSIFIER, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            InvalidScoreValueException.class,
            InvalidTotalScoreValueException.class})
    public ResponseEntity<ErrorResponse> handleScoreExceptions(BowlingValidationException e) {
        return new ResponseEntity<>(new ErrorResponse(ERR_SCORE_CLASSIFIER, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BowlingGameIsEndedException.class)
    public ResponseEntity<ErrorResponse> handleGameExceptions(BowlingValidationException e) {
        return new ResponseEntity<>(new ErrorResponse(ERR_GAME_CLASSIFIER, e.getMessage()),
                HttpStatus.ALREADY_REPORTED);
    }
}
