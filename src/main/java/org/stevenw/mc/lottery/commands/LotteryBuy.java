package org.stevenw.mc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.data.LotteryPurchase;
import org.stevenw.mc.lottery.exceptions.LotteryPurchaseNoAccessException;
import org.stevenw.mc.lottery.exceptions.LotteryPurchaseNoMoneyException;
import org.stevenw.mc.lottery.exceptions.LotteryPurchaseOverPlayerLimitException;
import org.stevenw.mc.lottery.sLottery;
import org.stevenw.mc.lottery.exceptions.LotteryPurchaseException;

public class LotteryBuy implements SubHandler {
    private sLottery plugin;
    public LotteryBuy(sLottery plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CallInfo call) throws MondoFailure {
        String lotteryName = call.getArg(0);
        Integer amount;
        try {
            amount = Integer.valueOf(call.getArg(1));
        } catch (NumberFormatException ex) {
            throw new MondoFailure("Error getting amount");
        }
        Lottery lottery = plugin.getLotteryManager().getLottery(lotteryName);
        if(lottery == null) {
            throw new MondoFailure("Lottery " + lotteryName + " does not exist!");
        }

        try {
            LotteryPurchase purchase = lottery.purchase(call.getPlayer(), amount);
           // call.reply("Purchased " + amount + " tickets for $" + cost);
        } catch (LotteryPurchaseException e) {
            throw new MondoFailure(e.getMessage());
        }
    }
}
