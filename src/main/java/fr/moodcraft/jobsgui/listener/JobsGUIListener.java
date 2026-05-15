package fr.moodcraft.jobsgui.listener;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.gui.JobManageGUI;
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
        String cleanTitle = MoodStyle.cleanTitle(title);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1.2f);

        if (cleanTitle.equals(MoodStyle.cleanTitle(JobsMainGUI.TITLE))) {
            handleMain(player, slot);
            return;
        }

        if (cleanTitle.startsWith(JobManageGUI.TITLE_PREFIX)) {
            handleManage(player, title, slot);
        }
    }

    private void handleMain(Player player, int slot) {

        JobEntry job = JobsMainGUI.getJobBySlot(slot);

        if (job != null) {
            JobManageGUI.open(player, job);
            return;
        }

        switch (slot) {
            case 21 -> {
                player.closeInventory();
                JobsCommandBridge.stats(player);
            }
            case 23 -> {
                player.closeInventory();
                JobsCommandBridge.top(player);
            }
            case 26 -> {
                player.closeInventory();
                player.performCommand("menu");
            }
            default -> {
            }
        }
    }

    private void handleManage(Player player, String title, int slot) {
        String displayName = JobManageGUI.extractJobName(title);

        if (displayName == null) {
            JobsMainGUI.open(player);
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
            case 13 -> {
                player.closeInventory();
                JobsCommandBridge.info(player, job.commandName());
            }
            case 16 -> {
                player.closeInventory();
                JobsCommandBridge.leave(player, job.commandName());
            }
            case 22 -> JobsMainGUI.open(player);
            default -> {
            }
        }
    }

    private JobEntry findJobByName(String displayName) {
        for (JobEntry job : Main.getInstance().getJobConfigManager().getJobs()) {
            if (MoodStyle.cleanTitle(job.displayName()).equals(MoodStyle.cleanTitle(displayName))) {
                return job;
            }
        }

        return null;
    }

    private boolean isMoodJobsTitle(String title) {
        String cleanTitle = MoodStyle.cleanTitle(title);

        return cleanTitle.equals(MoodStyle.cleanTitle(JobsMainGUI.TITLE))
                || cleanTitle.startsWith(JobManageGUI.TITLE_PREFIX);
    }
}
