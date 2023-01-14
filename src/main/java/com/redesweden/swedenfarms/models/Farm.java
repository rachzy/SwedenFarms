package com.redesweden.swedenfarms.models;

import org.bukkit.Location;

import java.math.BigDecimal;
import java.util.List;

public class Farm {
    private final FarmMeta meta;
    private int quantidadeLiquida;
    private final List<Location> locais;
    private BigDecimal quantidadeGerada;

    public Farm(FarmMeta meta, int quantidadeLiquida, List<Location> locais, BigDecimal quantidadeGerada)  {
        this.meta = meta;
        this.quantidadeLiquida = quantidadeLiquida;
        this.locais = locais;
        this.quantidadeGerada = quantidadeGerada;
    }

    public FarmMeta getMeta() {
        return meta;
    }

    public int getQuantidadeLiquida() {
        return quantidadeLiquida;
    }

    public BigDecimal getQuantidadeGerada() {
        return quantidadeGerada;
    }

    public void addQuantidadeLiquida(int quantidade) {
        setQuantidadeLiquida(this.quantidadeLiquida + quantidade);
    }

    public void setQuantidadeLiquida(int quantidadeLiquida) {
        this.quantidadeLiquida = quantidadeLiquida;
    }

    public void subQuantidadeLiquida(int quantidade) {
        setQuantidadeLiquida(this.quantidadeLiquida - quantidade);
    }

    public void addLocal(Location local) {
        locais.add(local);
    }

    public List<Location> getLocais() {
        return locais;
    }

    public void removerLocal(Location local) {
        locais.remove(local);
    }

    public void addQuantidadeGerada(BigDecimal quantidade) {
        setQuantidadeGerada(this.quantidadeGerada.add(quantidade));
    }

    public void setQuantidadeGerada(BigDecimal quantidade) {
        this.quantidadeGerada = quantidade;
    }

    public void subQuantidadeGerada(BigDecimal quantidade) {
        setQuantidadeGerada(this.quantidadeGerada.subtract(quantidade));
    }
}
