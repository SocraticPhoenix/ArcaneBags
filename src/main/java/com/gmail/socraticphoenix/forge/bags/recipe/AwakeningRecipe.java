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

import com.gmail.socraticphoenix.forge.bags.item.BagItems;
import com.gmail.socraticphoenix.forge.bags.item.PagedBag;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class AwakeningRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        PagedBag item = null;
        ItemStack bag = ItemStack.EMPTY;
        ItemStack crystal = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (s.getItem() instanceof PagedBag) {
                if (bag.isEmpty()) {
                    bag = s;
                    item = (PagedBag) bag.getItem();
                } else {
                    return false;
                }
            } else if (s.getItem() == BagItems.awakeningCrystal) {
                if (crystal.isEmpty()) {
                    crystal = s;
                } else {
                    return false;
                }
            }
        }
        return !bag.isEmpty() && !item.hasData(bag) && !crystal.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        PagedBag item = null;
        ItemStack bag = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack.getItem() instanceof PagedBag) {
                bag = stack;
                item = (PagedBag) bag.getItem();
                break;
            }
        }
        if(!bag.isEmpty()) {
            ItemStack res = bag.copy();
            item.applyDefaultData(res);
            return res;
        } else {
            return bag;
        }
    }

    @Override
    public int getRecipeSize() {
        return 2;
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
