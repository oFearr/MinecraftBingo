package me.ofearr.mcbingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EventListeners implements Listener {

    HashMap<Location, ItemStack> cheatBreaker = new HashMap<>();

    public static String TranslateColour(String text){
        String translatedColour = ChatColor.translateAlternateColorCodes('&', text);

        return translatedColour;
    }



    @EventHandler
    public void ItemCollectEvent(PlayerAttemptPickupItemEvent e){
        if(MCBingo.gameActive == false) return;
        Material mat = e.getItem().getItemStack().getType();
        Player player = e.getPlayer();

        ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));

        for(int i = 0; i < splitObjectives.size(); i++){
            if(splitObjectives.get(i) == mat.toString()){
                splitObjectives.remove(i);
            }
        }

        if(splitObjectives.size() == 0){
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(TranslateColour("&a&lGame Over!"), TranslateColour("&aThe game has been won by " + player.getName() + "!"));
                p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has been won by " + player.getName() + "!"));
            }
            MCBingo.gameActive = false;
        }

    }

    @EventHandler
    public void SmeltCraftChestItemEvent(InventoryClickEvent e){
        ItemStack item = e.getCursor();
        if(item == null) return;
        if(e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if(e.getSlotType().equals(InventoryType.SlotType.RESULT)){
            Material mat = e.getCurrentItem().getType();
            Player player = (Player) e.getWhoClicked();

            ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));

            for(int i = 0; i < splitObjectives.size(); i++){
                if(splitObjectives.get(i) == mat.toString()){
                    splitObjectives.remove(i);
                }
            }

            if(splitObjectives.size() == 0){
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(TranslateColour("&a&lGame Over!"), TranslateColour("&aThe game has been won by " + player.getName() + "!"));
                    p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has been won by " + player.getName() + "!"));
                }
                MCBingo.gameActive = false;
            }

        } else if(e.getSlotType().equals(InventoryType.SlotType.CONTAINER)){
            Material mat = e.getCurrentItem().getType();
            Player player = (Player) e.getWhoClicked();
            Location loc = e.getClickedInventory().getLocation();

            if(cheatBreaker.containsKey(loc)) return;

            ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));

            for(int i = 0; i < splitObjectives.size(); i++){
                if(splitObjectives.get(i) == mat.toString()){
                    splitObjectives.remove(i);
                }
            }

            if(splitObjectives.size() == 0){
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(TranslateColour("&a&lGame Over!"), TranslateColour("&aThe game has been won by " + player.getName() + "!"));
                    p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has been won by " + player.getName() + "!"));
                }
                MCBingo.gameActive = false;
            }

            cheatBreaker.put(loc, e.getCurrentItem());

        }

    }


}
