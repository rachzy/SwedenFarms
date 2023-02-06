package com.redesweden.swedenfarms.events;

import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedeneconomia.data.Bolsa;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenfarms.GUIs.ArmazemGUI;
import com.redesweden.swedenfarms.GUIs.ComprarTorresGUI;
import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.factories.TorreFactory;
import com.redesweden.swedenfarms.files.ConfigFile;
import com.redesweden.swedenfarms.models.Farm;
import com.redesweden.swedenfarms.models.FarmMeta;
import com.redesweden.swedenfarms.models.PlayerFarms;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

        if(tituloInv.equals("§8Armazém de Farms")) {
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

            if(farm.getQuantidadeGerada().compareTo(BigDecimal.ZERO) <= 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§a§lFARMS §e>> §cSua farm ainda não gerou itens.");
                return;
            }

            if(e.getClick().isLeftClick()) {
                String valorFinal = Bolsa.venderCactos(player.getName(), farm.getQuantidadeGerada());

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.5F);
                player.sendMessage(String.format("§a§lFARMS §e>> §aVocê vendeu §e%s %ss §apor $§f%s", new ConverterQuantia(farm.getQuantidadeGerada()).emLetras(), farm.getMeta().getTitulo(), valorFinal));

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

            if(e.getClick().isKeyboardClick()) {
                if(farm.getQuantidadeGerada().compareTo(ConfigFile.getQuantidadeNecessariaPorLimite()) < 0) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage(String.format("§a§lFARMS §e>> §cVocê precisa de pelo menos %s de itens gerados para trocar os itens cultivados por limite.", ConfigFile.getQuantidadeNecessariaPorLimite()));
                    return;
                }

                SpawnerPlayer playerSpawner = com.redesweden.swedenspawners.data.Players.getPlayerByName(player.getName());
                BigDecimal valorFinal = farm.getQuantidadeGerada().add(BigDecimal.valueOf(1)).divideToIntegralValue(ConfigFile.getQuantidadeNecessariaPorLimite());
                playerSpawner.addLimite(valorFinal);

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.5F);
                player.sendMessage(String.format("§a§lFARMS §e>> §aVocê vendeu §e%s %ss §apor §c✤%s", new ConverterQuantia(farm.getQuantidadeGerada()).emLetras(), farm.getMeta().getTitulo(), valorFinal));

                farm.setQuantidadeGerada(BigDecimal.ZERO);
                player.openInventory(new ArmazemGUI(player.getName()).get());
                return;
            }
        }
        
        if(tituloInv.equals("§8Comprar Torres")) {
            e.setCancelled(true);

            if(tituloItem == null) return;

            if(tituloItem.startsWith("§2§lTORRE")) {
                int altura = Integer.parseInt(Arrays.stream(e.getCurrentItem().getItemMeta().getLore().get(1).split(":")).toArray()[1].toString().substring(3));
                BigDecimal custo = ConfigFile.getCustoPorAlturaTorre().multiply(BigDecimal.valueOf(altura));

                PlayerCash playerCash = com.redesweden.swedencash.data.Players.getPlayerPorNickname(player.getName());
                if(playerCash.getCash().compareTo(custo) < 0) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F,0.5F);
                    player.sendMessage("§cVocê não possui CASH suficiente para comprar este produto.");
                    return;
                }

                ItemStack[] playerContents = player.getInventory().getContents().clone();
                HashMap<Integer, ItemStack> itemAdicionado = player.getInventory().addItem(new TorreFactory(altura).gerarItem());

                if(itemAdicionado != null && itemAdicionado.size() > 0) {
                    player.getInventory().setContents(playerContents);
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F,0.5F);
                    player.sendMessage("§cLibere um pouco de espaço em seu inventário para comprar este produto.");
                    return;
                }

                playerCash.subCash(custo);
                player.openInventory(new ComprarTorresGUI(player.getName()).get());
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
                player.sendMessage(String.format("§aVocê comprou uma §2Torre de Cactos §ade altura §e%s §apor §6✰%s§a.", altura, new ConverterQuantia(custo).emLetras()));
                return;
            }
        }
    }
}
