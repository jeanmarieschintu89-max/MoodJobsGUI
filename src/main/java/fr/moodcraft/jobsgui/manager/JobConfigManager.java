package fr.moodcraft.jobsgui.manager;

import fr.moodcraft.jobsgui.Main;
import fr.moodcraft.jobsgui.model.JobEntry;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        ensureDefaultJobsPresent();
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

    private void ensureDefaultJobsPresent() {
        List<?> currentJobs = plugin.getConfig().getList("jobs");
        List<Object> updatedJobs = new ArrayList<>();

        if (currentJobs != null) {
            updatedJobs.addAll(currentJobs);
        }

        boolean changed = false;

        changed |= addDefaultJobIfMissing(
                updatedJobs,
                "Mineur",
                "mineur",
                "GOLDEN_PICKAXE",
                List.of(
                        "Casse des minerais et blocs rares.",
                        "Charbon 1€ / Fer 2€ / Or 3€.",
                        "Diamant 12€ / Émeraude 15€.",
                        "Débris antiques 25€."
                )
        );

        changed |= addDefaultJobIfMissing(
                updatedJobs,
                "Bûcheron",
                "bucheron",
                "DIAMOND_AXE",
                List.of(
                        "Coupe les arbres et tiges du Nether.",
                        "Bois classiques 2€.",
                        "Acacia 2.2€.",
                        "Mangrove, cerisier 3€.",
                        "Crimson, warped 4€."
                )
        );

        changed |= addDefaultJobIfMissing(
                updatedJobs,
                "Agriculteur",
                "fermier",
                "WOODEN_HOE",
                List.of(
                        "Cultive et récolte tes champs.",
                        "Blé, carottes, patates 1€.",
                        "Citrouille, melon, cacao 2€.",
                        "Miel et rayon de miel 3€."
                )
        );

        changed |= addDefaultJobIfMissing(
                updatedJobs,
                "Chasseur",
                "chasseur",
                "IRON_SWORD",
                List.of(
                        "Tue animaux, monstres et boss.",
                        "Animaux 2€ à 4€.",
                        "Monstres 4€ à 60€.",
                        "Boss et rares 100€ à 450€."
                )
        );

        changed |= addDefaultJobIfMissing(
                updatedJobs,
                "Pêcheur",
                "pecheur",
                "FISHING_ROD",
                List.of(
                        "Pêche poissons, trésors et objets rares.",
                        "Gagne de l'argent selon tes prises.",
                        "Progression XP selon la rareté."
                )
        );

        if (!changed) {
            return;
        }

        plugin.getConfig().set("jobs", updatedJobs);
        plugin.saveConfig();
        plugin.getLogger().info("Configuration métiers complétée avec les métiers par défaut manquants.");
    }

    private boolean addDefaultJobIfMissing(
            List<Object> jobsConfig,
            String name,
            String commandName,
            String icon,
            List<String> description
    ) {
        if (containsJob(jobsConfig, name, commandName)) {
            return false;
        }

        Map<String, Object> job = new LinkedHashMap<>();
        job.put("name", name);
        job.put("icon", icon);
        job.put("command-name", commandName);
        job.put("description", description);
        jobsConfig.add(job);
        return true;
    }

    private boolean containsJob(List<Object> jobsConfig, String displayName, String commandName) {
        String wantedDisplayName = clean(displayName);
        String wantedCommandName = clean(commandName);

        for (Object object : jobsConfig) {
            if (!(object instanceof Map<?, ?> map)) {
                continue;
            }

            Object rawName = map.get("name");
            Object rawCommandName = map.get("command-name");

            String existingName = rawName == null ? "" : clean(String.valueOf(rawName));
            String existingCommandName = rawCommandName == null ? existingName : clean(String.valueOf(rawCommandName));

            if (existingName.equals(wantedDisplayName)
                    || existingCommandName.equals(wantedCommandName)) {
                return true;
            }
        }

        return false;
    }

    private String normalizeJobsRebornName(String commandName, String displayName) {

        String raw = commandName == null || commandName.isBlank()
                ? displayName
                : commandName;

        String clean = clean(raw);

        return switch (clean) {
            case "mineur", "miner" -> "mineur";
            case "bucheron", "woodcutter" -> "bucheron";
            case "agriculteur", "fermier", "farmer" -> "fermier";
            case "chasseur", "hunter" -> "chasseur";
            case "pecheur", "fisher", "fisherman" -> "pecheur";
            default -> clean.isBlank() ? raw : clean.replace(" ", "_");
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
        jobs.add(new JobEntry("Mineur", "mineur", Material.DIAMOND_PICKAXE, List.of("Gagnez de l'argent en minant.")));
        jobs.add(new JobEntry("Bûcheron", "bucheron", Material.DIAMOND_AXE, List.of("Coupez du bois pour progresser.")));
        jobs.add(new JobEntry("Agriculteur", "fermier", Material.WHEAT, List.of("Cultivez et récoltez pour gagner.")));
        jobs.add(new JobEntry("Chasseur", "chasseur", Material.BOW, List.of("Chassez les créatures hostiles.")));
        jobs.add(new JobEntry("Pêcheur", "pecheur", Material.FISHING_ROD, List.of("Pêchez poissons, trésors et objets rares.")));
    }
}