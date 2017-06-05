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

import com.gmail.socraticphoenix.forge.bags.item.InfiniteArcaneBag;
import com.gmail.socraticphoenix.forge.bags.net.SearchUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public class InfinitePageWrapper implements PageWrapper {
    private Map<Integer, ItemStackHandler> pages;
    private Supplier<ItemStackHandler> newHandler;
    private int max;

    public InfinitePageWrapper(Map<Integer, ItemStackHandler> pages, Supplier<ItemStackHandler> newHandler) {
        this.pages = pages;
        this.newHandler = newHandler;
        this.max = 0;
    }

    @Override
    public boolean hasPage(int index) {
        return index >= 0;
    }

    @Override
    public ItemStackHandler getPage(int index) {
        if (!this.pages.containsKey(index)) {
            this.pages.put(index, this.newHandler.get());
        }
        if (index > this.max) {
            this.max = index;
        }
        return this.pages.get(index);
    }

    @Override
    public String getMaxPages() {
        return "\u221E";
    }

    @Override
    public int leftMost() {
        return 0;
    }

    @Override
    public int rightMost() {
        return this.max;
    }

    @Override
    public void applyData(ItemStack stack) {
        InfiniteArcaneBag bag = (InfiniteArcaneBag) stack.getItem();
        bag.applyData(stack, this.pages);
    }

    @Override
    public Collection<ItemStackHandler> pages() {
        return this.pages.values();
    }

    @Override
    public Supplier<ItemStackHandler> handler() {
        return this.newHandler;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public SearchUpdatePacket search(String search, EntityPlayer player) {
        SearchUpdatePacket packet = new SearchUpdatePacket();

        String[] term = search.toLowerCase().split(" ");
        search:
        for (Map.Entry<Integer, ItemStackHandler> entry : this.pages.entrySet()) {
            ItemStackHandler handler = entry.getValue();
            for (int j = 0; j < handler.getSlots(); j++) {
                ItemStack stack = handler.getStackInSlot(j);
                if (!stack.isEmpty() && PageWrapper.matches(term, stack, player)) {
                    packet.addElement(entry.getKey(), j);

                }
            }
        }

        return packet;
    }


}
