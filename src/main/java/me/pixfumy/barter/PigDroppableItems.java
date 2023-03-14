package me.pixfumy.barter;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Range;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.*;

public class PigDroppableItems {
    private static float TOTAL_WEIGHT;
    private static final TreeMap<Float, BarterEntry> DROPPABLE_ITEMS = new TreeMap<Float, BarterEntry>() {
        {
            TOTAL_WEIGHT = 0;
            put(TOTAL_WEIGHT, new BarterEntry(Items.ENDER_PEARL, Range.between(4, 8)));
            TOTAL_WEIGHT += 0.16;
            put(TOTAL_WEIGHT, new BarterEntry(Items.STRING, Range.between(8, 24)));
            TOTAL_WEIGHT += 0.16;
            put(TOTAL_WEIGHT, new BarterEntry(Item.fromBlock(Blocks.SOULSAND), Range.between(4, 16)));
            TOTAL_WEIGHT += 0.1;
            put(TOTAL_WEIGHT, new BarterEntry(Item.fromBlock(Blocks.GRAVEL), Range.between(8, 16)));
            TOTAL_WEIGHT += 0.1;
            put(TOTAL_WEIGHT, new BarterEntry(Items.LEATHER, Range.between(4, 10)));
            TOTAL_WEIGHT += 0.06;
            put(TOTAL_WEIGHT, new BarterEntry(Items.MAGMA_CREAM, Range.between(2, 6)));
            TOTAL_WEIGHT += 0.06;
            put(TOTAL_WEIGHT, new BarterEntry(Items.GLOWSTONE_DUST, Range.between(5, 12)));
            TOTAL_WEIGHT += 0.06;
            put(TOTAL_WEIGHT, new BarterEntry(Items.IRON_INGOT, Range.between(1, 4)));
            TOTAL_WEIGHT += 0.06;
            put(TOTAL_WEIGHT, new BarterEntry(Items.QUARTZ, Range.between(8, 16)));
            TOTAL_WEIGHT += 0.04;
            put(TOTAL_WEIGHT, new BarterEntry(Items.POTION, Range.between(1, 1)));
            TOTAL_WEIGHT += 0.04;
            put(TOTAL_WEIGHT, new BarterEntry(Items.ARROW, Range.between(4, 8)));
            TOTAL_WEIGHT += 0.04;
            put(TOTAL_WEIGHT, new BarterEntry(Items.SNOWBALL, Range.between(10, 16)));
            TOTAL_WEIGHT += 0.04;
            put(TOTAL_WEIGHT, new BarterEntry(Items.NETHERBRICK, Range.between(4, 16)));
            TOTAL_WEIGHT += 0.04;
            put(TOTAL_WEIGHT, new BarterEntry(Items.FIRE_CHARGE, Range.between(4, 16)));
            TOTAL_WEIGHT += 0.04;
        }
    };

    public static ItemStack getLootDrop() {
        BarterEntry barterEntry = pickItemToDrop();
        return barterEntry.getDropStack();
    }

    private static BarterEntry pickItemToDrop() {
        float i = (new Random()).nextFloat() * TOTAL_WEIGHT;
        float key = DROPPABLE_ITEMS.floorKey(i);
        return DROPPABLE_ITEMS.get(key);
    }

    private static class BarterEntry {
        private Item item;
        private Range<Integer> amountsRange;

        public BarterEntry(Item item, Range amountsRange) {
            this.item = item;
            this.amountsRange = amountsRange;
        }

        public ItemStack getDropStack() {
            Random random = new Random();
            int dropAmount = random.nextInt(amountsRange.getMaximum() - amountsRange.getMinimum() + 1) + amountsRange.getMinimum();
            return new ItemStack(item, dropAmount);
        }
    }
}
