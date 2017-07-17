package com.creativemd.creativecore.gui.controls.gui;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.utils.ColorUtils;
import com.creativemd.creativecore.common.utils.ColorUtils.ColorPart;
import com.creativemd.creativecore.gui.GuiRenderHelper;
import com.creativemd.creativecore.gui.client.style.Style;

public class GuiColoredSteppedSlider extends GuiSteppedSlider {
	
	public GuiColorPicker picker;
	public ColorPart part;
	
	public GuiColoredSteppedSlider(String name, int x, int y, int width, int height, GuiColorPicker picker, ColorPart part) {
		super(name, x, y, width, height, part.getColor(picker.color), 0, 255);
		this.picker = picker;
		this.part = part;
	}
	
	@Override
	public void setValue(float value)
	{
		super.setValue((int) value); 
		if(part != null)
		{
			part.setColor(picker.color, (int) this.value);
			picker.onColorChanged();
		}
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		helper.drawHorizontalChannelMaskGradientRect(0, 0, width, height, ColorUtils.RGBAToInt(picker.color), part.getBrightest());
		
		super.renderContent(helper, style, width, height);
	}
}