package be.florens.eggtab.mixin;

import be.florens.eggtab.config.Config;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static be.florens.eggtab.EggTab.*    ;

/**
 * Someone is going to kill me for this
 */
@Mixin(value = ForgeRegistry.class, remap = false)
public abstract class MixinForgeRegistry<V> {

    @Shadow public abstract Set<Map.Entry<ResourceLocation, V>> getEntries();

    @Shadow public abstract Class<V> getRegistrySuperType();

    @Inject(method = "freeze", at = @At("HEAD"))
    public void freeze(CallbackInfo info) {

        if (getRegistrySuperType() == Item.class) {
            // Spawn Eggs group
            if (Config.eggsGroup) {
                LOGGER.info("Moving spawn eggs");

                // Note: We get them from the registry because SpawnEggItem.getEggs is missing eggs in certain cases somehow (see endergetic)
                getEntries().forEach(entry -> {
                    if (entry.getValue() instanceof SpawnEggItem)  {
                        LOGGER.info("Egged: " + entry.getKey().toString());
                        ((ItemAccessor) entry.getValue()).setGroup(EGG_GROUP);
                    }
                });
            }

            // Enchanted Books group
            if (Config.booksGroup) {
                LOGGER.info("Moving enchanted books");

                // Remove enchantments from all groups
                Arrays.stream(ItemGroup.GROUPS).forEach(ItemGroup::setRelevantEnchantmentTypes);

                // Add all EnchantmentTypes
                BOOK_GROUP.setRelevantEnchantmentTypes(EnchantmentType.values());
            }
        }
    }
}
