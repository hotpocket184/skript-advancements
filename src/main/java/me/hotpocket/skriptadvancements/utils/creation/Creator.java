package me.hotpocket.skriptadvancements.utils.creation;

import ch.njol.skript.Skript;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.hotpocket.skriptadvancements.utils.CustomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class Creator {

    public static List<TempAdvancement> tempAdvancements = new ArrayList<>();
    public static HashMap<String, List<Advancement>> advancements = new HashMap<>();
    public static String lastCreatedTab;
    public static TempAdvancement lastCreatedAdvancement;

    public static void build() {
        if (CustomUtils.getAPI().getAdvancementTab(lastCreatedTab) != null && CustomUtils.getAPI().getAdvancementTab(lastCreatedTab).isInitialised()) {
            CustomUtils.getAPI().unregisterAdvancementTab(lastCreatedTab);
        }
        RootAdvancement root = new RootAdvancement(CustomUtils.getAPI().getAdvancementTab(lastCreatedTab), "temp_root_advancement_name_1289587", new AdvancementDisplay(Material.DIAMOND, "title", AdvancementFrameType.TASK, false, false, 0, 0, "description"), TempAdvancement.getTexture(Material.DIAMOND_BLOCK));
        Set<BaseAdvancement> baseAdvancements = new HashSet<>();
        if (advancements.get(lastCreatedTab) != null) {
            for (Advancement advancement : advancements.get(lastCreatedTab)) {
                if (advancement instanceof RootAdvancement rootAdvancement) {
                    root = rootAdvancement;
                } else {
                    baseAdvancements.add((BaseAdvancement) advancement);
                }
            }
        }
            CustomUtils.getAPI().getAdvancementTab(lastCreatedTab).registerAdvancements(root, baseAdvancements);
            for (AdvancementTab tab : CustomUtils.getAPI().getTabs()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    tab.updateAdvancementsToTeam(player);
                }
            }
        //}
        advancements.remove(lastCreatedTab);
    }
}
