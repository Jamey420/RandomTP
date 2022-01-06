package wtf.jaamey.rtp.cfg;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import wtf.jaamey.rtp.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins//RandomTP//config.yml"));

    public Config() {
        File ordner = new File("plugins//RandomTP");
        File file = new File("plugins//RandomTP//config.yml");
        if (!ordner.exists()){
            ordner.mkdir();
        }
        if (!file.exists()){
            try {
                file.createNewFile();
                FileConfiguration cfgg = YamlConfiguration.loadConfiguration(file);
                cfgg.set("prefix", "jaamey.wtf | ");
                cfgg.set("cooldownInSecounds", 10);
                cfgg.set("maximalRadius", 500);

                List<String> dd = new ArrayList<>();
                dd.add("world");
                dd.add("world_nether");
                cfgg.set("allowedWorlds", dd);

                try {
                    cfgg.save(file);
                } catch (IOException e){}
            } catch (IOException e){}
        }
    }

    public static String getPrefix() {
        return cfg.getString("prefix");
    }
    public static int getCooldown(){
        return cfg.getInt("cooldownInSecounds");
    }
    public static int getRadius(){
        return cfg.getInt("maximalRadius");
    }
    public static List<String> getWorlds(){
        return (List<String>)cfg.getList("allowedWorlds");
    }
}
