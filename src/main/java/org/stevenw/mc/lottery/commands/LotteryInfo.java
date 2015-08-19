package org.stevenw.mc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import org.bukkit.entity.Player;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.sLottery;

public class LotteryInfo implements SubHandler {
    private sLottery plugin;

    public LotteryInfo(sLottery plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CallInfo call) throws MondoFailure {
        String lotteryName = call.getArg(0);
        Lottery lottery = plugin.getLotteryManager().getLottery(lotteryName);
        if(lottery == null) {
            throw new MondoFailure("Lottery " + lotteryName + " does not exist!");
        }

		Long timeLeft = lottery.getNextDraw() - System.currentTimeMillis(); //TODO: Format to time


        call.reply("{HEADER}=====" + lottery.getName() +"=====");
        call.reply("{NOUN}Pot Size: {DESCRIPTION}$" + lottery.getData().getPot());
        call.reply("{NOUN}Time Left: {DESCRIPTION} " + timeLeft);
        if(call.getSender() instanceof Player) {
            call.reply("{NOUN}Your tickets: {DESCRIPTION}" + lottery.getData().getTicketsPurchased(call.getPlayer().getUniqueId()));
        }
        call.reply("{NOUN}Total tickets: {DESCRIPTION}" + lottery.getData().getTotalTicketsPurchased());
        call.reply("{NOUN}Players: {DESCRIPTION} " + lottery.getData().getTotalPlayers());
    }
}
