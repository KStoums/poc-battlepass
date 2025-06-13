package fr.kstars.battlepass;

import fr.kstars.battlepass.player.PlayerRepository;
import fr.kstars.battlepass.reward.RewardRepository;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

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
            player.sendMessage(Component.text("Usage: /battlepass", NamedTextColor.DARK_RED));
            return false;
        }

        Inventory battlepassInventory = new BattlepassInventory(this.rewardRepository, this.playerRepository).createInventory(1, player.getUniqueId());
        player.openInventory(battlepassInventory);
        return true;
    }
}
