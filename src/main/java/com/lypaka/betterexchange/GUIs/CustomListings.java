package com.lypaka.betterexchange.GUIs;

import com.lypaka.betterexchange.BetterExchange;
import com.lypaka.betterexchange.ConfigGetters;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomListings {

    public static ItemStack getCustomListingOrNull (String name, int price) throws ObjectMappingException {

        ItemStack display = null;
        Map<String, Map<String, String>> customMap = ConfigGetters.customListingsMap;
        if (customMap.containsKey(name)) {

            String id = BetterExchange.configManager.getConfigNode(0, "Special-Listings", name, "Display", "ID").getString();
            display = ItemStackHandler.buildFromStringID(id);
            if (!BetterExchange.configManager.getConfigNode(0, "Special-Listings", name, "Display", "Metadata").isVirtual()) {

                int meta = BetterExchange.configManager.getConfigNode(0, "Special-Listings", name, "Display", "Metadata").getInt();
                display.setDamage(meta);

            }
            List<Text> lore = new ArrayList<>();
            lore.add(FancyTextHandler.getFormattedText(""));
            lore.add(FancyTextHandler.getFormattedText("&ePrice: &a" + price));
            lore.add(FancyTextHandler.getFormattedText(""));
            List<String> stringLore = BetterExchange.configManager.getConfigNode(0, "Special-Listings", name, "Display", "Lore").getList(TypeToken.of(String.class));
            for (String s : stringLore) {

                lore.add(FancyTextHandler.getFormattedText(s));

            }

            display.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(BetterExchange.configManager.getConfigNode(0, "Special-Listings", name, "Display", "Name").getString()));
            display.set(DataComponentTypes.LORE, new LoreComponent(lore));

        }

        return display;

    }

}
