package com.lypaka.betterexchange.Commands;

import com.lypaka.betterexchange.ConfigGetters;
import com.lypaka.betterexchange.GUIs.MainGUI;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class MainCommand {

    public MainCommand (CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : BetterExchangeCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal(a)
                            .executes(c -> {

                                if (ConfigGetters.exchangeSystemEnabled) {

                                    if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                        ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                        if (!ConfigGetters.commandPermission.equalsIgnoreCase("")) {

                                            if (!PermissionHandler.hasPermission(player, ConfigGetters.commandPermission)) {

                                                player.sendMessage(FancyTextHandler.getFormattedText("&cYou don't have permission to use this command!"));
                                                return 1;

                                            }

                                        }

                                        MainGUI.openMainGui(player);

                                    }

                                } else {

                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&cThe exchange menu is currently disabled!"));
                                    return 1;

                                }

                                return 0;

                            })
            );

        }

    }

}
