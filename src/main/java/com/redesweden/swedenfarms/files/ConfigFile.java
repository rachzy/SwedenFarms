package com.redesweden.swedenfarms.files;

import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.FarmMeta;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ConfigFile {
    private static File file;
    private static FileConfiguration configFile;

    public static void setup() {
        file = new File(Bukkit.getPluginManager().getPlugin("SwedenFarms").getDataFolder(), "config.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo config.yml... " + e.getMessage());
                return;
            }
        }

        configFile = YamlConfiguration.loadConfiguration(file);

        if(configFile.getConfigurationSection("farms") != null) {
            for(String id : configFile.getConfigurationSection("farms").getKeys(false)) {
                String titulo = configFile.getString(String.format("farms.%s.titulo", id));
                int materialID = configFile.getInt(String.format("farms.%s.materialID", id));
                int materialSubID = configFile.getInt(String.format("farms.%s.materialSubID", id));
                int quantidadePorGeracao = configFile.getInt(String.format("farms.%s.quantidadePorGeracao", id));

                ItemStack item = new ItemStack(materialID, 1, (short) materialSubID);

                FarmMeta farmMeta = new FarmMeta(id, titulo, item, quantidadePorGeracao);
                FarmMetas.addFarmMeta(farmMeta);
            }
        }
        save();
    }

    public static void save() {
        try {
            configFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo config.yml..." + e.getMessage());
        }
    }
}
