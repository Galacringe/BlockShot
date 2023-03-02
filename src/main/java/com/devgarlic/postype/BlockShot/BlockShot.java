package com.devgarlic.postype.BlockShot;
import com.devgarlic.postype.BlockShot.Load.LoadJsonFiles;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockShot extends JavaPlugin{
    @Override
    public void onEnable() {
        // ChatColor.GREEN = Normal message
        // ChatColor.BOLD = Normal message (2)
        // ChatColor.YELLOW = Debug message
        // ChatColor.RED = Error or something like that critical.

        sendConsoleMessage(ChatColor.GREEN + "Hello World :)");
        LoadJsonFiles.init();

    }

    @Override
    public void onDisable() {
        sendConsoleMessage(ChatColor.GREEN + "Goodbye Sengen :)");
    }

    public void sendConsoleMessage(String message){
        getServer().getConsoleSender().sendMessage(message + " - [BlockShot]");
    }
}
