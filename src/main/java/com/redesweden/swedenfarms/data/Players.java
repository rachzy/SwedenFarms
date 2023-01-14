package com.redesweden.swedenfarms.data;

import com.redesweden.swedenfarms.SwedenFarms;
import com.redesweden.swedenfarms.files.PlayersFile;
import com.redesweden.swedenfarms.models.PlayerFarms;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Players {
    private static final List<PlayerFarms> playersList = new ArrayList<>();
    private static final Map<PlayerFarms, PlayerFarms> playersModificados = new HashMap<>();

    public static void addPlayer(PlayerFarms player) {
        playersList.add(player);
    }

    public static List<PlayerFarms> getPlayers() {
        return playersList;
    }

    public static PlayerFarms getPlayerByUUID(String UUID) {
        return playersList.stream().filter(player -> player.getUuid().equals(UUID)).findFirst().orElse(null);
    }

    public static PlayerFarms getPlayerByNickname(String nickname) {
        return playersList.stream().filter(player -> player.getNickname().equals(nickname)).findFirst().orElse(null);
    }

    public static void addPlayerModificado(PlayerFarms player) {
        playersModificados.put(player, player);
    }

    public static void salvarPlayersModificados() {
        System.out.println("[SwedenFarms] Salvando players modificados");
        playersModificados.values().forEach(PlayerFarms::salvarDados);
        PlayersFile.save();
    }

    public static void iniciarSalvamentoAutomatico() {
        salvarPlayersModificados();
        Bukkit.getScheduler().runTaskLater(SwedenFarms.getPlugin(SwedenFarms.class), Players::iniciarSalvamentoAutomatico, 20L * 1800L);
    }
}
