package fr.kstars.battlepass;

import fr.kstars.battlepass.player.PlayerProfile;
import fr.kstars.battlepass.player.PlayerRepository;
import fr.kstars.battlepass.reward.Reward;
import fr.kstars.battlepass.reward.RewardRepository;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class BattlepassInventory {
    private final RewardRepository rewardRepository;
    private final PlayerRepository playerRepository;
    public static final Component NEXT_PAGE_ITEM_NAME = Component.text("Next Page", NamedTextColor.GRAY);
    public static final Component PREVIOUS_PAGE_ITEM_NAME = Component.text("Previous Page", NamedTextColor.GRAY);
    public static final Component PROFILE_ITEM_NAME = Component.text("●", NamedTextColor.DARK_RED, TextDecoration.BOLD).
            appendSpace().
            append(Component.text("Profile", NamedTextColor.DARK_RED, TextDecoration.BOLD));
    public static final String BATTLEPASS_INVENTORY_NAME = "§4§lBattlepass §7| §4Level §l%levelStart% §4to §l%levelEnd%";

    public Inventory createInventory(int page, UUID playerUuid) {
        Optional<PlayerProfile> playerProfile = this.playerRepository.findById(playerUuid);
        if (playerProfile.isEmpty()) {
            PlayerProfile newPlayerProfile = new PlayerProfile(playerUuid, 0);
            this.playerRepository.add(newPlayerProfile);
            playerProfile = Optional.of(newPlayerProfile);
        }

        int pageEndLevel = 9 * page;
        int pageStartLevel = pageEndLevel - 9;

        String InventoryName = BATTLEPASS_INVENTORY_NAME.replace("%levelStart%", String.valueOf(pageStartLevel)).
                replace("%levelEnd%", String.valueOf(pageEndLevel));

        Inventory inventory = Bukkit.createInventory(null, 45, Component.text(InventoryName));
        addFreeItems(inventory, page, playerProfile.get());
        addProgressItems(inventory, page, playerProfile.get());
        addNavigationAndProfileItems(inventory, playerProfile.get(), page);
        addPremiumItems(inventory, page, playerProfile.get());
        return inventory;
    }

    private void addFreeItems(Inventory inventory, int currentPage, PlayerProfile playerProfile) {
        List<Reward> rewards = this.rewardRepository.findByPage(currentPage, false);

        for (Reward reward : rewards) {
            if (reward.getMaterial().isEmpty()) {
                reward.setMaterial(Material.CHEST_MINECART);
            }

            ItemStack item = new ItemStack(reward.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(reward.getName()));
            meta.lore(List.of(Component.text(reward.getDescription())));

            if (playerProfile.getLevel() >= reward.getLevel()) {
                meta.addEnchant(Enchantment.LOYALTY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);

            inventory.setItem(this.getItemSlot(reward.getLevel(), false), item);
        }
    }

    private void addPremiumItems(Inventory inventory, int currentPage, PlayerProfile playerProfile) {
        List<Reward> rewards = this.rewardRepository.findByPage(currentPage, true);

        for (Reward reward : rewards) {
            if (reward.getMaterial().isEmpty()) {
                reward.setMaterial(Material.CHEST_MINECART);
            }

            ItemStack item = new ItemStack(reward.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(reward.getName()));
            meta.lore(List.of(Component.text(reward.getDescription())));

            if (playerProfile.getLevel() >= reward.getLevel()) {
                meta.addEnchant(Enchantment.LOYALTY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);

            inventory.setItem(this.getItemSlot(reward.getLevel(), true), item);
        }
    }

    private void addProgressItems(Inventory inventory, int currentPage, PlayerProfile playerProfile) {
        int pageLevelIndex = (9 * currentPage) - 8;

        for (int i = 9; i < 18; i++) { //9 = First inventory slot where to start progress items & 19 = last inventory slots
            if (playerProfile.getLevel() >= pageLevelIndex) {
                ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text("UNLOCKED", NamedTextColor.DARK_GREEN));
                item.setItemMeta(meta);
                inventory.setItem(i, item);

                pageLevelIndex++;
                continue;
            }

            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("LOCKED", NamedTextColor.DARK_RED));
            item.setItemMeta(meta);
            inventory.setItem(i, item);
            pageLevelIndex++;
        }
    }

    private void addNavigationAndProfileItems(Inventory inventory, PlayerProfile playerProfile, int page) {
        ItemStack navigationItem = new ItemStack(Material.ARROW);
        ItemMeta meta = navigationItem.getItemMeta();
        if (page > 1) {
            meta.displayName(PREVIOUS_PAGE_ITEM_NAME);
            navigationItem.setItemMeta(meta);
            inventory.setItem(39, navigationItem);
        }

        if (!this.rewardRepository.findByPage(page + 1, false).isEmpty() && !this.rewardRepository.findByPage(page, true).isEmpty()) {
            meta.displayName(NEXT_PAGE_ITEM_NAME);
            navigationItem.setItemMeta(meta);
            inventory.setItem(41, navigationItem);
        }

        ItemStack playerProfileItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerProfileSkullMeta = (SkullMeta) playerProfileItem.getItemMeta();

        String playerUsername = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(Bukkit.getPlayer(playerProfile.getPlayerId())).displayName());
        playerProfileSkullMeta.displayName(PROFILE_ITEM_NAME);
        playerProfileSkullMeta.lore(List.of(
                Component.text("Username:", NamedTextColor.WHITE).
                        appendSpace().
                        append(Component.text(playerUsername, NamedTextColor.GRAY)),
                Component.text("Level:", NamedTextColor.WHITE).
                        appendSpace().
                        append(Component.text(playerProfile.getLevel(), NamedTextColor.GRAY))
        ));
        playerProfileSkullMeta.setOwningPlayer(Bukkit.getPlayer(playerProfile.getPlayerId()));
        playerProfileItem.setItemMeta(playerProfileSkullMeta);
        inventory.setItem(40, playerProfileItem);
    }

    private int getItemSlot(int itemLevel, boolean premium) {
        if (!premium) {
            return (itemLevel - 1) % 9; //9 corresponds to the number of slots on the first line of inventory
        }

        return 18 + (itemLevel - 1) % 9; //18 corresponds to the first slot of the third line of an inventory
    }
}
