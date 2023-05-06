package com.devgarlic.postype.BlockShot;
import com.devgarlic.postype.BlockShot.Load.LoadJsonFiles;
import com.devgarlic.postype.BlockShot.commands.BlockShotCommands;
import com.devgarlic.postype.BlockShot.event.BlockFireEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockShot extends JavaPlugin{
    @Override
    public void onEnable() {
        // ChatColor.GREEN = Normal message
        // ChatColor.BOLD = Normal message (2)
        // ChatColor.YELLOW = Debug message
        // ChatColor.RED = Error or something like that critical.
        // ChatColor.GOLD = minor error
        sendConsoleMessage(ChatColor.GREEN + "Hello World :)");
        LoadJsonFiles.init();
        initEvents();

        BlockShotCommands commands = new BlockShotCommands();
        getCommand("viewpersistantdatacontainer").setExecutor(commands);
        getCommand("getloadedweapons").setExecutor(commands);
    }

    @Override
    public void onDisable() {
        sendConsoleMessage(ChatColor.GREEN + "Goodbye Sengen :)");
    }

    public void sendConsoleMessage(String message){
        getServer().getConsoleSender().sendMessage(message + " - [BlockShot]");
    }

    private void initEvents(){
        getServer().getPluginManager().registerEvents(new BlockFireEvent(), this);
    }
}
