package org.stevenw.mc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.sLottery;

public class LotteryClaim implements SubHandler {
    private sLottery plugin;
    public LotteryClaim(sLottery plugin)
    {
        this.plugin = plugin;
    }
    @Override
    public void handle(CallInfo callInfo) throws MondoFailure {


        String lotteryname = callInfo.getArg(0);
        Lottery lottery = plugin.getLotteryManager().getLottery(lotteryname);

        if(lottery == null) {
            throw new MondoFailure("VaultLottery " + lotteryname + " does not exist!");
        }

            Long claim = lottery.claim(callInfo.getPlayer());
            if (claim == 0) {
                throw new MondoFailure("You have nothing to claim!");
            }
    }
}
