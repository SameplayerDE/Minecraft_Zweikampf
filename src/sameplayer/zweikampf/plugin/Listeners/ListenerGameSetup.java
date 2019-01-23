package sameplayer.zweikampf.plugin.Listeners;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scheduler.BukkitRunnable;
import sameplayer.zweikampf.plugin.Enums.GameStates;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;

public class ListenerGameSetup implements Listener {

    private Main plugin;
    private ZweikampfManager zweikampf;

    public ListenerGameSetup(Main plugin) {
        this.plugin = plugin;
        this.zweikampf = this.plugin.getZweikampf();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {

        Player player = event.getPlayer();

        /**
         serverIP.writeUTF("ServerIP");
         serverIP.writeUTF(server);
         player.sendPluginMessage(Main.getInstance(), "BungeeCord", serverIP.toByteArray());
         **/

        if (zweikampf.getGameState().equals(GameStates.SERVER_SETUP)) {

            if (player.hasPermission("zweikampf.konfiguration")) {

                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage("Bitte schließe die Konfiguration ab!");

            } else {

                event.disallow(Result.KICK_FULL, "This Server is not reay");

            }

        }

    }

}
