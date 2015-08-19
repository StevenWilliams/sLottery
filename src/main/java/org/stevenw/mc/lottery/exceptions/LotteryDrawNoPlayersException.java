package org.stevenw.mc.lottery.exceptions;

public class LotteryDrawNoPlayersException extends LotteryDrawException {
    public LotteryDrawNoPlayersException(String name) {
        super("No players in lottery " + name);
    }
}
