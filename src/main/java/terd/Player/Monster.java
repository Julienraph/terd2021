package terd.Player;

import terd.Map.Map;
import terd.Map.Pos;
import terd.Player.Player;

public interface Monster extends Props {
    void act(Pos posPlayer, Map map);
    String recompensePlayer(Player player);
}
