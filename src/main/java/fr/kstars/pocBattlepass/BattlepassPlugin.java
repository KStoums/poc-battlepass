package fr.kstars.pocBattlepass;

import fr.kstars.pocBattlepass.internal.battlepass.JsonPlayerRepository;
import fr.kstars.pocBattlepass.internal.battlepass.PlayerRepository;
import fr.kstars.pocBattlepass.internal.utils.ChatUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class BattlepassPlugin extends JavaPlugin {
    private PlayerRepository repository;

    @Override
    public void onEnable() {
        this.repository = new JsonPlayerRepository(this);

        getLogger().info(ChatUtils.PLUGIN_PREFIX_WITHOUT_COLOR + "Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        this.repository.gracefulStop();

        getLogger().info(ChatUtils.PLUGIN_PREFIX_WITHOUT_COLOR + "Plugin Disabled!");
    }
}
