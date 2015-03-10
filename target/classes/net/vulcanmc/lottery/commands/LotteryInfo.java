package net.vulcanmc.lottery.commands;

import mondocommand.CallInfo;
import mondocommand.MondoFailure;
import mondocommand.SubHandler;
import net.vulcanmc.lottery.AbstractLottery;
import net.vulcanmc.lottery.VulcanLottery;

public class LotteryInfo implements SubHandler {
    @Override
    public void handle(CallInfo call) throws MondoFailure {


        String lotteryname = call.getArg(0);
        AbstractLottery lottery = null;

        if(VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname) == null) {
            throw new MondoFailure("Lottery " + lotteryname + " does not exist!");
        }

        lottery = VulcanLottery.plugin.getLotteryManager().getLottery(lotteryname);

        call.reply("{HEADER}=====" + lottery.getName() +"=====");
        call.reply("{NOUN}Pot Size: {DESCRIPTION}$" + lottery.getPotAmount());
        call.reply("{NOUN}Time Left: {DESCRIPTION}UNKNOWN");
        call.reply("{NOUN}Your tickets: {DESCRIPTION}" + lottery.getTicketsPurchased(call.getPlayer()));
        call.reply("{NOUN}Total tickets: {DESCRIPTION}" + lottery.getTotalTickets());
        call.reply("{NOUN}Players: {DESCRIPTION}UNKNOWN");
    }
}
