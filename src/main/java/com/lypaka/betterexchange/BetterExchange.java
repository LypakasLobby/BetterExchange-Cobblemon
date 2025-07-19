package com.lypaka.betterexchange;

import com.lypaka.betterexchange.Commands.BetterExchangeCommand;
import com.lypaka.betterexchange.Listeners.LoginListener;
import com.lypaka.betterexchange.Listeners.ServerShuttingDownListener;
import com.lypaka.betterexchange.Listeners.ServerStartedListener;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class BetterExchange implements ModInitializer {

    public static final String MOD_ID = "betterexchange";
    public static final String MOD_NAME = "BetterExchange";
    public static Logger logger = LogManager.getLogger(MOD_NAME);
    public static BasicConfigManager configManager;
    public static List<String> legendaries = Arrays.asList("Moltres", "Articuno", "Zapdos", "Mewtwo", "Suicune", "Entei", "Raikou", "Lugia", "Ho-Oh", "Regirock", "Regice", "Registeel",
            "Latios", "Latias", "Kyogre", "Groudon", "Rayquaza", "Uxie", "Azelf", "Mesprit", "Dialga", "Palkia", "Giratina", "Heatran", "Regigigas", "Cresslia",
            "Cobalion", "Terrakion", "Virizon", "Tornadus", "Thundurus", "Landorus", "Reshiram", "Zekrom", "Kyurem", "Xerneas", "Yveltal", "Zygarde", "TypeNull", "Silvally",
            "Tapu Koko", "TapuKoko", "Tapu Lele", "TapuLele", "Tapu Bulu", "TapuBulu", "Tapu Fini", "TapuFini", "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Necrozma",
            "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Regieleki", "Regidrago", "Calyrex", "Glastrier", "Spectrier", "Enamorus", "Wo-Chien", "WoChien",
            "Chien-Pao", "ChienPao", "Ting-Lu", "TingLu", "Chi-Yu", "ChiYu", "Koraidon", "Miraidon", "Okidogi", "Munkidori", "Fezandipiti", "Ogerpon", "Terapagos");
    public static List<String> mythicals = Arrays.asList("Mew", "Celebi", "Jirachi", "Deoxys", "Manaphy", "Phione", "Darkrai", "Shaymin", "Arceus", "Victini", "Keldeo",
            "Meloetta", "Genesect", "Diancie", "Hoopa", "Volcanion", "Magearna", "Marshadow", "Zeraora", "Meltan", "Melmetal"); // yes, Meltan is a Mythical, Lotus, I'm sorry
    public static List<String> ultraBeasts = Arrays.asList("Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana", "Guzzlord", "Poipole", "Naganadel",
            "Stakataka", "Blacephalon");
    public static List<String> paradoxes = Arrays.asList("Great Tusk", "GreatTusk", "Scream Tail", "ScreamTail", "Brute Bonnet", "BruteBonnet", "Flutter Mane", "FlutterMane",
            "Slither Wing", "SlitherWing", "Sandy Shocks", "SandyShocks", "Roaring Moon", "RoaringMoon", "Koraidon", "Walking Wake", "WalkingWake", "Gouging Fire", "GougingFire",
            "Raging Bolt", "RagingBolt", "Iron Treads", "IronTreads", "Iron Bundle", "IronBundle", "Iron Hands", "IronHands", "Iron Jugulis", "IronJugulis", "Iron Moth", "IronMoth",
            "Iron Thorns", "IronThorns", "Iron Valiant", "IronValiant", "Miraidon", "Iron Leaves", "IronLeaves", "Iron Boulder", "IronBoulder", "Iron Crown", "IronCrown");

    @Override
    public void onInitialize() {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/betterexchange"));
        String[] files = new String[]{"betterexchange.conf", "points.conf", "blacklist.conf", "mainGUI.conf", "partyGUI.conf"};
        configManager = new BasicConfigManager(files, dir, BetterExchange.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        try {

            ConfigGetters.load(false);

        } catch (ObjectMappingException e) {

            throw new RuntimeException(e);

        }

        BetterExchangeCommand.register();

        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedListener());
        ServerLifecycleEvents.SERVER_STOPPING.register(new ServerShuttingDownListener());
        ServerPlayConnectionEvents.JOIN.register(new LoginListener());

    }

}
