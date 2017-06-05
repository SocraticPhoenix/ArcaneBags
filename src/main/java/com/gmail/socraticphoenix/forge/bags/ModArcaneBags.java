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

import com.gmail.socraticphoenix.forge.bags.block.BagInterfaceTileEntity;
import com.gmail.socraticphoenix.forge.bags.net.BagNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "arcanebags", name = "Arcane Bags")
public class ModArcaneBags {
    public static CreativeTabs TAB_BAGS = new CreativeTabs("arcanebags.bags") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(BagItems.arcaneBag);
        }
    };
    private static ModArcaneBags instance;

    public ModArcaneBags() {
        instance = this;
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new BagClientSideListener());
        }
        MinecraftForge.EVENT_BUS.register(new BagRegistryListener());
    }

    public static ModArcaneBags instance() {
        return instance;
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new BagGuiHandler());
        BagNetworking.initNetwork();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void onPreInit(FMLInitializationEvent ev) {
        GameRegistry.registerTileEntity(BagInterfaceTileEntity.class, "baginterface");
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            return tintIndex > 0 ? -1 : stack.hasTagCompound() && stack.getTagCompound().hasKey("color") ? stack.getTagCompound().getInteger("color") : 0x00FFF6;
        }, BagItems.arcaneBag, BagItems.infiniteBag);
    }


}
