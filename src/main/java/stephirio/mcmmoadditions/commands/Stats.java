package stephirio.mcmmoadditions.commands;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.SkillAPI;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import stephirio.mcmmoadditions.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class Stats implements CommandExecutor {



    private final Main plugin;
    private File configFile = null;
    private FileConfiguration dataConfig = null;
    private String configver = "Beta 1.3.1";



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


    /** A method that opens the main GUI of the Stats.
     * @param player The player of which you want to get information from.
     * @see Stats#openSkillGui(Player, ItemStack)
     * @see Stats#openSkillGuiByName(Player, String) */
    public void openMainGui(Player player) {
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
                if (getConfig().getList("locked-skill-item-lore") != null) {
                    for (Object loreLine : Objects.requireNonNull(getConfig()
                            .getList("locked-skill-item-lore")))
                        lore.add(plugin.placeholderColors(player, (String) loreLine));
                }
                meta.setLore(lore);
                if (getConfig().getBoolean("locked-item-enchanted")) {
                    meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
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
                lore.add(plugin.convertToInvisibleString(skill));
                if (getConfig().getList(skill.toLowerCase() + "-item-lore") != null) {
                    for (Object loreLine : Objects.requireNonNull(getConfig().getList(skill.toLowerCase() +
                            "-item-lore")))
                        lore.add(plugin.placeholderColors(player, (String) loreLine));
                }
                meta.setLore(lore);
                if (getConfig().getBoolean("unlocked-skills-enchanted") ||
                        getConfig().getBoolean(skill.toLowerCase() + "-item-enchanted")) {
                    meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                item.setItemMeta(meta);
                gui.setItem(getConfig().getInt(skill.toLowerCase() + "-item-position"), item);
            }
        }
        player.openInventory(gui);
    }


    /** Opens the GUI showing player's information related to a certain specified skill. The skill's name is extracted
     * from a codified string in the item lore.
     * @param player The player to get the information of.
     * @param clickeditem Gets the item the player clicked.
     * @see stephirio.mcmmoadditions.events.MenuHandler#onMenuClick(InventoryClickEvent)
     * @see Stats#openSkillGuiByName(Player, String)
     * @see Stats#openMainGui(Player)  */
    public void openSkillGui(Player player, ItemStack clickeditem) {
        if (clickeditem != null) {
            Map<String, String> keys = new HashMap<String, String>();
            keys.put("mIrnmIrnmG", "mining");
            keys.put("aTIcS", "acrobatics");
            keys.put("SelTIelnG", "smelting");
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

            String codified = clickeditem.getItemMeta().getLore().get(0).replace(
                    String.valueOf(ChatColor.COLOR_CHAR), "");

            ConfigurationSection section = plugin.stats.getConfig().getConfigurationSection(
                    keys.get(codified) + "-gui");

            Inventory gui = Bukkit.createInventory(
                    player,
                    section.getInt("size"),
                    clickeditem.getItemMeta().getDisplayName() + plugin.placeholderColors(player, section.getString("title")));

            for (String key : section.getKeys(false)) {
                if (!key.equals("title") && !key.equals("size")) {
                    ConfigurationSection iteminfo = section.getConfigurationSection(key);
                    ItemStack item = new ItemStack(plugin.materialParser(iteminfo.getString("item")),
                            1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(plugin.placeholderColors(player, iteminfo.getString("name")));
                    ArrayList<String> lore = new ArrayList<>();
                    if (iteminfo.getList("lore") != null) {
                        for (Object loreLine : Objects.requireNonNull(iteminfo.getList("lore")))
                            lore.add(plugin.placeholderColors(player, (String) loreLine));
                        meta.setLore(lore);
                    }
                    item.setItemMeta(meta);
                    gui.setItem(iteminfo.getInt("position"), item);
                }
            }

            ConfigurationSection back_button = plugin.stats.getConfig()
                    .getConfigurationSection("go-back-button");
            ItemStack item = new ItemStack(plugin.materialParser(back_button.getString("item")));
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(plugin.convertToInvisibleString("GOTOSTATSPG1"));
            if (back_button.getList("lore") != null) {
                for (Object loreList : back_button.getList("lore"))
                    lore.add(plugin.placeholderColors(player, (String) loreList));
            }
            meta.setDisplayName(plugin.placeholderColors(player, back_button.getString("name")));
            meta.setLore(lore);
            item.setItemMeta(meta);
            gui.setItem(section.getInt("size") - 1, item);

            player.openInventory(gui);
        }
    }


    /** This opens the skill GUI using the name of the skill. Useful when the item has no cryptographed lore string
     * @param player The player to get information from.
     * @param skillname The name of the skill to get more precise information.
     * @see Stats#openMainGui(Player)
     * @see Stats#openSkillGui(Player, ItemStack)
     * @see stephirio.mcmmoadditions.events.MenuHandler#rightClickCrouchedItemMenu(PlayerInteractEvent)   */
    public void openSkillGuiByName(Player player, String skillname) {
        skillname = skillname.toLowerCase();
        ConfigurationSection section = plugin.stats.getConfig().getConfigurationSection(skillname + "-gui");
        Inventory gui = Bukkit.createInventory(player, section.getInt("size"), plugin.placeholderColors(player,
                plugin.stats.getConfig().getString(skillname + "-item-name")) +
                plugin.placeholderColors(player, section.getString("title")));
        for (String key : section.getKeys(false)) {
            if (!key.equals("title") && !key.equals("size")) {
                ConfigurationSection iteminfo = section.getConfigurationSection(key);
                ItemStack item = new ItemStack(plugin.materialParser(iteminfo.getString("item")),
                        1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(plugin.placeholderColors(player, iteminfo.getString("name")));
                ArrayList<String> lore = new ArrayList<>();
                if (iteminfo.getList("lore") != null) {
                    for (Object loreLine : iteminfo.getList("lore"))
                        lore.add(plugin.placeholderColors(player, (String) loreLine));
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);
                gui.setItem(iteminfo.getInt("position"), item);
            }
        }
        ConfigurationSection back_button = plugin.stats.getConfig()
                .getConfigurationSection("close-button");
        ItemStack backitem = new ItemStack(plugin.materialParser(back_button.getString("item")));
        ItemMeta backmeta = backitem.getItemMeta();
        ArrayList<String> backlore = new ArrayList<>();
        backlore.add(plugin.convertToInvisibleString("CLOSE"));
        if (back_button.getList("lore") != null) {
            for (Object backloreList : back_button.getList("lore"))
                backlore.add(plugin.placeholderColors(player, (String) backloreList));
            backmeta.setLore(backlore);
        }
        backmeta.setDisplayName(plugin.placeholderColors(player, back_button.getString("name")));
        backitem.setItemMeta(backmeta);
        gui.setItem(section.getInt("size") - 1, backitem);
        player.openInventory(gui);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // GUI
            if (Objects.requireNonNull(getConfig().getString("mode")).equalsIgnoreCase("default") ||
                    Objects.requireNonNull(getConfig()
                    .getString("mode")).equalsIgnoreCase("gui")) {
                openMainGui(player);
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
