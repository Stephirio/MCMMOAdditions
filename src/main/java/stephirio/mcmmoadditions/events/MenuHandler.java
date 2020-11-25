package stephirio.mcmmoadditions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import stephirio.mcmmoadditions.Main;

/** Handler of the main menus of the plugin.
 * @see stephirio.mcmmoadditions.commands.Stats */
public class MenuHandler implements Listener {

    private final Main plugin;

    public MenuHandler(Main plugin) { this.plugin = plugin; } // Constructor

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equalsIgnoreCase(plugin.placeholderColors(player, plugin.stats.getConfig()
                .getString("gui-title")))) player.closeInventory();
    }

}

