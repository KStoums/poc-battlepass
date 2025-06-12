package fr.kstars.pocBattlepass.internal.battlepass;

import java.util.List;

public interface PlayerRepository {
    void addPlayerProfile(PlayerProfile playerProfil);
    void removePlayerProfile(String playerId);
    PlayerProfile getPlayerProfile(String playerId);
    List<PlayerProfile> getPlayerProfiles();
    void updatePlayerProfile(PlayerProfile playerProfil);
    void gracefulStop();
}
