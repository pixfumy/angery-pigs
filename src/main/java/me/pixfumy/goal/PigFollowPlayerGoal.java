package me.pixfumy.goal;

import me.pixfumy.mixin.access.FollowTargetGoalAccess;
import net.minecraft.entity.PathAwareEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;

import java.util.Arrays;

public class PigFollowPlayerGoal extends FollowTargetGoal {
    public PigFollowPlayerGoal(PathAwareEntity pathAwareEntity, Class class_, int i, boolean bl) {
        super(pathAwareEntity, class_, i, bl);
    }

    @Override
    public boolean shouldContinue() {
        PlayerEntity playerEntity;
        if (((FollowTargetGoalAccess)this).getTarget() == null || !(((FollowTargetGoalAccess)this).getTarget()  instanceof PlayerEntity) || !((FollowTargetGoalAccess)this).getTarget().isAlive()) {
            return false;
        }
        playerEntity = (PlayerEntity) ((FollowTargetGoalAccess)this).getTarget();
        return !(Arrays.stream(playerEntity.getArmorStacks()).anyMatch((itemStack) -> itemStack != null && ((ArmorItem)itemStack.getItem()).materialId == 4));
    }
}
