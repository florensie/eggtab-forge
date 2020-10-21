package be.florens.eggtab;

import be.florens.eggtab.compat.DragonMountsLegacyCompat;
import be.florens.eggtab.config.Config;
import be.florens.eggtab.mixin.ItemAccessor;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.*;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Mod(EggTab.MOD_ID)
public class EggTab {
    public static final String MOD_ID = "eggtab";
    private static final Logger LOGGER = LogManager.getLogger();

    public static ItemGroup EGG_GROUP;
    public static ItemGroup BOOK_GROUP;

    public EggTab() {
        // Lifecycle
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        // Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);

        // Remove incompatible server mods warning since we're client-side only
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    private void setup(final FMLClientSetupEvent event) {
        // Spawn Eggs group
        if (Config.eggsGroup) {
            LOGGER.info("Moving spawn eggs");
            EGG_GROUP = new ItemGroup(MOD_ID + ".egg_group") {
                @OnlyIn(Dist.CLIENT)
                public ItemStack createIcon() {
                    return new ItemStack(Items.CREEPER_SPAWN_EGG);
                }
            };

            // Note: We get them from the registry because SpawnEggItem.getEggs is missing eggs in certain cases somehow (see endergetic)
            ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
                if (isSpawnEgg(entry.getValue())) {
                    LOGGER.info("Egged: " + Objects.requireNonNull(getRegsistryEntryIdentifier(entry)).toString());
                    ((ItemAccessor) entry.getValue()).setGroup(EGG_GROUP);
                }
            });
        }

        // Enchanted Books group
        if (Config.booksGroup) {
            LOGGER.info("Moving enchanted books");

            // Remove enchantments from all groups
            Arrays.stream(ItemGroup.GROUPS).forEach(ItemGroup::setRelevantEnchantmentTypes);

            BOOK_GROUP = new ItemGroup(MOD_ID + ".book_group") {
                @OnlyIn(Dist.CLIENT)
                public ItemStack createIcon() {
                    return new ItemStack(Items.ENCHANTED_BOOK);
                }
            }.setRelevantEnchantmentTypes(EnchantmentType.values()); // Add all EnchantmentTypes
        }
    }

    public static boolean isSpawnEgg(Item item) {
        boolean isSpawnEgg = item instanceof SpawnEggItem;

        // Compat
        if (!isSpawnEgg && ModList.get().isLoaded("dragonmounts")) {
            isSpawnEgg = DragonMountsLegacyCompat.isSpawnEggItem(item);
        }

        return isSpawnEgg;
    }

    /**
     * Multi-version boogaloo
     */
    public static ResourceLocation getRegsistryEntryIdentifier(Map.Entry<?, ?> entry) {
        if (entry.getKey() instanceof ResourceLocation) { // < 1.16.1
            return (ResourceLocation) entry.getKey();
        } else if (entry.getKey() instanceof RegistryKey) { // > 1.16.2
            //noinspection rawtypes
            RegistryKey key = (RegistryKey) entry.getKey();
            return key.func_240901_a_();
        }
        return null;
    }
}
