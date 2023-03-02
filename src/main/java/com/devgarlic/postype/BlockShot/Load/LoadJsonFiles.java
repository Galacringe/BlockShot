package com.devgarlic.postype.BlockShot.Load;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class LoadJsonFiles {
    public static void init(){

        String current_path = System.getProperty("user.dir");
        current_path += "\\plugins\\weapons";
        File weapons_directory = new File(current_path);
        if(!weapons_directory.exists()){
            try {
                weapons_directory.mkdir();
                sendConsoleMessage(ChatColor.GREEN + "created weapons folders on " + weapons_directory + "...");
            }
            catch (Exception e) {
                sendConsoleMessage(ChatColor.RED + "an error occured while creating weapons folder....");
                e.printStackTrace();
            }
        }
        else {
            sendConsoleMessage(ChatColor.GREEN  + "Grabbing .json files on " + weapons_directory + " ...");
            
            // chatGPT is awesome :0
            File[] files = weapons_directory.listFiles((dir, name) -> name.endsWith(".json"));
            JsonObject[] weapons_json = new JsonObject[files.length];

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                try (InputStream inputStream = new FileInputStream(file);
                     JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {
                    Gson gson = new Gson();
                    weapons_json[i] = gson.fromJson(reader, JsonObject.class);
                } catch (IOException | JsonParseException e) {
                    sendConsoleMessage(ChatColor.RED + "An error occurred while parsing " + weapons_json[i] + ".");
                    e.printStackTrace();
                }
            }

            ArrayList <BlockWeapons.Data> weaponsData = new ArrayList<>();

            for (int i = 0; i < weapons_json.length; i++) {
                try {
                    ItemStack weaponitem = new ItemStack(Material.BARRIER, 1);
                    Material material;
                    material = Material.getMaterial(weapons_json[i].get("Item").getAsString());
                    weaponitem.setType(material);
                    BlockWeapons.Data temp = new BlockWeapons.Data(weaponitem, weapons_json[i]);
                    weaponsData.add(temp);
                    sendConsoleMessage(ChatColor.BOLD + "Loaded " + temp.jsondata.get("Name") + "...");
                }
                catch (NullPointerException | IllegalArgumentException e) {
                    sendConsoleMessage(ChatColor.RED + "AN ERROR OCCURRED - PLEASE CHECK THE WEAPONS FOLDER");
                    String[] keys = new String[weapons_json[i].keySet().size()];
                    Iterator<String> iterator = weapons_json[i].keySet().iterator();
                    int j = 0;
                    while (iterator.hasNext()) {
                        keys[j] = iterator.next();
                        j++;
                    }
                    for (int k = 0; k < j; k++) {
                        if (weapons_json[i].get(keys[k]).getAsString().isEmpty()) {
                            sendConsoleMessage(ChatColor.RED + "ERROR: On name: " + weapons_json[i].get("Name").getAsString() + " / " + keys[k] + ": " + weapons_json[i].get(keys[k]).toString() + " format is empty. please check it.");

                        }
                    }
                    e.printStackTrace();
                }
            }
            sendConsoleMessage(ChatColor.GREEN + "All .json file loaded, Here's the valid json list:");
            for (int i = 0; i < weaponsData.size(); i++) {
                sendConsoleMessage(ChatColor.BOLD+ weaponsData.get(i).jsondata.get("Name").getAsString() + " / " + weaponsData.get(i).item.toString());
            }

            // weapons_json 에 json 내용 다 담김.
            // weaponsdata 에 pair 로 itemStack 이랑 jsonObject 랑 다 들어있어요~
            BlockWeapons.setItemStack(weaponsData);
        }

    }

    public static void sendConsoleMessage(String message){
        getServer().getConsoleSender().sendMessage(message + " - [BlockShot]");
    }
}
