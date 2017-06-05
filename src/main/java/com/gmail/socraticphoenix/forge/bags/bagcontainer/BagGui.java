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

import com.gmail.socraticphoenix.forge.bags.net.BagNetworking;
import com.gmail.socraticphoenix.forge.bags.net.SearchStatePacket;
import com.gmail.socraticphoenix.forge.bags.net.SearchUpdatePacket;
import com.gmail.socraticphoenix.forge.bags.net.SetPagePacket;
import com.google.common.base.Predicate;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class BagGui extends GuiContainer {
    private static int[][] nextOffsets = {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}, {0, 8}, {0, 9}, {0, 10}, {0, 11}, {0, 12}, {0, 13}, {0, 14}, {0, 15}, {0, 16}, {0, 17}, {0, 18}, {0, 19}, {0, 20}, {0, 21}, {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}, {1, 8}, {1, 9}, {1, 10}, {1, 11}, {1, 12}, {1, 13}, {1, 14}, {1, 15}, {1, 16}, {1, 17}, {1, 18}, {1, 19}, {1, 20}, {1, 21}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, {2, 7}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {2, 12}, {2, 13}, {2, 14}, {2, 15}, {2, 16}, {2, 17}, {2, 18}, {2, 19}, {2, 20}, {2, 21}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {3, 13}, {3, 14}, {3, 15}, {3, 16}, {3, 17}, {3, 18}, {3, 19}, {3, 20}, {3, 21}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {4, 15}, {4, 16}, {4, 17}, {4, 18}, {4, 19}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}, {5, 8}, {5, 9}, {5, 10}, {5, 11}, {5, 12}, {5, 13}, {5, 14}, {5, 15}, {5, 16}, {5, 17}, {5, 18}, {5, 19}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14}, {6, 15}, {6, 16}, {6, 17}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13}, {7, 14}, {7, 15}, {7, 16}, {7, 17}, {8, 6}, {8, 7}, {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14}, {8, 15}, {9, 6}, {9, 7}, {9, 8}, {9, 9}, {9, 10}, {9, 11}, {9, 12}, {9, 13}, {9, 14}, {9, 15}, {10, 8}, {10, 9}, {10, 10}, {10, 11}, {10, 12}, {10, 13}, {11, 8}, {11, 9}, {11, 10}, {11, 11}, {11, 12}, {11, 13}, {12, 10}, {12, 11}, {13, 10}, {13, 11}};
    private static int[][] backOffsets = {{0, 10}, {0, 11}, {1, 10}, {1, 11}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {2, 12}, {2, 13}, {3, 8}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {3, 13}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {4, 15}, {5, 6}, {5, 7}, {5, 8}, {5, 9}, {5, 10}, {5, 11}, {5, 12}, {5, 13}, {5, 14}, {5, 15}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14}, {6, 15}, {6, 16}, {6, 17}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13}, {7, 14}, {7, 15}, {7, 16}, {7, 17}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {8, 7}, {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14}, {8, 15}, {8, 16}, {8, 17}, {8, 18}, {8, 19}, {9, 2}, {9, 3}, {9, 4}, {9, 5}, {9, 6}, {9, 7}, {9, 8}, {9, 9}, {9, 10}, {9, 11}, {9, 12}, {9, 13}, {9, 14}, {9, 15}, {9, 16}, {9, 17}, {9, 18}, {9, 19}, {10, 0}, {10, 1}, {10, 2}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 9}, {10, 10}, {10, 11}, {10, 12}, {10, 13}, {10, 14}, {10, 15}, {10, 16}, {10, 17}, {10, 18}, {10, 19}, {10, 20}, {10, 21}, {11, 0}, {11, 1}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {11, 6}, {11, 7}, {11, 8}, {11, 9}, {11, 10}, {11, 11}, {11, 12}, {11, 13}, {11, 14}, {11, 15}, {11, 16}, {11, 17}, {11, 18}, {11, 19}, {11, 20}, {11, 21}, {12, 0}, {12, 1}, {12, 2}, {12, 3}, {12, 4}, {12, 5}, {12, 6}, {12, 7}, {12, 8}, {12, 9}, {12, 10}, {12, 11}, {12, 12}, {12, 13}, {12, 14}, {12, 15}, {12, 16}, {12, 17}, {12, 18}, {12, 19}, {12, 20}, {12, 21}, {13, 0}, {13, 1}, {13, 2}, {13, 3}, {13, 4}, {13, 5}, {13, 6}, {13, 7}, {13, 8}, {13, 9}, {13, 10}, {13, 11}, {13, 12}, {13, 13}, {13, 14}, {13, 15}, {13, 16}, {13, 17}, {13, 18}, {13, 19}, {13, 20}, {13, 21}};
    private static int[][] magOffsets = {{0, 9}, {0, 10}, {1, 8}, {1, 9}, {1, 10}, {1, 11}, {2, 7}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 10}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}, {5, 8}, {5, 9}, {6, 0}, {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {7, 0}, {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {8, 0}, {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {8, 7}, {8, 8}, {8, 9}, {9, 0}, {9, 1}, {9, 2}, {9, 3}, {9, 4}, {9, 5}, {9, 6}, {9, 7}, {9, 8}, {9, 9}, {10, 1}, {10, 2}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {11, 6}, {11, 7}, {12, 3}, {12, 4}, {12, 5}, {12, 6}};

    private GuiTextField text;
    private BagContainer container;
    private String pastText;

    private Predicate<String> pageValidator;
    private Predicate<String> searchValidator;

    private int textWidth;

    public BagGui(BagContainer container) {
        super(container);
        this.container = container;
        this.ySize = 233;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.textWidth = this.fontRendererObj.getStringWidth(String.valueOf(Integer.MAX_VALUE));
        this.text = new GuiTextField(0, this.fontRendererObj, 0, 0, this.textWidth, this.fontRendererObj.FONT_HEIGHT + 4);

        this.pageValidator = k -> {
            if (k != null) {
                try {
                    int n = Integer.parseInt(k);
                    return this.container.getWrapper().hasPage(n - 1);
                } catch (NumberFormatException ignore) {
                }
                return k.isEmpty();
            }
            return false;
        };

        this.searchValidator = Objects::nonNull;
        if (!this.container.isSearching()) {
            this.text.setText(String.valueOf(this.container.getIndex() + 1));
        } else {
            this.text.setText("");
        }
        this.pastText = String.valueOf(this.container.getIndex() + 1);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.text.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.text.textboxKeyTyped(typedChar, keyCode) && !this.pastText.equals(this.text.getText())) {
            if (this.container.isSearching()) {
                this.pastText = this.text.getText();
                SearchUpdatePacket packet = this.text.getText().isEmpty() ? new SearchUpdatePacket() : this.container.getWrapper().search(this.text.getText(), this.mc.player);
                BagNetworking.INSTANCE.sendToServer(packet);
                this.container.updateSearch(packet);
            } else if (!this.text.getText().isEmpty()) {
                this.pastText = this.text.getText();
                this.setPage(this.getTextPage());
            }
        }

        if (!this.text.isFocused() || Character.toLowerCase(typedChar) != 'e') {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.text.mouseClicked(mouseX, mouseY, mouseButton);

        if (!this.text.isFocused() && !this.container.isSearching()) {
            this.text.setText(String.valueOf(this.container.getIndex() + 1));
        }

        if (mouseButton == 0) {
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            if (this.nextButtonContains(153 + i, 127 + j, mouseX, mouseY)) {
                if (!this.container.isSearching()) {
                    this.addPage(1);
                } else {
                    this.setSearchingPage(this.container.getSearchingPage() + 1);
                }
            } else if (this.previousButtonContains(8 + i, 127 + j, mouseX, mouseY)) {
                if (!this.container.isSearching()) {
                    this.addPage(-1);
                } else {
                    this.setSearchingPage(this.container.getSearchingPage() - 1);
                }
            } else if (this.magContains(156 + i, 4 + j, mouseX, mouseY)) {
                if (this.container.isSearching()) {
                    this.disableSearch();
                } else {
                    this.enableSearch();
                }
            }
        }
    }

    private void enableSearch() {
        BagNetworking.INSTANCE.sendToServer(new SearchStatePacket().setSearching(true));
        this.container.setSearching(true);
        this.text.setText("");
        this.text.setFocused(true);
    }

    private void disableSearch() {
        BagNetworking.INSTANCE.sendToServer(new SearchStatePacket().setSearching(false));
        this.container.setSearching(false);
        this.text.setText(String.valueOf(this.container.getIndex() + 1));
    }

    private void setSearchingPage(int page) {
        BagNetworking.INSTANCE.sendToServer(new SetPagePacket().setPage(page).setSearching(true));
        this.container.setSearchingPage(page);
    }

    private void setPage(int page) {
        if (this.container.isSearching()) {
            this.disableSearch();
        }
        BagNetworking.INSTANCE.sendToServer(new SetPagePacket().setPage(page));
        this.container.setAndUpdate(page);
        this.text.setText(String.valueOf(this.container.getIndex() + 1));
    }

    private void addPage(int page) {
        if (this.container.isSearching()) {
            this.setPage(this.container.getIndex());
        } else {
            this.setPage(this.container.getIndex() + page);
        }
    }

    public int getTextPage() {
        String t = this.text.getText();
        return t.isEmpty() ? 0 : Integer.parseInt(t) - 1;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.container.getBag().getDisplayName(), 8, 6, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("arcanebags:textures/gui/bag_inventory.png"));
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        GlStateManager.color(1f, 1f, 1f, 1f);
        this.drawText(i, j);
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("arcanebags:textures/gui/bag_buttons.png"));
        this.drawNext(i, j, mouseX, mouseY);
        this.drawPrevious(i, j, mouseX, mouseY);
        this.drawSearch(i, j, mouseX, mouseY);
    }

    private void drawNext(int i, int j, int mouseX, int mouseY) {
        int bx = 153 + i;
        int by = 127 + j;
        int width = 14;
        int height = 22;
        int tx;
        int ty;

        if (this.nextButtonContains(bx, by, mouseX, mouseY)) {
            tx = 16;
            ty = 37;
        } else {
            tx = 16;
            ty = 5;
        }

        this.drawTexturedModalRect(bx, by, tx, ty, width, height);
    }

    private void drawPrevious(int i, int j, int mouseX, int mouseY) {
        int bx = 8 + i;
        int by = 127 + j;
        int width = 14;
        int height = 22;
        int tx;
        int ty;

        if (this.previousButtonContains(bx, by, mouseX, mouseY)) {
            tx = 34;
            ty = 37;
        } else {
            tx = 34;
            ty = 5;
        }

        this.drawTexturedModalRect(bx, by, tx, ty, width, height);
    }

    private void drawSearch(int i, int j, int mouseX, int mouseY) {
        int bx = 156 + i;
        int by = 4 + j;
        int width = 13;
        int height = 12;
        int tx;
        int ty = 64;
        if (this.magContains(bx, by, mouseX, mouseY)) {
            tx = 34;
        } else if (this.container.isSearching()) {
            tx = 51;
        } else {
            tx = 16;
        }

        this.drawTexturedModalRect(bx, by, tx, ty, width, height);
    }

    private void drawText(int i, int j) {
        if (!this.container.isSearching()) {
            this.text.setMaxStringLength(10);
            this.text.width = this.textWidth;
            String post = " / " + this.container.getWrapper().getMaxPages();
            int width = this.text.width + this.fontRendererObj.getStringWidth(post) + 5;
            int heightOff = (this.text.height - this.fontRendererObj.FONT_HEIGHT) / 2;
            int y = (22 - this.fontRendererObj.FONT_HEIGHT) / 2 + 127 + j;
            int x = (this.xSize / 2) + i - width / 2;
            this.text.yPosition = y - heightOff;
            this.text.xPosition = x;
            x += 5 + this.text.width;
            this.fontRendererObj.drawSplitString(post, x, y, 222, Color.BLACK.getRGB());
            this.text.setValidator(this.pageValidator);
            this.text.drawTextBox();
        } else {
            this.text.setMaxStringLength(100);
            this.text.width = this.textWidth * 2;
            int heightOff = (this.text.height - this.fontRendererObj.FONT_HEIGHT) / 2;
            int y = (22 - this.fontRendererObj.FONT_HEIGHT) / 2 + 127 + j;
            int x = (this.xSize / 2) + i - this.text.width / 2;
            this.text.yPosition = y - heightOff;
            this.text.xPosition = x;
            this.text.setValidator(this.searchValidator);
            this.text.drawTextBox();

        }
    }

    public boolean nextButtonContains(int bx, int by, int x, int y) {
        int[] offset = {x - bx, y - by};
        for (int[] pixel : nextOffsets) {
            if (Arrays.equals(offset, pixel)) {
                return true;
            }
        }
        return false;
    }

    public boolean previousButtonContains(int bx, int by, int x, int y) {
        int[] offset = {x - bx, y - by};
        for (int[] pixel : backOffsets) {
            if (Arrays.equals(offset, pixel)) {
                return true;
            }
        }
        return false;
    }

    public boolean magContains(int bx, int by, int x, int y) {
        int[] offset = {x - bx, y - by};
        for (int[] pixel : magOffsets) {
            if (Arrays.equals(offset, pixel)) {
                return true;
            }
        }
        return false;
    }

}
