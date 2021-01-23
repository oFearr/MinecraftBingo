package me.ofearr.mcbingo;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EventListeners implements Listener {

    public static String TranslateColour(String text){
        String translatedColour = ChatColor.translateAlternateColorCodes('&', text);

        return translatedColour;
    }


    @EventHandler
    public void EventPVP(EntityDamageByEntityEvent e){
        if(MCBingo.gameActive == false) return;
        if(!(e.getEntity() instanceof Player)) return;

        e.setCancelled(true);
        e.getDamager().sendMessage(TranslateColour("&8[&e&lBingo&8] >> &cYou cannot damage other players during this game!"));
    }

    @EventHandler
    public void EventDrop(PlayerDropItemEvent e){

        if(MCBingo.gameActive == false) return;
        if(e.getItemDrop() == null) return;

        if(!(e.getItemDrop().getItemStack().getItemMeta().hasLore())){

            ItemMeta im = e.getItemDrop().getItemStack().getItemMeta();

            ArrayList<String> excludeLore = new ArrayList<>();

            excludeLore.add(" ");
            excludeLore.add(TranslateColour("&cExcluded from Game!"));
            excludeLore.add(TranslateColour("&cThis item cannot be used"));
            excludeLore.add(TranslateColour("&cto complete a bingo objective!"));
            excludeLore.add(" ");
            excludeLore.add(TranslateColour("&c(Can still be used for crafting!)"));
            im.setLore(excludeLore);

            e.getItemDrop().getItemStack().setItemMeta(im);
        } else{
            return;
        }


    }

    @EventHandler
    public void ItemCollectEvent(PlayerAttemptPickupItemEvent e){
        if(MCBingo.gameActive == false) return;
        if(e.getItem() == null) return;
        Material mat = e.getItem().getItemStack().getType();
        Player player = e.getPlayer();

        if(!(e.getItem().getItemStack().hasItemMeta())){
            ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));

            for(int i = 0; i < splitObjectives.size(); i++) {
                if (splitObjectives.get(i).equalsIgnoreCase(mat.toString())) {
                    splitObjectives.remove(i);

                    StringBuilder builder = new StringBuilder();
                    for (String cVal : splitObjectives) {
                        builder.append(cVal + ", ");
                    }

                    String updated = "";
                    if(builder.toString().contains(", ")){
                        updated = builder.toString().substring(0, builder.length() - 2);
                    } else {
                        updated = builder.toString();
                    }

                    MCBingo.playerObjectives.remove(player.getUniqueId());
                    MCBingo.playerObjectives.put(player.getUniqueId(), updated);

                    if(splitObjectives.size() == 0){
                        MCBingo.GameWon(player);
                    } else {
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                    }

                    break;
                }
            }
        }  else{
            return;
        }

        }

    @EventHandler
    public void BlockBreak(BlockBreakEvent e){
        if(MCBingo.gameActive == false) return;
        Material mat = e.getBlock().getType();
        Player player = e.getPlayer();

        ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));

        for(int i = 0; i < splitObjectives.size(); i++) {
            if (splitObjectives.get(i).equalsIgnoreCase(mat.toString())) {
                splitObjectives.remove(i);

                StringBuilder builder = new StringBuilder();
                for (String cVal : splitObjectives) {
                    builder.append(cVal + ", ");
                }

                String updated = "";
                if(builder.toString().contains(", ")){
                    updated = builder.toString().substring(0, builder.length() - 2);
                } else {
                    updated = builder.toString();
                }

                MCBingo.playerObjectives.remove(player.getUniqueId());
                MCBingo.playerObjectives.put(player.getUniqueId(), updated);

                if(splitObjectives.size() == 0){
                    MCBingo.GameWon(player);
                } else {
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                }

            }
        }


    }

    @EventHandler
    public void SmeltChestItemEvent(InventoryClickEvent e){
        if(MCBingo.gameActive == false) return;
        ItemStack item = e.getCursor();
        if(item == null) return;
        if(e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if(e.getSlotType().equals(InventoryType.SlotType.RESULT)){
            Material mat = e.getCurrentItem().getType();
            Player player = (Player) e.getWhoClicked();

            ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));


            for(int i = 0; i < splitObjectives.size(); i++) {
                if (splitObjectives.get(i).equalsIgnoreCase(mat.toString())) {
                    splitObjectives.remove(i);

                    StringBuilder builder = new StringBuilder();
                    for (String cVal : splitObjectives) {
                        builder.append(cVal + ", ");
                    }

                    String updated = "";
                    if(builder.toString().contains(", ")){
                        updated = builder.toString().substring(0, builder.length() - 2);
                    } else {
                        updated = builder.toString();
                    }
                    MCBingo.playerObjectives.remove(player.getUniqueId());
                    MCBingo.playerObjectives.put(player.getUniqueId(), updated);

                    if(splitObjectives.size() == 0){
                        MCBingo.GameWon(player);
                    } else {
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                    }
                    break;

                }
            }

        }

        if(e.getView().getTitle().equalsIgnoreCase(TranslateColour("&8[&e&lBingo Card&8]"))){
            if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
            if(e.getCurrentItem() == null) return;
            e.setCancelled(true);
        }

        if(e.getSlotType().equals(InventoryType.SlotType.CONTAINER) && !(e.getView().getTitle().equalsIgnoreCase(TranslateColour("&8[&e&lBingo Card&8]")))){
            if(e.getCurrentItem() == null) return;
            Material mat = e.getCurrentItem().getType();
            Player player = (Player) e.getWhoClicked();


            if(!(e.getCurrentItem().hasItemMeta())){
                ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));

                for(int i = 0; i < splitObjectives.size(); i++) {
                    if (splitObjectives.get(i).equalsIgnoreCase(mat.toString())) {
                        splitObjectives.remove(i);

                        StringBuilder builder = new StringBuilder();
                        for (String cVal : splitObjectives) {
                            builder.append(cVal + ", ");
                        }

                        String updated = "";
                        if(builder.toString().contains(", ")){
                            updated = builder.toString().substring(0, builder.length() - 2);
                        } else {
                            updated = builder.toString();
                        }

                        MCBingo.playerObjectives.remove(player.getUniqueId());
                        MCBingo.playerObjectives.put(player.getUniqueId(), updated);

                        if(splitObjectives.size() == 0){
                            MCBingo.GameWon(player);
                        } else {
                            player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                            player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                        }
                        break;

                    }
                }

                ItemMeta im = e.getCurrentItem().getItemMeta();

                ArrayList<String> excludeLore = new ArrayList<>();

                excludeLore.add(" ");
                excludeLore.add(TranslateColour("&cExcluded from Game!"));
                excludeLore.add(TranslateColour("&cThis item cannot be used"));
                excludeLore.add(TranslateColour("&cto complete a bingo objective!"));
                excludeLore.add(" ");
                excludeLore.add(TranslateColour("&c(Can still be used for crafting!)"));
                im.setLore(excludeLore);

                e.getCurrentItem().setItemMeta(im);
            } else{
                return;
            }



        }

    }

    @EventHandler
    public void CraftItem(CraftItemEvent e){

        Material mat = e.getCurrentItem().getType();
        Player player = (Player) e.getWhoClicked();

        ArrayList<String> splitObjectives = new ArrayList<>(Arrays.asList(MCBingo.playerObjectives.get(player.getUniqueId()).split(", ")));


        for(int i = 0; i < splitObjectives.size(); i++) {
            if (splitObjectives.get(i).equalsIgnoreCase(mat.toString())) {
                splitObjectives.remove(i);

                StringBuilder builder = new StringBuilder();
                for (String cVal : splitObjectives) {
                    builder.append(cVal + ", ");
                }
                String updated = builder.toString().substring(0, builder.length() - 2);

                MCBingo.playerObjectives.remove(player.getUniqueId());
                MCBingo.playerObjectives.put(player.getUniqueId(), updated);

                if(splitObjectives.size() == 0){
                    MCBingo.GameWon(player);
                } else {
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                }
                break;

            }
        }

    }


}
