package com.devgarlic.postype.BlockShot.event;

import com.devgarlic.postype.BlockShot.BlockShot;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import static com.devgarlic.postype.BlockShot.Load.LoadJsonFiles.sendConsoleMessage;
public class BlockFireEvent implements Listener{
    Plugin plugin = BlockShot.getPlugin(BlockShot.class);
    @EventHandler
    public void BlockFireEvent(PlayerInteractEvent event){
        sendConsoleMessage("a");
        if(event.hasItem()){
            sendConsoleMessage("b");
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Player player = event.getPlayer();
                ItemStack item = event.getItem();

                if(getContainerValueAsInt(player, item, "isBlockShotWeapon") == 1){

                    //fire event
                    int temp = getContainerValueAsInt(event.getPlayer(), item, "Max_Mag");
                    player.sendMessage(String.valueOf(temp));
                }
            }
        }

    }

    public int getContainerValueAsInt(Player player, ItemStack item, String key){
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.INTEGER);
    }
    public String getContainerValueAsString(Player player, ItemStack item, String key){
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
    }

}

