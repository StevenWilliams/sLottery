package net.vulcanmc.lottery.commands;


import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Range;
import org.bukkit.entity.Player;

public class Commands {
    @Command(aliases = "hello123", desc = "Set age")
    public void setAge(Player player, @Range(min = 1) int age) {
        player.sendMessage("Hello World!!!!! :)");

    }
}
