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
import org.bukkit.event.block.BlockBreakEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(e.isCancelled()) return;

        FarmMeta farmMeta = FarmMetas.getFarmPorMaterial(e.getBlock().getType());
        if(farmMeta == null) return;

        Player player = e.getPlayer();
        PlayerFarms playerFarms = Players.getPlayerByNickname(player.getName());

        if(!playerFarms.getFarmPorMeta(farmMeta).getLocais().contains(e.getBlock().getLocation())) {
            AtomicBoolean itemJaEmOutraFarm = new AtomicBoolean(false);
            Players.getPlayers().forEach(playerIn -> {
                if(!playerIn.getFarmPorMeta(farmMeta).getLocais().contains(e.getBlock().getLocation())) return;
                itemJaEmOutraFarm.set(true);
            });

            if(itemJaEmOutraFarm.get()) {
                e.setCancelled(true);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§a§lFARMS §e>> §cEsta farm pertence à outro jogador.");
            }
            return;
        }

        playerFarms.getFarmPorMeta(farmMeta).addQuantidadeLiquida(1);
        playerFarms.getFarmPorMeta(farmMeta).removerLocal(e.getBlock().getLocation());

        player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        player.sendMessage(String.format("§a§lFARMS §e>> §cVocê removeu §e1 %s §cda sua farm.", farmMeta.getTitulo()));
    }
}
