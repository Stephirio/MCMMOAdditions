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
import org.bukkit.event.inventory.InventoryClickEvent;
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
        if (event.getView().getTitle().equals(plugin.placeholderColors(player, (new Stats(plugin)).getConfig()
                .getString("gui-title")))) {
            if (event.getView().getTitle().equalsIgnoreCase(plugin.placeholderColors(player, plugin.stats.getConfig()
                    .getString("gui-title")))) {
                player.closeInventory();
                if (event.getCurrentItem() != null) {
                    Map<String, String> keys = new HashMap<String, String>();
                    keys.put("mIrnmIrnmG", "mining");
                    keys.put("aTIcS", "acrobatics");
                    keys.put("SelTIelnG", "smeling");
                    keys.put("HalISalm", "herbalism");
                    keys.put("cHemY", "alchemy");
                    keys.put("cHrY", "archery");
                    keys.put("aS", "axes");
                    keys.put("eVeTIe", "excavation");
                    keys.put("fISHIfnG", "fishing");
                    keys.put("ePaI", "repair");
                    keys.put("SalVaGe", "salvage");
                    keys.put("SWdS", "swords");
                    keys.put("Ud", "unarmed");
                    keys.put("TamIanmG", "taming");
                    keys.put("WcUTTIcnG", "woodcutting");
                    String codified = event.getCurrentItem().getItemMeta().getLore().get(0).replace(
                            String.valueOf(ChatColor.COLOR_CHAR), "");

                    ConfigurationSection section = plugin.stats.getConfig().getConfigurationSection(
                            keys.get(codified) + "-gui");

                    Inventory gui = Bukkit.createInventory(player, section.getInt("size"), event.getCurrentItem().getItemMeta()
                            .getDisplayName() + plugin.placeholderColors(player, section.getString("title")));

                    for (String key : section.getKeys(false)) {
                        if (!key.equals("title") && !key.equals("size")) {
                            ConfigurationSection iteminfo = section.getConfigurationSection(key);
                            ItemStack item = new ItemStack(plugin.materialParser(iteminfo.getString("item")),
                                    1);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(plugin.placeholderColors(player, iteminfo.getString("name")));
                            ArrayList<String> lore = new ArrayList<>();
                            for (Object loreLine : Objects.requireNonNull(iteminfo.getList("lore")))
                                lore.add(plugin.placeholderColors(player, (String) loreLine));
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            gui.setItem(iteminfo.getInt("position"), item);
                        }
                    }
                    
                    ConfigurationSection back_button = plugin.stats.getConfig().getConfigurationSection("go-back-button");
                    ItemStack item = new ItemStack(plugin.materialParser(back_button.getString("item")));
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(plugin.convertToInvisibleString("GO TO STATS PG 1"));
                    for (Object loreList : back_button.getList("lore"))
                        lore.add(plugin.placeholderColors(player, (String) loreList));
                    meta.setDisplayName(plugin.placeholderColors(player, back_button.getString("name")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    gui.setItem(section.getInt("size") - 1, item);

                    player.openInventory(gui);

                }
            }
        }


        if (event.getCurrentItem().getItemMeta().getLore().get(0).equals("GO TO STATS PG 1")) {
            // do somethin'
        }
    }

}

