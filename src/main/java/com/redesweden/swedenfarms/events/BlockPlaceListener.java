package com.redesweden.swedenfarms.events;

import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.FarmMeta;
import com.redesweden.swedenfarms.models.PlayerFarms;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;

        FarmMeta farmMeta = FarmMetas.getFarmPorMaterial(e.getBlockPlaced().getType());

        if (farmMeta == null) return;

        if(e.getBlockPlaced().getLocation().clone().subtract(0, 1, 0).getBlock().getType() == e.getBlockPlaced().getType()) {
            return;
        }

        Player player = e.getPlayer();
        PlayerFarms playerFarms = Players.getPlayerByNickname(player.getName());
        playerFarms.getFarmPorMeta(farmMeta).addQuantidadeLiquida(1);
        playerFarms.getFarmPorMeta(farmMeta).addLocal(e.getBlock().getLocation());
        player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        player.sendMessage(String.format("§a§lFARMS §e>> §aVocê adicionou §e+1 %s §aà sua farm.", farmMeta.getTitulo()));
    }
}
