package com.gmail.berndivader.mmSkriptAddon.mm400.effects;

import java.util.HashSet;
import java.util.Optional;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmSkriptAddon.ActivePlayer;
import com.gmail.berndivader.mmSkriptAddon.Main;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

public class MakePlayerCastSkill extends Effect {
	
	private Expression<Entity> skriptPlayer;
	private Expression<Entity> skriptTrigger;
	private Expression<String> skriptSkill;
	private Expression<Entity> skriptTarget;
	private Expression<Location> skriptLocation;
	private boolean bool;
	private boolean self;
	private Expression<Number> skriptDelay;
	private Expression<Number> skriptTimer;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean var3, ParseResult var4) {
		bool = matchedPattern==0?true:false;
		self = matchedPattern==2?true:false;
		skriptPlayer = (Expression<Entity>) expr[0];
		skriptSkill = (Expression<String>) expr[1];
		skriptTrigger = (Expression<Entity>) expr[2];
		if (self) {
			skriptDelay = (Expression<Number>) expr[3];
			skriptTimer = (Expression<Number>) expr[4];
			return true;
		}
		skriptDelay = (Expression<Number>) expr[4];
		skriptTimer = (Expression<Number>) expr[5];
		if (bool) {
			skriptTarget = (Expression<Entity>) expr[3];
		} else {
			skriptLocation = (Expression<Location>) expr[3];
		}
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean var2) {
		return null;
	}

	@Override
	protected void execute(Event e) {
		Entity caster = skriptPlayer.getSingle(e);
		Entity trigger = skriptTrigger.getSingle(e);
		Entity etarget = null;
		int ttimer = skriptTimer.getSingle(e).intValue();
		long tdelay = skriptDelay.getSingle(e).longValue();
		Location ltarget = null;
		if (bool) {
			etarget = skriptTarget.getSingle(e);
		} else if (!self){
			ltarget = skriptLocation.getSingle(e);
		}
		if (self) etarget = caster;
		String skill = skriptSkill.getSingle(e);
        HashSet<AbstractEntity> eTargets = new HashSet<AbstractEntity>();
        HashSet<AbstractLocation> lTargets = new HashSet<AbstractLocation>();
        if (etarget != null) eTargets.add(BukkitAdapter.adapt(etarget));
        if (ltarget != null) lTargets.add(BukkitAdapter.adapt(ltarget));
		castSkillFromPlayer(caster, skill, trigger, caster.getLocation(), eTargets, lTargets, 1.0f, ttimer, tdelay);
	}

	private boolean castSkillFromPlayer(Entity e, String skillName, Entity trigger, 
			Location origin, HashSet<AbstractEntity> feTargets, HashSet<AbstractLocation> flTargets, float power,
			int ttimer, long tdelay) {

        Optional<Skill> maybeSkill = MythicMobs.inst().getSkillManager().getSkill(skillName);
        if (!maybeSkill.isPresent()) {
            return false;
        }
        ActivePlayer ap = new ActivePlayer(e);
        Skill skill = maybeSkill.get();
		SkillMetadata data;
        if (skill.usable(data = new SkillMetadata(SkillTrigger.API, ap, BukkitAdapter.adapt(trigger), BukkitAdapter.adapt(origin), feTargets, flTargets, power), SkillTrigger.API)) {
        	new BukkitRunnable() {
        		int timer = ttimer;
        		public void run() {
        			if (timer!=-1) {
        				skill.execute(data);
        				timer--;
        			} else {
        				this.cancel();
        			}
                } 
            }.runTaskTimer(Main.plugin, 0, tdelay);
        }
		return true;
	}
}
