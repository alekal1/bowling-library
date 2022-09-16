package ee.alekal.bowlingscore.exception.frame;

import static ee.alekal.bowlingscore.util.Constants.ERR_FRAME_DOES_NOT_EXISTS;

public class FrameDoesNotExistException extends IllegalArgumentException {

    public FrameDoesNotExistException(Integer frameId) {
        super(String.format(ERR_FRAME_DOES_NOT_EXISTS, frameId));
    }
}
