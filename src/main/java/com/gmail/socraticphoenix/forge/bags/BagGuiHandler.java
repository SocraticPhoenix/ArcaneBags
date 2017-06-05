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

import com.gmail.socraticphoenix.forge.bags.bagcontainer.BagContainer;
import com.gmail.socraticphoenix.forge.bags.bagcontainer.BagGui;
import com.gmail.socraticphoenix.forge.bags.block.BagInterfaceTileEntity;
import com.gmail.socraticphoenix.forge.bags.interfacecontainer.BagInterfaceContainer;
import com.gmail.socraticphoenix.forge.bags.interfacecontainer.BagInterfaceGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BagGuiHandler implements IGuiHandler {
    public static final int BAG = 0;
    public static final int BAG_INTERFACE = 1;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == BAG) {
            ItemStack[] hands = {player.getHeldItemMainhand(), player.getHeldItemOffhand()};
            for (ItemStack stack : hands) {
                if (!stack.isEmpty() && stack.getItem() instanceof PagedBag) {
                    PagedBag bag = (PagedBag) stack.getItem();
                    return new BagContainer(stack, bag.getPageWrapper(stack), player.inventory);
                }
            }
        } else if (ID == BAG_INTERFACE) {
            BlockPos pos = new BlockPos(x, y, z);
            return new BagInterfaceContainer((BagInterfaceTileEntity) world.getTileEntity(pos), player.inventory);
        }
        return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == BAG) {
            ItemStack[] hands = {player.getHeldItemMainhand(), player.getHeldItemOffhand()};
            for (ItemStack stack : hands) {
                if (!stack.isEmpty() && stack.getItem() instanceof PagedBag) {
                    PagedBag bag = (PagedBag) stack.getItem();
                    return new BagGui(new BagContainer(stack, bag.getPageWrapper(stack), player.inventory));
                }
            }
        } else if (ID == BAG_INTERFACE) {
            BlockPos pos = new BlockPos(x, y, z);
            return new BagInterfaceGui(new BagInterfaceContainer((BagInterfaceTileEntity) world.getTileEntity(pos), player.inventory));
        }
        return null;
    }

}
