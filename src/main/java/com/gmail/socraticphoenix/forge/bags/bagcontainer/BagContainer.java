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
package com.gmail.socraticphoenix.forge.bags.bagcontainer;

import com.gmail.socraticphoenix.forge.bags.net.SearchUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class BagContainer extends Container {
    private ItemStack bag;
    private PageWrapper wrapper;
    private ItemStackHandler currentPage;
    private int page;
    private int numRows;

    private boolean searching;

    private DelegatingItemStackHandler delegate;
    private int searchingPage;

    private List<ItemStackHandler> searchingPages;

    public BagContainer(ItemStack bag, PageWrapper wrapper, InventoryPlayer playerInventory) {
        this.bag = bag;
        this.searching = false;
        this.wrapper = wrapper;
        this.page = 0;
        this.searchingPages = new ArrayList<>();
        this.searchingPage = 0;
        this.delegate = new DelegatingItemStackHandler();
        this.updatePage();
        this.numRows = this.delegate.getSlots() / 9;

        for (int j = 0; j < this.numRows; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new SlotItemHandler(this.delegate, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        int i = (this.numRows - 4) * 18 + 13;

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
        if(searching) {
            this.updateToSearching();
        } else {
            this.updatePage();
        }
    }

    public boolean isSearching() {
        return this.searching;
    }

    public void updateSearch(SearchUpdatePacket packet) {
        List<ItemStackHandler> pages = new ArrayList<>();
        NonNullList<ItemStackHandler> slots = NonNullList.create();
        for(SearchUpdatePacket.Element element : packet.getElements()) {
            slots.add(new SearchItemHandler(element.page, element.slot, this.wrapper));
            if(slots.size() == 54) {
                pages.add(new SearchPageItemHandler(slots));
                slots = NonNullList.create();
            }
        }
        if(pages.isEmpty() || !slots.isEmpty()) {
            pages.add(new SearchPageItemHandler(slots));
        }
        this.searchingPages = pages;
        this.setSearchingPage(0);
        this.detectAndSendChanges();
    }

    public void updateToSearching() {
        this.searchingPages = new ArrayList<>();
        this.searchingPages.add(new SearchPageItemHandler(NonNullList.create()));
        this.setSearchingPage(0);
        this.detectAndSendChanges();
    }

    public void setSearchingPage(int page) {
        if(page >= this.searchingPages.size()) {
            page = 0;
        } else if (page < 0) {
            page = this.searchingPages.size() - 1;
        }

        this.searchingPage = page;
        this.delegate.setHandler(this.searchingPages.get(page));
        this.detectAndSendChanges();
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack bag = this.getBag().copy();
        ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
        if (ItemStack.areItemStacksEqual(bag, stack)) {
            bag = stack;
        } else {
            bag = this.getBag();
        }
        this.wrapper.applyData(bag);
        return stack;
    }

    public ItemStack getBag() {
        return this.bag;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.numRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getHeldItemMainhand() == this.bag || playerIn.getHeldItemOffhand() == this.bag;
    }

    public PageWrapper getWrapper() {
        return this.wrapper;
    }

    public void setAndUpdate(int page) {
        this.setPage(page);
        this.updatePage();
    }

    public void setPage(int page) {
        this.page = page;
        if (!this.wrapper.hasPage(this.page)) {
            int left = this.wrapper.leftMost();
            int right = this.wrapper.rightMost();
            if (this.page < left) {
                this.page = right;
            } else if (this.page > right) {
                this.page = left;
            }
        }
    }

    public void updatePage() {
        this.currentPage = this.wrapper.getPage(this.page);
        this.delegate.setHandler(this.currentPage);
        this.detectAndSendChanges();
    }

    public int getIndex() {
        return this.page;
    }

    public int getSearchingPage() {
        return this.searchingPage;
    }

}
