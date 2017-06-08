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
package com.gmail.socraticphoenix.forge.bags.item;

import com.gmail.socraticphoenix.forge.bags.ModArcaneBags;
import com.gmail.socraticphoenix.forge.bags.container.bag.GeneralItemHandler;
import net.minecraft.item.Item;

import java.util.function.Function;

public class BagItems {
    public static ArcaneBag arcaneBag;
    public static InfiniteArcaneBag infiniteBag;

    public static Item compressedEssence;
    public static Item goldPaper;
    public static Item arcanePaper;
    public static Item arcanePage;
    public static Item infinityMatrix;
    public static Item awakeningCrystal;
    public static Item compressionMatrix;
    public static Item arcaneMagnet;
    public static Item soulBinder;

    public static void init() {
        arcaneBag = new ArcaneBag();
        infiniteBag = new InfiniteArcaneBag("infinitebag", () -> new GeneralItemHandler(54, Function.identity(), Function.identity(), k -> !(k.getItem() instanceof PagedBag)));

        compressedEssence = make("compressedessence");
        goldPaper = make("goldpaper");
        arcanePaper = make("arcanepaper");
        arcanePage = make("arcanepage");
        infinityMatrix = make("infinitymatrix");
        awakeningCrystal = make("awakeningcrystal");
        arcaneMagnet = make("arcanemagnet");
        soulBinder = make("soulbinder");

        compressionMatrix = make("compressionmatrix").setMaxDamage(10);
    }

    private static Item make(String name) {
        return new Item().setRegistryName(name).setUnlocalizedName(name).setCreativeTab(ModArcaneBags.TAB_BAGS);
    }

}
