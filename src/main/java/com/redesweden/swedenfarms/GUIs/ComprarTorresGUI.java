package com.redesweden.swedenfarms.GUIs;

import com.redesweden.swedencash.data.Players;
import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenfarms.factories.TorreFactory;
import com.redesweden.swedenfarms.files.ConfigFile;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ComprarTorresGUI {
    public Inventory inventario = Bukkit.createInventory(null, 54, "§8Comprar Torres");

    public ComprarTorresGUI(String nickname) {
        PlayerCash playerCash = Players.getPlayerPorNickname(nickname);

        ItemStack vidroVerde = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
        ItemMeta vidroMeta = vidroVerde.getItemMeta();
        vidroMeta.setDisplayName("§2§lTORRES");
        vidroVerde.setItemMeta(vidroMeta);

        for (int i = 0; i < 54; i++) {
            if (i <= 9 || i >= 44 || i % 9 == 0 || (i + 1) % 9 == 0) {
                inventario.setItem(i, vidroVerde.clone());
            }
        }

        ItemStack playerHead = SkullCreator.itemFromName(nickname);
        ItemMeta playerMeta = playerHead.getItemMeta();
        playerMeta.setDisplayName("§eSeu Perfil");

        List<String> lorePlayer = new ArrayList<>();
        lorePlayer.add("");
        lorePlayer.add(" §6■ §fCash: §6✰" + new ConverterQuantia(playerCash.getCash()).emLetras());
        lorePlayer.add("");
        playerMeta.setLore(lorePlayer);

        playerHead.setItemMeta(playerMeta);
        inventario.setItem(4, playerHead);

        int index = 0;
        for(int i = 10; i <= 100; i += 10) {
            int posicao = index + 20;
            if(index > 4) {
                posicao = index + 24;
            }

            BigDecimal preco = ConfigFile.getCustoPorAlturaTorre().multiply(BigDecimal.valueOf(i));

            ItemStack torre = new TorreFactory(i).gerarItem();
            ItemMeta torreMeta = torre.getItemMeta();
            List<String> loreTorre = new ArrayList<>(torreMeta.getLore());
            loreTorre.add("");
            loreTorre.add("§7Preço: §6✰" + new ConverterQuantia(preco).emLetras());
            if(playerCash.getCash().compareTo(preco) < 0) {
                loreTorre.add("§cVocê não possui CASH suficiente");
            } else {
                loreTorre.add("§aClique para comprar");
            }
            torreMeta.setLore(loreTorre);

            torre.setItemMeta(torreMeta);
            inventario.setItem(posicao, torre);
            index++;
        }
    }

    public Inventory get() {
        return inventario;
    }
}
