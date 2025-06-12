package fr.kstars.pocBattlepass.internal.battlepass.repositories;

import fr.kstars.pocBattlepass.internal.battlepass.entities.PlayerProfil;

import java.util.ArrayList;

public interface Repository {
    void addPlayerProfil(PlayerProfil playerProfil);
    void removePlayerProfil(String playerId);
    PlayerProfil getPlayerProfil(String playerId);
    ArrayList<PlayerProfil> getPlayerProfils();
    void updatePlayerProfil(PlayerProfil playerProfil);
    void gracefulStop();
}
