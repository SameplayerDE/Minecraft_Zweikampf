package sameplayer.zweikampf.plugin.Classes;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import zame.itemfactory.api.ItemFactory;

public class Armor {

    private ItemStack[] content = new ItemStack[4];


    public ItemStack[] getContent() {
        return content;
    }

    public Armor setHelmet(ItemStack helmet) {
        content[3] = helmet;
        return this;
    }

    public Armor setChest(ItemStack chest) {
        content[2] = chest;
        return this;
    }

    public Armor setLegs(ItemStack legs) {
        content[1] = legs;
        return this;
    }

    public Armor setBoots(ItemStack boots) {
        content[0] = boots;
        return this;
    }

}
