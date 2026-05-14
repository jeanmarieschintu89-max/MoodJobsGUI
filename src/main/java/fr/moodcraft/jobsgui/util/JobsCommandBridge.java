package fr.moodcraft.jobsgui.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class JobsCommandBridge {

    private static final Set<UUID> INTERNAL_EXECUTION = new HashSet<>();

    private JobsCommandBridge() {
    }

    public static boolean isInternalExecution(Player player) {
        return player != null && INTERNAL_EXECUTION.contains(player.getUniqueId());
    }

    public static void stats(Player player) {
        execute(player, "jobs stats");
    }

    public static void top(Player player) {
        execute(player, "jobs top");
    }

    public static void browse(Player player) {
        execute(player, "jobs browse");
    }

    public static void info(Player player, String jobName) {
        execute(player, "jobs info " + jobName);
    }

    public static void join(Player player, String jobName) {
        execute(player, "jobs join " + jobName);
    }

    public static void leave(Player player, String jobName) {
        execute(player, "jobs leave " + jobName);
    }

    private static void execute(Player player, String command) {
        if (player == null || command == null || command.isBlank()) {
            return;
        }

        INTERNAL_EXECUTION.add(player.getUniqueId());

        try {
            Bukkit.dispatchCommand(player, command);
        } finally {
            Bukkit.getScheduler().runTaskLater(
                    fr.moodcraft.jobsgui.Main.getInstance(),
                    () -> INTERNAL_EXECUTION.remove(player.getUniqueId()),
                    2L
            );
        }
    }
}
