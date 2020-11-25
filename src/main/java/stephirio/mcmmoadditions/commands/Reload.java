package stephirio.mcmmoadditions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import stephirio.mcmmoadditions.Main;



/** Reloads the configuration files of the plugin.*/
public class Reload implements CommandExecutor {



    public final Main plugin;



    public Reload(Main plugin) { this.plugin = plugin; }  // Constructor



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.stats.reloadConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin
                            .getConfig().getString("messages-prefix") + " The plugin has been reloaded."));
                    System.out.println("[MCMMO-A] The plugin has been reloaded.");
                }
            }
        }
        return false;
    }
}
