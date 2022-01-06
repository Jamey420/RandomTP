package wtf.jaamey.rtp.cmd;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.jaamey.rtp.cfg.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomTP implements CommandExecutor {

    private int maxRadius;
    private String prefix;
    private List<String> worlds;

    private List<Material> unsafeblocks;
    private HashMap<UUID, Long> cooldowns;

    public RandomTP(){
        this.maxRadius = Config.getRadius();
        this.prefix = Config.getPrefix();
        this.worlds = Config.getWorlds();

        this.unsafeblocks = new ArrayList<>();
        this.unsafeblocks.add(Material.AIR);
        this.cooldowns = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (p.hasPermission("jaamey.rtp")){
                Location myLoc = p.getLocation();
                // is er in der richtigen welt??
                if (!isAllowedWorld(myLoc.getWorld().getName())){
                    p.sendMessage(prefix + "RandomTeleport ist in dieser Welt nicht möglich");
                    return false;
                }
                // bypassed er den cooldown??
                if (!p.hasPermission("jaamey.rtp.bypass")){
                    Long lastCommandUsage = cooldowns.getOrDefault(p.getUniqueId(), 0L);
                    long nextU = lastCommandUsage + Config.getCooldown() * 1000; // * 1000 is wegen sekunde
                    if (nextU > System.currentTimeMillis()){
                        long diff = nextU - System.currentTimeMillis();
                        p.sendMessage(prefix + "Du musst noch " + diff + " Sekunden warten");
                        return true;
                    } else {
                        cooldowns.put(p.getUniqueId(), System.currentTimeMillis());
                    }
                }
                for (int i=0; i < 25; ++i){
                    Location newLoc = getRandomLoc(p);
                    boolean safetp = true;
                    if (safetp){
                        Material mat = newLoc.getBlock().getType();
                        if (unsafeblocks.contains(mat)){
                            continue;
                        }
                    }
                    p.teleport(newLoc.add(0.5d, 1d, 0.5d));
                    p.sendMessage(prefix + "Du wurdest " + Math.floor(newLoc.distance(myLoc)) + " Blöcke teleportiert");
                    return true;
                }
                p.sendMessage(prefix + "Es wurden keine sicheren Orte gefunden.");
                return false;
            } else {
                p.sendMessage(prefix + "Du hast nicht genügend Berechtigung");
                return false;
            }
        } else {
            sender.sendMessage(prefix + "Du musst ein Spieler sein");
            return false;
        }
    }

    private Location getRandomLoc(Player player){
        WorldBorder wb = player.getLocation().getWorld().getWorldBorder();
        int wbsize = (int) wb.getSize();
        if (wbsize>maxRadius * 2) {
            wbsize = maxRadius * 2;
        }
        ThreadLocalRandom rndm = ThreadLocalRandom.current();
        int x = rndm.nextInt(wbsize) - wbsize / 2;
        int z = rndm.nextInt(wbsize) - wbsize / 2;
        Location rndmLoc = wb.getCenter().add(x, 0, z);

        return player.getLocation().getWorld().getHighestBlockAt(rndmLoc).getLocation();
    }

    private boolean isAllowedWorld(String worldname){
        if (this.worlds.contains(worldname))return true;
        return false;
    }
}
