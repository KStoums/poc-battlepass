package fr.kstars.pocBattlepass.internal.battlepass.entities;

public class PlayerProfile {
    private final String playerId;
    private Integer level;

    public PlayerProfile(String userId, Integer level) {
        this.playerId = userId;
        this.level = level;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void updateLevel(Integer newLevel) {
        this.level = newLevel;
    }
}
