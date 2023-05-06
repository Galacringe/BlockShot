package com.devgarlic.postype.BlockShot.commands;

import com.devgarlic.postype.BlockShot.Load.BlockWeapons;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class BlockShotCommands implements CommandExecutor {
    public static ArrayList<BlockWeapons.Data> prebuiltweapons;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) { // 요청자가 Player 상속이 아니야? = 콘솔이나 커맨드 블록, 플레이어 아닌 대상이 명령어 실행 시
            sender.sendMessage("only players can use That Command!");
            return true;
        }

        Player player = (Player) sender; // player = 요청자

        if (cmd.getName().equalsIgnoreCase("viewpersistantdatacontainer")) {
            Set<NamespacedKey> keys = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().getKeys();
            for (NamespacedKey k : keys) {
                player.sendMessage(k.toString());
            }
        }
        else if (cmd.getName().equalsIgnoreCase("getloadedweapons")) {
            for(BlockWeapons.Data data : prebuiltweapons){
                player.getInventory().addItem(data.item );
            }

        }


        return true;
    }

    private void sendConsoleMessage(String message){
        getServer().getConsoleSender().sendMessage(message + " - [BlockShot]");
    }

    public static void setPrebuiltweapons(ArrayList<BlockWeapons.Data> data){
        prebuiltweapons = data;
    }
}
