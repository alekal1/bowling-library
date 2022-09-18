package ee.alekal.bowlingscore.internal.validation;

import ee.alekal.bowlingscore.dto.Player;
import ee.alekal.bowlingscore.dto.type.RollQueueType;
import ee.alekal.bowlingscore.exception.BowlingGameIsEndedException;
import ee.alekal.bowlingscore.exception.BowlingValidationException;
import ee.alekal.bowlingscore.exception.frame.FrameDoesNotExistException;
import ee.alekal.bowlingscore.exception.frame.FrameRollResultAlreadyReportedException;
import ee.alekal.bowlingscore.exception.score.InvalidScoreValueException;
import ee.alekal.bowlingscore.exception.player.PlayerAlreadyRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerNotRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerShouldMakeFirstRollException;
import ee.alekal.bowlingscore.exception.score.InvalidTotalScoreValueException;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import lombok.experimental.UtilityClass;


import static ee.alekal.bowlingscore.constants.Constants.BOWLING_BOARD_SIZE;

@UtilityClass
public class ValidationService {

    public static Validator withNickName(String nickname) {
        return new Validator(nickname, null);
    }

    public static Validator withNickNameAndFrameId(String nickname, Integer frameId) {
        return new Validator(nickname, frameId);
    }

    public static class Validator {
        Player player;

        String nickname;
        Integer frameId;

        private Validator(String nickname, Integer frameId) {
            this.player = InternalBowlingStorage.getPlayerByNickname(nickname);
            this.nickname = nickname;
            this.frameId = frameId;
        }

        public Validator canRegisterToInternalStorage() {
            checkConstraint(player != null,
                    new PlayerAlreadyRegisteredException(nickname));

            return this;
        }

        public Validator playerIsRegisteredInInternalStorage() {
            checkConstraint(player == null,
                    new PlayerNotRegisteredException(nickname));

            return this;
        }

        public Validator frameExistsInInternalStorage() {
            checkConstraint(frameId < 0,
                    new FrameDoesNotExistException(frameId));

            checkConstraint(frameId > player.getFrames().size(),
                    new FrameDoesNotExistException(frameId));

            return this;
        }

        public Validator gameIsNotEnded() {
            checkConstraint(frameId.equals(player.getFrames().size()),
                    // Current frame is 10, to display message correctly +1 was added
                    new BowlingGameIsEndedException(null));
            return this;
        }

        // TODO: Add check if current player already moved on current frame

        public Validator isValidScore(String score) {
            int integerScore;
            try {
                integerScore = Integer.parseInt(score);
            } catch (NumberFormatException e) {
                throw new InvalidScoreValueException(score);
            }

            checkConstraint(integerScore < 0 || integerScore > 10,
                    new InvalidScoreValueException(score));

            checkConstraint((player.getFrameTotalScore(frameId) + integerScore) > 10,
                    new InvalidTotalScoreValueException(String.valueOf(BOWLING_BOARD_SIZE)));

            return this;
        }

        public Validator canMakeRoll(RollQueueType rollQueueType) {
            switch (rollQueueType) {
                case FRAME_FIRST_ROLL:
                    checkConstraint(
                            player.getFrameFirstRollScore(frameId) != null,
                            new FrameRollResultAlreadyReportedException(frameId, rollQueueType));
                    break;
                case FRAME_SECOND_ROLL:
                    checkConstraint(
                            player.getFrameSecondRollScore(frameId) != null,
                            new FrameRollResultAlreadyReportedException(frameId, rollQueueType));
                    checkConstraint(
                            player.getFrameFirstRollScore(frameId) == null,
                            new PlayerShouldMakeFirstRollException(nickname, frameId)
                    );
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown roll type: " + rollQueueType);
            }
            return this;
        }

        private void checkConstraint(boolean constraint, BowlingValidationException e) throws BowlingValidationException {
            if (constraint) {
                throw e;
            }
        }
    }
}
