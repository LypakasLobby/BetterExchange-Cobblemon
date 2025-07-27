package com.lypaka.betterexchange.GUIs;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

public class CommonButtons {

    public static Button getNext() {

        ItemStack item = ItemStackHandler.buildFromStringID("minecraft:arrow");
        item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&eNext Page"));
        return LinkedPageButton.builder()
                .linkType(LinkType.Next)
                .display(item)
                .build();

    }

    public static Button getPrev() {

        ItemStack item = ItemStackHandler.buildFromStringID("minecraft:arrow");
        item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText("&ePrevious Page"));
        return LinkedPageButton.builder()
                .linkType(LinkType.Previous)
                .display(item)
                .build();

    }

}
