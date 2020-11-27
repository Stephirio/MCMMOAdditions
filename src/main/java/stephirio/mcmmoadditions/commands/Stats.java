package stephirio.mcmmoadditions.commands;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.SkillAPI;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import stephirio.mcmmoadditions.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class Stats implements CommandExecutor {



    private final Main plugin;
    private File configFile = null;
    private FileConfiguration dataConfig = null;



    public Stats(Main plugin) { this.plugin = plugin; }


    /** This method is pretty self-explanative. It reloads the configuration file.
     * @see Reload */
    public void reloadConfig() {
        if (this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), "stats.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("stats.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }


    /** This method is used to get config information from the stats.yml config file.
     * @return FileConfiguration */
    public FileConfiguration getConfig() {
        if (this.dataConfig == null) reloadConfig();
        return this.dataConfig;
    }


    /** Saves the configuration file after any hardcoded changes.
     *
     * @throws IOException Could not save the config
     * @see Stats#saveDefaultConfig()
     * @see Stats#reloadConfig()
     */
    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null) return;
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            Main.log.log(Level.SEVERE, "Could not save the config to " + this.configFile, e);
        }
    }


    /** Just like {@link Stats#saveConfig()} but loading from file not from hardcoded information.
     * @see Stats#saveConfig()
     * @see Stats#reloadConfig()
     */
    public void saveDefaultConfig() {
        if (this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), "stats.yml");
        if (!this.configFile.exists()) this.plugin.saveResource("stats.yml", false);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("lol");
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // GUI
            if (Objects.requireNonNull(getConfig().getString("mode")).equalsIgnoreCase("default") ||
                    Objects.requireNonNull(getConfig()
                    .getString("mode")).equalsIgnoreCase("gui")) {
                Inventory gui = Bukkit.createInventory(player, getConfig().getInt("gui-size"),
                        plugin.placeholderColors(player, getConfig().getString("gui-title")));
                for (String skill : SkillAPI.getSkills()) {
                    if (ExperienceAPI.getLevel(player, PrimarySkillType.valueOf(skill)) == 0) {
                        ItemStack item = new ItemStack(plugin.materialParser(Objects.requireNonNull(getConfig()
                                .getString("locked-skill-item"))), 1);
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        meta.setDisplayName(plugin.placeholderColors(player, getConfig()
                                .getString("locked-skill-item-name")));
                        ArrayList<String> lore = new ArrayList<>();
                        for (Object loreLine : Objects.requireNonNull(getConfig()
                                .getList("locked-skill-item-lore")))
                            lore.add(plugin.placeholderColors(player, (String) loreLine));
                        meta.setLore(lore);
                        meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
                        item.setItemMeta(meta);
                        gui.setItem(getConfig().getInt(skill.toLowerCase() + "-item-position"), item);
                    } else {
                        ItemStack item = new ItemStack(plugin.materialParser(Objects.requireNonNull(getConfig()
                                .getString(skill.toLowerCase() + "-item"))), 1);
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        meta.setDisplayName(plugin.placeholderColors(player, getConfig().getString(
                                skill.toLowerCase() + "-item-name")));
                        ArrayList<String> lore = new ArrayList<>();
                        for (Object loreLine : Objects.requireNonNull(getConfig().getList(skill.toLowerCase() +
                                "-item-lore")))
                            lore.add(plugin.placeholderColors(player, (String) loreLine));
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        gui.setItem(getConfig().getInt(skill.toLowerCase() + "-item-position"), item);
                    }
                }
                player.openInventory(gui);
            // TEXT
            } else if (Objects.requireNonNull(getConfig().getString("mode"))
                    .equalsIgnoreCase("text")) {
                for (String skill : SkillAPI.getSkills()) {
                    if (ExperienceAPI.getLevel(player, PrimarySkillType.valueOf(skill)) > 0 &&
                            !SkillAPI.getChildSkills().contains(skill)) {
                        Integer playerSLevel = ExperienceAPI.getLevel(player, PrimarySkillType.valueOf(skill));
                        Integer playerSXp = ExperienceAPI.getXP(player, skill);
                        Integer playerSNXp = ExperienceAPI.getXPToNextLevel(player, skill);
                        Integer playerSRXp = ExperienceAPI.getXPRemaining(player, skill);
                        Integer playerNextLevel = playerSLevel + 1;
                        for (Object messageLine : Objects.requireNonNull(getConfig()
                                .getList("message-template"))) {
                            String msgLine = messageLine.toString();
                            player.sendMessage(plugin.placeholderColors(player, msgLine
                                    .replace("%mcmmoa_skill_level%", playerSLevel.toString())
                                    .replace("%mcmmoa_skill_xp", playerSXp.toString())
                                    .replace("%mcmmoa_needed_skill_xp%", playerSNXp.toString())
                                    .replace("%mcmmoa_remaining_skill_xp%", playerSRXp.toString())
                                    .replace("%mcmmoa_next_level%", playerNextLevel.toString())
                                    .replace("%new_line%", "\n")
                                    .replace("%mcmmoa_skill_name%", skill.toLowerCase()
                                            .substring(0, 1).toUpperCase() + skill.toLowerCase().substring(1)
                                    )));
                        }
                    }
                }
            }
        }
        return false;
    }
}
