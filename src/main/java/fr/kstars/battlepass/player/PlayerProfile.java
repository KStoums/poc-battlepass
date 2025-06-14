package fr.kstars.battlepass.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor // <- Required for Jackson
@Setter
@Getter
public class PlayerProfile {
    private static double XP_BASE = 100;
    private static double GROWTH_RATE = 1.5;

    private UUID playerId;
    private double experience;

    public int experienceToLevel(double experience) {
        double total = 0;
        int level = 0;

        while (true) {
            double required = XP_BASE * Math.pow(GROWTH_RATE, level - 1);
            if (total + required > experience) break;

            total += required;
            level++;
        }

        return level;
    }

    public double levelToExperience(int level) {
        double total = 0;

        for (int i = 0; i < level; i++) {
            total += XP_BASE * Math.pow(GROWTH_RATE, i - 1);
        }

        return (int) Math.round(total);
    }
}