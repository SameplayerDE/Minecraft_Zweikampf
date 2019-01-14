package sameplayer.zweikampf.plugin;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sameplayer.zweikampf.plugin.Enums.GameStates;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Consumer;

public class ZweikampfManager {

    private Main plugin;

    private GameStates gameState;
    private int MININUM_PLAYERS = 2;

    private HashSet<UUID> brawlerSet;
    private HashSet<UUID> spectatorSet;

    public boolean isReady() {
        return gameState.equals(GameStates.WAIT_QUEUE) && isMinimumReached();
    }

    private boolean isMinimumReached() {
        if (Bukkit.getOnlinePlayers().size() >= MININUM_PLAYERS) {
            return true;
        }
        return false;
    }

    public GameStates getGameState() {
        return gameState;
    }

    public void setGameState(GameStates gameState) {
        Bukkit.getConsoleSender().sendMessage(this.gameState.toString() + " > " + gameState.toString());
        this.gameState = gameState;
    }

    public boolean isGoing() {
        return gameState.equals(GameStates.RUN_PICK_KIT);
    }

    public void addBrawler(Player player) {
        brawlerSet.add(player.getUniqueId());
    }

    public void addSpectator(Player player) {
        spectatorSet.add(player.getUniqueId());
    }

    public void purgeSets() {
        brawlerSet.clear();
        spectatorSet.clear();
    }

    private void startGame() {

        purgeSets();

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (brawlerSet.size() < MININUM_PLAYERS) {
                addBrawler(player);
                continue;
            }

            addSpectator(player);

        }

    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }

    public void broadcast(String message, @NotNull GameEntity team) {
        if (team.equals(GameEntity.BRAWLER)) {
            brawlerSet.forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(message));
        }
        if (team.equals(GameEntity.SPECTATOR)) {
            spectatorSet.forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(message));
        }
    }

    public void startCountdown() {
        new BukkitRunnable() {
            int countdown = 10;
            @Override
            public void run() {
                if (countdown != 0) {
                    if (countdown == 30 || countdown == 20 || countdown == 10 || countdown <= 5 && countdown > 0) {
                        //Bukkit.broadcastMessage("§aDie Runde beginnt in §e" + countdown + " Sekunden");
                    }
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.setLevel(countdown);
                    }
                    countdown--;
                }else{
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.setLevel(0);
                    }
                    cancel();
                    if (isMinimumReached() && gameState.equals(GameStates.WAIT_COUNTDOWN_FIGHT)) {
                        startGame();
                    }else{
                        setGameState(GameStates.WAIT_QUEUE);
                    }
                }

            }
        }.runTaskTimer(plugin, 20*2l, 20l);
    }

    private enum GameEntity {

        BRAWLER,
        SPECTATOR;

    }

}
