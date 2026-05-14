package fr.moodcraft.jobsgui.gui;

import fr.moodcraft.jobsgui.model.JobEntry;
import fr.moodcraft.jobsgui.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class JobManageGUI {

    public static final String TITLE_PREFIX = MoodStyle.guiTitle("Métier ");

    private JobManageGUI() {
    }

    public static void open(Player player, JobEntry job) {
        Inventory inventory = Bukkit.createInventory(null, 27, MoodStyle.guiTitle("Métier " + job.displayName()));

        inventory.setItem(4, GuiUtil.button(
                job.icon(),
                job.displayName(),
                "Métier Jobs : §e" + job.commandName(),
                "Choisissez une action."
        ));

        inventory.setItem(10, GuiUtil.button(
                Material.LIME_CONCRETE,
                "Rejoindre",
                "Rejoindre ce métier.",
                "Action exécutée via Jobs."
        ));

        inventory.setItem(12, GuiUtil.button(
                Material.BOOK,
                "Informations",
                "Voir les infos du métier.",
                "Gains, actions et progression."
        ));

        inventory.setItem(14, GuiUtil.button(
                Material.RED_CONCRETE,
                "Quitter",
                "Quitter ce métier.",
                "Utilisez avec attention."
        ));

        inventory.setItem(16, GuiUtil.button(
                Material.EXPERIENCE_BOTTLE,
                "Statistiques",
                "Voir votre progression Jobs."
        ));

        inventory.setItem(22, GuiUtil.button(
                Material.ARROW,
                "Retour",
                "Revenir à la liste des métiers."
        ));

        GuiUtil.fill(inventory);
        player.openInventory(inventory);
    }

    public static String extractJobName(String title) {
        String clean = title
                .replace("§6✦ §8§lMétier ", "")
                .replace(" §6✦", "")
                .trim();

        return clean.isBlank() ? null : clean;
    }
}
