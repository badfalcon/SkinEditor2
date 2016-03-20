package com.gmail.badfalcon610.SkinEditor;

import java.awt.image.BufferedImage;

public class Layer {
	String layerName;
	BufferedImage image;

	public Layer(String name) {
		layerName = name;
		image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB_PRE);
	}
}
