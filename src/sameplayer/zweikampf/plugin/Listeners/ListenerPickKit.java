package sameplayer.zweikampf.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sameplayer.zweikampf.plugin.Enums.GameStates;
import sameplayer.zweikampf.plugin.Enums.Kit;
import sameplayer.zweikampf.plugin.Main;
import sameplayer.zweikampf.plugin.ZweikampfManager;

import java.util.HashSet;
import java.util.UUID;

public class ListenerPickKit implements Listener {

    private HashSet<UUID> selected = new HashSet<>();

    private Main plugin;
    private ZweikampfManager zweikampf;

    public ListenerPickKit(Main plugin) {
        this.plugin = plugin;
        this.zweikampf = this.plugin.getZweikampf();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onKitPick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        //System.out.println(event.getSlot());
        //System.out.println(event.getRawSlot());

        event.setCancelled(true);

        if (selected.contains(player.getUniqueId())) {
            return;
        }

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
            return;
        }

        Inventory inventory = event.getClickedInventory();

        if (!inventory.getTitle().equals("Kits")) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        ItemStack itemStack = event.getCurrentItem();

        if (!itemStack.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.hasDisplayName()) {
            return;
        }

        String displayName = itemMeta.getDisplayName();

        Kit k = null;

        for (Kit kit : Kit.values()) {
            //System.out.println("Looped: " + kit.toString());
            if (kit.toString().equalsIgnoreCase(displayName)) {
                k = kit;
                //System.out.println("Picked: " + k.toString());
                player.closeInventory();
                break;
            }
        }

        //System.out.println("Setting Content");
        k.setItems(player);
        selected.add(player.getUniqueId());

        int i = 0;

        for (UUID uuid : zweikampf.getBrawlerSet()) {

            if (selected.contains(uuid)) {
                //System.out.println(Bukkit.getPlayer(uuid).getDisplayName() + " picked");
                i++;
                if (i == 2) {
                    zweikampf.setGameState(GameStates.RUN_FIGHT);
                }
                continue;
            }
            return;
        }

    }

}
