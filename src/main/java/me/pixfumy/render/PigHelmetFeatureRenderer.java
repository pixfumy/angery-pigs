package me.pixfumy.render;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.render.entity.model.QuadruPedEntityModel;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class PigHelmetFeatureRenderer implements FeatureRenderer<PigEntity>{
    private static final Identifier TEXTURE = new Identifier("textures/models/armor/gold_layer_1.png");
    private final PigEntityModel model = new PigEntityModel(0.5f);
    private final PigEntityRenderer pigRenderer;

    public PigHelmetFeatureRenderer(PigEntityRenderer pigRenderer) {
        this.pigRenderer = pigRenderer;
    }

    @Override
    public void render(PigEntity pigEntity, float f, float g, float h, float i, float j, float k, float l) {
        if (Arrays.stream(pigEntity.getArmorStacks()).allMatch(armorStack -> armorStack == null || armorStack == pigEntity.getStackInHand())) {
            return;
        }
        this.pigRenderer.bindTexture(TEXTURE);
        this.model.copy(this.pigRenderer.getModel());
        this.model.render(pigEntity, f, g, i, j, k, l);
    }

    @Override
    public boolean combineTextures() {
        return false;
    }
}
