package com.daanwbroere.BungeeStatus;

import com.daanwbroere.BungeeStatus.commands.BungeeStatusExecutor;
import com.daanwbroere.BungeeStatus.commands.CommandServers;
import com.daanwbroere.BungeeStatus.utils.ServerStatus;
import com.daanwbroere.BungeeStatus.utils.Status;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by daanb on 17/03/2017.
 */
@Plugin(id="bungeestatus", name="BungeeStatus", version="1.0")
public class BungeeStatus {

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private ConfigurationNode config;
    private ServerStatus serversStatus = new ServerStatus();
    public static Status status;

    @Inject
    Game game;

    @Inject
    Logger logger;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        try {
            config = loader.load();

            if(!defaultConfig.toFile().exists()) {
                config.getNode("placeholder").setValue(true);
                loader.save(config);
            }
        } catch (IOException e) {
            logger.warning("Error loading default configuration");
        }
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Server Started");

        CommandSpec bungeeStatusCommandSpec = CommandSpec.builder()
                .description(Text.of("Tells the caller which servers are online."))
                .executor(new BungeeStatusExecutor())
                .build();
        CommandSpec serversCommandSpec = CommandSpec.builder()
                .description(Text.of("Tells all the servers in the network"))
                .executor(new CommandServers())
                .build();

        Sponge.getCommandManager().register(this, bungeeStatusCommandSpec, Lists.newArrayList("goonstatus", "goonstat"));
        Sponge.getCommandManager().register(this, serversCommandSpec, Lists.newArrayList("goonservers", "goonserv"));

        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkServers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);
    }

    private void checkServers() throws IOException {
        URL url = new URL("http://status.thegoonsquad.eu/simpleapi.php?method=getServerStatus");
        InputStreamReader reader = new InputStreamReader(url.openStream());
        status = new Gson().fromJson(reader, Status.class);

        checkStatus(status);
    }

    public void checkStatus(Status status) {
        //SkyFactory
        if(status.sky.equals("online") && !serversStatus.sky){
            sendMessage("SkyFactory is now Online");
            serversStatus.sky = true;
        }
        else if(status.sky.equals("offline") && serversStatus.sky){
            sendMessage("SkyFactory is now Offline");
            serversStatus.sky = false;
        }
        //AllTheMods
        if(status.all.equals("online") && !serversStatus.all){
            sendMessage("AllTheMods is now Online");
            serversStatus.all = true;
        }
        else if(status.all.equals("offline") && serversStatus.all){
            sendMessage("AllTheMods is now Offline");
            serversStatus.all = false;
        }
        //FTB Beyond
        if(status.bey.equals("online") && !serversStatus.bey){
            sendMessage("FTB: Beyond is now Online");
            serversStatus.bey = true;
        }
        else if(status.bey.equals("offline") && serversStatus.bey){
            sendMessage("FTB: Beyond is now Offline");
            serversStatus.bey = false;
        }
        //Direwolf20
        if(status.dir.equals("online") && !serversStatus.dir){
            sendMessage("Direwolf20 is now Online");
            serversStatus.dir = true;
        }
        else if(status.dir.equals("offline") && serversStatus.dir){
            sendMessage("Direwolf20 is now Offline");
            serversStatus.dir = false;
        }
        //FoolCraft
        if(status.foo.equals("online") && !serversStatus.foo){
            sendMessage("FoolCraft is now Online");
            serversStatus.foo = true;
        }
        else if(status.foo.equals("offline") && serversStatus.foo){
            sendMessage("FoolCraft is now Offline");
            serversStatus.foo = false;
        }
        //Infinity Lite
        if(status.inf.equals("online") && !serversStatus.inf){
            sendMessage("FTB: Infinity Lite is now Online");
            serversStatus.inf = true;
        }
        else if(status.inf.equals("offline") && serversStatus.inf){
            sendMessage("FTB: Infinity Lite is now Offline");
            serversStatus.inf = false;
        }
    }

    public void sendMessage(String message) {
        game.getServer().getBroadcastChannel().send(Text.builder("[ServerStatus] ").color(TextColors.RED)
                .append(Text.builder(message).color(TextColors.GOLD).build()).build());
    }
}
