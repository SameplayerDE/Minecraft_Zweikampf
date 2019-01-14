package sameplayer.zweikampf.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import sameplayer.zweikampf.plugin.Enums.GameStates;
import sameplayer.zweikampf.plugin.Enums.LocationType;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;
import zame.itemfactory.api.ItemFactory;

public class ListenerPlayerJoin implements Listener {

    private Main plugin;
    private ZweikampfManager zweikampf;

    public ListenerPlayerJoin(Main plugin) {
        this.plugin = plugin;
        this.zweikampf = this.plugin.getZweikampf();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.getInventory().setItem(8, ItemFactory.generateItemStack("§eVerlassen", Material.RED_BED));

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        if (!zweikampf.isGoing()) {
            player.teleport(LocationType.WAITING.toLocation());
            if (zweikampf.isReady()) {
                zweikampf.startCountdown();
            }
            return;
        }

        if (zweikampf.getGameState().equals(GameStates.SERVER_SETUP)) {
            if (!player.hasPermission("zweikampf.setup")) {
                player.kickPlayer("");
            }
            return;
        }

        if (zweikampf.getGameState().toString().contains("REBOOT")) {
            player.kickPlayer("");
            return;
        }

        zweikampf.addSpectator(player);
        player.setScoreboard(zweikampf.getScoreboard());
        zweikampf.getScoreboard().getTeam("spectator").addEntry(player.getName());

       /**

        if (Main.getState().equals(ServerState.WAITING_QUEUE)) {

            Main.getZweikampf().addPlayer(player);
            player.teleport(LocationType.WAITING.toLocation());

            if (Bukkit.getOnlinePlayers().size() == 1) {

                player.sendMessage("§aDu hast die Warteschlange betreten!");

            }else if (Bukkit.getOnlinePlayers().size() == 2) {

                Main.setState(ServerState.WAITING_FIGHT);

                Main.getZweikampf().countStart();

                //player.sendMessage("Du hast die Warteschlange betreten!");

            }

            event.setJoinMessage("");

        } else if (Main.getState().equals(ServerState.WAITING_FIGHT)) {

            player.teleport(LocationType.SPECTATOR.toLocation());



            player.sendMessage("§aEs kämpft §2" + Main.getZweikampf().getPlayers().get(0) + " §ageg. §2" + Main.getZweikampf().getPlayers().get(1));


            event.setJoinMessage("§2" + player.getName() + " §aschaut dem Kampf nun zu");

        } else if (Main.getState().equals(ServerState.RUNNING)) {

            player.teleport(LocationType.SPECTATOR.toLocation());



            player.sendMessage("§aEs kämpft §2" + Main.getZweikampf().getPlayers().get(0) + " §ageg. §2" + Main.getZweikampf().getPlayers().get(1));

            event.setJoinMessage("§2" + player.getName() + " §aschaut dem Kampf nun zu");

        } else if (Main.getState().equals(ServerState.RESTARTING)) {

            player.kickPlayer("Server");

        }

        **/

    }

}
