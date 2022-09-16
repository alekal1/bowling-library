package ee.alekal.bowlingscore.internal.validation;

import ee.alekal.bowlingscore.exception.frame.FrameDoesNotExistException;
import ee.alekal.bowlingscore.exception.player.PlayerAlreadyRegisteredException;
import ee.alekal.bowlingscore.exception.player.PlayerNotRegisteredException;
import ee.alekal.bowlingscore.internal.db.InternalBowlingStorage;
import lombok.val;

import static ee.alekal.bowlingscore.internal.predicates.PlayerPredicates.filterByNamePredicate;

public class ValidationService {

    public static Validator withNickName(String nickname) {
        return new Validator(nickname, null);
    }

    public static Validator withNickNameAndFrameId(String nickname, Integer frameId) {
        return new Validator(nickname, frameId);
    }

    public static class Validator {
        String nickname;
        Integer frameId;

        private Validator(String nickname, Integer frameId) {
            this.nickname = nickname;
            this.frameId = frameId;
        }

        public Validator canRegisterToInternalStorage() {
            InternalBowlingStorage.getPlayers().stream()
                    .filter(filterByNamePredicate(nickname))
                    .findAny()
                    .ifPresent(p -> {
                        throw new PlayerAlreadyRegisteredException(p.getNickname());
                    });

            return this;
        }

        public Validator playerIsRegisteredInInternalStorage() {
            var registeredPlayer = InternalBowlingStorage.getPlayers().stream()
                    .filter(filterByNamePredicate(nickname))
                    .findAny();
            if (registeredPlayer.isEmpty()) {
                throw new PlayerNotRegisteredException(nickname);
            }

            return this;
        }

        public Validator frameExistsInStorage() {
            val player = InternalBowlingStorage.getPlayer(nickname);

            if (frameId > player.getFrames().size()) {
                throw new FrameDoesNotExistException(frameId);
            }

            return this;
        }
    }
}
