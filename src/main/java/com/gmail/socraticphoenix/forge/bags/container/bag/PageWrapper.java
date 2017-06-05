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

import com.gmail.socraticphoenix.forge.bags.net.SearchUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public interface PageWrapper {

    boolean hasPage(int index);

    ItemStackHandler getPage(int index);

    String getMaxPages();

    int leftMost();

    int rightMost();

    void applyData(ItemStack stack);

    Collection<ItemStackHandler> pages();

    Supplier<ItemStackHandler> handler();

    @SideOnly(Side.CLIENT)
    SearchUpdatePacket search(String term, EntityPlayer player);

    @SideOnly(Side.CLIENT)
    static boolean matches(String[] term, ItemStack stack, EntityPlayer player) {
        if(allContains(stack.getDisplayName(), term)) {
            return true;
        } else {
            List<String> tooltip = stack.getTooltip(player, false);
            for(String s : tooltip) {
                s = TextFormatting.getTextWithoutFormattingCodes(s).toLowerCase();
                if(allContains(s, term)) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean allContains(String n, String[] pieces) {
        for(String k : pieces) {
            if(!n.contains(k)) {
                return false;
            }
        }

        return true;
    }

}
