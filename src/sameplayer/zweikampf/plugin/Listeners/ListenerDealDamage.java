package sameplayer.zweikampf.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;

import java.util.ArrayList;

public class ListenerDealDamage implements Listener {

    private Main plugin;
    private ZweikampfManager zweikampf;

    public ListenerDealDamage(Main plugin) {
        this.plugin = plugin;
        this.zweikampf = this.plugin.getZweikampf();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!zweikampf.isGoing()) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player target = (Player) event.getEntity();

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();

        if (!zweikampf.isBrawler(damager) && !zweikampf.isBrawler(target)) {
            event.setCancelled(true);
            return;
        }

        if (target.getHealth() - event.getDamage() < 1D) {
            event.setCancelled(true);
            target.setHealth(20D);
            target.playEffect(EntityEffect.HURT);
            Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(target, new ArrayList<ItemStack>(), 0, ""));
            return;
        }

    }

}
