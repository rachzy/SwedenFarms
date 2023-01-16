package com.redesweden.swedenfarms.files;

import com.redesweden.swedenfarms.SwedenFarms;
import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.Farm;
import com.redesweden.swedenfarms.models.FarmMeta;
import com.redesweden.swedenfarms.models.PlayerFarms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlayersFile {
    private static File file;
    private static FileConfiguration playersFile;

    public static void setup() {
        file = new File(Bukkit.getPluginManager().getPlugin("SwedenFarms").getDataFolder(), "players.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo players.yml... " + e.getMessage());
                return;
            }
        }

        playersFile = YamlConfiguration.loadConfiguration(file);

        Bukkit.getScheduler().runTaskLater(SwedenFarms.getPlugin(SwedenFarms.class), () -> {
            lerDados();
            Players.iniciarSalvamentoAutomatico();
        }, 10L);
    }

    public static void lerDados() {
        playersFile = YamlConfiguration.loadConfiguration(file);
        if(playersFile.getConfigurationSection("players") != null) {
            for(String uuid : playersFile.getConfigurationSection("players").getKeys(false)) {
                String nickname = playersFile.getString(String.format("players.%s.nickname", uuid));

                List<Farm> farms = new ArrayList<>();

                if(playersFile.getConfigurationSection(String.format("players.%s.farms", uuid)) != null) {
                    for (String id : playersFile.getConfigurationSection(String.format("players.%s.farms", uuid)).getKeys(false)) {
                        BigDecimal quantidadeGerada = new BigDecimal(playersFile.getString(String.format("players.%s.farms.%s.quantidadeGerada", uuid, id)));

                        FarmMeta farmMeta = FarmMetas.getMetaPorId(id);

                        List<Location> locais = new ArrayList<>();
                        playersFile.getStringList(String.format("players.%s.farms.%s.locais", uuid, id)).forEach((local) -> {
                            String[] localSplit = local.split(",");
                            Location localFarm = new Location(Bukkit.getWorld(localSplit[0]), Double.parseDouble(localSplit[1]), Double.parseDouble(localSplit[2]), Double.parseDouble(localSplit[3]));

                            if(Bukkit.getWorld(localFarm.getWorld().getName()) == null) {
                                locais.add(localFarm);
                                return;
                            }

                            Bukkit.getWorld(localFarm.getWorld().getName()).loadChunk((int) localFarm.getX(), (int) localFarm.getY());

                            if(Bukkit.getWorld(localFarm.getWorld().getName()) == null || localFarm.getBlock() != null && localFarm.getBlock().getType() == farmMeta.getItem().getType()) {
                                locais.add(localFarm);
                            }
                        });


                        Farm farm = new Farm(farmMeta, locais.toArray().length, locais, quantidadeGerada);
                        farms.add(farm);
                    }
                }

                PlayerFarms playerFarms = new PlayerFarms(uuid, nickname, farms);
                Players.addPlayer(playerFarms);
            }
        }
    }

    public static FileConfiguration get() {
        return playersFile;
    }

    public static void addNewPlayer(Player player) {
        System.out.println("[SwedenFarms] Registrando jogador " + player.getName());
        String uuid = player.getUniqueId().toString();

        playersFile.set(String.format("players.%s.nickname", uuid), player.getName());

        PlayerFarms playerFarms = new PlayerFarms(uuid, player.getName(), new ArrayList<>());
        Players.addPlayer(playerFarms);

        save();
    }

    public static void save() {
        try {
            playersFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo players.yml..." + e.getMessage());
        }
    }
}
