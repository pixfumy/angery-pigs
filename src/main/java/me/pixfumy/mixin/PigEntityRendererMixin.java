package me.pixfumy.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.QuadruPedEntityModel;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(PigEntityRenderer.class)
public abstract class PigEntityRendererMixin extends MobEntityRenderer{
    private static final Identifier GOLD_INGOT_TEXTURE = new Identifier("textures/items/gold_ingot.png");
    private static final Identifier GOLD_HELMET_TEXTURE = new Identifier("textures/items/gold_helmet.png");
    public PigEntityRendererMixin(EntityModel entityModel, float f) {
        super(entityModel, f);
    }

    @Inject(method = "method_5779(Lnet/minecraft/entity/passive/PigEntity;IF)I", at = @At("HEAD"), cancellable = true)
    private void renderGoldIngotsAndArmor(PigEntity pigEntity, int i, float f, CallbackInfoReturnable<Integer> cir) {
        if (i == 1 && pigEntity.getStackInHand() != null && pigEntity.getStackInHand().getItem() == Items.GOLD_INGOT) {
            this.bindTexture(GOLD_INGOT_TEXTURE);
            cir.setReturnValue(1);
        } else if (i == 2 && Arrays.stream(pigEntity.getArmorStacks()).anyMatch(
                itemStack -> itemStack != null && itemStack.getItem() instanceof ArmorItem
                        && ((ArmorItem)itemStack.getItem()).materialId == 4 && itemStack != pigEntity.getStackInHand())) {
            this.bindTexture(GOLD_HELMET_TEXTURE);
            cir.setReturnValue(1);
        }
    }
}
