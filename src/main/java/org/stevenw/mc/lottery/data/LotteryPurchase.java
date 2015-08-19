package org.stevenw.mc.lottery.data;

import org.stevenw.mc.lottery.Lottery;

import java.util.UUID;

public class LotteryPurchase {
    private long cost;
    private int quantity;
    private Lottery lottery;
    private double value;
    private UUID uuid;

    public LotteryPurchase(Lottery lottery, UUID player, Long cost, int quantity)
    {
        this.cost = cost;
        this.quantity = quantity;
        this.lottery = lottery;
        this.value = cost;
        this.uuid = player;
    }

    public long getCost() {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public UUID getPlayer() {
        return this.uuid;
    }
}
