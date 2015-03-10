package net.vulcanmc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import net.vulcanmc.lottery.AbstractLottery;
import net.vulcanmc.lottery.VulcanLottery;

import java.util.List;

public class LotteryList implements SubHandler {
    @Override
    public void handle(CallInfo callInfo) throws MondoFailure {
        List<AbstractLottery> lotteries = VulcanLottery.plugin.getLotteryManager().getLotteries();
        if(lotteries.size() == 0) {
             new MondoFailure("There are no lotteries!");
        }
        callInfo.reply("{HEADER}Lotteries:");
        for(AbstractLottery lottery : lotteries) {
            callInfo.reply(lottery.getName());
        }
    }
}
