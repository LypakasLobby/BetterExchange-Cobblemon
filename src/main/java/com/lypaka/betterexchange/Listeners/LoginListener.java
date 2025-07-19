package com.lypaka.betterexchange.Listeners;

import com.lypaka.betterexchange.PointHandlers.Points;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class LoginListener implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady (ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {

        ServerPlayerEntity player = serverPlayNetworkHandler.player;
        Points.loadPlayer(player.getUuid());

    }

}
