package sameplayer.zweikampf.plugin;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sameplayer.zweikampf.plugin.Enums.LocationType;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Listeners.ListenerPlayerWaiting;
import zame.itemfactory.api.ItemFactory;
import zame.itemfactory.api.ItemFactoryAdvanced;

import java.util.LinkedList;

import static org.bukkit.enchantments.Enchantment.*;

public class Zweikampf {

    private LinkedList<Player> players;
    private BukkitTask waitingFightTimer;
    //private Countdown countdown;
    //private Player winner;

    public Zweikampf() {
        players = new LinkedList<>();
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public Player getWinnerByLooser(Player player) {
        for (Player inList : players) {
            if (!inList.equals(player)) {
                return inList;
            }
        }
        return null;
    }

    public void remove(Player player) {
        if (contains(player)) {
            players.remove(player);
        }
    }

    public void addPlayer(Player player) {
        if (players.size() >= 2) {
            return;
        }
        if (!players.contains(player)) {
            players.add(player);
        }else{
            return;
        }
    }

    public void forceWin(Player player) {



    }

    public void countStart() {

        waitingFightTimer = new BukkitRunnable() {

        int count = 0;

        @Override
        public void run() {
            if (count >= 5) {
                if (players.size() == 2) {
                    start();
                }else{
                    this.cancel();
                }
            }else {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.sendTitle("", "§e" + String.valueOf(5 - count), 1, 5, 1);
                    online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1l, 1l);
                }
            }
            count++;
        }

    }.runTaskTimer(Main.getInstance(), 20l, 20l);

        for (Player player : players) {
            player.getInventory().clear();

            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().setHelmet(new ItemFactoryAdvanced("§eHelm", Material.DIAMOND_HELMET, 1, "").withEnchantment(PROTECTION_ENVIRONMENTAL, 2));
            player.getInventory().setBoots(new ItemFactoryAdvanced("§eSchuhe", Material.IRON_BOOTS, 1, "").withEnchantment(PROTECTION_FALL, 2));
            player.getInventory().setLeggings(new ItemFactoryAdvanced("§eBeinlinge", Material.DIAMOND_LEGGINGS, 1, "").withEnchantment(PROTECTION_ENVIRONMENTAL, 2));
            player.getInventory().setChestplate(new ItemFactoryAdvanced("§eBrustplatte", Material.DIAMOND_CHESTPLATE, 1, "").withEnchantment(PROTECTION_ENVIRONMENTAL, 2));

            player.getInventory().setItemInOffHand(ItemFactory.generateItemStack(Material.SHIELD));

            player.getInventory().setItem(0, new ItemFactoryAdvanced("§eSchwert", Material.DIAMOND_SWORD, 1, "").withEnchantment(Enchantment.DAMAGE_ALL, 2));
            player.getInventory().setItem(1, new ItemFactoryAdvanced("§eBogen", Material.BOW, 1, "").withEnchantment(Enchantment.ARROW_DAMAGE, 1));
            player.getInventory().setItem(2, ItemFactory.generateItemStack("§egoldener Apfel", Material.GOLDEN_APPLE, 2));
            //player.getInventory().setItem(3, ItemFactory.generatePotion(3, Color.LIME, new PotionEffect(PotionEffectType.HEALTH_BOOST, 10, 2)));
            player.getInventory().setItem(6, ItemFactory.generateItemStack("§ePfeil", Material.ARROW, 16));
            player.getInventory().setItem(7, ItemFactory.generateItemStack("§eBeil", Material.DIAMOND_AXE));
            player.getInventory().setItem(8, ItemFactory.generateItemStack("§eHolzplanken", Material.OAK_PLANKS, 64));
        }
        players.get(0).teleport(LocationType.PLAYER_ONE.toLocation());
        players.get(1).teleport(LocationType.PLAYER_TWO.toLocation());
    }

    public void start() {
        waitingFightTimer.cancel();
        Main.setState(ServerState.RUNNING);
    }

}
