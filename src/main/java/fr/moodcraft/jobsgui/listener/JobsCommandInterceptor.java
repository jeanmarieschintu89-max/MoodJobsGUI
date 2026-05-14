package fr.moodcraft.jobsgui.listener;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.gui.JobsMainGUI;
import fr.moodcraft.jobsgui.util.JobsCommandBridge;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class JobsCommandInterceptor implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {

        if (!Main.getInstance().getJobConfigManager().shouldInterceptJobsCommand()) {
            return;
        }

        Player player = event.getPlayer();

        if (JobsCommandBridge.isInternalExecution(player)) {
            return;
        }

        String message = event.getMessage();

        if (message == null || message.isBlank()) {
            return;
        }

        String lower = message.toLowerCase(Locale.ROOT).trim();

        if (!lower.equals("/jobs")
                && !lower.equals("/jobs browse")
                && !lower.equals("/jobs join")
                && !lower.equals("/jobs leave")
                && !lower.equals("/jobs info")) {
            return;
        }

        event.setCancelled(true);
        JobsMainGUI.open(player);
    }
}
