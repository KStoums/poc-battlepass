package fr.kstars.battlepass;

import fr.kstars.battlepass.player.PlayerProfile;
import fr.kstars.battlepass.player.PlayerRepository;
import fr.kstars.battlepass.reward.RewardRepository;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@AllArgsConstructor
public class BattlepassCommand implements CommandExecutor {
    private final RewardRepository rewardRepository;
    private final PlayerRepository playerRepository;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String message, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length != 0) {
            player.sendMessage("§4Usage: /battlepass");
            return false;
        }

        Optional<PlayerProfile> playerProfile = this.playerRepository.findById(player.getUniqueId());
        if (playerProfile.isEmpty()) {
            PlayerProfile newPlayerProfile = new PlayerProfile(player.getUniqueId(), 0);
            this.playerRepository.add(newPlayerProfile);
            playerProfile = Optional.of(newPlayerProfile);
        }

        Inventory battlepassInventory = new BattlepassInventory(this.rewardRepository).createInventory(1, playerProfile.get());
        player.openInventory(battlepassInventory);
        return true;
    }
}
