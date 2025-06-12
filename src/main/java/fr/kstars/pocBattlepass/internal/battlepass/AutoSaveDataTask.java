package fr.kstars.pocBattlepass.internal.battlepass;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveDataTask {
    private final JavaPlugin plugin;
    private final PlayerRepository repository;

    public AutoSaveDataTask(JavaPlugin plugin, PlayerRepository repository) {
        this.plugin = plugin;
        this.repository = repository;
    }

    private void startTask() {
        PlayerRepository repository  = this.repository;

        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskLater(plugin, 1200 * 5); //Every 5 minutes
    }
}