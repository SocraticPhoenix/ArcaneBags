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

import com.gmail.socraticphoenix.forge.bags.item.PagedBag;
import com.google.common.collect.Lists;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.List;

public class BagDyeRecipe implements IRecipe {

    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack itemstack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof PagedBag) {
                    itemstack = itemstack1;
                } else {
                    if (!(itemstack1.getItem() instanceof ItemDye)) {
                        return false;
                    }

                    list.add(itemstack1);
                }
            }
        }

        return !itemstack.isEmpty() && !list.isEmpty();
    }

    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        int[] aint = new int[3];
        int i = 0;
        int j = 0;
        PagedBag pagedBag = null;

        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof PagedBag) {
                    pagedBag = (PagedBag) itemstack1.getItem();

                    itemstack = itemstack1.copy();
                    itemstack.setCount(1);

                    if (pagedBag.hasColor(itemstack1)) {
                        int l = pagedBag.getColor(itemstack);
                        float f = (float) (l >> 16 & 255) / 255.0F;
                        float f1 = (float) (l >> 8 & 255) / 255.0F;
                        float f2 = (float) (l & 255) / 255.0F;
                        i = (int) ((float) i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int) ((float) aint[0] + f * 255.0F);
                        aint[1] = (int) ((float) aint[1] + f1 * 255.0F);
                        aint[2] = (int) ((float) aint[2] + f2 * 255.0F);
                        ++j;
                    }
                } else {
                    if (!(itemstack1.getItem() instanceof ItemDye)) {
                        return ItemStack.EMPTY;
                    }

                    float[] afloat = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(itemstack1.getMetadata()));
                    int l1 = (int) (afloat[0] * 255.0F);
                    int i2 = (int) (afloat[1] * 255.0F);
                    int j2 = (int) (afloat[2] * 255.0F);
                    i += Math.max(l1, Math.max(i2, j2));
                    aint[0] += l1;
                    aint[1] += i2;
                    aint[2] += j2;
                    ++j;
                }
            }
        }

        if (pagedBag == null) {
            return ItemStack.EMPTY;
        } else {
            int i1 = aint[0] / j;
            int j1 = aint[1] / j;
            int k1 = aint[2] / j;
            float f3 = (float) i / (float) j;
            float f4 = (float) Math.max(i1, Math.max(j1, k1));
            i1 = (int) ((float) i1 * f3 / f4);
            j1 = (int) ((float) j1 * f3 / f4);
            k1 = (int) ((float) k1 * f3 / f4);
            int lvt_12_3_ = (i1 << 8) + j1;
            lvt_12_3_ = (lvt_12_3_ << 8) + k1;
            pagedBag.setColor(itemstack, lvt_12_3_);
            return itemstack;
        }
    }

    public int getRecipeSize() {
        return 10;
    }

    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }

}
