package com.redesweden.swedenfarms.commands;

import com.redesweden.swedenfarms.GUIs.ArmazemGUI;
import com.redesweden.swedenfarms.files.PlayersFile;
import com.redesweden.swedenminas.data.Players;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArmazemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 || !args[0].equals("reload")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("§a§lFARMS §e>> §cApenas players podem executar este comando.");
                return true;
            }

            Player player = (Player) sender;
            if(Players.getPlayerPorNickname(player.getName()) != null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§a§lFARMS §e>> §cVocê não acessar seu armazém enquanto está minerando.");
                return true;
            }

            player.openInventory(new ArmazemGUI(player.getName()).get());
            return true;
        }

        if(!sender.hasPermission("swedenfarms.admin")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§a§lFARMS §e>> §cVocê não tem permissão para executar este comando.");
            return true;
        }

        PlayersFile.lerDados();
        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
        }
        sender.sendMessage("§a§lFARMS §e>> §aDados recarregados com sucesso!");
        return true;
    }
}
