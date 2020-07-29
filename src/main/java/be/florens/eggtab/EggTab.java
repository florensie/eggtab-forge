package be.florens.eggtab;

import be.florens.eggtab.config.Config;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EggTab.MOD_ID)
public class EggTab {
    public static final String MOD_ID = "eggtab";
    public static final Logger LOGGER = LogManager.getLogger();

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
            EGG_GROUP = new ItemGroup(MOD_ID + ".egg_group") {
                @OnlyIn(Dist.CLIENT)
                public ItemStack createIcon() {
                    return new ItemStack(Items.CREEPER_SPAWN_EGG);
                }
            };
        }

        // Enchanted Books group
        if (Config.booksGroup) {
            BOOK_GROUP = new ItemGroup(MOD_ID + ".book_group") {
                @OnlyIn(Dist.CLIENT)
                public ItemStack createIcon() {
                    return new ItemStack(Items.ENCHANTED_BOOK);
                }
            };
        }
    }
}
