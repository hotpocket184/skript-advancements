package me.hotpocket.skriptadvancements.elements.expressions.display.vanilla;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprDisplayedDescription extends SimpleExpression<String> {

    static {
        if (Skript.classExists("io.papermc.paper.advancement.AdvancementDisplay"))
            Skript.registerExpression(ExprDisplayedDescription.class, String.class, ExpressionType.SIMPLE,
                    "[the] displayed description[s] of %advancements%");
    }

    private Expression<Advancement> advancements;

    @Override
    protected @Nullable String[] get(Event e) {
        for (Advancement advancement : advancements.getAll(e))
            if (advancement.getDisplay() != null)
                return new String[]{Bukkit.getUnsafe().legacyComponentSerializer().serialize(advancement.getDisplay().description())};
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the displayed description of " + advancements.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        advancements = (Expression<Advancement>) exprs[0];
        return true;
    }
}
