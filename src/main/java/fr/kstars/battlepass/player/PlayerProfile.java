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
    private UUID playerId;
    private int level;
}
