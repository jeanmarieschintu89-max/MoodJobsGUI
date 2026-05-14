package fr.moodcraft.jobsgui.listener;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.gui.JobsListGUI;
import fr.moodcraft.jobsgui.gui.JobsMainGUI;

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

        if (player.hasPermission("moodjobsgui.bypass")) {
            return;
        }

        String message = event.getMessage();

        if (message == null || message.isBlank()) {
            return;
        }

        String lower = message.toLowerCase(Locale.ROOT).trim();

        if (!lower.startsWith("/jobs")) {
            return;
        }

        String[] parts = lower.split("\\s+");

        if (parts.length == 1
                || parts[1].equals("browse")
                || parts[1].equals("join")
                || parts[1].equals("leave")
                || parts[1].equals("info")) {

            event.setCancelled(true);

            if (parts.length >= 2
                    && (parts[1].equals("join")
                    || parts[1].equals("leave")
                    || parts[1].equals("info"))) {
                JobsListGUI.open(player);
                return;
            }

            JobsMainGUI.open(player);
        }
    }
}
