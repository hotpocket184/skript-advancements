package me.hotpocket.skriptadvancements.elements.expressions.display.vanilla;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Display - Advancement Frame")
@Description("Allows you to get the displayed frame of an advancement")
@Examples("broadcast displayed frame of the advancement \"adventure/root\"")
@Since("1.4")

public class ExprDisplayedFrame extends SimpleExpression<AdvancementFrameType> {

    static {
        if (Skript.classExists("io.papermc.paper.advancement.AdvancementDisplay"))
            Skript.registerExpression(ExprDisplayedFrame.class, AdvancementFrameType.class, ExpressionType.SIMPLE,
                    "[the] displayed frame[s] of %advancements%");
    }

    private Expression<Advancement> advancements;

    @Override
    protected @Nullable AdvancementFrameType[] get(Event e) {
        for (Advancement advancement : advancements.getAll(e))
            if (advancement.getDisplay() != null)
                return new AdvancementFrameType[]{AdvancementFrameType.valueOf(advancement.getDisplay().frame().name())};
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends AdvancementFrameType> getReturnType() {
        return AdvancementFrameType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the displayed frame of " + advancements.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        advancements = (Expression<Advancement>) exprs[0];
        return true;
    }
}
