package team.creative.creativecore.common.gui.integration;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.StringTextComponent;
import team.creative.creativecore.common.gui.GuiLayer;
import team.creative.creativecore.common.gui.IScaleableGuiScreen;

public class ContainerScreenIntegration extends ContainerScreen<ContainerIntegration> implements IScaleableGuiScreen {
	
	protected ScreenEventListener listener;
	
	public ContainerScreenIntegration(ContainerIntegration screenContainer, PlayerInventory inv) {
		super(screenContainer, inv, new StringTextComponent("gui-api"));
		listener = new ScreenEventListener(getContainer(), this);
	}
	
	@Override
	protected void init() {
		this.addListener(listener);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		getContainer().render(matrixStack, this, listener, mouseX, mouseY);
	}
	
	@Override
	public int getWidth() {
		int width = 0;
		for (GuiLayer layer : getContainer().getLayers())
			width = Math.max(width, layer.width);
		return width;
	}
	
	@Override
	public int getHeight() {
		int height = 0;
		for (GuiLayer layer : getContainer().getLayers())
			height = Math.max(height, layer.height);
		return height;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (listener.keyPressed(keyCode, scanCode, modifiers))
			return true;
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (listener.keyReleased(keyCode, scanCode, modifiers))
			return true;
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (listener.charTyped(codePoint, modifiers))
			return true;
		return super.charTyped(codePoint, modifiers);
	}
	
}