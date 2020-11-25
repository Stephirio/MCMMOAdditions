package stephirio.mcmmoadditions.events;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import stephirio.mcmmoadditions.Main;
import java.util.Objects;


/** This class manages the mcMMO skills level ups.
 *
 * @author Stephirio <stephirioyt@gmail.com>
 * @version 1.0
 */
public class LevelUp implements @NotNull Listener {



    private final Main plugin;



    public LevelUp(Main plugin) { this.plugin = plugin; }  // Constructor


    /** Method that detects when the player levels up then gives him a certain amount of player based on his level and a
     * configurable percentage (by default 30%) of a third of his level. */ @EventHandler
    public boolean onPlayerLevelUp(McMMOPlayerLevelUpEvent event) {
        if (event.getPlayer().hasPermission("mcmmoa.levelup")) {
            if (plugin.getConfig().getBoolean("levelup-money-pex-" + event.getSkill().getName().toLowerCase())) {
                if (event.getPlayer().hasPermission("mcmmoa.levelup.money." + event.getSkill().getName()
                        .toLowerCase())) {
                    String percentage = plugin.getConfig().getString("levelup-money-" + event.getSkill().getName()
                            .toLowerCase());
                    assert percentage != null;
                    if (percentage.equalsIgnoreCase("default")) {  // The default value is 30.0
                        double moneyToGive =
                                1.0 * (((event.getSkillLevel() - event.getSkillLevel() / 3.0) * 30.0) / 100.0) *
                                        event.getSkillLevel();
                        plugin.vaultEconomy().depositPlayer(event.getPlayer(), moneyToGive);
                        String pApimessage = PlaceholderAPI.setPlaceholders(event.getPlayer(),
                                Objects.requireNonNull(plugin.getConfig().getString("levelup-message-" +
                                        event.getSkill().getName().toLowerCase())));
                        if (event.getPlayer().hasPermission("mcmmoa.levelup.message." + event.getSkill().getName()
                                .toLowerCase())) {
                            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfig()
                                    .getString("messages-prefix") + " " + pApimessage.replace("%gained_ma_money%"
                                    , String.valueOf(plugin.vaultEconomy().format(moneyToGive)))));
                        }
                    } else {
                        double doublePercentage = 1.0 * Integer.parseInt(Objects.requireNonNull(plugin.getConfig()
                                .getString("levelup-money-" + event.getSkill().getName().toLowerCase()))
                                .replaceAll("[^0-9]", ""));
                        double moneyToGive = 1.0 * (((event.getSkillLevel() - event.getSkillLevel() / 3.0) *
                                doublePercentage) / 100.0) * event.getSkillLevel();
                        String pApiMessage = PlaceholderAPI.setPlaceholders(event.getPlayer(),
                                Objects.requireNonNull(plugin.getConfig().getString("levelup-message-" +
                                        event.getSkill().getName().toLowerCase())));
                        if (event.getPlayer().hasPermission("mcmmoa.levelup.money.message." +
                                event.getSkill().getName().toLowerCase())) {
                            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfig().getString("messages-prefix") + " " +
                                            pApiMessage.replace("%gained_ma_money%"
                                    , String.valueOf(plugin.vaultEconomy().format(moneyToGive)))));
                        }
                    }
                }
            }
        }
        return true;
    }

}

