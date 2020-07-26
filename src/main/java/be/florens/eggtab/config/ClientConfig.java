package be.florens.eggtab.config;

import be.florens.eggtab.EggTab;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientConfig {

    final BooleanValue eggsGroup;
    final BooleanValue booksGroup;

    ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("eggtab");

        eggsGroup = builder
                .comment("Move spawn eggs to seperate tab")
                .translation(EggTab.MODID + ".config.eggs_group")
                .define("eggs_group", true);
        booksGroup = builder
                .comment("Move enchanted books to seperate tab")
                .translation(EggTab.MODID + ".config.books_group")
                .define("books_group", true);

        builder.pop();
    }
}