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
    private PigEntity pig;

    public PigAttackPlayerGoal(PathAwareEntity mob, Class<? extends Entity> targetClass, double speed, boolean pauseWhenMobIdle) {
        super(mob, targetClass, speed, pauseWhenMobIdle);
        this.pig = (PigEntity) mob;
    }

    @Override
    public boolean shouldContinue() {
        if (((IPigEntity)this.pig).getTargetItemEntity() != null) {
            return false;
        }
        if (this.pig.getStackInHand() != null) {
            return false;
        }
        LivingEntity target = this.pig.getTarget();
        if (target == null || !(target instanceof PlayerEntity) || !target.isAlive()) {
            return false;
        }
        PlayerEntity playerEntity = (PlayerEntity) target;
        if ((Arrays.stream(playerEntity.getArmorStacks()).anyMatch((itemStack) -> itemStack != null && ((ArmorItem)itemStack.getItem()).materialId == 4))) {
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
                this.pig.playSound("mob.pig.death", 1.2f, (this.pig.getRandom().nextFloat() - this.pig.getRandom().nextFloat()) * 0.2f + 0.5f);
                this.ticksSinceLastRoar = 0;
            }
        }
    }
}
