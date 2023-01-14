package com.redesweden.swedenfarms;

import com.redesweden.swedenfarms.commands.ArmazemCommand;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.events.*;
import com.redesweden.swedenfarms.files.ConfigFile;
import com.redesweden.swedenfarms.files.PlayersFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class SwedenFarms extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Ativando SwedenFarms...");

        getConfig().options().copyDefaults(true);
        saveConfig();

        // Inicializar arquivos
        ConfigFile.setup();

        PlayersFile.setup();
        PlayersFile.get().options().copyDefaults(true);
        PlayersFile.save();

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlantGrowListener(), this);

        // Registrar comandos
        getCommand("armazem").setExecutor(new ArmazemCommand());
    }

    @Override
    public void onDisable() {
        Players.salvarPlayersModificados();
        System.out.println("Desativando SwedenFarms...");
    }
}
