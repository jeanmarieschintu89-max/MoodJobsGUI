package fr.moodcraft.jobsgui.gui;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.model.JobEntry;
import fr.moodcraft.jobsgui.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public final class JobsMainGUI {

    public static final String TITLE = MoodStyle.guiTitle("Métiers MoodCraft");

    private static final int[] JOB_SLOTS = {
            10, 11, 12, 13, 14, 15, 16
    };

    private JobsMainGUI() {
    }

    public static void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        List<JobEntry> jobs = Main.getInstance().getJobConfigManager().getJobs();

        for (int i = 0; i < Math.min(JOB_SLOTS.length, jobs.size()); i++) {
            JobEntry job = jobs.get(i);
            List<String> lore = new ArrayList<>();

            for (String line : job.description()) {
                lore.add(MoodStyle.lore(line));
            }

            lore.add("");
            lore.add(MoodStyle.lore("Clique pour gérer ce métier."));
            lore.add(MoodStyle.lore("Argent et XP selon vos actions."));

            inventory.setItem(
                    JOB_SLOTS[i],
                    GuiUtil.rawLoreButton(job.icon(), job.displayName(), lore)
            );
        }

        inventory.setItem(21, GuiUtil.button(
                Material.EXPERIENCE_BOTTLE,
                "Mes Stats",
                "Voir vos métiers et niveaux.",
                "Argent, XP et progression."
        ));

        inventory.setItem(23, GuiUtil.button(
                Material.GOLD_INGOT,
                "Classement",
                "Voir le top des travailleurs.",
                "Classement Jobs."
        ));

        inventory.setItem(26, GuiUtil.button(
                Material.BARRIER,
                "Menu Principal",
                "Retour au menu MoodCraft.",
                "Ouvre /menu."
        ));

        GuiUtil.fill(inventory);
        player.openInventory(inventory);
    }

    public static JobEntry getJobBySlot(int slot) {
        List<JobEntry> jobs = Main.getInstance().getJobConfigManager().getJobs();

        for (int i = 0; i < Math.min(JOB_SLOTS.length, jobs.size()); i++) {
            if (JOB_SLOTS[i] == slot) {
                return jobs.get(i);
            }
        }

        return null;
    }
}