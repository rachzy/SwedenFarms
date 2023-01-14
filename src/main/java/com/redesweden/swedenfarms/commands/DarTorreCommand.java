package com.redesweden.swedenfarms.commands;

import com.redesweden.swedenfarms.factories.TorreFactory;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DarTorreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("swedenfarms.admin")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§a§lFARMS §e>> §cVocê não tem permissão para executar este comando.");
            return true;
        }

        if(args.length != 2) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§a§lFARMS §e>> §cUso: /dartorre <jogador> <altura>.");
            return true;
        }

        Player playerAlvo = Bukkit.getPlayer(args[0]);
        if(playerAlvo == null || !playerAlvo.isOnline()) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage(String.format("§a§lFARMS §e>> §cNão foi possível encontrar um jogador online com o nick '%s'.", args[0]));
            return true;
        }

        int altura;
        try {
            altura = Integer.parseInt(args[1]);
            if(altura < 1 || altura > 50) throw new Exception();
        } catch (Exception e) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§a§lFARMS §e>> §cO valor da altura precisa ser um número entre 1 à 50.");
            return true;
        }

        playerAlvo.getInventory().addItem(new TorreFactory(altura).gerarItem());
        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
        }
        sender.sendMessage(String.format("§a§lFARMS §e>> §aVocê deu uma Torre de Cactos de altura §e%s §apara o jogador §b%s", altura, playerAlvo.getName()));
        return true;
    }
}
