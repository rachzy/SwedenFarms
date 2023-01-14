package com.redesweden.swedenfarms.data;

import com.redesweden.swedenfarms.models.FarmMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FarmMetas {
    private final static List<FarmMeta> metasList = new ArrayList<>();

    public static void addFarmMeta(FarmMeta farmMeta) {
        metasList.add(farmMeta);
    }

    public static List<FarmMeta> getMetas() {
        return metasList;
    }

    public static FarmMeta getMetaPorId(String id) {
        return metasList.stream().filter(meta -> meta.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static FarmMeta getFarmPorMaterial(Material material) {
        return metasList.stream().filter(meta -> meta.getItem().getType() == material).findFirst().orElse(null);
    }
}
