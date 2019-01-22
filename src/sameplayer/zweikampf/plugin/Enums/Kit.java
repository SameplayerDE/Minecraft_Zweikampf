package sameplayer.zweikampf.plugin.Enums;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import sameplayer.zweikampf.plugin.Classes.Armor;
import sameplayer.zweikampf.plugin.Classes.Hotbar;
import sameplayer.zweikampf.plugin.Classes.Inner;
import zame.itemfactory.api.ItemFactoryAdvanced;

public enum Kit {

    SCHWERTKÄMPFER(new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ),
            new Hotbar(),
            new Inner()),
    FAUSTKÄMPFER(new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ),
            new Hotbar(),
            new Inner()),
    ORTSSPRINGER(new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ),
            new Hotbar(),
            new Inner()),
    BOGENSCHÜTZE(new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ),
            new Hotbar(),
            new Inner()),
    BAUER(new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.DAMAGE_ALL, 2)
            ),
            new Hotbar(),
            new Inner());

    private ItemStack[] armor, hotbar, inner;

    Kit(Armor armor, Hotbar hotbar, Inner inner) {
        this.armor = armor.getContent();
        this.hotbar = hotbar.getContent();
        this.inner = inner.getContent();
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getHotbar() {
        return hotbar;
    }

    public ItemStack[] getInner() {
        return inner;
    }

    public PlayerInventory getPlayerInventory() {
        PlayerInventory playerInventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
        playerInventory.setArmorContents(getArmor());
        for (int i = 0; i < 9; i++) {
            playerInventory.setItem(i + 36, hotbar[i]);
        }
        for (int i = 0; i < 27; i++) {
            playerInventory.setItem(i + 9, inner[i]);
        }
        return playerInventory;
    }
}
