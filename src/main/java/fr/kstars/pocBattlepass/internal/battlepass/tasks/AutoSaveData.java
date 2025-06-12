package fr.kstars.pocBattlepass.internal.battlepass.tasks;

import fr.kstars.pocBattlepass.internal.battlepass.repositories.Repository;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveData {
    private final JavaPlugin plugin;
    private final Repository repository;

    public AutoSaveData(JavaPlugin plugin, Repository repository) {
        this.plugin = plugin;
        this.repository = repository;
    }

    private void startTask() {
        Repository repository  = this.repository;

        new BukkitRunnable() {
            @Override
            public void run() {
            }
        }.runTaskLater(plugin, 1200 * 5); //Every 5 minutes
    }
}