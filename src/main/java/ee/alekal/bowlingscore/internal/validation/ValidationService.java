package ee.alekal.bowlingscore.internal.validation;

import ee.alekal.bowlingscore.dto.type.FrameRollQueueType;
import ee.alekal.bowlingscore.exception.BowlingValidationException;
import ee.alekal.bowlingscore.exception.frame.FrameDoesNotExistException;
import ee.alekal.bowlingscore.exception.frame.FrameRollResultAlreadyReportedException;
import ee.alekal.bowlingscore.exception.score.InvalidScoreValueException;
import ee.alekal.bowlingscore.exception.player.PlayerAlreadyRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerNotRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerShouldMakeFirstRollException;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.Set;

@UtilityClass
public class ValidationService {

    public static Validator withNickName(String nickname) {
        return new Validator(nickname, null);
    }

    public static Validator withNickNameAndFrameId(String nickname, Integer frameId) {
        return new Validator(nickname, frameId);
    }

    // TODO: A lot of InternalBowlingStorage.getPlayer(nickname), maybe use that only in constructor?
    public static class Validator {
        String nickname;
        Integer frameId;

        private Validator(String nickname, Integer frameId) {
            this.nickname = nickname;
            this.frameId = frameId;
        }

        public Validator canRegisterToInternalStorage() {
            checkConstraint(InternalBowlingStorage.getPlayer(nickname) != null,
                    new PlayerAlreadyRegisteredException(nickname));

            return this;
        }

        public Validator playerIsRegisteredInInternalStorage() {
            checkConstraint(InternalBowlingStorage.getPlayer(nickname) == null,
                    new PlayerNotRegisteredException(nickname));

            return this;
        }

        public Validator frameExistsInStorage() {
            val player = InternalBowlingStorage.getPlayer(nickname);

            checkConstraint(frameId < 0,
                    new FrameDoesNotExistException(frameId));

            checkConstraint(frameId > player.getFrames().size(),
                    new FrameDoesNotExistException(frameId));

            return this;
        }

        public Validator isValidScore(String score) {
            if (Set.of("/", "X").contains(score)) {
                return this;
            }

            Integer integerScore = null;
            try {
                integerScore = Integer.valueOf(score);
            } catch (NumberFormatException e) {
                throw new InvalidScoreValueException(score);
            }

            checkConstraint(integerScore < 0 || integerScore > 9,
                    new InvalidScoreValueException(score));

            // TODO: Calculate sum of two roll

            return this;
        }

        public Validator canMakeRoll(FrameRollQueueType rollQueueType) {
            val player = InternalBowlingStorage.getPlayer(nickname);
            switch (rollQueueType) {
                case FIRST_ROLL:
                    checkConstraint(
                            player.getFrameFirstRollScore(frameId) != null,
                            new FrameRollResultAlreadyReportedException(frameId, rollQueueType));
                    break;
                case SECOND_ROLL:
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
