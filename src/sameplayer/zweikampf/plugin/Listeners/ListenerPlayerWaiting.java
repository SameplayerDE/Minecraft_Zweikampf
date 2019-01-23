package sameplayer.zweikampf.plugin.Listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sameplayer.zweikampf.plugin.Enums.GameStates;
import sameplayer.zweikampf.plugin.Enums.LocationType;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;
import zame.itemfactory.api.ItemFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ListenerPlayerWaiting implements Listener {

    public static BukkitTask waitingFightTimer;

    private Main plugin;
    private ZweikampfManager zweikampf;

    public ListenerPlayerWaiting(Main plugin) {
        this.plugin = plugin;
        this.zweikampf = this.plugin.getZweikampf();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBedClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack != null) {
            if (itemStack.getType().equals(Material.RED_BED)) {
                if (ItemFactory.getUseableItemName(itemStack) != null) {
                    String name = ItemFactory.getUseableItemName(itemStack);
                    if (name.equalsIgnoreCase("Verlassen")) {
                        Bukkit.dispatchCommand(player, "verlassen");
                    }
                }
            }
        }
    }


    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (!zweikampf.isGoing()) {
            zweikampf.purgeSpectator(player);
            return;
        }

        zweikampf.purgeBrawler(player);

        Player winner = Bukkit.getPlayer(zweikampf.getBrawlerSet().iterator().next());

        for (Player online : Bukkit.getOnlinePlayers()) {

            online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 10f);

            if (online.equals(winner)) {

                online.sendTitle("Du hast", "diese Runde für dich entschieden", 20*2, 20*3, 20*2);

            }else{

                online.sendTitle(winner.getName(), "hat gesiegt", 20 * 2, 10 * 3, 10 * 2);

            }

        }

        zweikampf.setGameState(GameStates.REBOOT_COUNTDOWN_LOBBY);

        Bukkit.broadcastMessage("Du wirst in 15 Sekunden zurück zur Eingangshalle gebracht");

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

                            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");

                        }

                    }

                }, 20*1l, 1l);

            }

        }, 20*15l);

        zweikampf.purgeSets();

    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {

        Player player = event.getPlayer();

        if (!zweikampf.getGameState().equals(GameStates.SERVER_SETUP)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerAsyncChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        event.setFormat("§6" + player.getName() + "  §e" + event.getMessage());

    }

    @EventHandler
    public void onPlayerShot(ProjectileLaunchEvent event) {

        Arrow arrow = (Arrow) event.getEntity();
        Player player = (Player) arrow.getShooter();

        player.setCooldown(Material.BOW, 5);

    }

    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent event) {

        if (!zweikampf.isGoing()) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player target = (Player) event.getEntity();

        if (!zweikampf.isBrawler(target)) {
            event.setCancelled(true);
            return;
        }

    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            event.setCancelled(true);
        }
    }

}
