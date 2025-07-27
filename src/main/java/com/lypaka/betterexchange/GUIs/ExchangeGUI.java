package com.lypaka.betterexchange.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.betterexchange.ConfigGetters;
import com.lypaka.betterexchange.ExchangeHandlers.UsePointsForPrize;
import com.lypaka.betterexchange.PointHandlers.Points;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeGUI {

    public static Map<Integer, Map<Integer, Integer>> slotMap = new HashMap<>();

    public static void open (ServerPlayerEntity player) throws ObjectMappingException {

        PlaceholderButton placeholderButton = new PlaceholderButton();
        List<Button> buttons = new ArrayList<>();
        ItemStack border = ItemStackHandler.buildFromStringID("minecraft:red_stained_glass_pane");
        border.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(""));
        Map<String, Map<String, String>> buyMap = ConfigGetters.exchangeBuyMap;
        for (Map.Entry<String, Map<String, String>> entry : buyMap.entrySet()) {

            Map<String, String> entryData = entry.getValue();
            Button button = getSlotButton(entryData, player);
            buttons.add(button);

        }
        ItemStack sachet = ItemStackHandler.buildFromStringID("cobblemon:sachet");
        sachet.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&eCurrent LP:&a " + Points.getPoints(player.getUuid())));
        GooeyButton sachetButton = GooeyButton.builder()
                .display(sachet)
                .build();
        ItemStack diamond = ItemStackHandler.buildFromStringID("minecraft:diamond");
        diamond.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&eMain Menu"));
        GooeyButton diamondButton = GooeyButton.builder()
                .display(diamond)
                .onClick(action -> {

                    MainGUI.openMainGui(action.getPlayer());

                })
                .build();
        ItemStack close = ItemStackHandler.buildFromStringID("minecraft:barrier");
        close.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&cClose"));
        GooeyButton closeButton = GooeyButton.builder()
                .display(close)
                .onClick(action -> {

                    UIManager.closeUI(action.getPlayer());

                })
                .build();
        ChestTemplate template = ChestTemplate.builder(6)
                .rectangle(0, 0, 5, 9, placeholderButton)
                .fill(GooeyButton.builder().display(border).build())
                .set(45, CommonButtons.getPrev())
                .set(53, CommonButtons.getNext())
                .set(47, sachetButton)
                .set(49, diamondButton)
                .set(51, closeButton)
                .build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
        page.setTitle(FancyTextHandler.getFormattedText("&eExchange Menu"));
        setTitle(page);

        UIManager.openUIForcefully(player, page);

    }

    private static void setTitle (LinkedPage page) {

        LinkedPage next = page.getNext();
        if (next != null) {

            next.setTitle(FancyTextHandler.getFormattedText("&eExchange Menu"));
            setTitle(next);

        }

    }

    private static Button getSlotButton (Map<String, String> data, ServerPlayerEntity player) throws ObjectMappingException {

        String listing = data.get("Listing");
        ItemStack displayStack;
        int price = Integer.parseInt(data.get("Price"));
        Pokemon pokemon;// = PokemonSpecies.INSTANCE.getByName(pokemonFile.toLowerCase()).create(spawnLevel);
        try {

            pokemon = PokemonSpecies.INSTANCE.getByName(listing.toLowerCase()).create(1);

        } catch (Exception e) {

            pokemon = null;

        }
        String form = null;
        int level = 0;
        if (pokemon != null) {

            if (data.containsKey("Form")) {

                form = data.get("Form");
                if (!form.equalsIgnoreCase("default")) {

                    pokemon.setForm(pokemon.getSpecies().getFormByName(form));

                }

            }
            if (data.containsKey("Level")) {

                level = Integer.parseInt(data.get("Level"));
                pokemon.setLevel(level);

            }
            displayStack = PokemonItem.from(pokemon);
            displayStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&a" + pokemon.getSpecies().getName()));
            List<Text> lore = new ArrayList<>();
            if (form != null) {

                if (!form.equalsIgnoreCase("default")) {

                    lore.add(FancyTextHandler.getFormattedText("&eForm: &a" + form));

                }

            }
            if (level > 0) {

                lore.add(FancyTextHandler.getFormattedText("&eLevel: &a" + level));

            }
            lore.add(FancyTextHandler.getFormattedText(""));
            lore.add(FancyTextHandler.getFormattedText("&ePrice: &a" + price));
            displayStack.set(DataComponentTypes.LORE, new LoreComponent(lore));

        } else {

            displayStack = CustomListings.getCustomListingOrNull(listing, price);

        }

        Button button;
        if (displayStack != null) {

            if (Points.getPoints(player.getUuid()) >= price) {

                Pokemon finalPokemon = pokemon;
                button = GooeyButton.builder()
                        .display(displayStack)
                        .onClick(action -> {

                            if (finalPokemon != null) {

                                UsePointsForPrize.redeemPokemon(action.getPlayer(), finalPokemon, price);

                            } else {

                                try {

                                    UsePointsForPrize.redeemListing(action.getPlayer(), listing, price);

                                } catch (ObjectMappingException e) {

                                    e.printStackTrace();

                                }

                            }

                        })
                        .build();

            } else {

                button = GooeyButton.builder()
                        .display(displayStack)
                        .build();

            }

        } else {

            button = getAirButton();

        }

        return button;

    }

    private static Button getAirButton() {

        return GooeyButton.builder()
                .display(ItemStackHandler.buildFromStringID("minecraft:air"))
                .build();

    }

}
