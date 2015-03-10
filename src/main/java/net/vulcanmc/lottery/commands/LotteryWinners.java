package net.vulcanmc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import net.vulcanmc.lottery.AbstractLottery;
import net.vulcanmc.lottery.VulcanLottery;

public class LotteryWinners implements SubHandler{
    @Override
    public void handle(CallInfo callInfo) throws MondoFailure {


        String lotteryname = callInfo.getArg(0);
        AbstractLottery lottery = null;

        if(VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname) == null) {
            throw new MondoFailure("Lottery " + lotteryname + " does not exist!");
        }

        lottery = VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname);
        callInfo.reply("Not implemented yet.");
    }
}
