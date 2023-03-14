package me.pixfumy.mixinterface;

import net.minecraft.entity.ItemEntity;

public interface IPigEntity {
    ItemEntity getTargetItemEntity();
    void setTargetItemEntity(ItemEntity entity);
}
