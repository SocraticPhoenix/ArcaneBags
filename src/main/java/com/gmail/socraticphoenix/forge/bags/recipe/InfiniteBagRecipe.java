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
package com.gmail.socraticphoenix.forge.bags.recipe;

import com.gmail.socraticphoenix.forge.bags.container.bag.PageWrapper;
import com.gmail.socraticphoenix.forge.bags.item.BagItems;
import com.gmail.socraticphoenix.forge.bags.item.PagedBag;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class InfiniteBagRecipe extends ShapedOreRecipe {

    public InfiniteBagRecipe() {
        super(new ItemStack(BagItems.infiniteBag), "EIE", "IBI", "EIE", 'E', BagItems.compressedEssence, 'I', BagItems.infinityMatrix, 'B', BagItems.arcaneBag);
    }

    @Override
    public Object[] getInput() {
        ItemStack bag = new ItemStack(BagItems.arcaneBag);
        BagItems.arcaneBag.applyDefaultData(bag);
        return new Object[]{new ItemStack(BagItems.compressedEssence), new ItemStack(BagItems.infinityMatrix), new ItemStack(BagItems.compressedEssence),
                new ItemStack(BagItems.infinityMatrix), bag, new ItemStack(BagItems.infinityMatrix),
                new ItemStack(BagItems.compressedEssence), new ItemStack(BagItems.infinityMatrix), new ItemStack(BagItems.compressedEssence)};
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        boolean m = super.matches(inv, world);
        if (m) {
            ItemStack bag = null;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack s = inv.getStackInSlot(i);
                if(s.getItem() instanceof PagedBag) {
                    bag = s;
                    break;
                }
            }

            return BagItems.arcaneBag.hasData(bag);
        } else {
            return false;
        }
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack infinite = this.output.copy();
        ItemStack bag = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (s.getItem() instanceof PagedBag) {
                bag = s;
            }
        }

        if (BagItems.arcaneBag.hasData(bag)) {
            PageWrapper bagInv = BagItems.arcaneBag.getPageWrapper(bag);
            boolean magnetic = BagItems.arcaneBag.isMagnetic(bag);
            boolean soulbound = BagItems.arcaneBag.isSoulBound(bag);

            Map<Integer, ItemStackHandler> pages = new HashMap<>();
            int n = 0;
            for (ItemStackHandler page : bagInv.pages()) {
                pages.put(n, page);
                n++;
            }

            BagItems.infiniteBag.applyData(infinite, pages, soulbound, magnetic);

            if (BagItems.arcaneBag.hasColor(bag)) {
                BagItems.infiniteBag.setColor(infinite, BagItems.arcaneBag.getColor(bag));
            }
        }
        return infinite;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        ItemStack bag = new ItemStack(BagItems.infiniteBag);
        BagItems.infiniteBag.applyDefaultData(bag);
        return bag;
    }
}
