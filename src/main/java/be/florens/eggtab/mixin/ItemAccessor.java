package be.florens.eggtab.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * This might be possible with an AT but here's why I didn't:
 * - Mixins are now officially in Forge
 * - In Fabric, AWs (=AT) are only used where mixins are insufficient, I like to stay consistent
 * - It's cleaner in my opinion
 * - I just really like mixins
 * - I used this as a test for mixins on Forge
 */
@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor @Mutable
    void setGroup(ItemGroup group);
}
