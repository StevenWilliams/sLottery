package org.stevenw.mc.lottery.commands.admin;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import org.stevenw.mc.lottery.Lottery;
import org.stevenw.mc.lottery.exceptions.LotteryDrawNoPlayersException;
import org.stevenw.mc.lottery.sLottery;

public class LotteryDraw implements SubHandler {
    private sLottery plugin;
    public LotteryDraw(sLottery plugin){
        this.plugin = plugin;
    }

    @Override
    public void handle(CallInfo call) throws MondoFailure {
        String lotteryName = call.getArg(0);
        Lottery lottery = plugin.getLotteryManager().getLottery(lotteryName);

        if(lottery == null) {
            throw new MondoFailure("VaultLottery " + lotteryName + " does not exist!");
        }

        try {
            lottery.draw();
        } catch (LotteryDrawNoPlayersException e) {
            throw new MondoFailure("No players entered the lottery!");
        }
    }
}
