package me.hotpocket.skriptadvancements.utils.creation;

import ch.njol.skript.Skript;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.multiParents.MultiParentsAdvancement;
import me.hotpocket.skriptadvancements.utils.CustomUtils;
import me.hotpocket.skriptadvancements.utils.advancement.HiddenAdvancement;
import me.hotpocket.skriptadvancements.utils.advancement.HiddenMultiParentsAdvancement;
import me.hotpocket.skriptadvancements.utils.advancement.ParentGrantedAdvancement;
import me.hotpocket.skriptadvancements.utils.advancement.ParentGrantedMultiParentsAdvancement;
import me.hotpocket.skriptadvancements.utils.advancement.VisibilityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TempAdvancement {

    private static String name;
    private static String tab;
    private static List<String> parents = new ArrayList<>();
    private static AdvancementDisplay display;
    private static int maxProgression;
    private static boolean root;
    private static Material background;
    public static String backgroundString = "";
    private static VisibilityType visibility;


    public TempAdvancement(String name, String tab, AdvancementDisplay display, List<String> parents, int maxProgression, boolean root, Material background, VisibilityType visible) {
        TempAdvancement.name = name;
        TempAdvancement.tab = tab;
        TempAdvancement.parents = parents;
        TempAdvancement.display = display;
        TempAdvancement.maxProgression = maxProgression;
        TempAdvancement.root = root;
        TempAdvancement.background = background;
        TempAdvancement.visibility = visible;
    }

    public String getName() {
        return name;
    }

    public String getTab() {
        return tab;
    }

    public void setParents(List<String> parents) {
        TempAdvancement.parents = parents;
    }

    public List<String> getParents() {
        return parents;
    }

    public AdvancementDisplay getDisplay() {
        return display;
    }

    public int getMaxProgression() {
        return maxProgression;
    }

    public boolean getRoot() {
        return root;
    }

    public Material getBackground() {
        return background;
    }

    public String getBackgroundString() {
        return backgroundString;
    }

    public void setIcon(ItemStack icon) {
        display = new AdvancementDisplay(icon, display.getTitle(), display.getFrame(), display.doesShowToast(), display.doesAnnounceToChat(), display.getX(), display.getY(), display.getDescription());
    }

    public void setTitle(String title) {
        display = new AdvancementDisplay(display.getIcon(), translate(title), display.getFrame(), display.doesShowToast(), display.doesAnnounceToChat(), display.getX(), display.getY(), display.getDescription());
    }

    public void setFrame(AdvancementFrameType frame) {
        display = new AdvancementDisplay(display.getIcon(), display.getTitle(), frame, display.doesShowToast(), display.doesAnnounceToChat(), display.getX(), display.getY(), display.getDescription());
    }

    public void setToast(boolean toast) {
        display = new AdvancementDisplay(display.getIcon(), display.getTitle(), display.getFrame(), toast, display.doesAnnounceToChat(), display.getX(), display.getY(), display.getDescription());
    }

    public void setAnnounce(boolean announce) {
        display = new AdvancementDisplay(display.getIcon(), display.getTitle(), display.getFrame(), display.doesShowToast(), announce, display.getX(), display.getY(), display.getDescription());
    }

    public void setX(float x) {
        display = new AdvancementDisplay(display.getIcon(), display.getTitle(), display.getFrame(), display.doesShowToast(), display.doesAnnounceToChat(), x, display.getY(), display.getDescription());
    }

    public void setY(float y) {
        display = new AdvancementDisplay(display.getIcon(), display.getTitle(), display.getFrame(), display.doesShowToast(), display.doesAnnounceToChat(), display.getX(), y, display.getDescription());
    }

    public void setDescription(List<String> description) {
        display = new AdvancementDisplay(display.getIcon(), display.getTitle(), display.getFrame(), display.doesShowToast(), display.doesAnnounceToChat(), display.getX(), display.getY(), description);
    }

    public void setMaxProgression(int progression) {
        maxProgression = progression;
    }

    public void setRoot(boolean isRoot) {
        root = isRoot;
    }

    public void setBackground(Material backgroundMaterial) {
        background = backgroundMaterial;
    }

    public void setBackgroundString(String stringBackground) {
        backgroundString = stringBackground;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visible) {
        visibility = visible;
    }

    String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String getTexture(Material block) {
        if (block.isBlock() && block.isSolid()) {
            if (Skript.methodExists(Material.class, "getTranslationKey")) {
                return "textures/block/" + block.getTranslationKey().split("minecraft\\.")[1] + ".png";
            } else {
                return "textures/block/" + getTranslationKey(block).split("minecraft\\.")[1] + ".png";
            }
        }
        return "texture/block/dirt.png";
    }

    private static String getTranslationKey(Material block) {
        return "minecraft." + block.name().toLowerCase();
    }

    static String asString(Advancement a) {
        return a.getKey().getNamespace() + "/" + a.getKey().getKey();
    }

    public static Advancement fromString(String advancement) {
        if (Creator.advancements.get(tab) != null) {
            for (Advancement adv : Creator.advancements.get(tab)) {
                if (asString(adv).equals(advancement)) {
                    return adv;
                }
            }
        }
        return null;
    }

    public void build() {
        if (Creator.lastCreatedTab.equals(tab)) {
            Advancement advancement = null;
            if (fromString(getTab() + "/" + getName()) != null) {
                Advancement removed = null;
                for (Advancement adv : Creator.advancements.get(tab)) {
                    if (asString(adv).equals(getTab() + "/" + getName())) {
                        removed = adv;
                    }
                }
                if (removed != null) {
                    Creator.advancements.get(getTab()).remove(removed);
                } else {
                    Skript.error("Duplicate advancement found: " + getTab() + "/" + getName());
                    return;
                }
            }
            if (root) {
                if (maxProgression > 0) {
                    if (getBackgroundString().equals(""))
                        advancement = new RootAdvancement(CustomUtils.getAPI().getAdvancementTab(tab), name, display, getTexture(background), maxProgression);
                    else
                        advancement = new RootAdvancement(CustomUtils.getAPI().getAdvancementTab(tab), name, display, getBackgroundString(), maxProgression);
                } else {
                    if (getBackgroundString().equals(""))
                        advancement = new RootAdvancement(CustomUtils.getAPI().getAdvancementTab(tab), name, display, getTexture(background));
                    else
                        advancement = new RootAdvancement(CustomUtils.getAPI().getAdvancementTab(tab), name, display, getBackgroundString());
                }
            } else {
                if (parents.size() > 1) {
                    Set<BaseAdvancement> parentAdvancements = new HashSet<>();
                    for (String parent : parents) {
                        if (fromString(parent) != null && !(fromString(parent) instanceof RootAdvancement))
                            parentAdvancements.add((BaseAdvancement) fromString(parent));
                    }
                    if (parentAdvancements.size() > 1) {
                        if (maxProgression > 0) {
                            switch (getVisibility()) {
                                case HIDDEN -> advancement = new HiddenMultiParentsAdvancement(name, display, maxProgression, parentAdvancements);
                                case PARENT_GRANTED -> advancement = new ParentGrantedMultiParentsAdvancement(name, display, maxProgression, parentAdvancements);
                                default -> advancement = new MultiParentsAdvancement(name, display, maxProgression, parentAdvancements);
                            }
                        } else {
                            switch (getVisibility()) {
                                case HIDDEN -> advancement = new HiddenMultiParentsAdvancement(name, display, parentAdvancements);
                                case PARENT_GRANTED -> advancement = new ParentGrantedMultiParentsAdvancement(name, display, parentAdvancements);
                                default -> advancement = new MultiParentsAdvancement(name, display, parentAdvancements);
                            }
                        }
                    } else {
                        if (fromString(parents.get(0)) != null) {
                            if (maxProgression > 0) {
                                switch (getVisibility()) {
                                    case HIDDEN -> advancement = new HiddenAdvancement(name, display, fromString(parents.get(0)), maxProgression);
                                    case PARENT_GRANTED -> advancement = new ParentGrantedAdvancement(name, display, fromString(parents.get(0)), maxProgression);
                                    default -> advancement = new BaseAdvancement(name, display, fromString(parents.get(0)), maxProgression);
                                }
                            } else {
                                switch (getVisibility()) {
                                    case HIDDEN -> advancement = new HiddenAdvancement(name, display, fromString(parents.get(0)));
                                    case PARENT_GRANTED -> advancement = new ParentGrantedAdvancement(name, display, fromString(parents.get(0)));
                                    default -> advancement = new BaseAdvancement(name, display, fromString(parents.get(0)));
                                }
                            }
                        }
                    }
                } else {
                    if (fromString(parents.get(0)) != null) {
                        if (maxProgression > 0) {
                            switch (getVisibility()) {
                                case HIDDEN -> advancement = new HiddenAdvancement(name, display, fromString(parents.get(0)), maxProgression);
                                case PARENT_GRANTED -> advancement = new ParentGrantedAdvancement(name, display, fromString(parents.get(0)), maxProgression);
                                default -> advancement = new BaseAdvancement(name, display, fromString(parents.get(0)), maxProgression);
                            }
                        } else {
                            switch (getVisibility()) {
                                case HIDDEN -> advancement = new HiddenAdvancement(name, display, fromString(parents.get(0)));
                                case PARENT_GRANTED -> advancement = new ParentGrantedAdvancement(name, display, fromString(parents.get(0)));
                                default -> advancement = new BaseAdvancement(name, display, fromString(parents.get(0)));
                            }
                        }
                    }
                }
            }
            if (advancement != null) {
                List<Advancement> advancementList = new ArrayList<>();
                if (Creator.advancements.containsKey(tab)) {
                    Creator.advancements.get(tab).add(advancement);
                } else {
                    advancementList.add(advancement);
                    Creator.advancements.put(tab, advancementList);
                }
                Creator.tempAdvancements.remove(this);
            }
        }
    }
}
