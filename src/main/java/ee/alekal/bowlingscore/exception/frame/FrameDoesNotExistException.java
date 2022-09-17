package ee.alekal.bowlingscore.exception.frame;

import ee.alekal.bowlingscore.exception.BowlingValidationException;

import static ee.alekal.bowlingscore.constants.Constants.ERR_FRAME_DOES_NOT_EXISTS;

public class FrameDoesNotExistException extends BowlingValidationException {

    public FrameDoesNotExistException(Integer frameId) {
        super(String.format(ERR_FRAME_DOES_NOT_EXISTS, frameId));
    }
}
