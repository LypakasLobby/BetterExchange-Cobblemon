package com.lypaka.betterexchange.ExchangeHandlers;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.betterexchange.GUIs.MainGUI;
import com.lypaka.betterexchange.PointHandlers.Points;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import net.minecraft.server.network.ServerPlayerEntity;

public class SellPokemonForPoints {

    public static void tradeIn (ServerPlayerEntity player, Pokemon pokemon, int points) throws ObjectMappingException {

        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (party.size() > 1) {

            party.remove(pokemon);
            Points.setPoints(player.getUuid(), Points.getPoints(player.getUuid()) + points);
            player.sendMessage(FancyTextHandler.getFormattedText("&4You traded in your " + pokemon.getSpecies().getName() + " for " + points + " Legendary Points!"));

        } else {

            player.sendMessage(FancyTextHandler.getFormattedText("&cYou can't trade in your last Pokemon!"));

        }

        MainGUI.openMainGui(player);

    }

}
