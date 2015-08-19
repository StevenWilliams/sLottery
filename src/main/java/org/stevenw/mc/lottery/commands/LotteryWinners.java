package org.stevenw.mc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.data.LotteryWinner;
import org.stevenw.mc.lottery.sLottery;

import java.util.List;

public class LotteryWinners implements SubHandler{
    private sLottery plugin;
    public LotteryWinners(sLottery plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CallInfo call) throws MondoFailure {
        String lotteryName = call.getArg(0);
        Lottery lottery = plugin.getLotteryManager().getLottery(lotteryName);
        if(lottery == null) {
            throw new MondoFailure("Lottery " + lotteryName + " does not exist!");
        }
        List<LotteryWinner> winners = lottery.getData().getWinners();
        for(LotteryWinner winner : winners) {
            call.reply(winner.getWinner().toString());
        }
    }
}
