/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.forge.bags;

import com.gmail.socraticphoenix.forge.bags.block.BagBlocks;
import com.gmail.socraticphoenix.forge.bags.item.BagItems;
import com.gmail.socraticphoenix.forge.bags.net.BagNetworking;
import com.gmail.socraticphoenix.forge.bags.proxy.BagProxy;
import com.gmail.socraticphoenix.forge.bags.recipe.AwakeningRecipe;
import com.gmail.socraticphoenix.forge.bags.recipe.BagDyeRecipe;
import com.gmail.socraticphoenix.forge.bags.recipe.CompressionRecipe;
import com.gmail.socraticphoenix.forge.bags.recipe.UpgradeRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = "arcanebags", name = "Arcane Bags")
public class ModArcaneBags {
    public static CreativeTabs TAB_BAGS = new CreativeTabs("arcanebags.bags") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(BagItems.arcaneBag);
        }
    };
    private static ModArcaneBags instance;

    @SidedProxy(clientSide = "com.gmail.socraticphoenix.forge.bags.proxy.BagClientProxy", serverSide = "com.gmail.socraticphoenix.forge.bags.proxy.BagServerProxy")
    private static BagProxy proxy;

    public ModArcaneBags() {
        instance = this;
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new BagClientSideListener());
        }
        MinecraftForge.EVENT_BUS.register(new BagRegistryListener());
        MinecraftForge.EVENT_BUS.register(new BagAbilityListener());
    }

    public static ModArcaneBags instance() {
        return instance;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) {
        proxy.onPreInit(ev);

        RecipeSorter.register("arcanebags:dye_recipe", BagDyeRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        RecipeSorter.register("arcanebags:awakening", AwakeningRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        RecipeSorter.register("arcanebags:upgrade", UpgradeRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        RecipeSorter.register("arcanebags:compress", CompressionRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        CraftingManager manager = CraftingManager.getInstance();
        manager.addRecipe(new BagDyeRecipe());
        manager.addRecipe(new AwakeningRecipe());
        manager.addRecipe(new UpgradeRecipe());
        manager.addRecipe(new CompressionRecipe());

        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.magicalEssence), "DOD", "OEO", "DOD", 'D', "gemDiamond", 'O', "obsidian", 'E', Items.ENDER_EYE));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.compressionMatrix), "DPD", "PBP", "DPD", 'D', "gemDiamond", 'P', Blocks.PISTON, 'B', Items.BUCKET));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.goldPaper), "GPG", "GPG", "GPG", 'G', "ingotGold", 'P', "paper"));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.arcanePaper), "EEE", "EGE", "EEE", 'E', BagItems.compressedEssence, 'G', BagItems.goldPaper));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.arcanePage), "PLP", "PDP", "PLP", 'P', BagItems.arcanePaper, 'L', "leather", 'D', "gemDiamond"));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.infinityMatrix), "NNN", "NAN", "NNN", 'N', "netherStar", 'A', BagItems.arcanePage));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.arcaneBag), "WSW", "WAW", "WWW", 'W', Blocks.WOOL, 'S', "string", 'A', BagItems.arcanePage));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.infiniteBag), "EIE", "IBI", "EIE", 'E', BagItems.compressedEssence, 'I', BagItems.infinityMatrix, 'B', BagItems.arcaneBag));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.awakeningCrystal), "DLD", "LEL", "DLD", 'D', "gemDiamond", 'L', "gemLapis", 'E', BagItems.compressedEssence));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagBlocks.bagInterface), "CSC", "SES", "CSC", 'S', "cobblestone", 'C', BagItems.compressionMatrix, 'E', BagItems.compressedEssence));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.arcaneMagnet), "IRI", "ECE", "IRI", 'I', "ingotIron", 'R', "dustRedstone", 'C', Items.COMPASS, 'E', BagItems.compressedEssence));
        manager.addRecipe(new ShapedOreRecipe(new ItemStack(BagItems.soulBinder), "GDG", "GIG", "GDG", 'G', "ingotGold", 'D', "gemDiamond", 'I', BagItems.infinityMatrix));
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new BagGuiHandler());
        BagNetworking.initNetwork();
        proxy.onInit(ev);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent ev) {
        proxy.onPostInit(ev);
    }

}
