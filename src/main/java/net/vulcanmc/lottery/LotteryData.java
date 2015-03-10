package net.vulcanmc.lottery;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class LotteryData {
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;
    private VulcanLottery plugin;
    private String lotteryname;

    public LotteryData(VulcanLottery plugin, String lotteryname) {
        this.plugin = plugin;
        this.lotteryname = lotteryname;

    }

    public void reloadData() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder() + "/data/lotteries", lotteryname + ".yml");
            if(!customConfigFile.exists()) {
                    customConfigFile.mkdir();

            }
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }


    public FileConfiguration getData() {
        if (customConfig == null) {
            reloadData();
        }
        return customConfig;
    }

    public void saveData() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getData().save(customConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }
}
