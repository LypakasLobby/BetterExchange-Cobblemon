package com.lypaka.betterexchange.Commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.Arrays;
import java.util.List;

public class BetterExchangeCommand {

    public static final List<String> ALIASES = Arrays.asList("betterexchange", "bexchange", "exchange", "bex");

    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            new MainCommand(dispatcher);
            new ReloadCommand(dispatcher);

        });

    }

}
