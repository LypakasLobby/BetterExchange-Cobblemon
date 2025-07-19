package com.lypaka.betterexchange.Commands;

import com.lypaka.betterexchange.BetterExchange;
import com.lypaka.betterexchange.ConfigGetters;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : BetterExchangeCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal(a)
                            .then(
                                    CommandManager.literal("reload")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "betterexchange.command.admin")) {

                                                        player.sendMessage(FancyTextHandler.getFormattedText("&cYou don't have permission to use this command!"));
                                                        return 1;

                                                    }

                                                }

                                                try {

                                                    BetterExchange.configManager.load();
                                                    ConfigGetters.load(true);
                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&aSuccessfully reloaded BetterExchange!"));

                                                } catch (ObjectMappingException e) {

                                                    e.printStackTrace();

                                                }

                                                return 0;

                                            })
                            )
            );

        }

    }

}
