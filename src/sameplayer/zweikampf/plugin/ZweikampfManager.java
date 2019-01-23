package sameplayer.zweikampf.plugin;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.SAXParseException2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import sameplayer.zweikampf.plugin.Enums.GameStates;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Consumer;

public class ZweikampfManager {

    private Main plugin;

    private Scoreboard scoreboard;
    private GameStates gameState;
    private int MININUM_PLAYERS = 2;

    private HashSet<UUID> brawlerSet;
    private HashSet<UUID> spectatorSet;

    public ZweikampfManager() {
        brawlerSet = new HashSet<>();
        spectatorSet = new HashSet<>();
    }

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
        return gameState.equals(GameStates.RUN_FIGHT);
    }

    public HashSet<UUID> getBrawlerSet() {
        return brawlerSet;
    }

    public HashSet<UUID> getSpectatorSet() {
        return spectatorSet;
    }

    public void addBrawler(Player player) {
        brawlerSet.add(player.getUniqueId());
    }

    public boolean isBrawler(Player player) {
        return brawlerSet.contains(player.getUniqueId());
    }

    public void addSpectator(Player player) {
        spectatorSet.add(player.getUniqueId());
    }

    public void purgeSets() {
        brawlerSet.clear();
        spectatorSet.clear();
    }

    public void purgeBrawler(Player player) {
        brawlerSet.remove(player.getUniqueId());
    }

    public void purgeSpectator(Player player) {
        spectatorSet.remove(player.getUniqueId());
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private void startGame() {

        purgeSets();

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Team spectator = scoreboard.registerNewTeam("spectator");
        spectator.setColor(ChatColor.GRAY);
        spectator.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        spectator.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        spectator.setAllowFriendlyFire(false);
        spectator.setCanSeeFriendlyInvisibles(true);

        Team brawler = scoreboard.registerNewTeam("brawler");
        brawler.setColor(ChatColor.RED);
        brawler.setAllowFriendlyFire(true);
        brawler.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.setScoreboard(scoreboard);

            if (brawlerSet.size() < MININUM_PLAYERS) {
                addBrawler(player);
                brawler.addEntry(player.getName());
                continue;
            }

            addSpectator(player);
            spectator.addEntry(player.getName());

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
            int countdown = 15;
            @Override
            public void run() {
                if (countdown != 0) {
                    if (countdown == 30 || countdown == 20 || countdown == 10 || countdown <= 5 && countdown > 0) {
                        broadcast("§eDie Runde beginnt in §6" + countdown + " Sekunden");
                    }
                    countdown--;
                }else{
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
