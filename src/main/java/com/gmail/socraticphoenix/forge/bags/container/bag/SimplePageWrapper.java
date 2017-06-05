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

import com.gmail.socraticphoenix.forge.bags.item.ArcaneBag;
import com.gmail.socraticphoenix.forge.bags.net.SearchUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SimplePageWrapper implements PageWrapper {
    private List<ItemStackHandler> pages;
    private int maxPages;
    private Supplier<ItemStackHandler> newHandler;

    public SimplePageWrapper(List<ItemStackHandler> pages, int maxPages, Supplier<ItemStackHandler> newHandler) {
        this.pages = pages;
        this.maxPages = maxPages;
        this.newHandler = newHandler;
    }

    @Override
    public boolean hasPage(int index) {
        return index >= 0 && index < this.maxPages;
    }

    @Override
    public ItemStackHandler getPage(int index) {
        while (index >= this.pages.size()) {
            this.pages.add(this.newHandler.get());
        }
        return this.pages.get(index);
    }

    @Override
    public String getMaxPages() {
        return String.valueOf(this.maxPages);
    }

    @Override
    public int leftMost() {
        return 0;
    }

    @Override
    public int rightMost() {
        return this.maxPages - 1;
    }

    @Override
    public void applyData(ItemStack stack) {
        ArcaneBag bag = (ArcaneBag) stack.getItem();
        bag.applyData(stack, this.pages);
    }

    @Override
    public Collection<ItemStackHandler> pages() {
        return this.pages;
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
        for (int i = 0; i < this.rightMost(); i++) {
            ItemStackHandler handler = this.getPage(i);
            for (int j = 0; j < handler.getSlots(); j++) {
                ItemStack stack = handler.getStackInSlot(j);
                if(!stack.isEmpty() && PageWrapper.matches(term, stack, player)) {
                    packet.addElement(i, j);
                }
            }
        }

        return packet;
    }




}
