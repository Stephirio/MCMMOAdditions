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


/** Main class of the entire plugin. */
public final class Main extends JavaPlugin {


    public static Economy econ = null;
    private Integer pluginID = 85779;
    public Stats stats;
    String plugin_prefix = ChatColor.GREEN + "[" + ChatColor.GOLD + "MCMMO-A" + ChatColor.GREEN + "] " + ChatColor.RESET;



    public void warningLog(String string) {
        System.out.println(plugin_prefix + ChatColor.YELLOW + string);
    }
    public void successLog(String string) {
        System.out.println(plugin_prefix + ChatColor.GREEN + string);
    }
    public void severeLog(String string) {
        System.out.println(plugin_prefix + ChatColor.RED + string);
    }

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
        this.stats = new Stats(this);
        stats.getConfig().options().copyDefaults(true);
        stats.saveDefaultConfig();
        stats.reloadConfig();

        if (!stats.getConfig().getString("version").equals("Beta 1.4"))
            warningLog("Your stats.yml configuration file is outdated. Please use the new one: " +
                    "https://github.com/Stephirio/MCMMOAdditions/blob/master/src/main/resources/stats.yml");
        if (!setupEconomy()) severeLog("[%s] - Not able to detect Vault.");

        new UpdateChecker(this, pluginID).getLatestVersion(version -> {
            if(this.getDescription().getVersion().equalsIgnoreCase(version))
                System.out.println("[MCMMO-A] Plugin is up to date.");
            else
                warningLog("A new version of MCMMOAdditions is available. Download it now at " +
                        "https://www.spigotmc.org/threads/mcmmoadditions.85779/");
        });

        getCommand("stats").setExecutor(new Stats(this));
        getCommand("mcmmoareload").setExecutor(new Reload(this));
        getServer().getPluginManager().registerEvents(new MenuHandler(this), this);
        getServer().getPluginManager().registerEvents(new LevelUp(this), this);

    }


    /** From the Vault API "tutorial" made by MilkBowl. It setups the economy of the server using Vault.
     * @return boolean */
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



    /** Makes the vault economy API available for other classes
     * @return Economy */
    public Economy vaultEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        assert rsp != null;
        return rsp.getProvider();
    }


    /** A custom method to easily replace color codes.
     * @return String */
    public String colors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }


    /** A custom method to easily replace placeholders and color codes.
     * @return String */
    public String placeholderColors(Player player, String string) {
        return PlaceholderAPI.setPlaceholders(player, colors(string));
    }



    /** Returns a Material object from a string containing the item name. (mossy stone, mossy-stone, mossy_stone, MOSSY STONE, MOSSY-STONE and MOSSY_STONE are examples of how the material string can be passed)
     * @return Material */
    public Material materialParser(String material) {
        return Material.valueOf(material.toUpperCase().replace(' ', '_').replace('-', '_'));
    }


    /** This method converts a string to an invisible one and codifies it in a very basic way. This allows to hide strings in lores, items names and inventories.
     * @return String */
    public String convertToInvisibleString(String s) {
        StringBuilder hidden = new StringBuilder();
        for (char c : s.toCharArray()) hidden.append(ChatColor.COLOR_CHAR + "").append(c);
        return hidden.toString();
    }
}
