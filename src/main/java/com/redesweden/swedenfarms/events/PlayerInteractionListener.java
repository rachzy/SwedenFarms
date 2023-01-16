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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class PlayerInteractionListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Player player = e.getPlayer();

        if(!player.getItemInHand().hasItemMeta() || !player.getItemInHand().getItemMeta().hasLore()) return;

        Material tipoItem = player.getItemInHand().getType();
        
        if(tipoItem == Material.getMaterial(372)) {
            tipoItem = Material.getMaterial(115);
        }
        
        FarmMeta farmMeta = FarmMetas.getFarmPorMaterial(tipoItem);
        if(farmMeta == null) return;

        PlayerFarms playerFarms = Players.getPlayerByNickname(player.getName());
        if(playerFarms.getFarmPorMeta(farmMeta).getQuantidadeLiquida() == 0) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§a§lFARMS §e>> §cVocê precisa ter pelo menos um item desta farm plantado para armazenar seus itens.");
            return;
        }
        
        int quantidadeArmazenada = 0;

        if(player.isSneaking()) {
            ItemStack[] inventarioPlayer = player.getInventory().getContents().clone();
            for(ItemStack item : inventarioPlayer) {
                if(item != null && item.isSimilar(player.getItemInHand())) {
                    quantidadeArmazenada += item.getAmount();
                    item.setType(Material.AIR);
                }
            }
            player.getInventory().setContents(inventarioPlayer);
        } else {
            quantidadeArmazenada = player.getItemInHand().getAmount();
            player.setItemInHand(new ItemStack(Material.AIR));
        }

        playerFarms.getFarmPorMeta(farmMeta).addQuantidadeGerada(BigDecimal.valueOf(quantidadeArmazenada));
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
        player.sendMessage(String.format("§a§lFARMS §e>> §aVocê armazenou §e+%s %s%s §aem seu armazém.", quantidadeArmazenada, farmMeta.getTitulo(), quantidadeArmazenada > 1 ? "s" : ""));
    }
}
