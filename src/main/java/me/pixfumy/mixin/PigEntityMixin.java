package me.pixfumy.mixin;

import me.pixfumy.barter.PigUsableItems;
import me.pixfumy.mixinterface.IPigEntity;
import me.pixfumy.goal.PigAttackPlayerGoal;
import me.pixfumy.goal.PigBarterGoal;
import me.pixfumy.goal.PigChaseGoldGoal;
import me.pixfumy.goal.PigFollowPlayerGoal;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PathAwareEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends MobEntity implements IPigEntity {
    @Shadow protected abstract String getHurtSound();

    @Shadow protected abstract String getAmbientSound();

    @Shadow protected abstract String getDeathSound();

    private ItemEntity targetItemEntity;

    public PigEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addPiglinGoals(World world, CallbackInfo ci) {
        this.goals.add(-1, new PigBarterGoal((PigEntity)(Object)this));
        this.goals.add(0, new PigChaseGoldGoal((PigEntity)(Object)this, 1.25f));
        this.attackGoals.add(1, new PigFollowPlayerGoal((PathAwareEntity)(Object)this, PlayerEntity.class, false));
        this.goals.add(2, new PigAttackPlayerGoal((PigEntity)(Object)this, PlayerEntity.class, 1.25F, false));
        this.attackGoals.add(1, new FollowTargetGoal<CowEntity>((PathAwareEntity)(Object)this, CowEntity.class, true));
        this.goals.add(2, new MeleeAttackGoal((PathAwareEntity) (Object)this, CowEntity.class, 1.25F, false));
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 1))
    private void removeEscapeDangerGoal(GoalSelector instance, int priority, Goal goal) {

    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl;
        float f = 5.0F;
        int i = 0;
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.getStackInHand(), ((LivingEntity)target).getGroup());
            i += EnchantmentHelper.getKnockback(this);
        }
        if (bl = target.damage(DamageSource.mob(this), f)) {
            int j;
            if (i > 0) {
                target.addVelocity(-MathHelper.sin(this.yaw * (float)Math.PI / 180.0f) * (float)i * 0.5f, 0.1, MathHelper.cos(this.yaw * (float)Math.PI / 180.0f) * (float)i * 0.5f);
                this.velocityX *= 0.6;
                this.velocityZ *= 0.6;
            }
            if ((j = EnchantmentHelper.getFireAspect(this)) > 0) {
                target.setOnFireFor(j * 4);
            }
            this.playSound(getDeathSound(), 1.2f, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2f + 0.5f);
            this.dealDamage(this, target);
        }
        return bl;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.world.profiler.push("lootingGold");
        if (!this.world.isClient && !this.dead && this.world.getGameRules().getBoolean("mobGriefing")) {
            List<ItemEntity> list = this.world.getEntitiesInBox(ItemEntity.class, this.getBoundingBox().expand(1.0, 0.0, 1.0),
                    itemEntity -> !itemEntity.removed && this.getStackInHand() == null && PigUsableItems.isPigUsable(itemEntity.getItemStack().getItem()));
            for (ItemEntity itemEntity : list) {
                if (itemEntity.removed || itemEntity.cannotPickup()) continue;
                if (--itemEntity.getItemStack().count <= 0) {
                    itemEntity.removed = true;
                }
                this.setArmorSlot(0, new ItemStack(itemEntity.getItemStack().getItem(), 1));
                this.playSound(this.getAmbientSound(), this.getSoundVolume() + 0.2f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.5f);
            }
        }
        this.world.profiler.pop();
    }

    @Override
    public ItemEntity getTargetItemEntity() {
        return this.targetItemEntity;
    }

    @Override
    public void setTargetItemEntity(ItemEntity entity) {
        this.targetItemEntity = entity;
    }


}
