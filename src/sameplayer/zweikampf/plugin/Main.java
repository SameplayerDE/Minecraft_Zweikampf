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
import sameplayer.zweikampf.plugin.Enums.GameStates;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Listeners.ListenerGameSetup;
import sameplayer.zweikampf.plugin.Listeners.ListenerInFight;
import sameplayer.zweikampf.plugin.Listeners.ListenerPlayerJoin;
import sameplayer.zweikampf.plugin.Listeners.ListenerPlayerWaiting;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

public class Main extends JavaPlugin {

    private static Main m;
    private static ZweikampfManager zweikampf;

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
        zweikampf = new ZweikampfManager();

        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage());

        if (!this.getConfig().getBoolean("Abgeschlossen")) {
            zweikampf.setGameState(GameStates.SERVER_SETUP);
        }else{
            zweikampf.setGameState(GameStates.WAIT_QUEUE);
        }

        registerListeners();
        registerCommands();

    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new ListenerGameSetup(), this);
        pluginManager.registerEvents(new ListenerInFight(), this);
        pluginManager.registerEvents(new ListenerPlayerWaiting(), this);
        new ListenerPlayerJoin(this);
    }

    private void registerCommands() {
        this.getCommand("konfiguration").setExecutor(new CommandSetup());
        this.getCommand("verlassen").setExecutor(new CommandQuit());
    }

    public static ZweikampfManager getZweikampf() {
        return zweikampf;
    }



    public static Main getInstance() {
        return m;
    }

}
