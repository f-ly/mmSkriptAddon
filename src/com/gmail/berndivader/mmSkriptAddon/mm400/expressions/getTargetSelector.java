package com.gmail.berndivader.mmSkriptAddon.mm400.expressions;

import java.util.Optional;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class getTargetSelector extends SimpleExpression<SkillTargeter> {
	private Expression<String> targeterString;

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends SkillTargeter> getReturnType() {
		return SkillTargeter.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean arg2, ParseResult arg3) {
		this.targeterString = (Expression<String>) expr[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event var1, boolean var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Nullable
	protected SkillTargeter[] get(Event e) {
	    Optional<SkillTargeter> maybeTargeter = Optional.empty();
		String targeterName = this.targeterString.getSingle(e);
		targeterName = targeterName.startsWith("@")?targeterName:"@"+targeterName;
		maybeTargeter = Optional.of(AbstractSkill.parseSkillTargeter(targeterName));
		if (maybeTargeter.isPresent()) {
            SkillTargeter targeter = maybeTargeter.get();
            return new SkillTargeter[] {targeter};
		}
		return null;
	}
}
