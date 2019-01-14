package sameplayer.zweikampf.plugin;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PluginMessage implements PluginMessageListener {

    private boolean serverOnline = false;

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

        if (!s.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);

        String channel = input.readUTF();

        if (channel.equals("ServerIP")) {
            String servername = input.readUTF();
            String ip = input.readUTF();
            int port = input.readUnsignedShort();
            serverOnline = checkIP(ip, port);
        }

    }

    public void connect(Player player, String server) {

        ByteArrayDataOutput serverIP = ByteStreams.newDataOutput();
        ByteArrayDataOutput serverconnect = ByteStreams.newDataOutput();

        serverIP.writeUTF("ServerIP");
        serverIP.writeUTF(server);
        player.sendPluginMessage(Main.getInstance(), "BungeeCord", serverIP.toByteArray());

        new BukkitRunnable() {

            @Override
            public void run() {
                if (serverOnline) {
                    serverconnect.writeUTF("Connect");
                    serverconnect.writeUTF(server);
                    player.sendPluginMessage(Main.getInstance(), "BungeeCord", serverconnect.toByteArray());
                }
            }

        }.runTaskLater(Main.getInstance(), 20l);


    }

    public static boolean checkIP(String ip, int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 20);
            socket.close();
            return true;
        }catch (IOException e) {
            Main.getInstance().getServer().getConsoleSender().sendMessage("Could not connect to " + ip + ":" + port);
            return false;
        }
    }
}
