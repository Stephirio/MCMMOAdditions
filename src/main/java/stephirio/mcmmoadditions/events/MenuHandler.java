package stephirio.mcmmoadditions.events;

import com.gmail.nossr50.api.SkillAPI;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import stephirio.mcmmoadditions.Main;
import stephirio.mcmmoadditions.commands.Stats;

import java.lang.reflect.Array;
import java.util.*;

/** Handler of the main menus of the plugin.
 * @see stephirio.mcmmoadditions.commands.Stats */
public class MenuHandler implements Listener {

    private final Main plugin;

    public MenuHandler(Main plugin) { this.plugin = plugin; } // Constructor


    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        /* Stats GUI
          When clicking on an item on the Stats GUI this will happen. */
        if (event.getView().getTitle().equals(plugin.placeholderColors(player, plugin.stats.getConfig()
                .getString("gui-title")))) {
            player.closeInventory();
            plugin.stats.openSkillGui(player, event.getCurrentItem());
        } else {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    if (event.getCurrentItem().getItemMeta().getLore().get(0)
                            .replace(String.valueOf(ChatColor.COLOR_CHAR), "").equals("GoTroSTaTSPG1")) {
                        player.closeInventory();
                        plugin.stats.openMainGui(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void rightClickCrouchedItemMenu(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) && player.isSneaking()) {
            ArrayList<ConfigurationSection> items = new ArrayList<>();
            for (Object iteminconfig : plugin.stats.getConfig().getList("allowed-right-click-items"))
                items.add((ConfigurationSection) iteminconfig);
            if (items.contains(player.getInventory().getItemInMainHand())) {
                System.out.println("ok");
            }
        }
    }

}

