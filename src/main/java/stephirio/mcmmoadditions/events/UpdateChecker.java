package stephirio.mcmmoadditions.events;

import org.bukkit.Bukkit;
import org.bukkit.util.Consumer;
import stephirio.mcmmoadditions.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


/** Checks if there are new updates of the plugin.
 * @version 1.0
 * @author Stephirio <stephirioyt@gmail.com>
 */
public class UpdateChecker {

    private final Main plugin;
    private final int resourceId;

    public UpdateChecker(Main plugin, int resourceId) {
        this.plugin  = plugin;
        this.resourceId = resourceId;
    }  // Constructor


    /** Gets the latest version of the plugin */
    public void getLatestVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" +
                    this.resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Update checker is broken, can't find an update!" + exception.getMessage());
            }
        });
    }
}
