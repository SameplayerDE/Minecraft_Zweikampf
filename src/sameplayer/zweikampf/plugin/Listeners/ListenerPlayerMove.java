package sameplayer.zweikampf.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import sameplayer.zweikampf.plugin.Enums.GameStates;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;

public class ListenerPlayerMove implements Listener {

    private Main plugin;
    private ZweikampfManager zweikampf;

    public ListenerPlayerMove(Main plugin) {
        this.plugin = plugin;
        this.zweikampf = this.plugin.getZweikampf();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (!zweikampf.isGoing()) {
            return;
        }

        if (zweikampf.getGameState().equals(GameStates.RUN_PICK_KIT)) {
            if (zweikampf.isBrawler(player)) {
                if (event.getFrom().distance(event.getTo()) > 0) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }

}
