package fr.kstars.pocBattlepass.cmd;

import fr.kstars.pocBattlepass.internal.battlepass.repositories.JsonFileRepository;
import fr.kstars.pocBattlepass.internal.battlepass.repositories.Repository;
import fr.kstars.pocBattlepass.internal.utils.ChatUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private Repository repository;

    @Override
    public void onEnable() {
        this.repository = new JsonFileRepository(this);

        getLogger().info(ChatUtils.PluginPrefixWithoutColor + "Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        this.repository.gracefulStop();

        getLogger().info(ChatUtils.PluginPrefixWithoutColor + "Plugin Disabled!");
    }
}
