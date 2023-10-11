package me.hotpocket.skriptadvancements.elements.expressions.display.vanilla;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Display - Advancement Icon")
@Description("Allows you to get the displayed icon of an advancement")
@Examples("give player the displayed icon of the advancement \"adventure/root\"")
@Since("1.4")

public class ExprDisplayedIcon extends SimpleExpression<ItemType> {

    static {
        if (Skript.classExists("io.papermc.paper.advancement.AdvancementDisplay"))
            Skript.registerExpression(ExprDisplayedIcon.class, ItemType.class, ExpressionType.SIMPLE,
                    "[the] displayed icon[s] of %advancements%");
    }

    private Expression<Advancement> advancements;

    @Override
    protected @Nullable ItemType[] get(Event e) {
        for (Advancement advancement : advancements.getAll(e))
            if (advancement.getDisplay() != null)
                return new ItemType[]{new ItemType(advancement.getDisplay().icon())};
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the displayed icon of " + advancements.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        advancements = (Expression<Advancement>) exprs[0];
        return true;
    }
}
