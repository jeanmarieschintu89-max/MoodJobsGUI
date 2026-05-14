package fr.moodcraft.jobsgui.command;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.gui.JobsMainGUI;
import fr.moodcraft.jobsgui.util.MoodStyle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoodJobsCommand implements CommandExecutor {

    private final Main plugin;

    public MoodJobsCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (label.equalsIgnoreCase("moodjobsgui")) {
            return handleAdmin(sender, args);
        }

        if (!(sender instanceof Player player)) {
            MoodStyle.errorMessage(
                    sender,
                    MoodStyle.MODULE,
                    "Commande joueur uniquement."
            );
            return true;
        }

        if (!player.hasPermission("moodjobsgui.use")) {
            MoodStyle.errorMessage(
                    player,
                    MoodStyle.MODULE,
                    "Accès refusé.",
                    MoodStyle.detail("Permission requise : §emoodjobsgui.use")
            );
            return true;
        }

        JobsMainGUI.open(player);
        return true;
    }

    private boolean handleAdmin(CommandSender sender, String[] args) {

        if (!sender.hasPermission("moodjobsgui.admin")) {
            MoodStyle.errorMessage(
                    sender,
                    MoodStyle.MODULE,
                    "Accès réservé à l'administration."
            );
            return true;
        }

        if (args.length > 0
                && args[0].equalsIgnoreCase("reload")) {

            plugin.getJobConfigManager().load();

            MoodStyle.successMessage(
                    sender,
                    MoodStyle.MODULE,
                    "Configuration rechargée.",
                    MoodStyle.detail("Métiers chargés : §e" + plugin.getJobConfigManager().getJobs().size())
            );
            return true;
        }

        MoodStyle.send(
                sender,
                MoodStyle.MODULE,
                MoodStyle.info("Commandes MoodJobsGUI."),
                MoodStyle.detail("/metiers §8• §7ouvrir le menu"),
                MoodStyle.detail("/moodjobsgui reload §8• §7recharger la config")
        );
        return true;
    }
}
