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
package com.gmail.socraticphoenix.forge.bags;

import com.gmail.socraticphoenix.forge.bags.container.bag.PageWrapper;
import com.gmail.socraticphoenix.forge.bags.item.PagedBag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BagAbilityListener {

    @SubscribeEvent
    public void onPickup(EntityItemPickupEvent ev) {
        EntityPlayer player = ev.getEntityPlayer();
        List<ItemStack> held = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                held.add(stack);
            }
        }
        held.add(player.getHeldItemOffhand());

        for (ItemStack stack : held) {
            if (stack.getItem() instanceof PagedBag) {
                PagedBag item = (PagedBag) stack.getItem();
                if (item.hasData(stack) && item.isMagnetic(stack)) {
                    ItemStack toAdd = ev.getItem().getEntityItem();
                    PageWrapper inv = item.getPageWrapper(stack);

                    pages:
                    for (int i = 0; true; i++) {
                        if (inv.hasPage(i) && !toAdd.isEmpty()) {
                            ItemStackHandler handler = inv.getPage(i);
                            for (int j = 0; j < handler.getSlots(); j++) {
                                ItemStack remaining = handler.insertItem(j, toAdd, false);
                                toAdd = remaining;
                                if (remaining.isEmpty()) {
                                    break pages;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                    inv.applyData(stack);

                    if (toAdd.isEmpty()) {
                        ev.setCanceled(true);
                        ev.getItem().isDead = true;
                    } else {
                        ev.getItem().setEntityItemStack(toAdd);
                    }
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onDrop(PlayerDropsEvent ev) {
        Iterator<EntityItem> drops = ev.getDrops().iterator();
        while (drops.hasNext()) {
            EntityItem entityItem = drops.next();
            ItemStack stack = entityItem.getEntityItem();
            if (stack.getItem() instanceof PagedBag) {
                PagedBag item = (PagedBag) stack.getItem();
                if (item.hasData(stack) && item.isSoulBound(stack)) {
                    drops.remove();
                    ev.getEntityPlayer().inventory.addItemStackToInventory(stack.copy());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.Clone ev) {
        EntityPlayer old = ev.getOriginal();
        EntityPlayer cloned = ev.getEntityPlayer();
        if(!old.world.getGameRules().getBoolean("keepInventory") && ev.isWasDeath()) {
            for (int i = 0; i < old.inventory.getSizeInventory(); i++) {
                ItemStack stack = old.inventory.getStackInSlot(i);
                if(stack.getItem() instanceof PagedBag) {
                    PagedBag bag = (PagedBag) stack.getItem();
                    if(bag.hasData(stack) && bag.isSoulBound(stack)) {
                        cloned.inventory.setInventorySlotContents(i, stack.copy());
                    }
                }
            }
        }
    }

}
