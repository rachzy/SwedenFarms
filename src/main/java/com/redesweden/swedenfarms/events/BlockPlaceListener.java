package com.redesweden.swedenfarms.events;

import com.redesweden.swedenfarms.SwedenFarms;
import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.Farm;
import com.redesweden.swedenfarms.models.FarmMeta;
import com.redesweden.swedenfarms.models.PlayerFarms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        ItemStack itemNaMao = player.getItemInHand();

        if (itemNaMao.hasItemMeta() && itemNaMao.getItemMeta().hasDisplayName() && itemNaMao.getItemMeta().getDisplayName().startsWith("§2§lTORRE DE CACTOS")) {

            if(player.getLocation().getY() >= 200) {
                e.setCancelled(true);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§a§lFARMS §e>> §cVocê está muito alto para colocar uma torre de cactos.");
                return;
            }

            int altura = Integer.parseInt(Arrays.stream(e.getPlayer().getItemInHand().getItemMeta().getLore().get(1).split(":")).toArray()[1].toString().substring(3));
            Location centro = e.getBlockPlaced().getLocation().clone();

            Bukkit.getScheduler().runTaskLater(SwedenFarms.getPlugin(SwedenFarms.class), () -> {
                centro.getBlock().setType(Material.AIR);
            }, 1L);

            boolean placeAutorizado = true;

            AtomicInteger i = new AtomicInteger(0);
            PlayerFarms playerFarms = Players.getPlayerByNickname(player.getName());
            FarmMeta farmMeta = FarmMetas.getMetaPorId("CACTO");
            Farm farm = playerFarms.getFarmPorMeta(farmMeta);

            while (i.get() < altura * 3) {
                int finalI = i.get();
                List<Block> blocosBase = new ArrayList<>();

                Location bloco1 = centro.clone().add(1, finalI, 0);
                if (i.get() == 0 && bloco1.getBlock().getType() != Material.AIR) {
                    placeAutorizado = false;
                    break;
                }
                blocosBase.add(bloco1.getBlock());

                Location bloco2 = centro.clone().add(0, finalI, 1);
                if (i.get() == 0 && bloco2.getBlock().getType() != Material.AIR) {
                    placeAutorizado = false;
                    break;
                }
                blocosBase.add(bloco2.getBlock());

                Location bloco3 = centro.clone().add(0, finalI, -1);
                if (i.get() == 0 && bloco3.getBlock().getType() != Material.AIR) {
                    placeAutorizado = false;
                    break;
                }
                blocosBase.add(bloco3.getBlock());

                Location bloco4 = centro.clone().add(-1, finalI, 0);
                if (i.get() == 0 && bloco4.getBlock().getType() != Material.AIR) {
                    placeAutorizado = false;
                    break;
                }
                blocosBase.add(bloco4.getBlock());

                Bukkit.getScheduler().runTaskLater(SwedenFarms.getPlugin(SwedenFarms.class), () -> {
                    blocosBase.forEach((bloco) -> {
                        if (finalI == 0) {
                            bloco.setType(Material.SAND);
                            bloco.getLocation().clone().add(0, 1, 0).getBlock().setType(Material.CACTUS);
                            farm.addLocal(bloco.getLocation().clone().add(0, 1, 0));

                            if(centro.getBlock().getLocation().clone().add(0, finalI + 2, 0).getBlock().getType() != Material.SEA_LANTERN) {
                                centro.getBlock().getLocation().clone().add(0, finalI + 2, 0).getBlock().setType(Material.SEA_LANTERN);
                            }
                        } else {
                            bloco.getLocation().clone().add(0, 0.7, 0).getBlock().setType(Material.RED_SANDSTONE, false);
                            bloco.getLocation().clone().add(0, 1, 0).getBlock().setType(Material.SAND);
                            bloco.getLocation().clone().add(0, 2, 0).getBlock().setType(Material.CACTUS);

                            farm.addLocal(bloco.getLocation().clone().add(0, 2, 0));

                            System.out.println(bloco.getLocation());
                            System.out.println(bloco.getType());

                            if(centro.getBlock().getLocation().clone().add(0, finalI + 3, 0).getBlock().getType() != Material.SEA_LANTERN) {
                                centro.getBlock().getLocation().clone().add(0, finalI + 3, 0).getBlock().setType(Material.SEA_LANTERN);
                            }
                        }
                    });
                }, 1L);

                if (i.get() == 0) {
                    i.addAndGet(3);
                } else {
                    i.addAndGet(4);
                }
            }

            if (!placeAutorizado) {
                e.setCancelled(true);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§a§lFARMS §e>> §cVocê está muito próximo de outra construção.");
                return;
            }

            farm.addQuantidadeLiquida(altura * 4);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 2F);
            player.sendMessage(String.format("§a§lFARMS §e>> §aVocê colocou §e+%s §2Cactos §acom sua torre.", altura * 4));
            return;
        }

        FarmMeta farmMeta = FarmMetas.getFarmPorMaterial(e.getBlockPlaced().getType());

        if (farmMeta == null) return;

        if (e.getBlockPlaced().getLocation().clone().subtract(0, 1, 0).getBlock().getType() == e.getBlockPlaced().getType()) {
            return;
        }

        PlayerFarms playerFarms = Players.getPlayerByNickname(player.getName());
        playerFarms.getFarmPorMeta(farmMeta).addQuantidadeLiquida(1);
        playerFarms.getFarmPorMeta(farmMeta).addLocal(e.getBlock().getLocation());
        player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        player.sendMessage(String.format("§a§lFARMS §e>> §aVocê adicionou §e+1 %s §aà sua farm.", farmMeta.getTitulo()));
    }
}
