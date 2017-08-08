package zachy.cosmic.client.gui;

import zachy.cosmic.common.container.ContainerCompressor;
import zachy.cosmic.common.tile.TileCompressor;

public class GuiCompressor extends GuiBase {

    private TileCompressor tile;

    public GuiCompressor(ContainerCompressor container) {
        super(container, 176, 182);

        tile = (TileCompressor) container.getTile();
    }

    @Override
    public void init(int x, int y) {

    }

    @Override
    public void update(int x, int y) {

    }

    @Override
    public void drawBackground(int x, int y, int mouseX, int mouseY) {
        bindTexture("gui/compressor.png");

        drawTexture(x, y, 0, 0, screenWidth, screenHeight);

        if (tile.isWorking()) {
            drawTexture(x + 81, y + 45, 176, 0, getProgressScaled(22), 11);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        drawStringCentred(xSize, 5, format("gui.cosmic:compressor"));

        if (!tile.isValid()) {
            drawString(80, 90, format("gui.cosmic:invalid"));
        }

        drawString(8, 90, format("container.inventory"));
    }

    private int getProgressScaled(int scale) {
        float progress = tile.getProgress();
        float duration = tile.getDuration();

        if (progress > duration) {
            return scale;
        }

        return (int) (progress / duration * (float) scale);
    }
}
