package sameplayer.zweikampf.plugin.Classes;

import org.bukkit.inventory.ItemStack;

public class Inner {

    private ItemStack[] content = new ItemStack[27];

    public ItemStack[] getContent() {
        return content;
    }

    public Inner setSlot(int slot, ItemStack itemStack) {
        content[slot] = itemStack;
        return this;
    }

}
