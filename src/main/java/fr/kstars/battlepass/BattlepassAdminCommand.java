package fr.kstars.battlepass;

import fr.kstars.battlepass.player.PlayerProfile;
import fr.kstars.battlepass.player.PlayerRepository;
import fr.kstars.battlepass.reward.JsonRewardFileLoader;
import fr.kstars.battlepass.reward.Reward;
import fr.kstars.battlepass.reward.RewardRepository;
import fr.kstars.battlepass.util.ChatUtil;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BattlepassAdminCommand implements CommandExecutor {
    private static final String RESET_PLAYER_OPTION = "resetplayer";
    private static final String SET_LEVEL_OPTION = "setlevel";
    private static final String ADD_LEVEL_OPTION = "addlevel";
    private static final String RELOAD_REWARDS_OPTION = "reloadrewards";

    private final PlayerRepository playerRepository;
    private final RewardRepository rewardRepository;
    private final JsonRewardFileLoader jsonRewardFileLoader;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String message, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /battlepass-admin <option>", NamedTextColor.DARK_RED));
            return false;
        }

        switch (args[0]) {
            case RESET_PLAYER_OPTION:
                resetPlayerOption(player, args);
                break;
            case SET_LEVEL_OPTION:
                setLevelOption(player, args);
                break;
            case ADD_LEVEL_OPTION:
                addLevelOption(player, args);
                break;
            case RELOAD_REWARDS_OPTION:
                reloadRewardsOption(player, args);
                break;
            default:
                player.sendMessage(Component.text("Usage: /battlepass-admin <option>", NamedTextColor.DARK_RED));
                return false;
        }
        return true;
    }

    private void resetPlayerOption(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(Component.text("Usage: /battlepass-admin resetplayer <player>"));
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);

        Optional<PlayerProfile> targetProfile = this.playerRepository.findById(targetPlayer.getUniqueId());
        if (targetProfile.isEmpty()) {
            player.sendMessage(
                    Component.text("Error: Player", NamedTextColor.DARK_RED).
                            appendSpace().
                            append(Component.text(Objects.requireNonNull(targetPlayer.getName()), NamedTextColor.DARK_RED)).
                            appendSpace().
                            append(Component.text("does not exist.", NamedTextColor.DARK_RED))
            );
            return;
        }

        targetProfile.get().setExperience(0);
        player.sendMessage(ChatUtil.PLUGIN_PREFIX_WITH_COLOR.
                appendSpace().
                append(Component.text(Objects.requireNonNull(targetPlayer.getName()) + "'s", NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, false)).
                appendSpace().
                append(Component.text("profile has been", NamedTextColor.WHITE)).
                appendSpace().
                append(Component.text("reset", NamedTextColor.DARK_RED)).
                append(Component.text(".", NamedTextColor.WHITE))
        );
    }

    private void setLevelOption(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(Component.text("Usage: /battlepass-admin setlevel <level> <player>"));
            return;
        }

        String level = args[1];
        try {
            int levelInt = Integer.parseInt(level);
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);

            Optional<PlayerProfile> targetProfile = this.playerRepository.findById(targetPlayer.getUniqueId());
            if (targetProfile.isEmpty()) {
                player.sendMessage(
                        Component.text("Error: Player", NamedTextColor.DARK_RED).
                                appendSpace().
                                append(Component.text(Objects.requireNonNull(targetPlayer.getName()), NamedTextColor.DARK_RED)).
                                appendSpace().
                                append(Component.text("does not exist.", NamedTextColor.DARK_RED))
                );
                return;
            }

            double levelExperienceRequired = targetProfile.get().levelToExperience(levelInt);
            targetProfile.get().setExperience(levelExperienceRequired);
            player.sendMessage(ChatUtil.PLUGIN_PREFIX_WITH_COLOR.
                    appendSpace().
                    append(Component.text("The", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text(Objects.requireNonNull(targetPlayer.getName()) + "'s", NamedTextColor.DARK_RED)).
                    appendSpace().
                    append(Component.text("level has been", NamedTextColor.WHITE)).
                    appendSpace().
                    append(Component.text("updated", NamedTextColor.DARK_RED)).
                    append(Component.text(".", NamedTextColor.WHITE))
            );
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("Usage: /battlepass-admin setlevel <level> <player>"));
        }
    }

    private void addLevelOption(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(Component.text("Usage: /battlepass-admin addlevel <level> <player>"));
            return;
        }

        String level = args[1];
        try {
            int levelInt = Integer.parseInt(level);
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);

            Optional<PlayerProfile> targetProfile = this.playerRepository.findById(targetPlayer.getUniqueId());
            if (targetProfile.isEmpty()) {
                player.sendMessage(
                        Component.text("Error: Player", NamedTextColor.DARK_RED).
                                appendSpace().
                                append(Component.text(Objects.requireNonNull(targetPlayer.getName()), NamedTextColor.DARK_RED)).
                                appendSpace().
                                append(Component.text("does not exist.", NamedTextColor.DARK_RED))
                );
                return;
            }

            double levelExperienceRequired = targetProfile.get().levelToExperience(levelInt);
            targetProfile.get().setExperience(targetProfile.get().getExperience() + levelExperienceRequired);
            player.sendMessage(ChatUtil.PLUGIN_PREFIX_WITH_COLOR.
                    appendSpace().
                    append(Component.text("The", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text(Objects.requireNonNull(targetPlayer.getName()) + "'s", NamedTextColor.DARK_RED)).
                    appendSpace().
                    append(Component.text("level has been", NamedTextColor.WHITE)).
                    appendSpace().
                    append(Component.text("updated", NamedTextColor.DARK_RED)).
                    append(Component.text(".", NamedTextColor.WHITE))
            );
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("Usage: /battlepass-admin addlevel <level> <player>"));
        }
    }

    private void reloadRewardsOption(Player player, String[] args) { //TODO RE OPEN FILE IF NEW DATA HAS BEEN ADDED
        if (args.length != 1) {
            player.sendMessage(Component.text("Usage: /battlepass-admin reloadrewards", NamedTextColor.DARK_RED));
            return;
        }

        try {
            File jsonDataFile = this.jsonRewardFileLoader.loadJsonRewardFile();
            Reward[] rewardsJsonData = this.jsonRewardFileLoader.getJsonData(jsonDataFile);
            this.rewardRepository.updateRewards(rewardsJsonData);

            player.sendMessage(ChatUtil.PLUGIN_PREFIX_WITH_COLOR.
                    appendSpace().
                    append(Component.text("Rewards reloaded.", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
            );
        } catch (IOException e) {
            player.sendMessage(Component.text("Error: Could not load json reward file.", NamedTextColor.DARK_RED));
        }
    }
}
