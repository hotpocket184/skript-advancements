package me.hotpocket.skriptadvancements.elements.expressions.display.vanilla;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Vanilla Advancement Display")
@Description("Returns the specified display of advancements")
@Examples("broadcast frame of advancement \"adventure/root\"")
@Since("1.4")

public class ExprAdvancementDisplay extends SimplePropertyExpression<Advancement, Object> {

    static {
        if (Skript.classExists("io.papermc.paper.advancement.AdvancementDisplay"))
            register(ExprAdvancementDisplay.class, Object.class,
                    "[displayed] (title|1:description|2:display name|3:icon|4:frame|5:toast|6:announcement|7:visibility)", "advancements");
    }

    private int pattern;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.pattern = parseResult.mark;
        setExpr((Expression<? extends Advancement>) exprs[0]);
        return true;
    }

    @Override
    public @Nullable Object convert(Advancement advancement) {
        return getValue(advancement);
    }

    private Object getValue(Advancement advancement) {
        switch (pattern) {
            case 0 -> {
                if (advancement.getDisplay().title() != null)
                    return LegacyComponentSerializer.legacyAmpersand().serialize(advancement.getDisplay().title());
            }
            case 1 -> {
                if (advancement.getDisplay().description() != null)
                    return LegacyComponentSerializer.legacyAmpersand().serialize(advancement.getDisplay().description());
            }
            case 2 -> {
                if (advancement.getDisplay().displayName() != null)
                    return LegacyComponentSerializer.legacyAmpersand().serialize(advancement.getDisplay().displayName());
            }
            case 3 -> {
                if (advancement.getDisplay().icon() != null)
                    return new ItemType(advancement.getDisplay().icon());
            }
            case 4 -> {
                if (advancement.getDisplay().frame() != null)
                    return advancement.getDisplay().frame();
            }
            case 5 -> {
                return advancement.getDisplay().doesShowToast();
            }
            case 6 -> {
                return advancement.getDisplay().doesAnnounceToChat();
            }
            case 7 -> {
                return !advancement.getDisplay().isHidden();
            }
            default -> {
                return null;
            }
        }
        return null;
    }

    @Override
    public @NotNull Class<? extends Object> getReturnType() {
        switch (pattern) {
            case 0, 1, 2 -> { return String.class; }
            case 3 -> { return ItemType.class; }
            case 4 -> { return AdvancementDisplay.Frame.class; }
            case 5,6,7 -> { return Boolean.class; }
            default -> { return null; }
        }
    }

    @Override
    protected @NotNull String getPropertyName() {
        switch (pattern) {
            case 0 -> {
                return "title";
            }
            case 1 -> {
                return "description";
            }
            case 2 -> {
                return "display name";
            }
            case 3 -> {
                return "icon";
            }
            case 4 -> {
                return "frame";
            }
            case 5 -> {
                return "toast";
            }
            case 6 -> {
                return "announcement";
            }
            case 7 -> {
                return "visibility";
            }
            default -> {
                return null;
            }
        }
    }
}
