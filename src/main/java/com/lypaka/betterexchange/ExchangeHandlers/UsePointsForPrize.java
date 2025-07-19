package com.lypaka.betterexchange.ExchangeHandlers;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.betterexchange.BetterExchange;
import com.lypaka.betterexchange.GUIs.MainGUI;
import com.lypaka.betterexchange.PointHandlers.Points;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class UsePointsForPrize {

    public static void redeemListing (ServerPlayerEntity player, String listingName, int points) throws ObjectMappingException {

        Points.setPoints(player.getUuid(), Points.getPoints(player.getUuid()) - points);
        List<String> commands = BetterExchange.configManager.getConfigNode(0, "Special-Listings", listingName, "Execute").getList(TypeToken.of(String.class));
        for (String c : commands) {

            player.getWorld().getServer().getCommandManager().executeWithPrefix(player.getWorld().getServer().getCommandSource(), c.replace("%player%", player.getName().getString()));

        }
        String msg = BetterExchange.configManager.getConfigNode(0, "Special-Listings", listingName, "Message").getString();
        player.sendMessage(FancyTextHandler.getFormattedText(msg));
        BetterExchange.logger.info("Player " + player.getName().getString() + " used " + points + " points to redeem listing " + listingName);
        MainGUI.openMainGui(player);

    }

    public static void redeemPokemon (ServerPlayerEntity player, Pokemon pokemon, int points) {

        Points.setPoints(player.getUuid(), Points.getPoints(player.getUuid()) - points);
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        party.add(pokemon);
        player.sendMessage(FancyTextHandler.getFormattedText("&eYou used " + points + " points to redeem a " + pokemon.getSpecies().getName() + "!"));
        BetterExchange.logger.info("Player " + player.getName().getString() + " used " + points + " points to redeem a " + pokemon.getSpecies().getName());
        MainGUI.openMainGui(player);

    }

}
