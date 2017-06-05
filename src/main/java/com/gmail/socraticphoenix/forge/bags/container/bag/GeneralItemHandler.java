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
package com.gmail.socraticphoenix.forge.bags.container.bag;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Predicate;

public class GeneralItemHandler extends ItemStackHandler {
    private Function<ItemStack, ItemStack> inTransform;
    private Function<ItemStack, ItemStack> outTransform;
    private Predicate<ItemStack> verify;

    public GeneralItemHandler(int size, Function<ItemStack, ItemStack> inTransform, Function<ItemStack, ItemStack> outTransform, Predicate<ItemStack> verify) {
        super(size);
        this.inTransform = inTransform;
        this.outTransform = outTransform;
        this.verify = verify;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        super.setStackInSlot(slot, this.inTransform.apply(stack));
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.outTransform.apply(super.getStackInSlot(slot));
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!this.verify.test(stack)) {
            return stack;
        }
        return super.insertItem(slot, this.inTransform.apply(stack), simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.outTransform.apply(super.extractItem(slot, amount, simulate));
    }

}
