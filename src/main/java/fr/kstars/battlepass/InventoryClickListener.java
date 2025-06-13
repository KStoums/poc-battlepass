package fr.kstars.battlepass;

import fr.kstars.battlepass.player.PlayerRepository;
import fr.kstars.battlepass.reward.RewardRepository;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class InventoryClickListener implements Listener {
    private final RewardRepository rewardRepository;
    private final PlayerRepository playerRepository;

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) {
            return;
        }

        String inventoryTitle = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (inventoryTitle.isEmpty()) {
            return;
        }

        Optional<Integer> currentPage = getCurrentPage(inventoryTitle);
        if (currentPage.isEmpty()) {
            return;
        }

        String itemDisplayName = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().displayName());
        String nextPageItemNameString = PlainTextComponentSerializer.plainText().serialize(BattlepassInventory.NEXT_PAGE_ITEM_NAME);
        String previousPageItemNameString = PlainTextComponentSerializer.plainText().serialize(BattlepassInventory.PREVIOUS_PAGE_ITEM_NAME);

        if (itemDisplayName.equals("[" + nextPageItemNameString + "]")) {
            Inventory nextPageBattlepassInventory = new BattlepassInventory(this.rewardRepository, playerRepository).createInventory(currentPage.get()+1, event.getWhoClicked().getUniqueId());
            event.getWhoClicked().openInventory(nextPageBattlepassInventory);
        } else if (itemDisplayName.equals("[" + previousPageItemNameString + "]")) {
            Inventory previousPageBattlepassInventory = new BattlepassInventory(this.rewardRepository, playerRepository).createInventory(currentPage.get()-1, event.getWhoClicked().getUniqueId());
            event.getWhoClicked().openInventory(previousPageBattlepassInventory);
        }

        event.setCancelled(true);
    }

    private Optional<Integer> getCurrentPage(String inventoryTitle) {
        Pattern pattern = Pattern.compile("to §l(\\d+)");
        Matcher matcher = pattern.matcher(inventoryTitle);

        if (matcher.find()) {
            return Optional.of((int) Math.ceil(Integer.parseInt(matcher.group(1)) / 9.0));
        }

        return Optional.empty();
    }
}
