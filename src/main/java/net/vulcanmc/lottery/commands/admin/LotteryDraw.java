package net.vulcanmc.lottery.commands.admin;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import net.vulcanmc.lottery.AbstractLottery;
import net.vulcanmc.lottery.VulcanLottery;

public class LotteryDraw implements SubHandler {

    @Override
    public void handle(CallInfo call) throws MondoFailure {
        String lotteryname = call.getArg(0);
        AbstractLottery lottery = null;

        if(VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname) == null) {
            throw new MondoFailure("Lottery " + lotteryname + " does not exist!");
        }

        lottery = VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname);

        if(lottery.draw()) {
            call.reply("Drew Lottery!");
        } else {
            throw new MondoFailure("Error drawing lottery!");
        }
    }
}
