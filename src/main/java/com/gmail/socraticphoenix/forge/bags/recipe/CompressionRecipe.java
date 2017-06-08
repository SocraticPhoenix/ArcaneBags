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

import com.gmail.socraticphoenix.forge.bags.block.BagBlocks;
import com.gmail.socraticphoenix.forge.bags.item.BagItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.Arrays;

public class CompressionRecipe extends ShapelessRecipes {

    public CompressionRecipe() {
        super(new ItemStack(BagItems.compressedEssence), Arrays.asList(new ItemStack(BagItems.compressionMatrix), new ItemStack(BagBlocks.magicalEssence)));
    }

    private boolean isMagicEssence(ItemStack stack) {
        return stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == BagBlocks.magicalEssence;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack matrix = ItemStack.EMPTY;
        int essence = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == BagItems.compressionMatrix) {
                if (!matrix.isEmpty() || stack.getItemDamage() >= stack.getMaxDamage()) {
                    return false;
                } else {
                    matrix = stack;
                }
            } else if (this.isMagicEssence(stack)) {
                essence++;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        return !matrix.isEmpty() && matrix.getMaxDamage() - matrix.getItemDamage() >= essence;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int essence = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (this.isMagicEssence(stack)) {
                essence++;
            }
        }
        return new ItemStack(BagItems.compressedEssence, essence);
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        ItemStack matrix = ItemStack.EMPTY;
        int essence = 0;
        int mI = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == BagItems.compressionMatrix) {
                matrix = stack;
                mI = i;
            } else if (this.isMagicEssence(stack)) {
                essence++;
            }
        }

        matrix = matrix.copy();
        matrix.setItemDamage(matrix.getItemDamage() + essence);
        if(matrix.getItemDamage() >= matrix.getMaxDamage()) {
            matrix = ItemStack.EMPTY;
        }

        NonNullList<ItemStack> rem = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        rem.set(mI, matrix);
        return rem;
    }

}
