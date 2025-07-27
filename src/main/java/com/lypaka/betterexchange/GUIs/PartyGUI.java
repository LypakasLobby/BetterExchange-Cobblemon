package com.lypaka.betterexchange.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.betterexchange.BetterExchange;
import com.lypaka.betterexchange.ConfigGetters;
import com.lypaka.betterexchange.ExchangeHandlers.SellPokemonForPoints;
import com.lypaka.betterexchange.PointHandlers.PointValidation;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PartyGUI {

    private final ServerPlayerEntity player;

    public PartyGUI (ServerPlayerEntity player) {

        this.player = player;

    }

    public void openMenu() throws ObjectMappingException {

        ChestTemplate template = ChestTemplate.builder(ConfigGetters.partyGUIRows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyTextHandler.getFormattedString(ConfigGetters.partyGUITitle))
                .build();

        int max = ConfigGetters.partyGUIRows * 9;
        String[] borderSlots = ConfigGetters.partyGUIBorderSlots.split(", ");
        for (String b : borderSlots) {

            page.getTemplate().getSlot(Integer.parseInt(b)).setButton(getBorder());

        }
        page.getTemplate().getSlot(ConfigGetters.partyGUIMainMenuButtonSlot).setButton(getMainMenu());
        PlayerPartyStore storage = Cobblemon.INSTANCE.getStorage().getParty(player);
        for (int i = 0; i < 6; i++) {

            Pokemon pokemon = storage.get(i);
            int slot = Integer.parseInt(ConfigGetters.partyGUIPartySlotsMap.get("Party-Slot-" + i).get("Slot"));
            boolean passes = passes(pokemon);
            String displayName = passes ?
                    FancyTextHandler.getFormattedString(ConfigGetters.partyGUIPartySlotsMap.get("Party-Slot-" + i).get("Display-Name")
                            .replace("%pokemon%", pokemon.getSpecies().getName())) :
                    FancyTextHandler.getFormattedString(ConfigGetters.partyGUINoPokemonButtonName);
            ItemStack item = passes ? PokemonItem.from(pokemon) : ItemStackHandler.buildFromStringID(ConfigGetters.partyGUINoPokemonButtonID);
            item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(displayName));
            Button pokeButton;
            if (passes) {

                int points = PointValidation.getPointWorth(pokemon);
                List<String> displayLore = ConfigGetters.partyGUISpriteLore;
                List<Text> lore = new ArrayList<>();
                for (String l : displayLore) {

                    lore.add(FancyTextHandler.getFormattedText(l.replace("%points%", String.valueOf(points))));

                }
                item.set(DataComponentTypes.LORE, new LoreComponent(lore));
                pokeButton = GooeyButton.builder()
                        .display(item)
                        .onClick(click -> {

                            try {

                                SellPokemonForPoints.tradeIn(this.player, pokemon, points);

                            } catch (ObjectMappingException e) {

                                e.printStackTrace();

                            }

                        })
                        .build();

            } else {

                pokeButton = GooeyButton.builder()
                        .display(item)
                        .build();

            }
            page.getTemplate().getSlot(slot).setButton(pokeButton);

        }

        UIManager.openUIForcefully(this.player, page);

    }

    private boolean passes (Pokemon pokemon) {

        if (pokemon == null) {

            return false;

        } else {

            boolean passes = ConfigGetters.blacklist.isEmpty();
            for (String s : ConfigGetters.blacklist) {

                if (s.equalsIgnoreCase("!legendary")) {

                    if (isLegendary(pokemon)) {

                        passes = true;

                    }

                }
                if (!passes) {

                    if (s.equalsIgnoreCase("legendary")) {

                        if (!isLegendary(pokemon)) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.equalsIgnoreCase("!mythical")) {

                        if (isMythical(pokemon)) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.equalsIgnoreCase("mythical")) {

                        if (!isMythical(pokemon)) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.equalsIgnoreCase("!ultrabeast")) {

                        if (isUltraBeast(pokemon)) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.equalsIgnoreCase("ultrabeast")) {

                        if (!isUltraBeast(pokemon)) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.equalsIgnoreCase("!paradox")) {

                        if (isParadox(pokemon)) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.equalsIgnoreCase("paradox")) {

                        if (!isParadox(pokemon)) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.contains("!generation")) {

                        int generation = Integer.parseInt(s.replace("!generation", ""));
                        int pokemonGeneration = getGenerationFromDexBecauseCobblemonJustDoesEverythingToMakeMyLifeFuckingDifficult(pokemon.getForm().getSpecies().getNationalPokedexNumber());
                        if (generation == pokemonGeneration) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.contains("generation")) {

                        int generation = Integer.parseInt(s.replace("generation", ""));
                        int pokemonGeneration = getGenerationFromDexBecauseCobblemonJustDoesEverythingToMakeMyLifeFuckingDifficult(pokemon.getForm().getSpecies().getNationalPokedexNumber());
                        if (generation != pokemonGeneration) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.contains("!shiny")) {

                        if (pokemon.getShiny()) {

                            passes = true;

                        }

                    }

                }
                if (!passes) {

                    if (s.contains("shiny")) {

                        if (!pokemon.getShiny()) {

                            passes = true;

                        }

                    }

                }

                if (s.equalsIgnoreCase(pokemon.getSpecies().getName())) {

                    return false;

                }

            }

            return passes;

        }

    }

    private Button getMainMenu() {

        ItemStack diamond = ItemStackHandler.buildFromStringID(ConfigGetters.partyGUIMainMenuButtonID);
        diamond.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.partyGUIMainMenuButtonName));
        return GooeyButton.builder()
                .display(diamond)
                .onClick(click -> {

                    MainGUI.openMainGui(click.getPlayer());

                })
                .build();

    }

    private Button getBorder() {

        ItemStack glass = ItemStackHandler.buildFromStringID(ConfigGetters.partyGUIBorderID);
        glass.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(""));
        return GooeyButton.builder()
                .display(glass)
                .build();

    }

    private static int getGenerationFromDexBecauseCobblemonJustDoesEverythingToMakeMyLifeFuckingDifficult (int dex) {

        int gen = 1;
        if (dex >= 152 && dex <= 251) gen = 2;
        if (dex >= 252 && dex <= 386) gen = 3;
        if (dex >= 387 && dex <= 493) gen = 4;
        if (dex >= 494 && dex <= 649) gen = 5;
        if (dex >= 650 && dex <= 721) gen = 6;
        if (dex >= 722 && dex <= 809) gen = 7;
        if (dex >= 810 && dex <= 905) gen = 8;
        if (dex >= 906 && dex <= 1025) gen = 9;
        return gen;

    }

    public static boolean isLegendary (Pokemon pokemon) {

        boolean is = false;
        for (String s : BetterExchange.legendaries) {

            if (pokemon.getSpecies().getName().equalsIgnoreCase(s)) {

                is = true;
                break;

            }

        }

        return is;

    }

    public static boolean isMythical (Pokemon pokemon) {

        boolean is = false;
        for (String s : BetterExchange.mythicals) {

            if (pokemon.getSpecies().getName().equalsIgnoreCase(s)) {

                is = true;
                break;

            }

        }

        return is;

    }

    public static boolean isUltraBeast (Pokemon pokemon) {

        boolean is = false;
        for (String s : BetterExchange.ultraBeasts) {

            if (pokemon.getSpecies().getName().equalsIgnoreCase(s)) {

                is = true;
                break;

            }

        }

        return is;

    }

    public static boolean isParadox (Pokemon pokemon) {

        boolean is = false;
        for (String s : BetterExchange.paradoxes) {

            if (pokemon.getSpecies().getName().equalsIgnoreCase(s)) {

                is = true;
                break;

            }

        }

        return is;

    }

}
