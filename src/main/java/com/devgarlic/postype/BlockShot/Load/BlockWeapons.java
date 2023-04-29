package com.devgarlic.postype.BlockShot.Load;

import com.devgarlic.postype.BlockShot.BlockShot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jdk.nashorn.internal.ir.Block;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class BlockWeapons {
    static class Data {
        public ItemStack item;
        public JsonObject jsondata;

        public Data(ItemStack itemStack, JsonObject jsonObject) {
            this.item = itemStack;
            this.jsondata = jsonObject;
        }
    }

    public static ArrayList<Data> setItemStack(ArrayList<Data> weaponsdata) {
        sendConsoleMessage(ChatColor.GREEN + "Started setting ItemStack...");
        Plugin plugin = BlockShot.getPlugin(BlockShot.class);
        ItemStack itemStack; // target Itemstack
        ItemMeta itemMeta; // target ItemMeta
        JsonObject jsonObject; // target JsonObject
        for(int i = 0; i < weaponsdata.size(); i++){
            itemStack = weaponsdata.get(i).item;
            jsonObject = weaponsdata.get(i).jsondata;
            itemMeta = itemStack.getItemMeta();


            // Item's name
            itemMeta.setDisplayName(jsonObject.get("Name").getAsString() + " / " +jsonObject.get("Projectile_Damage").getAsString());
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "DefaultName"), PersistentDataType.STRING, jsonObject.get("Name").getAsString());

            // sets base data
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "Max_Mag"),PersistentDataType.INTEGER, jsonObject.get("Max_Mag").getAsInt());
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "Reload_Cooldown"),PersistentDataType.INTEGER, jsonObject.get("Reload_Cooldown").getAsInt());
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "Projectile_Damage"),PersistentDataType.INTEGER, jsonObject.get("Projectile_Damage").getAsInt());
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "Projectile_Amount"),PersistentDataType.INTEGER, jsonObject.get("Projectile_Amount").getAsInt());
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "Projectile_Speed"),PersistentDataType.INTEGER, jsonObject.get("Projectile_Speed").getAsInt());

            // sets lore
            ArrayList<String> lore = new ArrayList<>();
            JsonArray jsonlore = weaponsdata.get(i).jsondata.getAsJsonArray("Lore");
            for(int j = 0; j < jsonlore.size(); j++){
                lore.add(jsonlore.get(j).getAsString().replace('$', '§'));
            }
            String templore = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "Projectile_Damage"),PersistentDataType.INTEGER).toString();
            lore.add(ChatColor.AQUA + "Damage - " + templore + "  "   + ChatColor.RED + Double.valueOf(templore)/2 + " ♥" );
            templore = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "Projectile_Speed"),PersistentDataType.INTEGER).toString();
            lore.add(ChatColor.AQUA + "Projectile Speed - ≫" + templore);
            templore = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "Reload_Cooldown"),PersistentDataType.INTEGER).toString();
            lore.add(ChatColor.AQUA + "Reload - ®" + templore + " " +  Double.valueOf(templore)/20 + " sec");

            itemMeta.setLore(lore);

            // to verify this is valid BlockShot item
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "isBlockShotWeapons"), PersistentDataType.INTEGER, 1);

            // idx - fast way to get index of this weapon's data
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "idx"), PersistentDataType.INTEGER, i);

            // weapon


            itemStack.setItemMeta(itemMeta); // itemMeta 를 통해서 값들을 수정해두고, 다시 itemStack에 연결
            weaponsdata.get(i).item = itemStack; // 다시 그걸 data에 있는 item에 넣기


            sendConsoleMessage(ChatColor.GREEN + weaponsdata.get(i).item.getItemMeta().getDisplayName() + " Created...");
            sendConsoleMessage(ChatColor.BOLD+ itemMeta.getDisplayName() +" - itemstack idx - " + itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "idx"), PersistentDataType.INTEGER).toString());



            Bukkit.getServer().getPlayer("_GalaKrond").getInventory().addItem(itemStack);
        }
        return weaponsdata;
    }

    public static void sendConsoleMessage(String message){
        getServer().getConsoleSender().sendMessage(message + " - [BlockShot]");
    }

}
