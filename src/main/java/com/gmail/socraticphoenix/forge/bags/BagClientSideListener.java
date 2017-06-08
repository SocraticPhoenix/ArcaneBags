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
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BagClientSideListener {

    @SideOnly(Side.CLIENT)
    private static void r(Item item, String loc) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("arcanebags:" + loc, "inventory"));
    }

    @SideOnly(Side.CLIENT)
    private static void r(Block block, String loc) {
        r(Item.getItemFromBlock(block), loc);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelLoad(ModelRegistryEvent ev) {
        r(BagItems.arcaneBag, "arcanebag");
        r(BagItems.infiniteBag, "infinitebag");
        r(BagItems.compressedEssence, "compressedessence");
        r(BagItems.goldPaper, "goldpaper");
        r(BagItems.arcanePage, "arcanepage");
        r(BagItems.arcanePaper, "arcanepaper");
        r(BagItems.infinityMatrix, "infinitymatrix");
        r(BagItems.awakeningCrystal, "awakeningcrystal");
        r(BagItems.compressionMatrix, "compressionmatrix");
        r(BagItems.arcaneMagnet, "arcanemagnet");
        r(BagItems.soulBinder, "soulbinder");

        r(BagBlocks.bagInterface, "baginterface");
        r(BagBlocks.magicalEssence, "magicalessence");
    }

}
