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
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class BagRegistryListener {

    @SubscribeEvent
    public void onBlockRegister(RegistryEvent.Register<Block> ev) {
        BagBlocks.init();
        r(ev, BagBlocks.bagInterface);
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> ev) {
        BagItems.init();
        r(ev, BagItems.arcaneBag);
        r(ev, BagItems.infiniteBag);
        r(ev, BagItems.magicalEssence);
        r(ev, BagItems.compressedEssence);
        r(ev, BagItems.goldPaper);
        r(ev, BagItems.arcanePaper);
        r(ev, BagItems.arcanePage);
        r(ev, BagItems.infinityMatrix);
        r(ev, BagItems.awakeningCrystal);
        r(ev, BagItems.compressionMatrix);
        r(ev, BagItems.arcaneMagnet);
        r(ev, BagItems.soulBinder);
        r(ev, new ItemBlock(BagBlocks.bagInterface).setRegistryName("baginterface"));
    }

    private static <T extends IForgeRegistryEntry<T>> void r(RegistryEvent.Register<T> ev, T val) {
        ev.getRegistry().register(val);
    }

}
