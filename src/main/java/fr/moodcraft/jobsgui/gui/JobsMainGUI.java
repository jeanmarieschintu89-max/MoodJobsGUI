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

    private JobsMainGUI() {
    }

    public static void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);

        inventory.setItem(4, GuiUtil.button(
                Material.EMERALD,
                "Bureau des Métiers",
                "Choisissez un métier.",
                "Les gains restent gérés par Jobs.",
                "Interface officielle MoodCraft."
        ));

        inventory.setItem(10, GuiUtil.button(
                Material.WRITABLE_BOOK,
                "Mes Métiers",
                "Voir vos métiers actuels.",
                "Ouvre la commande Jobs stats."
        ));

        inventory.setItem(12, GuiUtil.button(
                Material.LIME_DYE,
                "Rejoindre un Métier",
                "Sélectionnez un métier disponible.",
                "Compatible Java et Bedrock."
        ));

        inventory.setItem(14, GuiUtil.button(
                Material.RED_DYE,
                "Quitter un Métier",
                "Choisissez le métier à quitter.",
                "Action sécurisée par confirmation Jobs."
        ));

        inventory.setItem(16, GuiUtil.button(
                Material.GOLD_INGOT,
                "Classement Métiers",
                "Voir les meilleurs travailleurs.",
                "Ouvre le classement Jobs."
        ));

        inventory.setItem(28, GuiUtil.button(
                Material.EXPERIENCE_BOTTLE,
                "Statistiques",
                "Voir votre progression.",
                "Niveaux, XP et métiers actifs."
        ));

        inventory.setItem(30, GuiUtil.button(
                Material.COMPASS,
                "Liste des Métiers",
                "Parcourir tous les métiers.",
                "Rejoindre ou consulter un métier."
        ));

        inventory.setItem(32, GuiUtil.button(
                Material.BOOK,
                "Aide Métiers",
                "Comprendre le système Jobs.",
                "Gains, XP, niveaux et limites."
        ));

        inventory.setItem(34, GuiUtil.button(
                Material.BARRIER,
                "Fermer",
                "Retour au jeu."
        ));

        int[] slots = {37, 38, 39, 40, 41, 42, 43};
        List<JobEntry> jobs = Main.getInstance().getJobConfigManager().getJobs();

        for (int i = 0; i < Math.min(slots.length, jobs.size()); i++) {
            JobEntry job = jobs.get(i);
            List<String> lore = new ArrayList<>();
            lore.add(MoodStyle.lore("Métier : §e" + job.displayName()));
            lore.add(MoodStyle.lore("Clique pour gérer."));
            inventory.setItem(slots[i], GuiUtil.rawLoreButton(job.icon(), job.displayName(), lore));
        }

        GuiUtil.fill(inventory);
        player.openInventory(inventory);
    }
}
