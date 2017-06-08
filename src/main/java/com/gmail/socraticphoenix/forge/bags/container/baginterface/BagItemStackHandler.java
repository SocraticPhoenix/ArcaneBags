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
package com.gmail.socraticphoenix.forge.bags.container.baginterface;

import com.gmail.socraticphoenix.forge.bags.item.PagedBag;
import com.gmail.socraticphoenix.forge.bags.container.bag.PageWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class BagItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
    private ItemStack bag;
    private PageWrapper bagInv;
    private BagDelegateStackHandler delegate;

    public BagItemStackHandler() {
        this.delegate = new BagDelegateStackHandler(this);
        this.bag = ItemStack.EMPTY;
        this.bagInv = null;
    }

    public BagDelegateStackHandler getDelegate() {
        return this.delegate;
    }

    public PageWrapper getBagInv() {
        return this.bagInv;
    }

    public boolean hasBagInv() {
        if(this.bag.isEmpty()) {
            this.updateBagInv();
        }
        return this.bagInv != null;
    }

    private void updateBagInv() {
        if(this.bag != null && this.bag.getItem() instanceof PagedBag) {
            this.bagInv = ((PagedBag) this.bag.getItem()).getPageWrapper(this.bag);
        } else {
            this.bagInv = null;
        }

        this.delegate.setInv(this.bagInv);
    }

    public void recordChanges() {
        if(this.hasBagInv()) {
            this.getBagInv().applyData(this.bag);
        }

    }

    public void update() {
        this.recordChanges();
        this.updateBagInv();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        this.update();
        this.bag.writeToNBT(compound);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.bag = new ItemStack(nbt);
        this.update();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.recordChanges();
        this.bag = stack;
        this.updateBagInv();
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.update();
        return this.bag;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!(stack.getItem() instanceof PagedBag) || !((PagedBag) stack.getItem()).hasData(stack)) {
            return stack;
        }

        if(this.bag.isEmpty()) {
            this.recordChanges();
            if(!simulate) {
                this.bag = stack;
            }
            this.updateBagInv();
            return ItemStack.EMPTY;
        } else {
            return stack;
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(!this.bag.isEmpty()) {
            this.recordChanges();
            ItemStack bag = this.bag;
            if(!simulate) {
                this.bag = ItemStack.EMPTY;
            }
            this.updateBagInv();
            return bag;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

}
