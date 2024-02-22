package me.hotpocket.skriptadvancements;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.fren_gor.ultimateAdvancementAPI.AdvancementMain;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.hotpocket.skriptadvancements.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public final class SkriptAdvancements extends JavaPlugin implements Listener {

    private static SkriptAdvancements instance;
    public static AdvancementMain main;
    private UltimateAdvancementAPI api;
    private SkriptAddon addon;
    private static List<UUID> joined = new ArrayList<>();
    private static boolean updated = true;

    public static SkriptAdvancements getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        main = new AdvancementMain(this);
        main.load();
    }

    @Override
    public void onEnable() {
        instance = this;

        main.enableSQLite(new File("database.db"));
        api = UltimateAdvancementAPI.getInstance(this);

        Bukkit.getPluginManager().registerEvents(this, this);

        this.addon = Skript.registerAddon(this);
        this.addon.setLanguageFileDirectory("lang");

        final int id = 15554;
        new Metrics(this, id);

        try {
            this.addon.loadClasses("me.hotpocket.skriptadvancements", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }

        log("§7[§bSkript-Advancements§7] §fChecking for updates...");
        if (getDescription().getVersion().equals(getVersion())) {
            log("§7[§bSkript-Advancements§7] §aNo updates found!");
        } else {
            log("§7[§bSkript-Advancements§7] §cYou are running an §noutdated version§r §cof skript-advancements!");
            updated = false;
        }
    }

    @Override
    public void onDisable() {
        main.disable();
    }

    @EventHandler
    private void onPlayerLoadingCompleted(PlayerLoadingCompletedEvent event) {
        for (AdvancementTab tab : api.getTabs())
            if (tab.isInitialised())
                tab.updateAdvancementsToTeam(event.getPlayer());
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        if (!updated) {
            if (event.getPlayer().isOp() && !joined.contains(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage("§7[§bSkript-Advancements§7] §cYou are running an §noutdated version§r §cof skript-advancements!");
                joined.add(event.getPlayer().getUniqueId());
            }
        }
    }

    private String getVersion() {
        try {
            URL url = new URI("https://api.github.com/repos/hotpocket184/skript-advancements/releases/latest").toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000); // ms

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                this.getLogger().info("Got code " + con.getResponseCode() + " while checking for updates.");
                return "";
            }

            String response = new String(con.getInputStream().readAllBytes());
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(response, JsonObject.class);
            if (json.has("tag_name")) return json.get("tag_name").getAsString();

        } catch (URISyntaxException | IOException exception) {
            this.getLogger().info("Unable to check for updates: " + exception.getMessage());
        }

        return "";
    }

    private void log(String text) {
        getServer().getConsoleSender().sendMessage(text);
    }
}
