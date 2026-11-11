package net.indtech.client;

import net.indtech.screen.GeneratorScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class GeneratorScreen extends HandledScreen<GeneratorScreenHandler> {
    public GeneratorScreen(GeneratorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        MachineGui.drawPanel(ctx, x, y);
        MachineGui.drawSlot(ctx, x + 80, y + 35);
        // energy bar (left)
        MachineGui.drawVerticalBar(ctx, x + 12, y + 18, 10, 52,
                handler.getEnergy() / (float) handler.getMaxEnergy(), 0xFFE0B000);
        // burn bar (below fuel slot)
        float burn = handler.getBurnTime() / (float) handler.getMaxBurnTime();
        MachineGui.drawHorizontalBar(ctx, x + 72, y + 58, 32, 4, burn, 0xFFE05A00);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        drawMouseoverTooltip(ctx, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
        super.drawForeground(ctx, mouseX, mouseY);
        ctx.drawText(textRenderer, Text.literal(handler.getEnergy() + " / " + handler.getMaxEnergy() + " EU"),
                28, 20, 0x404040, false);
    }
}
