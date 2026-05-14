package fr.moodcraft.jobsgui.model;

import org.bukkit.Material;

import java.util.List;

public record JobEntry(
        String displayName,
        String commandName,
        Material icon,
        List<String> description
) {
}
