package me.pixfumy.mixin.access;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FollowTargetGoal.class)
public interface FollowTargetGoalAccess {
    @Accessor
    LivingEntity getTarget();
}
