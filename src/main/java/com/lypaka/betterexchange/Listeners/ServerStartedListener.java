package com.lypaka.betterexchange.Listeners;

import com.lypaka.betterexchange.GUIs.ExchangeGUI;
import com.lypaka.betterexchange.PointHandlers.PointSaveTask;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;

public class ServerStartedListener implements ServerLifecycleEvents.ServerStarted {

    @Override
    public void onServerStarted (MinecraftServer minecraftServer) {

        Map<Integer, Integer> miniMap = new HashMap<>();
        miniMap.put(0, 45);
        ExchangeGUI.slotMap.put(1, miniMap);
        miniMap = new HashMap<>();
        miniMap.put(46, 91);
        ExchangeGUI.slotMap.put(2, miniMap);
        miniMap = new HashMap<>();
        miniMap.put(92, 137);
        ExchangeGUI.slotMap.put(3, miniMap);
        miniMap = new HashMap<>();
        miniMap.put(138, 183);
        ExchangeGUI.slotMap.put(4, miniMap);
        miniMap = new HashMap<>();
        miniMap.put(184, 229);
        ExchangeGUI.slotMap.put(5, miniMap);
        miniMap = new HashMap<>();
        miniMap.put(230, 243);
        ExchangeGUI.slotMap.put(6, miniMap);

        PointSaveTask.startTimer();

    }

}
