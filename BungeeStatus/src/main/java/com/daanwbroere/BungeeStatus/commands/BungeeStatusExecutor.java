package com.daanwbroere.BungeeStatus.commands;

import com.daanwbroere.BungeeStatus.BungeeStatus;
import flavor.pie.bungeelib.BungeeLib;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class BungeeStatusExecutor implements CommandExecutor {

    public BungeeLib bLib;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player || src instanceof ConsoleSource) {
            List<Text> contents = new ArrayList<>();

            colorStatus sky = new colorStatus();
            colorStatus bey = new colorStatus();
            colorStatus all = new colorStatus();
            colorStatus dir = new colorStatus();
            colorStatus foo = new colorStatus();
            colorStatus inf = new colorStatus();
            if (BungeeStatus.status.sky.equals("offline")) {
                sky.newColor = TextColors.RED;
                sky.status = "Offline";
            }
            if (BungeeStatus.status.bey.equals("offline")) {
                bey.newColor = TextColors.RED;
                bey.status = "Offline";
            }
            if (BungeeStatus.status.all.equals("offline")) {
                all.newColor = TextColors.RED;
                all.status = "Offline";
            }
            if (BungeeStatus.status.dir.equals("offline")) {
                dir.newColor = TextColors.RED;
                dir.status = "Offline";
            }
            if (BungeeStatus.status.foo.equals("offline")) {
                foo.newColor = TextColors.RED;
                foo.status = "Offline";
            }
            if (BungeeStatus.status.inf.equals("offline")) {
                inf.newColor = TextColors.RED;
                inf.status = "Offline";
            }
            contents.add(Text.of(TextColors.GOLD, "[SkyFactory 3] ", sky.newColor, sky.status));
            contents.add(Text.of(TextColors.GOLD, "[FTB: Beyond] ", bey.newColor, bey.status));
            contents.add(Text.of(TextColors.GOLD, "[AllTheMods] ", all.newColor, all.status));
            contents.add(Text.of(TextColors.GOLD, "[Direwolf20] ", dir.newColor, dir.status));
            contents.add(Text.of(TextColors.GOLD, "[Hermitpack] ", foo.newColor, foo.status));
            contents.add(Text.of(TextColors.GOLD, "[FTB: Infinity Lite] ", inf.newColor, inf.status));

            PaginationList.builder()
                    .title(Text.of(TextColors.GREEN, "Server Status"))
                    .contents(contents)
                    .padding(Text.of( "="))
                    .sendTo(src);

            return CommandResult.success();
        }
        else {
            src.sendMessage(Text.of("I dont know what you are :/"));
        }
        return CommandResult.empty();
    }

    private class colorStatus{
        TextColor newColor = TextColors.GREEN;
        String status = "Online";
    }
}
