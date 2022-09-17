package ee.alekal.bowlingscore.exception.frame;

import ee.alekal.bowlingscore.dto.type.FrameRollQueueType;
import ee.alekal.bowlingscore.exception.BowlingValidationException;

import static ee.alekal.bowlingscore.constants.Constants.ERR_FRAME_ROLL_RESULT_ALREADY_REPORTED;

public class FrameRollResultAlreadyReportedException extends BowlingValidationException {

    public FrameRollResultAlreadyReportedException(Integer frameId, FrameRollQueueType queueType) {
        super(String.format(ERR_FRAME_ROLL_RESULT_ALREADY_REPORTED, frameId, queueType.name()));
    }
}
