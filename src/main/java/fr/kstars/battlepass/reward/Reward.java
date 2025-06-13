package fr.kstars.battlepass.reward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor // <- Required for Jackson
@Getter
public class Reward {
    private UUID uuid;
    private String name;
    private String description;
    private Material material;
    private int level;
    private boolean premium;
}
