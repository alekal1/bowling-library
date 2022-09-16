package ee.alekal.bowlingscore.internal.predicates;

import ee.alekal.bowlingscore.dto.Player;

import java.util.function.Predicate;

public class PlayerPredicates {

    public static Predicate<Player> filterByNamePredicate(String name) {
        return player -> name.equals(player.getNickname());
    }
}
