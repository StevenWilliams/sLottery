package net.vulcanmc.lottery;

import mondocommand.ChatMagic;
import mondocommand.FormatConfig;
import mondocommand.MondoCommand;
import net.milkbowl.vault.economy.Economy;
import net.vulcanmc.lottery.commands.*;
import net.vulcanmc.lottery.commands.admin.LotteryDraw;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * Created by steven on 29/12/14.
 */
public class VulcanLottery extends JavaPlugin {
    private LotteryManager lotteryManager;
    public static Economy econ = null;
    public static VulcanLottery plugin;
    @Override
    public void onEnable(){
        this.saveDefaultConfig();
        loadCommands();
        if (!setupEconomy() ) {
            this.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        loadLotteries();
        plugin = this;
    }
    private void loadLotteries() {
        this.lotteryManager = new LotteryManager(this);
        Set<String> lotteries = getConfig().getConfigurationSection("lotteries").getKeys(false);
        for(String lotteryname : lotteries) {
            LotteryData lotteryData = new LotteryData(this, lotteryname);
            Lottery lottery = new Lottery(lotteryname, this, lotteryData);
            this.lotteryManager.enableLottery(lottery);
        }
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    public boolean deposit(Player player, Long amount) {

        return econ.depositPlayer(player, amount).transactionSuccess();

        //return true;
    }
    public boolean withdraw(Player player, Long amount) {
        return econ.withdrawPlayer(player, amount).transactionSuccess();
        //return true;
    }

    public LotteryManager getLotteryManager() {
        return this.lotteryManager;
    }

    private void loadCommands() {
        FormatConfig fmt = new FormatConfig()
                .setReplyPrefix("{DARK_GREEN}[VulcanLottery] ")
                .setPermissionWarning("{RED}No permissions to perform this action.");
        ChatMagic.registerAlias("{VERB}", ChatColor.RED);
        ChatMagic.registerAlias("{USAGE}", ChatColor.GRAY);
        ChatMagic.registerAlias("{DESCRIPTION}", ChatColor.DARK_GRAY);

        MondoCommand base = new MondoCommand(fmt);
        base.autoRegisterFrom(this);
        base.addSub("buy", "vulcan.lottery.buy")
                .setDescription("Buy a lottery ticket")     // Description is shown in command help
                .setMinArgs(2)                       // Won't run command without this many args
                .setUsage("<lotteryname> <amount>")          // Sets argument usage information
                .setHandler(new LotteryBuy());

        base.addSub("claim", "vulcan.lottery.claim")
                .setDescription("Claim prize")
                .setMinArgs(0)
                .setHandler(new LotteryClaim());

        base.addSub("info", "vulcan.lottery.info")
                .setDescription("Information about a lottery")
                .setMinArgs(1)
                .setUsage("<lotteryname>")
                .allowConsole()
                .setHandler(new LotteryInfo());

        base.addSub("list", "vulcan.lottery.list")
                .setDescription("List Lotteries")
                .setMinArgs(0)
                .allowConsole()
                .setHandler(new LotteryList());

        base.addSub("winners", "vulcan.lottery.winners")
                .setDescription("Show winners of a lottery")
                .setMinArgs(1)
                .setUsage("<lotteryname>")
                .allowConsole()
                .setHandler(new LotteryWinners());
        base.addSub("draw", "vulcan.lottery.admin.draw")
                .setDescription("Force draw of lottery!")
                .setMinArgs(1)
                .setUsage("<lotteryname>")
                .allowConsole()
                .setHandler(new LotteryDraw());
        getCommand("lottery").setExecutor(base);
    }
    @Override
    public void onDisable(){

    }
}
