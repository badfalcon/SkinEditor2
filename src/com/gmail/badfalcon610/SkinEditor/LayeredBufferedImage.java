package com.gmail.badfalcon610.SkinEditor;

import java.util.LinkedList;

public class LayeredBufferedImage {

	LinkedList<Layer> layers;
	int index;

	public LayeredBufferedImage() {
		layers = new LinkedList<Layer>();
		index = 0;
	}

	public void addLayer() {
		layers.add(index + 1, new Layer("new Layer"));
	}

	public void removeLayer() {
		layers.remove(index);
		index--;
	}
}
