package ee.alekal.bowlingscore.internal.db;

import ee.alekal.bowlingscore.dto.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


/**
 * NB! There is no database connectivity in internal package yet. Players' information is stored as a list.
 *
 * You could create your own BowlingManagementService and BowlingGameService implementation and add database connection.
 *
 * @see ee.alekal.bowlingscore.service.BowlingManagementService
 * @see ee.alekal.bowlingscore.service.BowlingGameService
 */
public class InternalBowlingStorage {

    @Getter
    protected static final List<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static Player getPlayerByNickname(String nickname) {
        return players.stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
    }

    public static void removePlayerByNickname(String nickname) {
        players.removeIf(player -> player.getNickname().equals(nickname));
    }

    public static void removeAllPlayers() {
        players.clear();
    }
}
