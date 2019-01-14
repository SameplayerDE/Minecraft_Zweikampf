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
import sameplayer.zweikampf.plugin.Enums.LocationType;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Main;
import zame.itemfactory.api.ItemFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ListenerPlayerWaiting implements Listener {

    public static BukkitTask waitingFightTimer;

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
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Main.getState().equals(ServerState.WAITING_FIGHT)) {
            if (Main.getZweikampf().contains(player)) {
                if (event.getFrom().distance(event.getTo()) > 0) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (Main.getState().equals(ServerState.WAITING_QUEUE)) {

            if (Main.getZweikampf().contains(player)) {

                Main.getZweikampf().remove(player);

            }

        } else if (Main.getState().equals(ServerState.RUNNING) || Main.getState().equals(ServerState.WAITING_FIGHT)) {

            if (Main.getZweikampf().contains(player)) {

                Player winner = Main.getZweikampf().getWinnerByLooser(player);

                for (Player online : Bukkit.getOnlinePlayers()) {

                    online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 10f);

                    if (online.equals(winner)) {

                        online.sendTitle("Du hast", "diese Runde für dich entschieden", 20*2, 20*3, 20*2);

                    }else{

                        online.sendTitle(winner.getName(), "hat gesiegt", 20 * 2, 10 * 3, 10 * 2);

                    }

                }

                Main.setState(ServerState.RESTARTING);
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

                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");

                                }

                            }

                        }, 20*1l, 1l);

                    }

                }, 20*15l);

            }

            Main.getZweikampf().remove(player);

        }

    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {

        Player player = event.getPlayer();

        if (!Main.getState().equals(ServerState.SETUP)) {
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

        player.setCooldown(Material.BOW, 20);

    }

    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent event) {

        if (Main.getState().equals(ServerState.WAITING_QUEUE)) {

            event.setCancelled(true);

        }else if (Main.getState().equals(ServerState.RUNNING)) {

            if (event.getEntity() instanceof Player) {

                Player player = (Player) event.getEntity();

                if (!Main.getZweikampf().contains(player)) {

                    event.setCancelled(true);

                }

                if (player.isBlocking()) {

                    player.getInventory().getItemInOffHand().setAmount(0);

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.getInventory().setItemInOffHand(ItemFactory.generateItemStack("§eSchild", Material.SHIELD));
                        }
                    }.runTaskLater(Main.getInstance(), 20l*5);

                }

            }else{

                event.setCancelled(true);

            }

        }else{

            event.setCancelled(true);

        }

    }

    @EventHandler
    public void onPlayerDamageEvent(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof Player) {
                if (Main.getState().equals(ServerState.WAITING_QUEUE)) {

                    event.setCancelled(true);

                }else if (Main.getState().equals(ServerState.RUNNING)) {

                    if (event.getEntity() instanceof Player) {

                        Player player = (Player) event.getEntity();
                        Player damager = (Player) event.getDamager();

                        if (!Main.getZweikampf().contains(player)) {

                            event.setCancelled(true);

                        }else{
                            if (!Main.getZweikampf().contains(damager)) {
                                event.setCancelled(true);
                            }
                        }

                    }else{

                        event.setCancelled(true);

                    }

                }else{

                    event.setCancelled(true);

                }

            }

        }

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
