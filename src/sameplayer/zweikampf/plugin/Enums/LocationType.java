package sameplayer.zweikampf.plugin.Enums;

import org.bukkit.Location;
import sameplayer.zweikampf.plugin.Main;

public enum LocationType {

    WAITING(Main.configToLocation("Wartebereich")),
    PLAYER_ONE(Main.configToLocation("Teilnehmer.eins")),
    PLAYER_TWO(Main.configToLocation("Teilnehmer.zwei")),
    SPECTATOR(Main.configToLocation("Zuschauer"));

    private Location location;


    LocationType(Location location) {
        this.location = location;
    }

    public Location toLocation() {
        return location;
    }

}
