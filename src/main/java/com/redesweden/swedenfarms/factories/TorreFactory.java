package com.redesweden.swedenfarms.factories;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TorreFactory {
    private final int altura;

    public TorreFactory(int altura) {
        this.altura = altura;
    }

    public ItemStack gerarItem() {
        ItemStack torreHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFmMTEzYjkyMGQ0ZWIyMWUyYjQwYTU1MjQ1OGIzZmIxMDQzMzNiMjAyZDY5MDdmMWE5OWMzZTZkMDlmMzNkMiJ9fX0=");
        ItemMeta torreMeta = torreHead.getItemMeta();
        torreMeta.setDisplayName(String.format("§2§lTORRE DE CACTOS §7(§b%s§7)", altura));

        List<String> loreTorre = new ArrayList<>();
        loreTorre.add("");
        loreTorre.add(" §a| §7Altura: §a" + altura);
        loreTorre.add("");
        loreTorre.add("§7Clique com este item em sua plot e erga");
        loreTorre.add("§7uma torre de cactos automaticamente");
        torreMeta.setLore(loreTorre);

        torreHead.setItemMeta(torreMeta);
        return torreHead;
    }
}
