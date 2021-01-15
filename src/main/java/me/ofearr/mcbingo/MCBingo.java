package me.ofearr.mcbingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public final class MCBingo extends JavaPlugin {

    public static String TranslateColour(String text){
        String translatedColour = ChatColor.translateAlternateColorCodes('&', text);

        return translatedColour;
    }

    public static boolean gameActive;
    public static HashMap<UUID, String> playerObjectives = new HashMap<>();

    @Override
    public void onEnable() {
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().toLowerCase().equals("startbingo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("bingo.start"))) {
                    player.sendMessage("&cInsufficent permissions!");
                } else {
                        for (Player p : Bukkit.getOnlinePlayers()) {

                            p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has been started by " + sender.getName() + "!"));
                        }

                        new BukkitRunnable() {

                            int gameTimer = 10;

                            @Override
                            public void run() {

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

                                if (gameTimer == 0) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&aThe game has started! Use /bingocard to view your objectives!"));
                                        p.sendMessage(TranslateColour("&8[&e&lBingo&8] >> &aThe game has started! Use /bingocard to view your objectives!"));
                                    }
                                    gameActive = true;

                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.getInventory().clear();
                                        String playerGoal = "";
                                        for(int i = 0; i < getConfig().getInt("Settings.Goal-Amount"); i++){
                                            Random rand = new Random();
                                            int locIndex = rand.nextInt(getConfig().getStringList("Materials").size());

                                            String selectedItem = getConfig().getStringList("Materials").get(locIndex);

                                            if(i < getConfig().getInt("Settings.Goal-Amount") - 1){
                                                playerGoal = playerGoal + selectedItem + ", ";
                                            } else{
                                                playerGoal = playerGoal + selectedItem;
                                            }

                                        }
                                        playerObjectives.put(p.getUniqueId(), playerGoal);
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
                    }
                }
            }
        }
        return false;
    }


}
