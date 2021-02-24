package me.ofearr.mcbingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class MCBingo extends JavaPlugin {

    public static String TranslateColour(String text){
        String translatedColour = ChatColor.translateAlternateColorCodes('&', text);

        return translatedColour;
    }

    public static MCBingo plugin;

    public static boolean gameActive;
    public static HashMap<UUID, String> playerObjectives = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        gameActive = false;
        loadConfig();
        Bukkit.getPluginManager().registerEvents(new EventListeners(), this);
    }

    @Override
    public void onDisable() {
        playerObjectives.clear();
    }

    public void loadConfig(){
        saveDefaultConfig();
    }

    public Inventory BingoCardGUI(Player player){

        ArrayList<String> remainingObj = new ArrayList<>(Arrays.asList(playerObjectives.get(player.getUniqueId()).split(", ")));
        int ObjAmt = getConfig().getInt("Settings.Goal-Amount");
        int slots = getConfig().getInt("Settings.GUI-Slots");

        String completionCodes = TranslateColour("&e" + remainingObj.size() + "&f/" + "&a" + ObjAmt + " &7Remaining");

        Inventory cardGUI = Bukkit.createInventory(null, slots, TranslateColour("&8[&e&lBingo Card&8]"));

        ItemStack fillerItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);

        ItemMeta fillerMeta = fillerItem.getItemMeta();
        fillerMeta.setDisplayName(TranslateColour("&8[&e&lBingo&8] >> " + completionCodes));

        ArrayList<String> fillerLore = new ArrayList<>();

        fillerLore.add(TranslateColour(" "));
        fillerLore.add(TranslateColour("&aShould you encounter any bugs,"));
        fillerLore.add(TranslateColour("&aplease report them to &noFearr&a,"));
        fillerLore.add(TranslateColour("&a&nDemonify&a or &nOhEmily."));

        fillerMeta.setLore(fillerLore);
        fillerItem.setItemMeta(fillerMeta);

        for (int i = 0; i < slots; i++){
            cardGUI.setItem(i, fillerItem);
        }

        for (int i = 0; i < remainingObj.size(); i++){

                ItemStack currentItem = new ItemStack(Material.getMaterial(String.valueOf(remainingObj.get(i))));

                cardGUI.setItem(i, currentItem);

        }

        player.openInventory(cardGUI);

        return cardGUI;
    }

    public static void GameWon(Player player){
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(TranslateColour("&a&lGame Over!"), TranslateColour("&aThe game has been won by " + player.getName() + "!"));
            p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has been won by " + player.getName() + "!"));
            playerObjectives.clear();
            gameActive = false;
            p.setCustomName(p.getName());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().toLowerCase().equals("startbingo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("bingo.start"))) {
                    player.sendMessage("&cInsufficent permissions!");
                } else {
                        for (Player p : Bukkit.getOnlinePlayers()) {

                            p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game countdown has been started by " + sender.getName() + "!"));
                        }

                        playerObjectives.clear();

                        new BukkitRunnable() {

                            int gameTimer = 10;

                            @Override
                            public void run() {

                                if(gameActive == true){
                                    this.cancel();
                                }

                                if (gameTimer == 10) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game will start in 10 seconds!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game will start in 10 seconds!"));
                                    }
                                }

                                if (gameTimer == 5) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game will start in 5 seconds!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game will start in 5 seconds!"));
                                    }
                                }

                                if (gameTimer == 4) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game will start in 4 seconds!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game will start in 4 seconds!"));
                                    }
                                }

                                if (gameTimer == 3) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game will start in 3 seconds!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game will start in 3 seconds!"));
                                    }
                                }

                                if (gameTimer == 2) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game will start in 2 seconds!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game will start in 2 seconds!"));
                                    }
                                }

                                if (gameTimer == 1) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game will start in 1 second!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game will start in 1 second!"));
                                    }
                                }

                                if (gameTimer <= 0) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game has started! Use /bingocard to view your objectives!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has started! Use /bingocard to view your objectives!"));
                                        this.cancel();
                                    }
                                    gameActive = true;
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.getInventory().clear();
                                        String playerGoal = "";
                                        for(int i = 0; i < getConfig().getInt("Settings.Goal-Amount"); i++){
                                            Random rand = new Random();
                                            int locIndex = rand.nextInt(getConfig().getStringList("Settings.Materials").size());

                                            String selectedItem = getConfig().getStringList("Settings.Materials").get(locIndex);

                                            if(i < getConfig().getInt("Settings.Goal-Amount") - 1){
                                                playerGoal = playerGoal + selectedItem + ", ";
                                            } else{
                                                playerGoal = playerGoal + selectedItem;
                                            }

                                        }
                                        playerObjectives.put(p.getUniqueId(), playerGoal);
                                        p.setCustomName(TranslateColour("&b" + p.getName() + "&8[&d" + getConfig().get("Settings.Goal-Amount") + "&f/&d" + getConfig().get("Settings.Goal-Amount") + "&8]"));
                                    }

                                }
                                gameTimer--;
                            }
                        }.runTaskTimer(this, 0, 20L);


                }
            }
        }

        if (command.getName().toLowerCase().equals("endbingo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("bingo.end"))) {
                    player.sendMessage(ChatColor.RED + "Insufficient permissions!");
                } else {
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has been ended by " + sender.getName() + "!"));
                        playerObjectives.clear();
                        gameActive = false;
                        p.setCustomName(p.getName());
                    }
                }
            }
        }

        if (command.getName().toLowerCase().equals("bingocard")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("bingo.card"))) {
                    player.sendMessage(ChatColor.RED + "Insufficient permissions!");
                } else {
                    if(playerObjectives.containsKey(player.getUniqueId())){
                        BingoCardGUI(player);
                    } else {
                        player.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &cYou can only issue this command during a game!"));
                    }

                }
            }
        }
        return false;
    }


}
