package com.daanwbroere.BungeeStatus.commands;

import flavor.pie.bungeelib.BungeeLib;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

public class CommandServers implements CommandExecutor {

    public BungeeLib bLib;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player || src instanceof ConsoleSource) {
            bLib.getServerName().thenAccept(name -> Task.builder().execute(() -> src.sendMessage(Text.of(name))).submit(this));
            return CommandResult.success();
            blib.
        }
        else {
            src.sendMessage(Text.of("I dont know what you are :/"));
        }
        return CommandResult.empty();
    }
}
