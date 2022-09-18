package ee.alekal.bowlingscore.internal.handler;

import ee.alekal.bowlingscore.dto.api.ErrorResponse;
import ee.alekal.bowlingscore.exception.frame.FrameDoesNotExistException;
import ee.alekal.bowlingscore.exception.frame.FrameRollResultAlreadyReportedException;
import ee.alekal.bowlingscore.exception.score.InvalidScoreValueException;
import ee.alekal.bowlingscore.exception.player.PlayerAlreadyRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerNotRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerShouldMakeFirstRollException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final String ERR_PLAYER_CLASSIFIER = "player:err";
    private static final String ERR_FRAME_CLASSIFIER = "frame:err";
    private static final String ERR_SCORE_CLASSIFIER = "score:err";

    @ExceptionHandler({
            PlayerAlreadyRegisteredException.class,
            PlayerNotRegisteredException.class,
            PlayerShouldMakeFirstRollException.class})
    public ResponseEntity<ErrorResponse> handlePlayerExceptions(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(ERR_PLAYER_CLASSIFIER, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            FrameDoesNotExistException.class,
            FrameRollResultAlreadyReportedException.class})
    public ResponseEntity<ErrorResponse> handleFrameExceptions(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(ERR_FRAME_CLASSIFIER, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidScoreValueException.class)
    public ResponseEntity<ErrorResponse> handleScoreExceptions(InvalidScoreValueException e) {
        return new ResponseEntity<>(new ErrorResponse(ERR_SCORE_CLASSIFIER, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
