package com.redesweden.swedenfarms.events;

import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.files.PlayersFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if(Players.getPlayerByNickname(player.getName()) != null) return;
        PlayersFile.addNewPlayer(player);
    }
}
