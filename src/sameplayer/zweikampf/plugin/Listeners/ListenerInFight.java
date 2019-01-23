package sameplayer.zweikampf.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import sameplayer.zweikampf.plugin.Enums.GameStates;
import sameplayer.zweikampf.plugin.Enums.LocationType;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

public class ListenerInFight implements Listener {

    public static LinkedList<Block> blockList = new LinkedList<>();

    private Main plugin;
    private ZweikampfManager zweikampf;

    public ListenerInFight(Main plugin) {
        this.plugin = plugin;
        this.zweikampf = this.plugin.getZweikampf();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCraftTableClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (block.getType().equals(Material.CRAFTING_TABLE)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBlockEvent(BlockPlaceEvent event) {
        if (!zweikampf.isGoing() && !zweikampf.getGameState().equals(GameStates.SERVER_SETUP)) {
            event.setCancelled(true);
            return;
        }

        if (zweikampf.getGameState().equals(GameStates.SERVER_SETUP)) {
            event.setCancelled(false);
            return;
        }

        Player player = event.getPlayer();

        if (!zweikampf.isBrawler(player)) {
            event.setCancelled(true);
            return;
        }

        blockList.add(event.getBlockPlaced());
        event.setCancelled(false);
    }

    @EventHandler
    public void onPlayerBlockEvent(BlockBreakEvent event) {
        if (!zweikampf.isGoing() && !zweikampf.getGameState().equals(GameStates.SERVER_SETUP)) {
            event.setCancelled(true);
            return;
        }

        if (zweikampf.getGameState().equals(GameStates.SERVER_SETUP)) {
            event.setCancelled(false);
            return;
        }

        Player player = event.getPlayer();

        if (!zweikampf.isBrawler(player)) {
            event.setCancelled(true);
            return;
        }

        if (!blockList.contains(event.getBlock())) {
            event.setCancelled(true);
            return;
        }

        blockList.remove(event.getBlock());
        event.setCancelled(false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        event.setDeathMessage("");

        if (!zweikampf.isGoing()) {
            return;
        }

        if (!zweikampf.isBrawler(player)) {
            return;
        }

        event.getDrops().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(LocationType.SPECTATOR.toLocation());

        Player winner = player.getKiller();

        for (Player online : Bukkit.getOnlinePlayers()) {

            online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 10f);

            if (online.equals(winner)) {

                online.sendTitle("§aDu hast", "§adiese Runde für dich entschieden", 20*2, 20*3, 20*2);

            }else{

                online.sendTitle("§a" + winner.getName(), "§ahat gesiegt", 20 * 2, 10 * 3, 10 * 2);

            }

        }

        zweikampf.setGameState(GameStates.WAIT_SERVER_REBOOT);
        Bukkit.broadcastMessage("§aDu wirst in 5 Sekunden zurück zur Eingangshalle gebracht");

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

            @Override
            public void run() {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                try {
                    dataOutputStream.writeUTF("Connect");
                    dataOutputStream.writeUTF("eingangshalle");
                }catch (Exception e) {

                }
                for (Player online : Bukkit.getOnlinePlayers()) {

                    online.sendPluginMessage(Main.getInstance(), "BungeeCord", outputStream.toByteArray());

                }

                Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {

                    @Override
                    public void run() {

                        if (Bukkit.getOnlinePlayers().size() == 0) {

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");

                        }

                    }

                }, 20*1l, 1l);

            }

        }, 20*5l);

    }

}
