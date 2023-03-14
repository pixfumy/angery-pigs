package me.pixfumy.goal;

import com.google.common.base.Predicates;
import me.pixfumy.barter.PigDroppableItems;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.predicate.EntityPredicate;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.RandomVectorGenerator;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PigBarterGoal extends Goal {
    private final PigEntity pigEntity;
    private int tradingTime = 0;
    private Vec3d wanderingTarget;
    public PigBarterGoal(PigEntity pigEntity) {
        this.pigEntity = pigEntity;
    }

    @Override
    public boolean canStart() {
        return this.pigEntity.getStackInHand() != null;
    }

    @Override
    public boolean shouldContinue() {
        return this.tradingTime <= 120;
    }

    @Override
    public void start() {
        this.wanderingTarget = Optional.of(RandomVectorGenerator.method_2799(this.pigEntity, 7, 7)).orElse(this.pigEntity.getPos());
    }

    @Override
    public void tick() {
        this.tradingTime++;
        this.pigEntity.getNavigation().startMovingTo(wanderingTarget.x, wanderingTarget.y, wanderingTarget.z, 0.5F);
    }

    @Override
    public void stop() {
        this.tradingTime = 0;
        ItemStack heldItemStack = this.pigEntity.getStackInHand();
        if (!this.pigEntity.isBaby() && heldItemStack.getItem() == Items.GOLD_INGOT) {
            ItemStack itemStackToDrop = PigDroppableItems.getLootDrop();
            ItemEntity itemEntity = new ItemEntity(this.pigEntity.world, this.pigEntity.x, this.pigEntity.y + 1.0, this.pigEntity.z, itemStackToDrop) {
                @Override
                public void onPlayerCollision(PlayerEntity playerEntity) {
                    if (!this.world.isClient) {
                        playerEntity.damage(DamageSource.GENERIC, 1.0F);
                    }
                }
            };
            itemEntity.setToDefaultPickupDelay();
            this.pigEntity.world.spawnEntity(itemEntity);
            List<PlayerEntity> list = this.pigEntity.world.getEntitiesInBox(PlayerEntity.class, this.pigEntity.getBoundingBox().expand(10, 4.0, 10), EntityPredicate.EXCEPT_SPECTATOR);
            Collections.sort(list, Comparator.comparingDouble(player -> this.pigEntity.distanceTo(player)));
            if (!list.isEmpty()) {
                PlayerEntity nearestPlayer = list.get(0);
                itemEntity.velocityX += (nearestPlayer.x - this.pigEntity.x) * 0.1f;
                itemEntity.velocityY += (nearestPlayer.y - this.pigEntity.y) * 0.1f;
                itemEntity.velocityZ += (nearestPlayer.z - this.pigEntity.z) * 0.1f;
            } else {
                itemEntity.velocityX += (this.pigEntity.getRandom().nextInt(3) - 1) * 0.1f;
                itemEntity.velocityZ += (this.pigEntity.getRandom().nextInt(3) - 1) * 0.1f;
            }
            pigEntity.playSound("mob.pig.death", 1.2f, (pigEntity.getRandom().nextFloat() - pigEntity.getRandom().nextFloat()) * 0.2f + 2.0f);
        } else if (heldItemStack.getItem() instanceof ArmorItem) {
            this.pigEntity.setArmorSlot(((ArmorItem) heldItemStack.getItem()).slot+1, heldItemStack);
        }
        this.pigEntity.setArmorSlot(0, null);
    }
}
