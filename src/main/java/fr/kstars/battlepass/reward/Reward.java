package fr.kstars.battlepass.reward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

@AllArgsConstructor
@NoArgsConstructor // <- Required for Jackson
@Getter
@Setter
public class Reward {
    private String name;
    private String description;
    private String command;
    private Material material;
    private int level;
    private boolean premium;
}