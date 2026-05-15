package fr.moodcraft.jobsgui.manager;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.model.JobEntry;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class JobConfigManager {

    private final Main plugin;
    private final List<JobEntry> jobs = new ArrayList<>();

    public JobConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void load() {
        jobs.clear();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        List<?> list = plugin.getConfig().getList("jobs");

        if (list == null || list.isEmpty()) {
            loadFallbackJobs();
            return;
        }

        boolean normalizeDefaultJobs = plugin.getConfig().getBoolean(
                "settings.normalize-default-jobs-reborn-names",
                true
        );

        for (int i = 0; i < list.size(); i++) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("jobs." + i);

            if (section == null) {
                continue;
            }

            String name = section.getString("name", "Métier");
            String commandName = section.getString("command-name", name);

            if (normalizeDefaultJobs) {
                commandName = normalizeJobsRebornName(commandName, name);
            }

            String iconName = section.getString("icon", "BOOK");
            Material icon = Material.matchMaterial(iconName);

            if (icon == null || !icon.isItem()) {
                icon = Material.BOOK;
            }

            List<String> description = section.getStringList("description");

            if (description.isEmpty()) {
                description = List.of("Métier disponible sur " + Main.BRAND_PLAIN + ".");
            }

            jobs.add(new JobEntry(name, commandName, icon, description));
        }

        if (jobs.isEmpty()) {
            loadFallbackJobs();
        }
    }

    public List<JobEntry> getJobs() {
        return Collections.unmodifiableList(jobs);
    }

    public boolean shouldInterceptJobsCommand() {
        return plugin.getConfig().getBoolean("settings.intercept-jobs-command", true);
    }

    private String normalizeJobsRebornName(String commandName, String displayName) {

        String raw = commandName == null || commandName.isBlank()
                ? displayName
                : commandName;

        String clean = clean(raw);

        return switch (clean) {
            case "mineur", "miner" -> "Miner";
            case "bucheron", "woodcutter" -> "Woodcutter";
            case "agriculteur", "fermier", "farmer" -> "Farmer";
            case "chasseur", "hunter" -> "Hunter";
            default -> raw;
        };
    }

    private String clean(String text) {

        if (text == null) {
            return "";
        }

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized
                .replaceAll("§.", "")
                .replace("_", " ")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private void loadFallbackJobs() {
        jobs.add(new JobEntry("Mineur", "Miner", Material.DIAMOND_PICKAXE, List.of("Gagnez de l'argent en minant.")));
        jobs.add(new JobEntry("Bûcheron", "Woodcutter", Material.DIAMOND_AXE, List.of("Coupez du bois pour progresser.")));
        jobs.add(new JobEntry("Agriculteur", "Farmer", Material.WHEAT, List.of("Cultivez et récoltez pour gagner.")));
        jobs.add(new JobEntry("Chasseur", "Hunter", Material.BOW, List.of("Chassez les créatures hostiles.")));
    }
}
