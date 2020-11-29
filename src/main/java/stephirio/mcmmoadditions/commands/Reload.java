package stephirio.mcmmoadditions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import stephirio.mcmmoadditions.Main;

import java.util.ArrayList;


/** Reloads the configuration files of the plugin.*/
public class Reload implements CommandExecutor {

    public final Main plugin;
    public Reload(Main plugin) { this.plugin = plugin; }  // Constructor

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            (new Stats(plugin)).reloadConfig();  // stats related configuration
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin
                    .getConfig().getString("messages-prefix") + ChatColor.GREEN + " The configuration has been reloaded."));
            System.out.println(ChatColor.GREEN + "[MCMMO-A] The configuration has been reloaded");
        }
        return false;
    }
}
