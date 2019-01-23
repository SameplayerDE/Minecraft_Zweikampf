package sameplayer.zweikampf.plugin.Enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import sameplayer.zweikampf.plugin.Main;

public enum LocationType {

    WAITING(configToLocation("Wartebereich")),
    PLAYER_ONE(configToLocation("Teilnehmer.eins")),
    PLAYER_TWO(configToLocation("Teilnehmer.zwei")),
    SPECTATOR(configToLocation("Zuschauer"));

    private Location location;
    private final static Main plugin = Main.getInstance();

    LocationType(Location location) {
        this.location = location;
    }

    public static Location configToLocation(String path) {

        double x, y, z;
        float yaw, pitch;
        String world;

        x =  Main.getInstance().getConfig().getDouble(path + ".X");
        y =  Main.getInstance().getConfig().getDouble(path + ".Y");
        z =  Main.getInstance().getConfig().getDouble(path + ".Z");

        yaw = (float)  Main.getInstance().getConfig().getDouble(path + ".Gierung");
        pitch = (float)  Main.getInstance().getConfig().getDouble(path + ".Neigung");

        world =  Main.getInstance().getConfig().getString(path + ".Welt");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);


    }

    public Location toLocation() {
        return location;
    }

}
