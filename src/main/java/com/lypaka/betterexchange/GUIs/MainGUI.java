package com.lypaka.betterexchange.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.betterexchange.ConfigGetters;
import com.lypaka.betterexchange.PointHandlers.Points;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class MainGUI {

    public static void openMainGui (ServerPlayerEntity player) {

        ChestTemplate template = ChestTemplate.builder(ConfigGetters.mainGUIRows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyTextHandler.getFormattedString(ConfigGetters.mainGUITitle))
                .build();

        int max = ConfigGetters.mainGUIRows * 9;
        for (int i = 0; i < max; i++) {

            page.getTemplate().getSlot(i).setButton(getButton(i, player));

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getButton (int slot, ServerPlayerEntity player) {

        GooeyButton button;
        if (slot == ConfigGetters.mainGUIPartyButtonSlot) {

            ItemStack item = ItemStackHandler.buildFromStringID(ConfigGetters.mainGUIPartyButtonID);
            item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.mainGUIPartyButtonName));
            button = GooeyButton.builder()
                    .display(item)
                    .onClick(action -> {

                        try {

                            PartyGUI partyMenu = new PartyGUI(action.getPlayer());
                            partyMenu.openMenu();

                        } catch (ObjectMappingException e) {

                            e.printStackTrace();

                        }

                    })
                    .build();

        } else if (slot == ConfigGetters.mainGUIPointsDisplayButtonSlot) {

            ItemStack item = ItemStackHandler.buildFromStringID(ConfigGetters.mainGUIPointsDisplayButtonID);
            item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.mainGUIPointsDisplayButtonName.replace("%points%", String.valueOf(Points.getPoints(player.getUuid())))));
            button = GooeyButton.builder()
                    .display(item)
                    .build();

        } else if (slot == ConfigGetters.mainGUIExchangeButtonSlot) {

            ItemStack item = ItemStackHandler.buildFromStringID(ConfigGetters.mainGUIExchangeButtonID);
            item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.mainGUIExchangeButtonName));
            button = GooeyButton.builder()
                    .display(item)
                    .onClick(action -> {

                        try {

                            ExchangeGUI.openExchangeMenu(action.getPlayer(), 1);

                        } catch (ObjectMappingException e) {

                            e.printStackTrace();

                        }

                    })
                    .build();

        } else {

            ItemStack glass = ItemStackHandler.buildFromStringID(ConfigGetters.mainGUIBorderID);
            glass.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(""));
            button = GooeyButton.builder()
                    .display(glass)
                    .build();

        }

        return button;

    }

}
