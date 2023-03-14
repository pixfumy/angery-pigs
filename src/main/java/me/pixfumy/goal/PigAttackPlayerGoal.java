package me.pixfumy.goal;

import me.pixfumy.mixinterface.IPigEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PathAwareEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;

import java.util.Arrays;

public class PigAttackPlayerGoal extends MeleeAttackGoal {
    private int ticksSinceLastRoar = 0;
    public PigAttackPlayerGoal(PathAwareEntity mob, Class<? extends Entity> targetClass, double speed, boolean pauseWhenMobIdle) {
        super(mob, targetClass, speed, pauseWhenMobIdle);
    }

    @Override
    public boolean shouldContinue() {
        if (((IPigEntity)this.mob).getTargetItemEntity() != null) {
            return false;
        }
        if (this.mob.getStackInHand() != null) {
            return false;
        }
        LivingEntity target = this.mob.getTarget();
        if (target == null || !(target instanceof PlayerEntity) || !target.isAlive()) {
            return false;
        }
        PlayerEntity playerEntity = (PlayerEntity) target;
        if ((Arrays.stream(playerEntity.getArmorStacks()).anyMatch((itemStack) -> itemStack != null && ((ArmorItem)itemStack.getItem()).getMaterial() == ArmorItem.Material.GOLD))) {
            return false;
        }
        return super.shouldContinue();
    }

    @Override
    public void tick() {
        ++this.ticksSinceLastRoar;
        if (this.shouldContinue()) {
            super.tick();
            if (this.ticksSinceLastRoar > 150) {
                PigEntity pigEntity = (PigEntity) this.mob;
                pigEntity.playSound("mob.pig.death", 1.2f, (pigEntity.getRandom().nextFloat() - pigEntity.getRandom().nextFloat()) * 0.2f + 0.5f);
                this.ticksSinceLastRoar = 0;
            }
        }
    }
}
