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
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Display - Advancement Title")
@Description("Allows you to get the displayed title of an advancement")
@Examples("broadcast displayed title of the advancement \"adventure/root\"")
@Since("1.4")

public class ExprDisplayedTitle extends SimpleExpression<String> {

    static {
        if (Skript.classExists("io.papermc.paper.advancement.AdvancementDisplay"))
            Skript.registerExpression(ExprDisplayedTitle.class, String.class, ExpressionType.SIMPLE,
                    "[the] displayed title[s] of %advancements%");
    }

    private Expression<Advancement> advancements;

    @Override
    protected @Nullable String[] get(Event e) {
        for (Advancement advancement : advancements.getAll(e))
            if (advancement.getDisplay() != null)
                return new String[]{Bukkit.getUnsafe().legacyComponentSerializer().serialize(advancement.getDisplay().title())};
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
        return "the displayed title of " + advancements.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        advancements = (Expression<Advancement>) exprs[0];
        return true;
    }
}
