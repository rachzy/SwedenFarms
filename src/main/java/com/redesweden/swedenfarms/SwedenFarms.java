package com.redesweden.swedenfarms;

import com.redesweden.swedenfarms.commands.FarmsCommand;
import com.redesweden.swedenfarms.commands.DarTorreCommand;
import com.redesweden.swedenfarms.commands.TorresCommand;
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
        getServer().getPluginManager().registerEvents(new PlayerInteractionListener(), this);

        // Registrar comandos
        getCommand("farms").setExecutor(new FarmsCommand());
        getCommand("dartorre").setExecutor(new DarTorreCommand());
        getCommand("torres").setExecutor(new TorresCommand());
    }

    @Override
    public void onDisable() {
        Players.salvarPlayersModificados();
        System.out.println("Desativando SwedenFarms...");
    }
}
