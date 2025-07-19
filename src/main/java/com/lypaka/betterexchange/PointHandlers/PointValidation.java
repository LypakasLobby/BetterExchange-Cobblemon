package com.lypaka.betterexchange.PointHandlers;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.betterexchange.ConfigGetters;
import com.lypaka.betterexchange.GUIs.PartyGUI;

import java.util.Map;

public class PointValidation {

    public static int getPointWorth (Pokemon pokemon) {

        int points = 0;
        if (ConfigGetters.exchangeSellMap.containsKey(pokemon.getSpecies().getName())) {

            Map<String, String> priceMap = ConfigGetters.exchangeSellMap.get(pokemon.getSpecies().getName());
            for (Map.Entry<String, String> entry : priceMap.entrySet()) {

                String key = entry.getKey();
                String value = entry.getValue();

                if (key.equalsIgnoreCase("default")) {

                    if (!value.equalsIgnoreCase("ivs")) {

                        points = Integer.parseInt(value);

                    } else {

                        points = getPointsByIVs(pokemon);

                    }
                    break;

                }

                if (key.contains("form-")) {

                    String form = key.replace("form-", "");
                    if (pokemon.getForm().getName().equalsIgnoreCase(form)) {

                        if (!value.equalsIgnoreCase("ivs")) {

                            points = Integer.parseInt(value);

                        } else {

                            points = getPointsByIVs(pokemon);

                        }
                        break;

                    }

                } else if (key.equalsIgnoreCase("shiny")) {

                    if (pokemon.getShiny()) {

                        if (!value.equalsIgnoreCase("ivs")) {

                            points = Integer.parseInt(value);

                        } else {

                            points = getPointsByIVs(pokemon);

                        }
                        break;

                    }


                }

            }

        } else if (ConfigGetters.exchangeSellMap.containsKey("Legendary") && PartyGUI.isLegendary(pokemon)) {

            points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("Legendary").get("default"));
            if (pokemon.getShiny()) {

                if (ConfigGetters.exchangeSellMap.containsKey("ShinyLegendary")) {

                    points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("ShinyLegendary").get("default"));

                }

            }

        } else if (ConfigGetters.exchangeSellMap.containsKey("Mythical") && PartyGUI.isMythical(pokemon)) {

            points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("Mythical").get("default"));
            if (pokemon.getShiny()) {

                if (ConfigGetters.exchangeSellMap.containsKey("ShinyMythical")) {

                    points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("ShinyMythical").get("default"));

                }

            }

        } else if (ConfigGetters.exchangeSellMap.containsKey("UltraBeast") && PartyGUI.isUltraBeast(pokemon)) {

            points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("UltraBeast").get("default"));
            if (pokemon.getShiny()) {

                if (ConfigGetters.exchangeSellMap.containsKey("ShinyUltraBeast")) {

                    points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("ShinyUltraBeast").get("default"));

                }

            }

        } else if (ConfigGetters.exchangeSellMap.containsKey("Paradox") && PartyGUI.isParadox(pokemon)) {

            points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("Paradox").get("default"));
            if (pokemon.getShiny()) {

                if (ConfigGetters.exchangeSellMap.containsKey("ShinyParadox")) {

                    points = Integer.parseInt(ConfigGetters.exchangeSellMap.get("ShinyParadox").get("default"));

                }

            }

        } else {

            points = getPointsByIVs(pokemon);

        }

        return points;

    }

    private static int getPointsByIVs (Pokemon pokemon) {

        int hp = pokemon.getIvs().get(Stats.HP);
        int atk = pokemon.getIvs().get(Stats.ATTACK);
        int def = pokemon.getIvs().get(Stats.DEFENCE);
        int sAtk = pokemon.getIvs().get(Stats.SPECIAL_ATTACK);
        int sDef = pokemon.getIvs().get(Stats.SPECIAL_DEFENCE);
        int spd = pokemon.getIvs().get(Stats.SPEED);

        int total = hp + atk + def + sAtk + sDef + spd;

        double percent = ((float) total / 186) * 100;
        return (int) percent;

    }

}
