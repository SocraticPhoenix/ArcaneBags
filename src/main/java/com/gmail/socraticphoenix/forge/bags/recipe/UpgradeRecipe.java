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

import com.gmail.socraticphoenix.forge.bags.item.ArcaneBag;
import com.gmail.socraticphoenix.forge.bags.item.BagItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class UpgradeRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ArcaneBag item = null;
        ItemStack bag = ItemStack.EMPTY;
        int pageCount = 0;
        boolean magnet = false;
        boolean soulbound = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ArcaneBag) {
                if (bag.isEmpty()) {
                    bag = stack;
                    item = (ArcaneBag) bag.getItem();
                } else {
                    return false;
                }
            } else if (stack.getItem() == BagItems.arcanePage) {
                pageCount++;
            } else if (stack.getItem() == BagItems.arcaneMagnet) {
                if(magnet) {
                    return false;
                } else {
                    magnet = true;
                }
            } else if (stack.getItem() == BagItems.soulBinder) {
                if(soulbound) {
                    return false;
                } else {
                    soulbound = true;
                }
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        return pageCount > 0 && !bag.isEmpty() && item.hasData(bag);
    }


    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ArcaneBag item = null;
        ItemStack bag = ItemStack.EMPTY;
        int pageCount = 0;
        boolean soulbound = false;
        boolean magnet = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ArcaneBag) {
                bag = stack;
                item = (ArcaneBag) bag.getItem();
            } else if (stack.getItem() == BagItems.arcanePage) {
                pageCount++;
            } else if (stack.getItem() == BagItems.arcaneMagnet) {
                magnet = true;
            } else if (stack.getItem() == BagItems.soulBinder) {
                soulbound = true;
            }
        }

        bag = bag.copy();
        item.applyPages(bag, item.getPages(bag) + pageCount);
        if(soulbound) {
            item.applySoulbound(bag, true);
        }

        if(magnet) {
            item.applyMagnetic(bag, true);
        }

        return bag;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

}
