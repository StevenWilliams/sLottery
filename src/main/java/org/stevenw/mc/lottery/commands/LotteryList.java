package org.stevenw.mc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.sLottery;

import java.util.List;

public class LotteryList implements SubHandler {
    private sLottery plugin;
    public LotteryList(sLottery plugin) {
        this.plugin = plugin;
    }
    @Override
    public void handle(CallInfo callInfo) throws MondoFailure {
        List<Lottery> lotteries = plugin.getLotteryManager().getLotteries();
        if(lotteries.size() == 0) {
             new MondoFailure("There are no lotteries!");
        }
        callInfo.reply("{HEADER}Lotteries:");
        for(Lottery lottery : lotteries) {
            callInfo.reply(lottery.getName());
        }
    }
}
