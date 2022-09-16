package ee.alekal.bowlingscore.internal.db;

import ee.alekal.bowlingscore.dto.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static ee.alekal.bowlingscore.internal.predicates.PlayerPredicates.filterByNamePredicate;

/**
 * NB! There is no database connectivity in internal package yet. Players' information is stored as a list.
 *
 * You could create own BowlingService implementation and add database connection.
 *
 * @see ee.alekal.bowlingscore.service.BowlingService
 */
public class InternalBowlingStorage {

    @Getter
    private static final List<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static Player getPlayer(String nickname) {
        return players.stream()
                .filter(filterByNamePredicate(nickname))
                .findFirst()
                .orElseThrow();
    }
}
