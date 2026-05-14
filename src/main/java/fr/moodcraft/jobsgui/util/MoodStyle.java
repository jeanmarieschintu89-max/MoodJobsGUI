package fr.moodcraft.jobsgui.util;

import org.bukkit.command.CommandSender;

public final class MoodStyle {

    public static final String BRAND = "§aMood§6Craft";
    public static final String MODULE = "Bureau des Métiers";
    public static final String FRAME = "§8-----------------------------";

    private MoodStyle() {
    }

    public static String guiTitle(String title) {
        return "§6✦ §8§l" + title + " §6✦";
    }

    public static String button(String name) {
        return "§6✦ §f" + name + " §6✦";
    }

    public static String header(String module) {
        return "§8----- §6✦ " + module + " ✦ §8-----";
    }

    public static String info(String text) {
        return "§e➜ §f" + text;
    }

    public static String success(String text) {
        return "§a✔ §f" + text;
    }

    public static String error(String text) {
        return "§c✖ §f" + text;
    }

    public static String detail(String text) {
        return "§8• §7" + text;
    }

    public static String lore(String text) {
        return "§8• §7" + text;
    }

    public static void send(CommandSender sender, String module, String... lines) {
        sender.sendMessage("");
        sender.sendMessage(header(module));

        if (lines != null) {
            for (String line : lines) {
                sender.sendMessage(line);
            }
        }

        sender.sendMessage(FRAME);
    }

    public static void successMessage(CommandSender sender, String module, String message, String... details) {
        sender.sendMessage("");
        sender.sendMessage(header(module));
        sender.sendMessage(success(message));

        if (details != null) {
            for (String detail : details) {
                sender.sendMessage(detail);
            }
        }

        sender.sendMessage(FRAME);
    }

    public static void errorMessage(CommandSender sender, String module, String message, String... details) {
        sender.sendMessage("");
        sender.sendMessage(header(module));
        sender.sendMessage(error(message));

        if (details != null) {
            for (String detail : details) {
                sender.sendMessage(detail);
            }
        }

        sender.sendMessage(FRAME);
    }
}
