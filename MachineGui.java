package net.indtech.client;

import net.minecraft.client.gui.DrawContext;

/**
 * Draws machine GUIs with plain rectangles so no custom GUI texture file is needed.
 */
final class MachineGui {
    private MachineGui() {
    }

    static void drawPanel(DrawContext ctx, int x, int y) {
        ctx.fill(x, y, x + 176, y + 166, 0xFF555555);
        ctx.fill(x + 2, y + 2, x + 174, y + 164, 0xFFC6C6C6);
        // player inventory + hotbar slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlot(ctx, x + 8 + col * 18, y + 84 + row * 18);
            }
        }
        for (int col = 0; col < 9; col++) {
            drawSlot(ctx, x + 8 + col * 18, y + 142);
        }
    }

    /** x,y is the top-left corner of the 16x16 item area. */
    static void drawSlot(DrawContext ctx, int x, int y) {
        ctx.fill(x - 1, y - 1, x + 17, y + 17, 0xFF373737);
        ctx.fill(x, y, x + 16, y + 16, 0xFF8B8B8B);
    }

    static void drawVerticalBar(DrawContext ctx, int x, int y, int w, int h, float fraction, int color) {
        ctx.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xFF373737);
        ctx.fill(x, y, x + w, y + h, 0xFF202020);
        int filled = Math.round(h * Math.max(0f, Math.min(1f, fraction)));
        ctx.fill(x, y + h - filled, x + w, y + h, color);
    }

    static void drawHorizontalBar(DrawContext ctx, int x, int y, int w, int h, float fraction, int color) {
        ctx.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xFF373737);
        ctx.fill(x, y, x + w, y + h, 0xFF202020);
        int filled = Math.round(w * Math.max(0f, Math.min(1f, fraction)));
        ctx.fill(x, y, x + filled, y + h, color);
    }
}
