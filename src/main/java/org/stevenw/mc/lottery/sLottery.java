package org.stevenw.mc.lottery;

import mondocommand.FormatConfig;
import mondocommand.MondoCommand;
import org.bukkit.ChatColor;
import org.stevenw.mc.lottery.commands.*;
import org.stevenw.mc.lottery.commands.admin.LotteryDraw;
import org.stevenw.mc.lottery.data.YAMLLotteryData;
import org.stevenw.mc.lottery.listeners.LoginListener;
import org.stevenw.mc.lottery.listeners.PurchaseListener;
import org.stevenw.mc.lottery.listeners.WinListener;
import org.stevenw.mc.lottery.lotteries.VaultLottery;
import org.stevenw.mc.lottery.runnables.LotteryDrawChecker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class sLottery extends JavaPlugin {
    private LotteryManager lotteryManager;
    @Override
    public void onEnable(){
        this.saveDefaultConfig();
        loadCommands();
        loadLotteries();
        this.getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        this.getServer().getPluginManager().registerEvents(new WinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PurchaseListener(this), this);
        new LotteryDrawChecker(this).runTaskTimer(this, 10, 20);
    }
    private void loadLotteries() {
        this.lotteryManager = new LotteryManager(this);
        Set<String> lotteries = getConfig().getConfigurationSection("lotteries").getKeys(false);
        for(String lotteryname : lotteries) {
            Lottery lottery = null;
            LotteryData lotteryData = new YAMLLotteryData(this, lottery);
            String type = getConfig().getString("lotteries." + lotteryname + ".type");
            if(true || type.equals(VaultLottery.class.getSimpleName())) { //TODO: add different lottery types...
                lottery = new VaultLottery(this, lotteryname, lotteryData, this.getConfig());
                this.lotteryManager.enableLottery(lottery);
                lotteryData.reloadData(lottery);
            }
        }
    }
    public LotteryManager getLotteryManager() {
        return this.lotteryManager;
    }
    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("msg-prefix"));
    }

    private void loadCommands() {
        FormatConfig fmt = new FormatConfig()
                .setReplyPrefix(this.getPrefix())
                .setPermissionWarning(this.getConfig().getString("no-permission"));

        MondoCommand base = new MondoCommand(fmt);
        base.autoRegisterFrom(this);
        base.addSub("buy", "sLottery.buy")
                .setDescription("Buy a lottery ticket")
                .setMinArgs(2)
                .setUsage("<lotteryname> <amount>")
                .setHandler(new LotteryBuy(this));

        base.addSub("claim", "sLottery.claim")
                .setDescription("Claim prize")
                .setMinArgs(0)
                .setHandler(new LotteryClaim(this));

        base.addSub("info", "sLottery.info")
                .setDescription("Information about a lottery")
                .setMinArgs(1)
                .setUsage("<lotteryname>")
                .allowConsole()
                .setHandler(new LotteryInfo(this));

        base.addSub("list", "sLottery.list")
                .setDescription("List Lotteries")
                .setMinArgs(0)
                .allowConsole()
                .setHandler(new LotteryList(this));

        base.addSub("winners", "sLottery.winners")
                .setDescription("Show winners of a lottery")
                .setMinArgs(1)
                .setUsage("<lotteryname>")
                .allowConsole()
                .setHandler(new LotteryWinners(this));
        base.addSub("draw", "sLottery.admin.draw")
                .setDescription("Force draw of lottery!")
                .setMinArgs(1)
                .setUsage("<lotteryname>")
                .allowConsole()
                .setHandler(new LotteryDraw(this));
        getCommand("lottery").setExecutor(base);
    }
    @Override
    public void onDisable(){
        this.lotteryManager = null;
    }
}
