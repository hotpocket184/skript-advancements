package me.hotpocket.skriptadvancements.elements.conditions.custom;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class CondCustomAnnouncesToChat extends Condition {

    static {
        Skript.registerCondition(CondCustomAnnouncesToChat.class,
                "%customadvancements% [do[es]] announce to [the] chat",
                "%customadvancements% (doesn't|does not|don't|do not) announce to [the] chat");
    }

    private Expression<Advancement> advancements;

    @Override
    public boolean check(Event e) {
        return advancements.check(e, advancement -> {
            if (advancement.getDisplay() != null)
                return advancement.getDisplay().doesAnnounceToChat();
            return false;
        }, isNegated());
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return advancements.toString(e, debug) + " do/do not announce to chat";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        advancements = (Expression<Advancement>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }
}
