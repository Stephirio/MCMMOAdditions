package stephirio.mcmmoadditions;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import stephirio.mcmmoadditions.commands.Reload;
import stephirio.mcmmoadditions.commands.Stats;
import stephirio.mcmmoadditions.events.LevelUp;
import stephirio.mcmmoadditions.events.MenuHandler;
import stephirio.mcmmoadditions.events.UpdateChecker;

import java.util.Objects;
import java.util.logging.Logger;


/** Main class of the entire plugin. */
public final class Main extends JavaPlugin {


    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    private Integer pluginID = 472449;
    public Stats stats;



    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.stats = new Stats(this);
        stats.saveDefaultConfig();
        stats.reloadConfig();
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Not able to detect Vault.", getDescription().getName()));
        }
        getCommand("stats").setExecutor(new Stats(this));
        getCommand("mcmmoareload").setExecutor(new Reload(this));
        getServer().getPluginManager().registerEvents(new MenuHandler(this), this);
        getServer().getPluginManager().registerEvents(new LevelUp(this), this);
        new UpdateChecker(this, pluginID).getLatestVersion(version -> {
            if(this.getDescription().getVersion().equalsIgnoreCase(version)) {
                System.out.println("[MCMMO-A] Plugin is up to date.");
            } else {
                log.warning("A new version of MCMMOAdditions is available. Download it now at " +
                        "https://www.spigotmc.org/threads/mcmmoadditions.472449/");
            }
        });
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }



    /** Makes the vault economy API available for other classes */
    public Economy vaultEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        assert rsp != null;
        return rsp.getProvider();
    }



    /** A custom method to easily replace placeholders and color codes.
     * @return String */
    public String placeholderColors(Player player, String string) {
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, string));
    }



    /** Returns a Material object from a string containing the item name. (mossy stone, mossy-stone, mossy_stone, MOSSY STONE, MOSSY-STONE and MOSSY_STONE are examples of how the material string can be passed)
     * @return Material */
    public Material materialParser(String material) {
        return Material.valueOf(material.toUpperCase().replace(' ', '_').replace('-', '_'));
    }
}
