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

import com.gmail.socraticphoenix.forge.bags.bagcontainer.GeneralItemHandler;
import com.gmail.socraticphoenix.forge.bags.bagcontainer.PageWrapper;
import com.gmail.socraticphoenix.forge.bags.bagcontainer.SimplePageWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ArcaneBag extends Item implements PagedBag {

    public ArcaneBag() {
        this.setUnlocalizedName("arcanebag").setRegistryName("arcanebag").setCreativeTab(ModArcaneBags.TAB_BAGS).setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if(this.hasData(stack)) {
            tooltip.add(TextFormatting.GRAY + I18n.format("arcanebags.pages") + ": " + this.getPages(stack));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == this) {
            if(!this.hasData(stack)) {
                this.applyData(stack);
            }
            player.openGui(ModArcaneBags.instance(), BagGuiHandler.BAG, worldIn, 0, 0, 0);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }


    public boolean hasData(ItemStack stack) {
        return stack.hasTagCompound() && stack.getSubCompound("arcanebags") != null;
    }

    public void applyData(ItemStack stack) {
        NBTTagCompound sub = stack.getOrCreateSubCompound("arcanebags");
        sub.setInteger("pageCount", 10);
        sub.setTag("pages", new NBTTagList());
    }

    public void applyData(ItemStack stack, List<ItemStackHandler> inventory, int pages) {
        NBTTagCompound sub = stack.getOrCreateSubCompound("arcanebags");
        sub.setInteger("pageCount", pages);
        NBTTagList list = new NBTTagList();
        for (ItemStackHandler handler : inventory) {
            list.appendTag(handler.serializeNBT());
        }
        sub.setTag("pages", list);
    }

    public void applyData(ItemStack stack, List<ItemStackHandler> inventory) {
        NBTTagCompound sub = stack.getOrCreateSubCompound("arcanebags");
        NBTTagList list = new NBTTagList();
        for (ItemStackHandler handler : inventory) {
            list.appendTag(handler.serializeNBT());
        }
        sub.setTag("pages", list);
    }

    public List<ItemStackHandler> getInventory(ItemStack stack) {
        if (!this.hasData(stack)) {
            this.applyData(stack);
        }

        NBTTagList list = stack.getOrCreateSubCompound("arcanebags").getTagList("pages", 10);
        List<ItemStackHandler> inv = new ArrayList<>();
        for (int i = 0; i < list.tagCount(); i++) {
            ItemStackHandler handler = new GeneralItemHandler(54, Function.identity(), Function.identity(), k -> !(k.getItem() instanceof PagedBag));
            handler.deserializeNBT(list.getCompoundTagAt(i));
            inv.add(handler);
        }

        return inv;
    }

    public int getPages(ItemStack stack) {
        if (!this.hasData(stack)) {
            this.applyData(stack);
        }

        return stack.getOrCreateSubCompound("arcanebags").getInteger("pageCount");
    }

    public ItemStack makeStack(int pages) {
        ItemStack stack = new ItemStack(this);
        this.applyData(stack, new ArrayList<>(), pages);
        return stack;
    }

    @Override
    public PageWrapper getPageWrapper(ItemStack stack) {
        return new SimplePageWrapper(this.getInventory(stack), this.getPages(stack), () -> new GeneralItemHandler(54, Function.identity(), Function.identity(), k -> !(k.getItem() instanceof PagedBag)));
    }

}
