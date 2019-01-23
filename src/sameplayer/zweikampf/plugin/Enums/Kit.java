package sameplayer.zweikampf.plugin.Enums;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import sameplayer.zweikampf.plugin.Classes.Armor;
import sameplayer.zweikampf.plugin.Classes.Hotbar;
import sameplayer.zweikampf.plugin.Classes.Inner;
import zame.itemfactory.api.ItemFactory;
import zame.itemfactory.api.ItemFactoryAdvanced;

public enum Kit {

    SCHWERTKÄMPFER("Schwertkämpfer",
            new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ),
            new Hotbar(),
            new Inner()),
    FAUSTKÄMPFER("Faustkämpfer",
            new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ),
            new Hotbar(),
            new Inner()),
    ORTSSPRINGER("Ortsspringer",
            new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ),
            new Hotbar(),
            new Inner()),
    BOGENSCHÜTZE("Bogenschütze",
            new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ),
            new Hotbar().setSlot(0, new ItemStack(Material.BOW)).setSlot(8, new ItemStack(Material.ARROW, 16)),
            new Inner()),
    BAUER("Bauer",
            new Armor()
            .setHelmet(new ItemFactoryAdvanced("Helm", Material.DIAMOND_HELMET, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setChest(new ItemFactoryAdvanced("Brustplatte", Material.DIAMOND_CHESTPLATE, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setLegs(new ItemFactoryAdvanced("Beinlinge", Material.DIAMOND_LEGGINGS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ).setBoots(new ItemFactoryAdvanced("Stiefel", Material.DIAMOND_BOOTS, 1, "")
                    .withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            ),
            new Hotbar().setSlot(0, new ItemStack(Material.DIRT)),
            new Inner().setSlot(0, new ItemStack(Material.STONE)));

    private ItemStack[] armor, hotbar, inner;
    private String name;

    Kit(String name, Armor armor, Hotbar hotbar, Inner inner) {
        this.name = name;
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
            playerInventory.setItem(i, hotbar[i]);
        }
        for (int i = 9; i < 35; i++) {
            playerInventory.setItem(i, inner[i]);
        }
        return playerInventory;
    }

    public void setItems(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setArmorContents(getArmor());
        for (int i = 0; i < 9; i++) {
            playerInventory.setItem(i, hotbar[i]);
        }
        for (int i = 9; i < 35; i++) {
            playerInventory.setItem(i, inner[i - 9]);
        }
    }

    public ItemStack toItemStack() {
        return ItemFactory.generateItemStack(name, Material.CHEST);
    }
}
