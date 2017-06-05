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
package com.gmail.socraticphoenix.forge.bags.interfacecontainer;

import com.gmail.socraticphoenix.forge.bags.PagedBag;
import com.gmail.socraticphoenix.forge.bags.bagcontainer.PageWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BagDelegateStackHandler implements IItemHandler {
    private PageWrapper wrapper;
    private BagItemStackHandler parent;

    public BagDelegateStackHandler(BagItemStackHandler parent) {
        this.parent = parent;
    }

    public void setInv(PageWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        ItemStack res = ItemStack.EMPTY;
        if (this.wrapper != null) {
            for (ItemStackHandler handler : this.wrapper.pages()) {
                ItemStack stack = handler.getStackInSlot(slot);
                res = stack;
                if (!stack.isEmpty()) {
                    break;
                }
            }
        }
        this.parent.update();
        return res;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.getItem() instanceof PagedBag) {
            return stack;
        }

        ItemStack r = stack;

        if (this.wrapper != null) {
            for (int i = 0; true; i++) {
                if (this.wrapper.hasPage(i)) {
                    ItemStackHandler handler = this.wrapper.getPage(i);
                    ItemStack res = handler.insertItem(slot, stack, true);
                    if (res.isEmpty() || stack.getCount() - res.getCount() >= 64) {
                        r = handler.insertItem(slot, stack, simulate);
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        this.parent.recordChanges();
        return r;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack r = ItemStack.EMPTY;
        if (this.wrapper != null) {
            for (ItemStackHandler handler : this.wrapper.pages()) {
                ItemStack stack = handler.extractItem(slot, amount, true);
                if (!stack.isEmpty()) {
                    r = handler.extractItem(slot, amount, simulate);
                    break;
                }
            }
        }
        this.parent.recordChanges();
        return r;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

}
