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
        String[] essential_keys = {
                "Name",
                "Item",
                "Lore",
                "Max_Mag",
                "Reload_Cooldown",
                "Fire_Rate",
                "Reload_Sound",
                "Fire_Sound",
                "Projectile_Damage",
                "Projectile_Amount",
                "Onfire_Effect"
        };



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
                    sendConsoleMessage(ChatColor.BOLD + "Checking " + temp.jsondata.get("Name") + "...");
                    boolean isVaild = false;
                    for(int j = 0;  j < essential_keys.length; j++){
                        if(!temp.jsondata.has(essential_keys[j])){
                            sendConsoleMessage(ChatColor.GOLD + "ERROR: Essential Key - " + essential_keys[j] + " is not declared on "+ weapons_json[i].get("Name").getAsString() + ", Please check the file and declare it.");
                            isVaild = true;
                        }
                    }
                    if(isVaild) {sendConsoleMessage(ChatColor.BOLD + weapons_json[i].get("Name").getAsString() + ChatColor.RED +  " Disabled.");
                    continue;}


                    weaponsData.add(temp);
                    sendConsoleMessage(ChatColor.BOLD + "Loaded " + temp.jsondata.get("Name") + "...");
                }
                catch (NullPointerException | IllegalArgumentException e) {
                    sendConsoleMessage(ChatColor.RED + "AN ERROR OCCURRED - PLEASE CHECK THE WEAPONS FOLDER");

                    if (weapons_json[i] == null){sendConsoleMessage(ChatColor.BOLD + files[i].getName() + ChatColor.RED + " is Empty!" + ChatColor.GRAY+ " (it is NULL)"); continue;}


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
