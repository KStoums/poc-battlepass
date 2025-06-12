package fr.kstars.pocBattlepass.internal.battlepass.repositories;

import fr.kstars.pocBattlepass.internal.battlepass.entities.PlayerProfile;

import java.util.List;

public interface Repository {
    void addPlayerProfile(PlayerProfile playerProfil);
    void removePlayerProfile(String playerId);
    PlayerProfile getPlayerProfile(String playerId);
    List<PlayerProfile> getPlayerProfiles();
    void updatePlayerProfile(PlayerProfile playerProfil);
    void gracefulStop();
}
