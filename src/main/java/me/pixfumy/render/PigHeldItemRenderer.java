package me.pixfumy.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class PigHeldItemRenderer implements FeatureRenderer<LivingEntity> {
    private final LivingEntityRenderer<?> entityRenderer;

    public PigHeldItemRenderer(LivingEntityRenderer<?> entityRenderer) {
        this.entityRenderer = entityRenderer;
    }

    @Override
    public void render(LivingEntity entity, float handSwing, float handSwingAmount, float tickDelta, float age, float headYaw, float headPitch, float scale) {
        ItemStack itemStack = entity.getStackInHand();
        if (itemStack == null) {
            return;
        }
        GlStateManager.pushMatrix();
        if (this.entityRenderer.getModel().child) {
            float f = 0.5f;
            GlStateManager.translatef(0.0f, 0.625f, 0.0f);
            GlStateManager.rotatef(-20.0f, -1.0f, 0.0f, 0.0f);
            GlStateManager.scalef(f, f, f);
        }
        GlStateManager.translatef(-0.0625f, 0.4375f, 0.0625f);
        Item item = itemStack.getItem();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (item instanceof BlockItem && Block.getBlockFromItem(item).getBlockType() == 2) {
            GlStateManager.translatef(0.0f, 0.1875f, -0.3125f);
            GlStateManager.rotatef(20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotatef(45.0f, 0.0f, 1.0f, 0.0f);
            float g = 0.375f;
            GlStateManager.scalef(-g, -g, g);
        }
        if (entity.isSneaking()) {
            GlStateManager.translatef(0.0f, 0.203125f, 0.0f);
        }
        minecraftClient.getHeldItemRenderer().renderItem(entity, itemStack, ModelTransformation.Mode.THIRD_PERSON);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean combineTextures() {
        return false;
    }
}
