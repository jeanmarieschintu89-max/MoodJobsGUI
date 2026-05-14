package fr.moodcraft.jobsgui.listener;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.gui.JobManageGUI;
import fr.moodcraft.jobsgui.gui.JobsListGUI;
import fr.moodcraft.jobsgui.gui.JobsMainGUI;
import fr.moodcraft.jobsgui.model.JobEntry;
import fr.moodcraft.jobsgui.util.JobsCommandBridge;
import fr.moodcraft.jobsgui.util.MoodStyle;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class JobsGUIListener implements Listener {

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();

        if (isMoodJobsTitle(title)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();

        if (!isMoodJobsTitle(title)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getClickedInventory() == null
                || event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        int slot = event.getSlot();

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1.2f);

        if (title.equals(JobsMainGUI.TITLE)) {
            handleMain(player, slot);
            return;
        }

        if (title.equals(JobsListGUI.TITLE)) {
            handleList(player, slot);
            return;
        }

        if (title.startsWith("§6✦ §8§lMétier ")) {
            handleManage(player, title, slot);
        }
    }

    private void handleMain(Player player, int slot) {

        switch (slot) {
            case 10, 28 -> JobsCommandBridge.stats(player);
            case 12, 30 -> JobsListGUI.open(player);
            case 14 -> JobsListGUI.open(player);
            case 16 -> JobsCommandBridge.top(player);
            case 32 -> sendHelp(player);
            case 34 -> player.closeInventory();
            default -> {
                JobEntry job = findJobByDisplaySlot(slot);
                if (job != null) {
                    JobManageGUI.open(player, job);
                }
            }
        }
    }

    private void handleList(Player player, int slot) {

        if (slot == 45) {
            JobsMainGUI.open(player);
            return;
        }

        if (slot == 49) {
            player.closeInventory();
            return;
        }

        JobEntry job = JobsListGUI.getJobBySlot(slot);

        if (job != null) {
            JobManageGUI.open(player, job);
        }
    }

    private void handleManage(Player player, String title, int slot) {
        String displayName = JobManageGUI.extractJobName(title);

        if (displayName == null) {
            JobsListGUI.open(player);
            return;
        }

        JobEntry job = findJobByName(displayName);

        if (job == null) {
            MoodStyle.errorMessage(
                    player,
                    MoodStyle.MODULE,
                    "Métier introuvable.",
                    MoodStyle.detail("Recharge la configuration avec §e/moodjobsgui reload")
            );
            player.closeInventory();
            return;
        }

        switch (slot) {
            case 10 -> {
                player.closeInventory();
                JobsCommandBridge.join(player, job.commandName());
            }
            case 12 -> {
                player.closeInventory();
                JobsCommandBridge.info(player, job.commandName());
            }
            case 14 -> {
                player.closeInventory();
                JobsCommandBridge.leave(player, job.commandName());
            }
            case 16 -> {
                player.closeInventory();
                JobsCommandBridge.stats(player);
            }
            case 22 -> JobsListGUI.open(player);
            default -> {
            }
        }
    }

    private JobEntry findJobByDisplaySlot(int slot) {
        int[] slots = {37, 38, 39, 40, 41, 42, 43};

        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == slot
                    && i < Main.getInstance().getJobConfigManager().getJobs().size()) {
                return Main.getInstance().getJobConfigManager().getJobs().get(i);
            }
        }

        return null;
    }

    private JobEntry findJobByName(String displayName) {
        for (JobEntry job : Main.getInstance().getJobConfigManager().getJobs()) {
            if (job.displayName().equalsIgnoreCase(displayName)) {
                return job;
            }
        }

        return null;
    }

    private boolean isMoodJobsTitle(String title) {
        return title != null
                && (title.equals(JobsMainGUI.TITLE)
                || title.equals(JobsListGUI.TITLE)
                || title.startsWith("§6✦ §8§lMétier "));
    }

    private void sendHelp(Player player) {
        MoodStyle.send(
                player,
                MoodStyle.MODULE,
                MoodStyle.info("Les métiers récompensent vos activités."),
                MoodStyle.detail("Mine, pêche, construction, chasse et plus."),
                MoodStyle.detail("Les gains, niveaux et XP restent gérés par Jobs."),
                MoodStyle.detail("Utilisez §e/metiers §7pour revenir au menu.")
        );
        player.closeInventory();
    }
}
