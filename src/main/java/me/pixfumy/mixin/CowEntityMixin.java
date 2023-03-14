package me.pixfumy.mixin;

import me.pixfumy.goal.PigAttackPlayerGoal;
import me.pixfumy.goal.PigBarterGoal;
import me.pixfumy.goal.PigChaseGoldGoal;
import me.pixfumy.goal.PigFollowPlayerGoal;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PathAwareEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends MobEntity {

    public CowEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addFightingPigGoals(World world, CallbackInfo ci) {
        this.attackGoals.add(1, new FollowTargetGoal<PigEntity>((PathAwareEntity)(Object)this, PigEntity.class, true));
        this.goals.add(2, new MeleeAttackGoal((PathAwareEntity) (Object)this, PigEntity.class, 1.25F, false));
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
}
