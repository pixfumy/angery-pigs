package me.pixfumy.goal;

import net.minecraft.entity.PathAwareEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;

import java.util.Arrays;

public class PigFollowPlayerGoal extends FollowTargetGoal {
    public PigFollowPlayerGoal(PathAwareEntity mob, Class targetClass, boolean checkVisibility) {
        super(mob, targetClass, checkVisibility);
    }

    @Override
    public boolean shouldContinue() {
        PlayerEntity playerEntity;
        if (this.target == null || !(this.target instanceof PlayerEntity) || !this.target.isAlive()) {
            return false;
        }
        playerEntity = (PlayerEntity) this.target;
        return !(Arrays.stream(playerEntity.getArmorStacks()).anyMatch((itemStack) -> itemStack != null && ((ArmorItem)itemStack.getItem()).getMaterial() == ArmorItem.Material.GOLD));
    }
}
