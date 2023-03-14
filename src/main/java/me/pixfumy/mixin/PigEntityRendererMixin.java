package me.pixfumy.mixin;

import me.pixfumy.render.PigHeldItemRenderer;
import me.pixfumy.render.PigHelmetFeatureRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.passive.PigEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntityRenderer.class)
public abstract class PigEntityRendererMixin extends MobEntityRenderer<PigEntity> {
    public PigEntityRendererMixin(EntityRenderDispatcher entityRenderDispatcher, EntityModel entityModel, float f) {
        super(entityRenderDispatcher, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addNewFeatures(EntityRenderDispatcher entityRenderDispatcher, EntityModel entityModel, float f, CallbackInfo ci) {
        this.addFeature(new PigHelmetFeatureRenderer((PigEntityRenderer)(Object)this));
        this.addFeature(new PigHeldItemRenderer((PigEntityRenderer)(Object)this));
    }
}
