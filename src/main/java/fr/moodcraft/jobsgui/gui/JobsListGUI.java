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

public final class JobsListGUI {

    public static final String TITLE = MoodStyle.guiTitle("Liste des Métiers");

    private static final int[] JOB_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private JobsListGUI() {
    }

    public static void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);

        List<JobEntry> jobs = Main.getInstance().getJobConfigManager().getJobs();

        for (int i = 0; i < Math.min(JOB_SLOTS.length, jobs.size()); i++) {
            JobEntry job = jobs.get(i);
            List<String> lore = new ArrayList<>();

            for (String line : job.description()) {
                lore.add(MoodStyle.lore(line));
            }

            lore.add("");
            lore.add(MoodStyle.lore("Commande Jobs : §e" + job.commandName()));
            lore.add(MoodStyle.lore("Clique pour gérer ce métier."));

            inventory.setItem(
                    JOB_SLOTS[i],
                    GuiUtil.rawLoreButton(job.icon(), job.displayName(), lore)
            );
        }

        inventory.setItem(45, GuiUtil.button(
                Material.ARROW,
                "Retour",
                "Revenir au bureau des métiers."
        ));

        inventory.setItem(49, GuiUtil.button(
                Material.BARRIER,
                "Fermer",
                "Retour au jeu."
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
