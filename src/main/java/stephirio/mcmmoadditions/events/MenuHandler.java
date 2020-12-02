package stephirio.mcmmoadditions.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import stephirio.mcmmoadditions.Main;
import java.util.*;

/** Handler of the main menus of the plugin.
 * @see stephirio.mcmmoadditions.commands.Stats */
public class MenuHandler implements Listener {

    private final Main plugin;

    public MenuHandler(Main plugin) { this.plugin = plugin; } // Constructor


    /** When the player clicks on a menu. */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(plugin.placeholderColors(player, plugin.stats.getConfig()
                .getString("gui-title")))) {
            player.closeInventory();
            plugin.stats.openSkillGui(player, event.getCurrentItem());
        } else {
            String[] names = {plugin.stats.getConfig().getString("mining-item-name"),
                    plugin.stats.getConfig().getString("woodcutting-item-name"),
                    plugin.stats.getConfig().getString("excavation-item-name"),
                    plugin.stats.getConfig().getString("fishing-item-name"),
                    plugin.stats.getConfig().getString("swords-item-name"),
                    plugin.stats.getConfig().getString("repair-item-name"),
                    plugin.stats.getConfig().getString("salvage-item-name"),
                    plugin.stats.getConfig().getString("archery-item-name"),
                    plugin.stats.getConfig().getString("acrobatics-item-name"),
                    plugin.stats.getConfig().getString("unarmed-item-name"),
                    plugin.stats.getConfig().getString("smelting-item-name"),
                    plugin.stats.getConfig().getString("axes-item-name"),
                    plugin.stats.getConfig().getString("taming-item-name"),
                    plugin.stats.getConfig().getString("alchemy-item-name"),
                    plugin.stats.getConfig().getString("herbalism-item-name"),
            };
            for (String name : names) {
                if (event.getView().getTitle().contains(plugin.placeholderColors(player, name)))
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                            if (event.getCurrentItem().getItemMeta().getLore().get(0)
                                    .replace(String.valueOf(ChatColor.COLOR_CHAR), "").equals("GoTroSTaTSPG1")) {
                                player.closeInventory();
                                plugin.stats.openMainGui(player);
                            } else if (event.getCurrentItem().getItemMeta().getLore().get(0)
                                    .replace(String.valueOf(ChatColor.COLOR_CHAR), "").equals("cloSe")) {
                                player.closeInventory();
                            }
                        } else {
                            player.closeInventory();
                        }
                    }
                }
            }
    }


    /** This event is executed when a player right clicks with an item in the main hand. */
    @EventHandler
    public void rightClickCrouchedItemMenu(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                player.isSneaking()) {
            if (plugin.stats.getConfig().getBoolean("crouch-right-click")) {
                ConfigurationSection section = plugin.stats.getConfig()
                        .getConfigurationSection("allowed-right-click-items");
                for (Object skilllistitem : section.getKeys(false)) {
                    if (section.getList((String) skilllistitem).contains(player.getInventory().getItemInMainHand().getType()
                            .toString())) {
                        ArrayList<String> skillitems = new ArrayList<>();
                        for (Object skillitem : section.getList((String) skilllistitem)) {
                            skillitems.add(plugin.materialParser((String) skillitem).toString());
                        }
                        if (skillitems.contains(player.getInventory().getItemInMainHand().getType().toString())) {
                            plugin.stats.openSkillGuiByName(player, (String) skilllistitem);
                        }
                    }
                }
            }
        }
    }

}

