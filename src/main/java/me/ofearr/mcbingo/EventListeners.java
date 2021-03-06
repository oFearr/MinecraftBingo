package me.ofearr.mcbingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EventListeners implements Listener {

    public static String TranslateColour(String text){
        String translatedColour = ChatColor.translateAlternateColorCodes('&', text);

        return translatedColour;
    }

    MCBingo plugin = MCBingo.plugin;

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

        if(!MCBingo.playerObjectives.containsKey(player.getUniqueId())) return;

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
                        Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " has complete all their objectives!"));
                        MCBingo.GameWon(player);
                        MCBingo.playerObjectives.remove(player.getUniqueId());
                    } else {
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                        Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " complete an objective &8[&d" + splitObjectives.size() + "&f/&d" + plugin.getConfig().get("Settings.Goal-Amount") + "&8]&a remaining!"));
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

        if(!MCBingo.playerObjectives.containsKey(player.getUniqueId())) return;

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
                    Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " has complete all their objectives!"));
                    MCBingo.GameWon(player);
                    MCBingo.playerObjectives.remove(player.getUniqueId());
                } else {
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                    Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " complete an objective &8[&d" + splitObjectives.size() + "&f/&d" + plugin.getConfig().get("Settings.Goal-Amount") + "&8]&a remaining!"));
                }

            }
        }


    }

    @EventHandler
    public void blockInteract(PlayerInteractEvent e){
        Action action = e.getAction();
        Player player = e.getPlayer();

        if(!MCBingo.playerObjectives.containsKey(player.getUniqueId())) return;

        if(player.getInventory().getItemInMainHand() == null && player.getInventory().getItemInOffHand() == null) return;
        if(e.getClickedBlock() == null) return;
        else if(player.getInventory().getItemInMainHand().getType() == Material.BUCKET || player.getInventory().getItemInOffHand().getType() == Material.BUCKET && action == Action.RIGHT_CLICK_BLOCK){
            if(e.getClickedBlock().getType() == Material.LAVA || e.getClickedBlock().getType() == Material.WATER){
                Material mat = e.getClickedBlock().getType();

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
                            Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " has complete all their objectives!"));
                            MCBingo.GameWon(player);
                            MCBingo.playerObjectives.remove(player.getUniqueId());
                        } else {
                            player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                            player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                            Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " complete an objective &8[&d" + splitObjectives.size() + "&f/&d" + plugin.getConfig().get("Settings.Goal-Amount") + "&8]&a remaining!"));
                        }

                    }
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

            if(!MCBingo.playerObjectives.containsKey(player.getUniqueId())) return;

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
                        Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " has complete all their objectives!"));
                        MCBingo.GameWon(player);
                        MCBingo.playerObjectives.remove(player.getUniqueId());
                    } else {
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                        Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " complete an objective &8[&d" + splitObjectives.size() + "&f/&d" + plugin.getConfig().get("Settings.Goal-Amount") + "&8]&a remaining!"));

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
                if(!MCBingo.playerObjectives.containsKey(player.getUniqueId())) return;
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
                            Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " has complete all their objectives!"));
                            MCBingo.GameWon(player);
                            MCBingo.playerObjectives.remove(player.getUniqueId());
                        } else {
                            player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                            player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                            Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " complete an objective &8[&d" + splitObjectives.size() + "&f/&d" + plugin.getConfig().get("Settings.Goal-Amount") + "&8]&a remaining!"));
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
               if(builder.length() > 2){
                   String updated = builder.toString().substring(0, builder.length() - 2);

                   MCBingo.playerObjectives.remove(player.getUniqueId());
                   MCBingo.playerObjectives.put(player.getUniqueId(), updated);
               }

                if(splitObjectives.size() == 0){
                    Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " has complete all their objectives!"));
                    MCBingo.GameWon(player);
                    MCBingo.playerObjectives.remove(player.getUniqueId());
                } else {
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aYou've complete one of your bingo objectives!"));
                    player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aCheck your /bingocard to see what's next!"));
                    Bukkit.broadcastMessage(TranslateColour("&8[&e&lBingo&8] >> &a" + player.getName() + " complete an objective &8[&d" + splitObjectives.size() + "&f/&d" + plugin.getConfig().get("Settings.Goal-Amount") + "&8]&a remaining!"));
                    player.setCustomName(TranslateColour("&b" + player.getName() + "&8[&d" + splitObjectives.size() + "&f/&d" + plugin.getConfig().get("Settings.Goal-Amount") + "&8]"));
                }
                break;

            }
        }

    }


}
