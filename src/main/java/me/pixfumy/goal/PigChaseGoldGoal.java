package me.pixfumy.goal;

import me.pixfumy.barter.PigUsableItems;
import me.pixfumy.mixinterface.IPigEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PigChaseGoldGoal extends Goal {
    private PigEntity pigEntity;
    private boolean reached;
    private int tryingTime;
    private double speed;

    public PigChaseGoldGoal(PigEntity pigEntity, double speed) {
        this.pigEntity = pigEntity;
        this.speed = speed;
    }

    @Override
    public boolean canStart() {
        if (this.pigEntity.getStackInHand() != null) {
            return false;
        }
        double d = 10.0D;
        List<ItemEntity> list = this.pigEntity.world.getEntitiesInBox(ItemEntity.class, this.pigEntity.getBoundingBox().expand(d, 4.0, d),
                entity -> !entity.removed && PigUsableItems.isPigUsable(entity.getDataTracker().getStack(10).getItem()));
        if (list.isEmpty()) {
            return false;
        }
        Collections.sort(list, Comparator.comparingDouble(obj -> obj.squaredDistanceTo(PigChaseGoldGoal.this.pigEntity)));
        ((IPigEntity)this.pigEntity).setTargetItemEntity(list.get(0));
        return true;
    }

    @Override
    public void stop() {
        ((IPigEntity)this.pigEntity).setTargetItemEntity(null);
    }

    @Override
    public void tick() {
        BlockPos targetPos = ((IPigEntity)this.pigEntity).getTargetItemEntity().getBlockPos();
        if (this.pigEntity.squaredDistanceToCenter(targetPos.up()) > 1.0) {
            this.reached = false;
            ++this.tryingTime;
            if (this.tryingTime > 5) {
                this.pigEntity.getNavigation().startMovingTo((double)targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5, this.speed);
            }
        } else {
            this.reached = true;
            --this.tryingTime;
        }
    }
}
