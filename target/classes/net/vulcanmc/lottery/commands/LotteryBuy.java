package net.vulcanmc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import net.vulcanmc.lottery.AbstractLottery;
import net.vulcanmc.lottery.VulcanLottery;
import net.vulcanmc.lottery.commands.exceptions.LotteryPurchaseException;

public class LotteryBuy implements SubHandler {
    @Override
    public void handle(CallInfo call) throws MondoFailure {
        String lotteryname = call.getArg(0);
        Integer amount = null;
        try {
            amount = Integer.valueOf(call.getArg(1));

        } catch (NumberFormatException ex) {
            if (amount == null) {
                throw new MondoFailure("Error getting amount");
            }
        }
        AbstractLottery lottery = null;

        if(VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname) == null) {
            throw new MondoFailure("Lottery " + lotteryname + " does not exist!");
        }

        lottery = VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname);

        try {
            Long cost = lottery.purchase(call.getPlayer(), amount);
            if(cost != null) {
                call.reply("Purchased " + amount + " tickets for $" + cost);
            }
        } catch (LotteryPurchaseException e) {
            throw new MondoFailure(e.getMessage());
        }
    }
}
