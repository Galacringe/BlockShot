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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

import static com.devgarlic.postype.BlockShot.Load.LoadJsonFiles.sendConsoleMessage;
public class BlockFireEvent implements Listener{
    // https://www.spigotmc.org/threads/bullet-speed.172917/
    // used this concept
    public static HashMap<Player, Long> timeSinceLastShot = new HashMap<>();
    Plugin plugin = BlockShot.getPlugin(BlockShot.class);
    @EventHandler
    public void BlockFireEvent(PlayerInteractEvent event){
        sendConsoleMessage("a");
        if(event.hasItem()){
            sendConsoleMessage("b");
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Player player = event.getPlayer();
                ItemStack item = event.getItem();

                if(item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "isBlockShotWeapon"), PersistentDataType.INTEGER) && getContainerValueAsInt(player, item, "isBlockShotWeapon") == 1){

                    // yes this is BlockShot Weapon and I will check it has bullet
                    int temp = getContainerValueAsInt(player, item, "VAR_CURR_MAG");
                    long delay = (long)getContainerValueAsInt(player, item, "Fire_Rate");
                    player.sendMessage(ChatColor.RED + String.valueOf(temp)); // Current Left Magazine

                    if (System.currentTimeMillis() - timeSinceLastShot.getOrDefault(player, delay) < delay) {
                        player.sendMessage("too fast");
                        event.setCancelled(true);
                        return;
                    }
                    long recent = System.currentTimeMillis();
                    timeSinceLastShot.put(player, recent);
                    player.sendMessage(String.valueOf(recent));

                    if(temp > 0){
                        // Has Bullet
                        temp = temp - 1;
                        // Modifying the necessary values in the ItemMeta
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "VAR_CURR_MAG"), PersistentDataType.INTEGER, temp);

                        // change visible left ammo
                        itemMeta.setDisplayName(getContainerValueAsString(player, item, "Name") + " / " + temp);

                        // Updating the item stack with the modified item meta
                        item.setItemMeta(itemMeta);

                        player.sendMessage(String.valueOf(temp));

                    }
                    else{
                        // NO Bullets?
                        // https://preview.redd.it/hidsexy1emo81.jpg?auto=webp&s=11ba115d67fd85cb27323946fb0f2f3a9630929a
                        player.sendMessage("Reload!!");
                    }
                    event.setCancelled(true);


                }
            }
        }

    }

    private int getContainerValueAsInt(Player player, ItemStack item, String key){
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.INTEGER);
    }
    private String getContainerValueAsString(Player player, ItemStack item, String key){
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
    }

}

