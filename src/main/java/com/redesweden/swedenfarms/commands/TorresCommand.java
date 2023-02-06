package com.redesweden.swedenfarms.commands;

import com.redesweden.swedenfarms.GUIs.ComprarTorresGUI;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TorresCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cApenas players podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
        player.openInventory(new ComprarTorresGUI(player.getName()).get());
        return true;
    }
}
