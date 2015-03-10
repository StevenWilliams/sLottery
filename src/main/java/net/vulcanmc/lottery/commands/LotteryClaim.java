package net.vulcanmc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import net.vulcanmc.lottery.AbstractLottery;
import net.vulcanmc.lottery.VulcanLottery;

public class LotteryClaim implements SubHandler {
    @Override
    public void handle(CallInfo callInfo) throws MondoFailure {


        String lotteryname = callInfo.getArg(0);
        AbstractLottery lottery = null;

        if(VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname) == null) {
            throw new MondoFailure("Lottery " + lotteryname + " does not exist!");
        }

        lottery = VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname);
        if(lottery.hasWon(callInfo.getPlayer())) {
            Long claim = lottery.claim(callInfo.getPlayer());
            if (claim == 0) {
                throw new MondoFailure("You have nothing to claim!");
            }
            callInfo.reply("You've claimed $" + claim);
        } else {
            throw new MondoFailure("You have not won!");
        }
    }
}
