package com.gmail.berndivader.mmSkriptAddon.mm400.effects.mythicspawner;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;

public class ActivateMythicSpawner extends Effect {
	private Expression<MythicSpawner> skriptSpawner;
	private Boolean bool;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean var3, ParseResult var4) {
		skriptSpawner = (Expression<MythicSpawner>) expr[0];
		bool = matchedPattern == 0 ? true : false;
		return true;
	}

	@Override
	public String toString(@Nullable Event var1, boolean var2) {
		return null;
	}

	@Override
	protected void execute(Event var1) {
		if (bool) {
			skriptSpawner.getSingle(var1).ActivateSpawner();
		} else skriptSpawner.getSingle(var1).Disable();
	}
}
