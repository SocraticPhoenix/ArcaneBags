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
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SearchPageItemHandler extends ItemStackHandler {
    public NonNullList<ItemStackHandler> handlers;

    public SearchPageItemHandler(NonNullList<ItemStackHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.validateSlot(slot);
        if(slot < this.handlers.size()) {
            this.handlers.get(slot).setStackInSlot(0, stack);
        }
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlot(slot);
        if(slot < this.handlers.size()) {
            return this.handlers.get(slot).getStackInSlot(0);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        this.validateSlot(slot);
        if(slot < this.handlers.size()) {
            return this.handlers.get(slot).insertItem(0, stack, simulate);
        } else {
            return stack;
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        this.validateSlot(slot);
        if(slot < this.handlers.size()) {
            return this.handlers.get(slot).extractItem(0, amount, simulate);
        } else {
            return ItemStack.EMPTY;
        }
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= 54)
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

}
