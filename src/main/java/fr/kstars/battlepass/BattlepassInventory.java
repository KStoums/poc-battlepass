package fr.kstars.battlepass;

import fr.kstars.battlepass.player.PlayerProfile;
import fr.kstars.battlepass.reward.Reward;
import fr.kstars.battlepass.reward.RewardRepository;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BattlepassInventory {
    private final RewardRepository rewardRepository;

    public Inventory createInventory(int page, PlayerProfile playerProfile) {
        int pageEndLevel = 9 * page;
        int pageStartLevel = pageEndLevel - 9;

        Inventory inventory = Bukkit.createInventory(null, 45, Component.text("§4§lBattlepass §7| §4Level §l" + pageStartLevel + " §4to §l" + pageEndLevel));
        addFreeItems(inventory, page, playerProfile);
        addProgressItems(inventory, page, playerProfile);
        addNavigationAndProfileItems(inventory, playerProfile);
//        addPremiumItems(inventory, page, playerProfile);
        return inventory;
    }

    private void addFreeItems(Inventory inventory, int page, PlayerProfile playerProfile) {
        List<Reward> rewards = this.rewardRepository.findByPage(page, false);

        for (Reward reward : rewards) {
            ItemStack item = new ItemStack(reward.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(reward.getName()));
            meta.lore(List.of(Component.text(reward.getDescription())));

            if (playerProfile.getLevel() >= reward.getLevel()) {
                meta.addEnchant(Enchantment.LOYALTY, 1, false);
            }
            item.setItemMeta(meta);

            int itemSlot = this.getItemSlot(reward.getLevel());
            inventory.setItem(itemSlot, item);
        }
    }

    private void addPremiumItems(Inventory inventory, int page, PlayerProfile playerProfile) {
        List<Reward> rewards = this.rewardRepository.findByPage(page, true);

        for (Reward reward : rewards) {
            ItemStack item = new ItemStack(reward.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(reward.getName()));
            meta.lore(List.of(Component.text(reward.getDescription())));

            if (playerProfile.getLevel() >= reward.getLevel()) {
                meta.addEnchant(Enchantment.LOYALTY, 1, false);
            }
            item.setItemMeta(meta);

            int itemSlot = this.getItemSlot(reward.getLevel());

            inventory.setItem(itemSlot, item);
        }
    }

    private void addProgressItems(Inventory inventory, int page, PlayerProfile playerProfile) {
        int pageLevelIndex = (9 * page) - 8;

        for (int i = 9; i < 18; i++) { //9 = First inventory slot where to start progress items & 19 = last inventory slots
            if (playerProfile.getLevel() >= pageLevelIndex) {
                ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text("§2UNLOCKED"));
                item.setItemMeta(meta);
                inventory.setItem(i, item);

                pageLevelIndex++;
                continue;
            }

            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("§4LOCKED"));
            item.setItemMeta(meta);
            inventory.setItem(i, item);
            pageLevelIndex++;
        }
    }

    private void addNavigationAndProfileItems(Inventory inventory, PlayerProfile playerProfile) {
        ItemStack navigationItem = new ItemStack(Material.ARROW);
        ItemMeta meta = navigationItem.getItemMeta();
        meta.displayName(Component.text("§7Previous Page"));
        navigationItem.setItemMeta(meta);
        inventory.setItem(39, navigationItem);

        meta.displayName(Component.text("§7Next Page"));
        navigationItem.setItemMeta(meta);
        inventory.setItem(41, navigationItem);

        ItemStack playerProfileItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta playerProfileItemMeta = playerProfileItem.getItemMeta();

        String playerUsername = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(Bukkit.getPlayer(playerProfile.getPlayerId())).displayName());
        playerProfileItemMeta.displayName(Component.text("§4§l● Profile"));
        playerProfileItemMeta.lore(List.of(
                Component.text("§fUsername: §7" + playerUsername),
                Component.text("§fLevel: §7" + playerProfile.getLevel())
        ));
        playerProfileItem.setItemMeta(playerProfileItemMeta);
        inventory.setItem(40, playerProfileItem);
    }

    private int getItemSlot(int itemLevel) {
        return (itemLevel - 1) % 9;
    }
}
