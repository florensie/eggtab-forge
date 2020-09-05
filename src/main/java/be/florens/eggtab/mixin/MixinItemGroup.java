package be.florens.eggtab.mixin;

import be.florens.eggtab.EggTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemGroup.class)
public class MixinItemGroup {

    /**
     * Fix mods that have a custom fillItemGroup implementation (to change order for example)
     * This would otherwise not remove the spawn eggs from the original creative tab
     */
    @Redirect(method = "fill", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;fillItemGroup(Lnet/minecraft/item/ItemGroup;Lnet/minecraft/util/NonNullList;)V"))
    private void fixCustomFill(Item item, ItemGroup itemGroup, NonNullList<ItemStack> items) {
        if (itemGroup != EggTab.EGG_GROUP && EggTab.isSpawnEgg(item)) {
            // Don't run fillItemGroup for spawn eggs in the wrong item group
            return;
        }

        // Default behaviour
        item.fillItemGroup(itemGroup, items);
    }
}
