package com.lypaka.betterexchange.Listeners;

import com.lypaka.betterexchange.BetterExchange;
import com.lypaka.betterexchange.ConfigGetters;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerShuttingDownListener implements ServerLifecycleEvents.ServerStopping {

    @Override
    public void onServerStopping (MinecraftServer minecraftServer) {

        BetterExchange.configManager.getConfigNode(1, "Points").setValue(ConfigGetters.pointMap);
        BetterExchange.configManager.save();

    }

}
