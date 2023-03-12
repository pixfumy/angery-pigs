package me.pixfumy.goal;

import me.pixfumy.IPigEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PathAwareEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class PigAttackPlayerGoal extends MeleeAttackGoal {
    public PigAttackPlayerGoal(PathAwareEntity mob, Class<? extends Entity> targetClass, double speed, boolean pauseWhenMobIdle) {
        super(mob, targetClass, speed, pauseWhenMobIdle);
    }

    @Override
    public boolean shouldContinue() {
        if (((IPigEntity)this.mob).getTargetItemEntity() != null) {
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
        if (this.shouldContinue()) {
            super.tick();
        }
    }
}
