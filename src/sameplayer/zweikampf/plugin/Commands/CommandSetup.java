package sameplayer.zweikampf.plugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import sameplayer.zweikampf.plugin.Enums.ServerState;
import sameplayer.zweikampf.plugin.Main;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class CommandSetup implements TabExecutor {

    private LinkedHashMap<UUID, Integer> setup = new LinkedHashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;

            if (player.hasPermission("zweikampf.konfiguration")) {

                if (strings.length == 0) {

                    if (!setup.containsKey(player.getUniqueId())) {

                        setup.put(player.getUniqueId(), 0);

                        if (!Main.getState().equals(ServerState.SETUP)) {
                            Main.setState(ServerState.SETUP);
                            player.setGameMode(GameMode.CREATIVE);
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                if (!online.hasPermission("zweikampf.konfiguration")) {
                                    online.kickPlayer("Der Dienst ist nicht Spielbereit!");
                                }
                            }
                        }

                        player.sendMessage("§6Konfiguration beginnt nun!");
                        player.sendMessage("1. Schritt: Bestimme den Einstiegspunkt des Wartebereichs");
                        return true;

                    } else {

                        int step = setup.get(player.getUniqueId());

                        if (step == 0) {

                            Main.getInstance().getConfig().set("Wartebereich.X", player.getLocation().getX());
                            Main.getInstance().getConfig().set("Wartebereich.Y", player.getLocation().getY());
                            Main.getInstance().getConfig().set("Wartebereich.Z", player.getLocation().getZ());
                            Main.getInstance().getConfig().set("Wartebereich.Welt", player.getLocation().getWorld().getName());
                            Main.getInstance().getConfig().set("Wartebereich.Gierung", player.getLocation().getYaw());
                            Main.getInstance().getConfig().set("Wartebereich.Neigung", player.getLocation().getPitch());

                            player.sendMessage("§61. Schritt wurde abgeschlossen!");
                            player.sendMessage("2. Schritt: Bestimme den Einstiegspunkt des ersten Teilnehmers!");
                            setup.replace(player.getUniqueId(), 1);
                            return true;

                        }

                        if (step == 1) {

                            Main.getInstance().getConfig().set("Teilnehmer.eins.X", player.getLocation().getX());
                            Main.getInstance().getConfig().set("Teilnehmer.eins.Y", player.getLocation().getY());
                            Main.getInstance().getConfig().set("Teilnehmer.eins.Z", player.getLocation().getZ());
                            Main.getInstance().getConfig().set("Teilnehmer.eins.Welt", player.getLocation().getWorld().getName());
                            Main.getInstance().getConfig().set("Teilnehmer.eins.Gierung", player.getLocation().getYaw());
                            Main.getInstance().getConfig().set("Teilnehmer.eins.Neigung", player.getLocation().getPitch());

                            player.sendMessage("§62. Schritt wurde abgeschlossen!");
                            player.sendMessage("3. Schritt: Bestimme den Einstiegspunkt des zweiten Teilnehmers!");
                            setup.replace(player.getUniqueId(), 2);
                            return true;

                        }

                        if (step == 2) {

                            Main.getInstance().getConfig().set("Teilnehmer.zwei.X", player.getLocation().getX());
                            Main.getInstance().getConfig().set("Teilnehmer.zwei.Y", player.getLocation().getY());
                            Main.getInstance().getConfig().set("Teilnehmer.zwei.Z", player.getLocation().getZ());
                            Main.getInstance().getConfig().set("Teilnehmer.zwei.Welt", player.getLocation().getWorld().getName());
                            Main.getInstance().getConfig().set("Teilnehmer.zwei.Gierung", player.getLocation().getYaw());
                            Main.getInstance().getConfig().set("Teilnehmer.zwei.Neigung", player.getLocation().getPitch());

                            player.sendMessage("§63. Schritt wurde abgeschlossen!");
                            player.sendMessage("4. Schritt: Bestimme den Einstiegspunkt der Zuschauer!");
                            setup.replace(player.getUniqueId(), 3);
                            return true;

                        }

                        if (step == 3) {

                            Main.getInstance().getConfig().set("Zuschauer.X", player.getLocation().getX());
                            Main.getInstance().getConfig().set("Zuschauer.Y", player.getLocation().getY());
                            Main.getInstance().getConfig().set("Zuschauer.Z", player.getLocation().getZ());
                            Main.getInstance().getConfig().set("Zuschauer.Welt", player.getLocation().getWorld().getName());
                            Main.getInstance().getConfig().set("Zuschauer.Gierung", player.getLocation().getYaw());
                            Main.getInstance().getConfig().set("Zuschauer.Neigung", player.getLocation().getPitch());

                            player.sendMessage("§64. Schritt wurde abgeschlossen!");
                            player.sendMessage("§65 Schritt: §fSchließe die Konfiguration ab!");
                            setup.replace(player.getUniqueId(), 4);
                            return true;

                        }

                        if (step == 4) {

                            player.sendMessage("5 Schritt abgschlossen!");
                            Main.getInstance().getConfig().set("Abgeschlossen", true);
                            Main.getInstance().saveConfig();
                            Main.setState(ServerState.WAITING_QUEUE);
                            setup.remove(player.getUniqueId());
                            return true;

                        }

                    }

                }

                if (strings.length == 1) {

                    if (setup.containsKey(player.getUniqueId())) {

                        if (strings[0].equalsIgnoreCase("weiter")) {

                            int currentStep = setup.get(player.getUniqueId());

                            switch (currentStep + 1) {
                                case 1:
                                    player.sendMessage("2. Schritt: Bestimme den Einstiegspunkt des ersten Teilnehmers!");
                                    currentStep += 1;
                                    break;
                                case 2:
                                    player.sendMessage("3. Schritt: Bestimme den Einstiegspunkt des zweiten Teilnehmers!");
                                    currentStep += 1;
                                    break;
                                case 3:
                                    player.sendMessage("4. Schritt: Bestimme den Einstiegspunkt der Zuschauer!");
                                    currentStep += 1;
                                    break;
                                case 4:
                                    player.sendMessage("§65 Schritt: §fSchließe die Konfiguration ab!");
                                    currentStep += 1;
                                    break;
                                case 5:
                                    player.sendMessage("Du kannst diesen Befehl nicht in diesem Schritt verwenden!");
                                    break;

                            }

                            setup.replace(player.getUniqueId(), currentStep);

                        }

                        return true;

                    }else{

                        return false;

                    }

                }

            }else{

                Bukkit.getConsoleSender().sendMessage("§c" + player.getName() + " tried using the SETUP command!");
                return true;

            }

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
