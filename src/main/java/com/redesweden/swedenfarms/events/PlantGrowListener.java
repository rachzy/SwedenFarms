package com.redesweden.swedenfarms.events;

import com.redesweden.swedeneconomia.data.Bolsa;
import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.Farm;
import com.redesweden.swedenfarms.models.FarmMeta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class PlantGrowListener implements Listener {
    @EventHandler
    public void onPlantGrow(BlockGrowEvent e) {
        Location localDoBloco = e.getBlock().getLocation();

        if(e.getBlock().getType() == Material.AIR) {
            localDoBloco = e.getBlock().getLocation().clone().subtract(0, 1, 0);
        }

        Material tipo = localDoBloco.getBlock().getType();

        FarmMeta farmMeta = FarmMetas.getFarmPorMaterial(tipo);

        if(tipo == Material.NETHER_STALK) {
            FarmMetas.getMetaPorId("FUNGOS");
        }

        if(farmMeta == null) return;
        e.setCancelled(true);

        AtomicReference<Farm> farm = new AtomicReference<>(null);
        Location finalLocalDoBloco = localDoBloco;
        Players.getPlayers().forEach((player) -> {
            Player playerB = Bukkit.getServer().getPlayer(player.getNickname());
            if(playerB == null || !playerB.isOnline()) return;
            if(!player.getFarmPorMeta(farmMeta).getLocais().contains(finalLocalDoBloco)) return;
            farm.set(player.getFarmPorMeta(farmMeta));
        });

        if(farm.get() == null) return;
        farm.get().addQuantidadeGerada(BigDecimal.valueOf(farmMeta.getQuantidadePorGeracao()));
    }
}
