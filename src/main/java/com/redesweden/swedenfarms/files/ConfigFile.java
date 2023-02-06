package com.redesweden.swedenfarms.files;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.FarmMeta;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigDecimal;

public class ConfigFile {
    private static File file;
    private static FileConfiguration configFile;
    private static BigDecimal quantidadeNecessariaPorLimite;
    private static BigDecimal custoPorAlturaTorre;

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

        lerDados();
    }

    public static void lerDados() {
        FarmMetas.getMetas().clear();
        configFile = YamlConfiguration.loadConfiguration(file);

        try {
            quantidadeNecessariaPorLimite = new ConverterQuantia(configFile.getString("quantidadeNecessariaPorLimite")).emNumeros();
            custoPorAlturaTorre = new ConverterQuantia(configFile.getString("precoPorAltura")).emNumeros();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Valor inválido inserido na config.yml!");
        }

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
    }

    public static BigDecimal getQuantidadeNecessariaPorLimite() {
        return quantidadeNecessariaPorLimite;
    }

    public static BigDecimal getCustoPorAlturaTorre() {
        return custoPorAlturaTorre;
    }
}
