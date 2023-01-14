package com.redesweden.swedenfarms.GUIs;

import com.redesweden.swedeneconomia.data.Bolsa;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenfarms.data.FarmMetas;
import com.redesweden.swedenfarms.data.Players;
import com.redesweden.swedenfarms.models.Farm;
import com.redesweden.swedenfarms.models.FarmMeta;
import com.redesweden.swedenfarms.models.PlayerFarms;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ArmazemGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 45, "§2Armazém de Farms");

    public ArmazemGUI(String nickname) {
        PlayerFarms playerFarms = Players.getPlayerByNickname(nickname);
        PlayerSaldo playerSaldo = com.redesweden.swedeneconomia.data.Players.getPlayer(nickname);
        List<FarmMeta> farmMetas = FarmMetas.getMetas();
        
        farmMetas.forEach((meta) -> {
            int index = farmMetas.indexOf(meta);
            ItemStack farmItem = meta.getItem().clone();

            if(meta.getId().equals("FUNGOS")) {
                farmItem = new ItemStack(372, 1);
            }

            ItemMeta itemMeta = farmItem.getItemMeta();
            Farm farm = playerFarms.getFarmPorMeta(meta);

            BigDecimal valorNaBolsa = null;

            switch(meta.getId()) {
                case "CACTO":
                    valorNaBolsa = Bolsa.getPrecoPorCacto();
                    break;
                case "FUNGOS":
                    valorNaBolsa = Bolsa.getPrecoPorFungo();
                    break;
            }

            if(valorNaBolsa == null) return;

            itemMeta.setDisplayName(String.format("%ss", meta.getTitulo()));
            
            List<String> loreItem = new ArrayList<>();
            if(farm.getQuantidadeLiquida() > 0) {
                loreItem.add("");
                loreItem.add(" §e| §7Quantidade farmada: §e" + new ConverterQuantia(farm.getQuantidadeGerada()).emLetras());
                loreItem.add(" §2| §7Valor unitário na bolsa: §2$" + new ConverterQuantia(valorNaBolsa).emLetras());
                loreItem.add("");
                loreItem.add(" §a| §7Valor total: §a$" + new ConverterQuantia(valorNaBolsa.multiply(farm.getQuantidadeGerada())).emLetras());
                loreItem.add("");
                loreItem.add("§fClique §a§lESQUERDO §fpara vender tudo");
                loreItem.add("§fClique §a§lDIREITO §fpara coletar 64");
            } else {
                loreItem.add("§cVocê ainda não tem uma farm deste item.");
            }
            itemMeta.setLore(loreItem);

            farmItem.setItemMeta(itemMeta);

            inventario.setItem(index + 10, farmItem);
        });

        ItemStack playerHead = SkullCreator.itemFromName(nickname);
        ItemMeta playerMeta = playerHead.getItemMeta();
        playerMeta.setDisplayName("§eSeu perfil");

        List<String> lorePlayer = new ArrayList<>();
        lorePlayer.add("");
        lorePlayer.add(" §a| §7Saldo: §a$" + playerSaldo.getSaldo(true));
        lorePlayer.add("");
        playerMeta.setLore(lorePlayer);

        playerHead.setItemMeta(playerMeta);

        inventario.setItem(31, playerHead);
    }

    public Inventory get() {
        return inventario;
    }
}
