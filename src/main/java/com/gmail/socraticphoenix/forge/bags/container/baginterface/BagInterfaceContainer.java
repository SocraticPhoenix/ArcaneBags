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
import com.gmail.socraticphoenix.forge.bags.block.BagInterfaceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class BagInterfaceContainer extends Container {
    private BagInterfaceTileEntity entity;
    private InventoryPlayer player;

    public BagInterfaceContainer(BagInterfaceTileEntity entity, InventoryPlayer player) {
        this.entity = entity;
        this.player = player;
        this.addSlotToContainer(new SlotItemHandler(this.entity.getBagSlot(), 0, 80, 18));


        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(player, j1 + l * 9 + 9, 8 + j1 * 18, 50 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(player, i1, 8 + i1 * 18, 108));
        }
    }

    public InventoryPlayer getPlayerInv() {
        return this.player;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        this.detectAndSendChanges();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.entity.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if(index == 0) {
                if(!this.mergeItemStack(current, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(!(current.getItem() instanceof PagedBag) || this.inventorySlots.get(0).getHasStack()) {
                    return ItemStack.EMPTY;
                }

                this.inventorySlots.get(0).putStack(current);
                slot.putStack(ItemStack.EMPTY);
                this.detectAndSendChanges();
                return current;
            }


            if (current.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (current.getCount() == previous.getCount()) {
                return ItemStack.EMPTY;
            } else {
                slot.onTake(playerIn, current);
            }
        }
        this.detectAndSendChanges();
        return previous;
    }

}
