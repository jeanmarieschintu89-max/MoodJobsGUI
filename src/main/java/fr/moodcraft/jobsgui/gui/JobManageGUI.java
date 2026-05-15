package fr.moodcraft.jobsgui.gui;

import fr.moodcraft.jobsgui.model.JobEntry;
import fr.moodcraft.jobsgui.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class JobManageGUI {

    public static final String TITLE_PREFIX = MoodStyle.cleanTitle(MoodStyle.guiTitle("Métier "));

    private JobManageGUI() {
    }

    public static void open(Player player, JobEntry job) {
        Inventory inventory = Bukkit.createInventory(null, 27, MoodStyle.guiTitle("Métier " + job.displayName()));

        inventory.setItem(4, GuiUtil.button(
                job.icon(),
                job.displayName(),
                "Bureau des Métiers.",
                "Argent et XP selon vos actions."
        ));

        inventory.setItem(10, GuiUtil.button(
                Material.LIME_CONCRETE,
                "Rejoindre",
                "Rejoindre ce métier.",
                "Gagnez argent et XP métier."
        ));

        inventory.setItem(13, GuiUtil.button(
                Material.BOOK,
                "Infos",
                "Voir les gains.",
                "Argent, XP et actions payées."
        ));

        inventory.setItem(16, GuiUtil.button(
                Material.RED_CONCRETE,
                "Quitter",
                "Quitter ce métier.",
                "Arrête les gains de ce métier."
        ));

        inventory.setItem(22, GuiUtil.button(
                Material.BARRIER,
                "Retour",
                "Retour aux métiers.",
                "Menu précédent."
        ));

        GuiUtil.fill(inventory);
        player.openInventory(inventory);
    }

    public static String extractJobName(String title) {
        String clean = MoodStyle.cleanTitle(title);

        if (!clean.startsWith(TITLE_PREFIX)) {
            return null;
        }

        String jobName = clean.substring(TITLE_PREFIX.length()).trim();
        return jobName.isBlank() ? null : jobName;
    }
}
