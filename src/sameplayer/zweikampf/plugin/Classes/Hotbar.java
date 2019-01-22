package sameplayer.zweikampf.plugin.Classes;

import org.bukkit.inventory.ItemStack;

public class Hotbar {

    private ItemStack[] content = new ItemStack[9];

    public ItemStack[] getContent() {
        return content;
    }

    public Hotbar setSlot(int slot, ItemStack itemStack) {
        content[slot] = itemStack;
        return this;
    }

}
