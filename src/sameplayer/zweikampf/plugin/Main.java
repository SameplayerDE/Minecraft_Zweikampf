package sameplayer.zweikampf.plugin;

import io.netty.channel.local.LocalAddress;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import sameplayer.zweikampf.plugin.Commands.CommandQuit;
import sameplayer.zweikampf.plugin.Commands.CommandSetup;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Listeners.ListenerGameSetup;
import sameplayer.zweikampf.plugin.Listeners.ListenerInFight;
import sameplayer.zweikampf.plugin.Listeners.ListenerPlayerWaiting;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

public class Main extends JavaPlugin implements PluginMessageListener {

    private static Main m;
    private static Zweikampf zweikampf;
    private static ServerState state;

    @Override
    public void onEnable() {

        for (Entity e : Bukkit.getWorld("world").getEntities()) {
            if (e.getType().equals(EntityType.DROPPED_ITEM)) {
                e.remove();
            }else if (e.getType().equals(EntityType.AREA_EFFECT_CLOUD)) {
                e.remove();
            }else if (e.getType().equals(EntityType.ARROW)) {
                e.remove();
            }
        }

        m = this;
        zweikampf = new Zweikampf();

        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        if (!this.getConfig().getBoolean("Abgeschlossen")) {
            state = ServerState.SETUP;
        }else{
            state = ServerState.WAITING_QUEUE;
        }

        registerListeners();
        registerCommands();

        /**new BukkitRunnable() {

            @Override
            public void run() {
                System.out.println(getState());
            }
        }.runTaskTimer(this, 20, 20);**/
    }

    @Override
    public void onDisable() {

        for (Block block : ListenerInFight.blockList) {

            block.setType(Material.AIR);

        }


        if (getState().getValue() != 4) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                online.kickPlayer("§aDienst führt einen Neustart durch");

            }

        }

    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new ListenerGameSetup(), this);
        pluginManager.registerEvents(new ListenerInFight(), this);
        pluginManager.registerEvents(new ListenerPlayerWaiting(), this);
    }

    private void registerCommands() {
        this.getCommand("konfiguration").setExecutor(new CommandSetup());
        this.getCommand("verlassen").setExecutor(new CommandQuit());
    }

    public static ServerState getState() {
        return state;
    }

    public static void setState(ServerState state) {
        Main.state = state;
    }

    public static Zweikampf getZweikampf() {
        return zweikampf;
    }

    public static Location configToLocation(String path) {

        double x, y, z;
        float yaw, pitch;
        String world;

        x = Main.getInstance().getConfig().getDouble(path + ".X");
        y = Main.getInstance().getConfig().getDouble(path + ".Y");
        z = Main.getInstance().getConfig().getDouble(path + ".Z");

        yaw = (float) Main.getInstance().getConfig().getDouble(path + ".Gierung");
        pitch = (float) Main.getInstance().getConfig().getDouble(path + ".Neigung");

        world = Main.getInstance().getConfig().getString(path + ".Welt");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);


    }

    public static Main getInstance() {
        return m;
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }
}
