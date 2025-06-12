package fr.kstars.pocBattlepass.internal.battlepass.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.kstars.pocBattlepass.internal.battlepass.entities.PlayerProfile;
import fr.kstars.pocBattlepass.internal.utils.ChatUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class JsonFileRepository implements Repository {
    private final JavaPlugin plugin;

    private File jsonDataFile;
    private List<PlayerProfile> cachedPlayerProfils = List.of();

    public JsonFileRepository(JavaPlugin plugin) {
        this.plugin = plugin;

        try {
            initJsonDataFile();
        } catch (IOException error) {
            plugin.getLogger().log(Level.SEVERE, ChatUtils.PluginPrefixWithoutColor + "Could not initialize JsonFileRepository!", error);
        }
    }

    @Override
    public void addPlayerProfile(PlayerProfile playerProfil) {
        if (!this.cachedPlayerProfils.contains(playerProfil)) {
            this.cachedPlayerProfils.add(playerProfil);
        }
    }

    @Override
    public void removePlayerProfile(String playerId) {
        for (PlayerProfile playerProfil : this.cachedPlayerProfils) {
            if (playerProfil.getPlayerId().equals(playerId)) {
                this.cachedPlayerProfils.remove(playerProfil);
                return;
            }
        }
    }

    @Override
    public PlayerProfile getPlayerProfile(String playerId) {
        for (PlayerProfile playerProfil : this.cachedPlayerProfils) {
            if (playerProfil.getPlayerId().equals(playerId)) {
                return playerProfil;
            }
        }
        return null;
    }

    @Override
    public List<PlayerProfile> getPlayerProfiles() {
        return this.cachedPlayerProfils;
    }

    @Override
    public void updatePlayerProfile(PlayerProfile newPlayerProfil) {
        for (int i = 0; i < this.cachedPlayerProfils.size(); i++) {
            if (newPlayerProfil.getPlayerId().equals(this.cachedPlayerProfils.get(i).getPlayerId())) {
                this.cachedPlayerProfils.set(i, newPlayerProfil);
            }
        }
    }

    @Override
    public void gracefulStop() {
        try {
            saveCachedPlayerProfils();
        } catch (IOException error) {
            this.plugin.getLogger().log(Level.SEVERE, ChatUtils.PluginPrefixWithoutColor + "Could not save JsonFileRepository!", error);
        }
    }

    private void initJsonDataFile() throws IOException {
        try {
            this.jsonDataFile = new File("./data/players.json");
            if (!this.jsonDataFile.exists()) {
                this.jsonDataFile.createNewFile();
            }

            ObjectMapper playerProfilMapper = new ObjectMapper();
            this.cachedPlayerProfils = playerProfilMapper.readValue(this.jsonDataFile, new TypeReference<ArrayList<PlayerProfile>>() {});
        } catch (IOException error) {
            throw  new IOException(error.getMessage());
        }
    }

    private void saveCachedPlayerProfils() throws IOException {
        try {
            new ObjectMapper().writeValue(this.jsonDataFile, this.cachedPlayerProfils);
        } catch (IOException error) {
            throw  new IOException(error.getMessage());
        }
    }
}
