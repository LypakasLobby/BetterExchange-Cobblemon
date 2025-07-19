package com.lypaka.betterexchange.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
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

    public static void openExchangeMenu (ServerPlayerEntity player, int pageNum) throws ObjectMappingException {

        Map<String, Map<String, String>> buyMap = ConfigGetters.exchangeBuyMap;
        int slots = buyMap.size();
        if (slots > 243) {

            player.sendMessage(FancyTextHandler.getFormattedText("&cExceeding maximum slots...somehow!"));
            return;

        }

        // Properly handle more than 45 slots available for buying
        int[] utilBar = new int[]{45, 46, 47, 48, 49, 50, 51, 52, 53};

        ChestTemplate template = ChestTemplate.builder(6).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyTextHandler.getFormattedString("&eExchange Menu Page " + pageNum))
                .build();

        Map<Integer, Integer> miniMap = slotMap.get(pageNum);
        int max = 0;
        int min = 0;
        for (Map.Entry<Integer, Integer> entry : miniMap.entrySet()) {

            min = entry.getKey();
            max = entry.getValue();

        }
        int slotIndex = 0;
        int filledSlots = 0;
        for (int i = min; i < max; i++) {

            if (buyMap.containsKey("Slot-" + i)) {

                page.getTemplate().getSlot(slotIndex).setButton(getSlotButton(buyMap.get("Slot-" + i), player));
                filledSlots++;

            } else {

                page.getTemplate().getSlot(slotIndex).setButton(getAirButton());

            }

            slotIndex++;

        }

        for (int i : utilBar) {

            page.getTemplate().getSlot(i).setButton(getUtilButton(i, player));

        }

        // max of 243 slots (3 for each legendary)
        // max of like 5 and a half pages
        if (pageNum < 6) {

            if (slots > 45) {

                if (filledSlots == 45) {

                    int nextPage = pageNum + 1;
                    page.getTemplate().getSlot(53).setButton(getNextButton(nextPage));

                }

            }

        }

        if (pageNum > 1) {

            int prevPage = pageNum - 1;
            page.getTemplate().getSlot(45).setButton(getPrevButton(prevPage));

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getPrevButton (int pageNum) {

        ItemStack prev = ItemStackHandler.buildFromStringID("minecraft:arrow");
        prev.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&ePrevious Page"));
        return GooeyButton.builder()
                .display(prev)
                .onClick(action -> {

                    try {

                        openExchangeMenu(action.getPlayer(), pageNum);

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                })
                .build();

    }

    private static Button getNextButton (int pageNum) {

        ItemStack next = ItemStackHandler.buildFromStringID("minecraft:arrow");
        next.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&eNext Page"));
        return GooeyButton.builder()
                .display(next)
                .onClick(action -> {

                    try {

                        openExchangeMenu(action.getPlayer(), pageNum);

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                })
                .build();

    }

    // int[] utilBar = new int[]{45, 46, 47, 48, 49, 50, 51, 52, 53};
    private static Button getUtilButton (int slot, ServerPlayerEntity player) {

        Button button;
        ItemStack itemStack;
        if (slot == 45 || slot == 46 || slot == 48 || slot == 50 || slot == 52 || slot == 53) {

            itemStack = ItemStackHandler.buildFromStringID("minecraft:red_stained_glass_pane");
            itemStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(""));
            button = GooeyButton.builder()
                    .display(itemStack)
                    .build();

        } else if (slot == 47) {

            itemStack = ItemStackHandler.buildFromStringID("cobblemon:sachet");
            itemStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&eCurrent LP:&a " + Points.getPoints(player.getUuid())));
            button = GooeyButton.builder()
                    .display(itemStack)
                    .build();

        } else if (slot == 49) {

            itemStack = ItemStackHandler.buildFromStringID("minecraft:diamond");
            itemStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&eMain Menu"));
            button = GooeyButton.builder()
                    .display(itemStack)
                    .onClick(action -> {

                        MainGUI.openMainGui(action.getPlayer());

                    })
                    .build();

        } else {

            itemStack = ItemStackHandler.buildFromStringID("minecraft:barrier");
            itemStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&cClose"));
            button = GooeyButton.builder()
                    .display(itemStack)
                    .onClick(action -> {

                        UIManager.closeUI(action.getPlayer());

                    })
                    .build();

        }

        return button;

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
        if (pokemon != null) {

            if (data.containsKey("Form")) {

                String form = data.get("Form");
                if (!form.equalsIgnoreCase("default")) {

                    pokemon.setForm(pokemon.getSpecies().getFormByName(form));

                }

            }
            if (data.containsKey("Level")) {

                pokemon.setLevel(Integer.parseInt(data.get("Level")));

            }
            displayStack = PokemonItem.from(pokemon);
            displayStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&a" + pokemon.getSpecies().getName()));
            List<Text> lore = new ArrayList<>();
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
