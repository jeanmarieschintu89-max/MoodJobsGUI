package fr.moodcraft.jobsgui.gui;

import fr.moodcraft.jobsgui.util.MoodStyle;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class GuiUtil {

    private GuiUtil() {
    }

    public static ItemStack item(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material == null ? Material.BOOK : material);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName(name);

        if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore);
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack button(Material material, String name, String... lore) {
        List<String> lines = new ArrayList<>();

        if (lore != null) {
            for (String line : lore) {
                lines.add(MoodStyle.lore(line));
            }
        }

        return item(material, MoodStyle.button(name), lines);
    }

    public static ItemStack rawLoreButton(Material material, String name, List<String> lore) {
        return item(material, MoodStyle.button(name), lore);
    }

    public static void fill(Inventory inventory) {
        ItemStack pane = item(Material.BLACK_STAINED_GLASS_PANE, "§8 ", List.of());

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, pane);
            }
        }
    }
}
