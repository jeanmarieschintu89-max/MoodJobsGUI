package fr.moodcraft.jobsgui.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class JobsCommandBridge {

    private JobsCommandBridge() {
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
        Bukkit.dispatchCommand(player, command);
    }
}
