package com.redesweden.swedenfarms.models;

import org.bukkit.inventory.ItemStack;

public class FarmMeta {
    private final String id;
    private final String titulo;
    private final ItemStack item;
    private final int quantidadePorGeracao;

    public FarmMeta(String id, String titulo, ItemStack item, int quantidadePorGeracao) {
        this.id = id;
        this.titulo = titulo;
        this.item = item;
        this.quantidadePorGeracao = quantidadePorGeracao;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getQuantidadePorGeracao() {
        return quantidadePorGeracao;
    }
}
