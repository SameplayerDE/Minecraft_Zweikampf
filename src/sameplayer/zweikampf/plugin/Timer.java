package sameplayer.zweikampf.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Timer extends BukkitRunnable {

    private final JavaPlugin plugin;
    private int minutes = 5;

    public Timer(JavaPlugin plugin, int minutes) {
        if (minutes < 1) {
            throw  new IllegalArgumentException("minutes must be greater than 1");
        }else{
            this.minutes = minutes;
        }
        this.plugin = plugin;
    }

    @Override
    public void run() {

    }
}
