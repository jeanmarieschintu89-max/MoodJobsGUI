package fr.moodcraft.jobsgui;

import fr.moodcraft.jobsgui.command.MoodJobsCommand;
import fr.moodcraft.jobsgui.listener.JobsCommandInterceptor;
import fr.moodcraft.jobsgui.listener.JobsGUIListener;
import fr.moodcraft.jobsgui.manager.JobConfigManager;
import fr.moodcraft.jobsgui.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static final String BRAND_PLAIN = "MoodCraft";

    private static Main instance;

    private JobConfigManager jobConfigManager;

    public static Main getInstance() {
        return instance;
    }

    public JobConfigManager getJobConfigManager() {
        return jobConfigManager;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        jobConfigManager = new JobConfigManager(this);
        jobConfigManager.load();

        MoodJobsCommand command = new MoodJobsCommand(this);
        registerCommand("metiers", command);
        registerCommand("moodjobsgui", command);

        Bukkit.getPluginManager().registerEvents(new JobsGUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new JobsCommandInterceptor(), this);

        getLogger().info("=================================");
        getLogger().info("✦ MoodJobsGUI activé");
        getLogger().info("Interface métiers MoodCraft prête");
        getLogger().info("Métiers chargés : " + jobConfigManager.getJobs().size());
        getLogger().info("Redirection /jobs : " + jobConfigManager.shouldInterceptJobsCommand());
        getLogger().info("=================================");

        if (Bukkit.getPluginManager().getPlugin("Jobs") == null) {
            getLogger().warning("Jobs Reborn introuvable. MoodJobsGUI reste actif, mais les boutons Jobs ne pourront pas agir.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("✦ MoodJobsGUI désactivé");
    }

    private void registerCommand(String name, org.bukkit.command.CommandExecutor executor) {
        PluginCommand command = getCommand(name);

        if (command == null) {
            getLogger().warning("Commande introuvable dans plugin.yml : " + name);
            return;
        }

        command.setExecutor(executor);
    }
}
