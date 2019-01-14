package sameplayer.zweikampf.plugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sameplayer.zweikampf.plugin.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class CommandQuit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            try {
                dataOutputStream.writeUTF("Connect");
                dataOutputStream.writeUTF("eingangshalle");
            }catch (Exception e) {

            }
            player.sendPluginMessage(Main.getInstance(), "BungeeCord", outputStream.toByteArray());

            return true;

        }

        return false;

    }

}
