package sameplayer.zweikampf.plugin.Enums;

public enum ServerState {

    WAITING_QUEUE(0),
    RUNNING(1),
    RESTARTING(2),
    ERROR(3),
    SETUP(4),
    WAITING_FIGHT(5);

    private int i;

    ServerState(int i) {
        this. i = i;
    }

    public int getValue() {
        return i;
    }
}
