package com.redesweden.swedenfarms.models;

import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.files.PlayersFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlayerFarms {
    private final String uuid;
    private final String nickname;
    private final List<Farm> farms;

    public PlayerFarms(String uuid, String nickname, List<Farm> farms) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.farms = farms;
    }

    public void salvarDados() {
        farms.forEach((farm) -> {
            PlayersFile.get().set(String.format("players.%s.farms.%s.quantidadeGerada", uuid, farm.getMeta().getId()), farm.getQuantidadeGerada().toString());
            List<String> locais = new ArrayList<>();

            farm.getLocais().forEach((local) -> {
                locais.add(String.format("%s,%s,%s,%s", local.getWorld().getName(), Math.floor(local.getX()), Math.floor(local.getY()), Math.floor(local.getZ())));
            });
            PlayersFile.get().set(String.format("players.%s.farms.%s.locais", uuid, farm.getMeta().getId()), locais);
        });
    }

    public String getUuid() {
        return uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Farm> getFarms() {
        return farms;
    }

    public Farm getFarmPorMeta(FarmMeta meta) {
        Players.addPlayerModificado(this);
        Farm playerFarm = farms.stream().filter(farm -> farm.getMeta() == meta).findFirst().orElse(null);

        if(playerFarm == null) {
            Farm novaFarm = new Farm(meta, 0, new ArrayList<>(), BigDecimal.ZERO);
            farms.add(novaFarm);
            return novaFarm;
        }
        return playerFarm;
    }
}
