package com.redesweden.swedenfarms.events;

import com.redesweden.swedeneconomia.data.Bolsa;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenfarms.GUIs.ArmazemGUI;
import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.Farm;
import com.redesweden.swedenfarms.models.FarmMeta;
import com.redesweden.swedenfarms.models.PlayerFarms;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String tituloInv = e.getView().getTitle();
        String tituloItem = null;

        if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            tituloItem = e.getCurrentItem().getItemMeta().getDisplayName();
        }

        if(tituloInv.equals("§2Armazém de Farms")) {
            e.setCancelled(true);

            if(tituloItem == null) return;

            String finalTituloItem = tituloItem;
            FarmMeta farmMeta = FarmMetas.getMetas().stream().filter((meta) -> finalTituloItem.startsWith(meta.getTitulo())).findFirst().orElse(null);

            if(farmMeta == null) return;

            PlayerFarms playerFarms = Players.getPlayerByNickname(player.getName());
            Farm farm = playerFarms.getFarmPorMeta(farmMeta);
            BigDecimal valorNaBolsa = null;

            switch(farmMeta.getId()) {
                case "CACTO":
                    valorNaBolsa = Bolsa.getPrecoPorCacto();
                    break;
                case "FUNGOS":
                    valorNaBolsa = Bolsa.getPrecoPorFungo();
                    break;
            }

            if(valorNaBolsa == null) return;

            if(farm.getQuantidadeLiquida() <= 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§a§lFARMS §e>> §cVocê não possui nenhum item desta farm.");
                return;
            }

            if(farm.getQuantidadeGerada().compareTo(BigDecimal.ZERO) <= 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§a§lFARMS §e>> §cSua farm ainda não gerou itens.");
                return;
            }

            if(e.getClick().isLeftClick()) {
                Bolsa.venderCactos(player.getName(), farm.getQuantidadeGerada());

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.5F);
                player.sendMessage(String.format("§a§lFARMS §e>> §aVocê vendeu §e%s %ss §apor $§f%s", new ConverterQuantia(farm.getQuantidadeGerada()).emLetras(), farm.getMeta().getTitulo(), new ConverterQuantia(valorNaBolsa.multiply(farm.getQuantidadeGerada())).emLetras()));

                farm.setQuantidadeGerada(BigDecimal.ZERO);
                player.openInventory(new ArmazemGUI(player.getName()).get());
                return;
            }

            if(e.getClick().isRightClick()) {
                int quantidadeColetada = 64;
                if(farm.getQuantidadeGerada().compareTo(new BigDecimal("64")) < 0) {
                    quantidadeColetada = farm.getQuantidadeGerada().intValue();
                }

                ItemStack item = farmMeta.getItem().clone();

                if(farmMeta.getId().equals("FUNGOS")) {
                    item = new ItemStack(372, 1);
                }

                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(farmMeta.getTitulo());

                List<String> loreItem = new ArrayList<>();
                loreItem.add("§7Este item foi coletado de um armazém");
                loreItem.add("");
                loreItem.add("§7Clique §a§lESQUERDO §7para armazenar 64");
                loreItem.add("§7Shift + Clique §a§lESQUERDO §7para armazenar tudo");
                itemMeta.setLore(loreItem);

                item.setItemMeta(itemMeta);

                item.setAmount(quantidadeColetada);

                ItemStack[] inventarioPlayer = player.getInventory().getContents();
                HashMap<Integer, ItemStack> itensAdicionados = player.getInventory().addItem(item);

                if(itensAdicionados != null && itensAdicionados.values().toArray().length > 0) {
                    player.getInventory().setContents(inventarioPlayer);
                    player.sendMessage("§a§lFARMS §e>> §cSeu inventário está cheio.");
                    return;
                }

                player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
                player.sendMessage(String.format("§a§lFARMS §e>> §aVocê coletou §e%s %s%s§a.", quantidadeColetada, farmMeta.getTitulo(), quantidadeColetada == 1 ? "" : "s"));
                farm.subQuantidadeGerada(BigDecimal.valueOf(quantidadeColetada));
                player.openInventory(new ArmazemGUI(player.getName()).get());
                return;
            }
        }
    }
}
