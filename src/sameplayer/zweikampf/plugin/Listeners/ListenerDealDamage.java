package sameplayer.zweikampf.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;

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
            target.playEffect(EntityEffect.HURT);
            return;
        }

    }

}
