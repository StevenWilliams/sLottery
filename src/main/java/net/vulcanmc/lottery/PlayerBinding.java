package net.vulcanmc.lottery;

import com.sk89q.intake.parametric.ParameterException;
import com.sk89q.intake.parametric.argument.ArgumentStack;
import com.sk89q.intake.parametric.binding.BindingBehavior;
import com.sk89q.intake.parametric.binding.BindingHelper;
import com.sk89q.intake.parametric.binding.BindingMatch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerBinding extends BindingHelper {

    @BindingMatch(type = Player.class,
            behavior = BindingBehavior.CONSUMES,
            consumedCount = 1)
    public Player getPlayerInput(ArgumentStack context) throws ParameterException {
        String input = context.next(); // Get the next string from the argument stream

        Player player = Bukkit.getServer().getPlayer(input); // Read the next argument
        if (player == null) {
            throw new ParameterException("Sorry, I couldn't find a player named '" + input + "'.");
        } else {
            return player;
        }
    }

}