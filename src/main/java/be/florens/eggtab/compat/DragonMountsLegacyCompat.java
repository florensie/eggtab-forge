package be.florens.eggtab.compat;

import net.minecraft.util.IItemProvider;
import wolfshotz.dml.misc.LazySpawnEggItem;

public class DragonMountsLegacyCompat {

    public static boolean isSpawnEggItem(IItemProvider itemProvider) {
        return itemProvider.asItem() instanceof LazySpawnEggItem;
    }
}
