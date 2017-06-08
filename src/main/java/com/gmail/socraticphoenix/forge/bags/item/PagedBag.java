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

import com.gmail.socraticphoenix.forge.bags.container.bag.PageWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface PagedBag {

    PageWrapper getPageWrapper(ItemStack stack);

    boolean hasData(ItemStack stack);

    void applyData(ItemStack stack);

    void applyDefaultData(ItemStack stack);

    default void setColor(ItemStack stack, int color) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("color", color);
    }

    default int getColor(ItemStack stack) {
        return stack.getTagCompound().getInteger("color");
    }

    default boolean isMagnetic(ItemStack stack) {
        return stack.getSubCompound("arcanebags").getBoolean("magnetic");
    }

    default boolean isSoulBound(ItemStack stack) {
        return stack.getSubCompound("arcanebags").getBoolean("soulbound");
    }

    default void applyMagnetic(ItemStack stack, boolean magnetic) {
        if(!this.hasData(stack)) {
            this.applyData(stack);
        }
        stack.getOrCreateSubCompound("arcanebags").setBoolean("magnetic", magnetic);
    }

    default void applySoulbound(ItemStack stack, boolean soulbound) {
        if(!this.hasData(stack)) {
            this.applyData(stack);
        }
        stack.getOrCreateSubCompound("arcanebags").setBoolean("soulbound", soulbound);
    }

    default boolean hasColor(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("color");
    }
}
