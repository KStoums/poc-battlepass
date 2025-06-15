package fr.kstars.battlepass;

import fr.kstars.battlepass.player.PlayerProfile;
import fr.kstars.battlepass.player.PlayerRepository;
import fr.kstars.battlepass.reward.Reward;
import fr.kstars.battlepass.reward.RewardRepository;
import fr.kstars.battlepass.util.ChatUtil;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class EntityDeathListener implements Listener {
    private final PlayerRepository playerRepository;
    private final RewardRepository rewardRepository;
    public static final int EXP_PER_PLAYER_KILL = 20;
    public static final int EXP_PER_MOB_KILL = 10;

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDeath(EntityDeathEvent event) {
//        if (!(event.getEntity() instanceof Player player)) { //Activate if you only want to earn exp by killing players
//            return;
//        }

        if (event.getEntity().getKiller() == null) {
            return;
        }

        Player killer = event.getEntity().getKiller();

        Optional<PlayerProfile> optionalKillerProfile = this.playerRepository.findById(killer.getUniqueId());
        if (optionalKillerProfile.isEmpty()) {
            PlayerProfile newPlayerProfile = new PlayerProfile(killer.getUniqueId(), 0);
            this.playerRepository.add(newPlayerProfile);
            optionalKillerProfile = Optional.of(newPlayerProfile);
        }

        PlayerProfile killerProfile = optionalKillerProfile.get();
        int oldKillerLevel = killerProfile.expToLevel(killerProfile.getExp());
        String entityType;
        int expEarns;
        if (event.getEntity() instanceof Player) {
            entityType = "player";
            expEarns = EXP_PER_PLAYER_KILL;
        } else {
            entityType = "mob";
            expEarns = EXP_PER_MOB_KILL;
        }

        if (!killerProfile.addExpCheckLevelUp(expEarns)) {
            killer.sendMessage(ChatUtil.PLUGIN_PREFIX_WITH_COLOR.
                    append(Component.empty().decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text("Killing", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text(entityType, NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text("earns you", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text(expEarns, NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text("EXP.", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
            );
            return;
        }

        List<Reward> rewards = this.rewardRepository.findAllByLevel(killerProfile.expToLevel(killerProfile.getExp()));
        if (rewards.isEmpty()) {
            killer.sendMessage(ChatUtil.PLUGIN_PREFIX_WITH_COLOR.
                    append(Component.empty().decoration(TextDecoration.BOLD, false)).
                    appendSpace().
                    append(Component.text("You've gone up a level!", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
            );
            return;
        }

        killerProfile.checkIfRewardUnlocked(killer, rewards, killerProfile.expToLevel(killerProfile.getExp()), oldKillerLevel);
    }
}
