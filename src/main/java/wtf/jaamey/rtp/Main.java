package wtf.jaamey.rtp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.jaamey.rtp.cfg.Config;
import wtf.jaamey.rtp.cmd.RandomTP;


public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        new Config();
        PluginManager plm = Bukkit.getPluginManager();
        this.getCommand("rtp").setExecutor(new RandomTP());
    }

    @Override
    public void onDisable() {}

}
