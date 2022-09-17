package ee.alekal.bowlingscore.exception.frame;

import ee.alekal.bowlingscore.exception.BowlingValidationException;
import ee.alekal.bowlingscore.internal.blogic.GameBehaviour;

import static ee.alekal.bowlingscore.constants.Constants.ERR_INVALID_CURRENT_FRAME;

public class InvalidCurrentFrameException extends BowlingValidationException {

    public InvalidCurrentFrameException(Integer frameId) {
        super(String.format(ERR_INVALID_CURRENT_FRAME, frameId, GameBehaviour.getInstance().getCurrentFrame()));
    }
}
