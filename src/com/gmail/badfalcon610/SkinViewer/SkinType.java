package com.gmail.badfalcon610.SkinViewer;

public class SkinType {
	public boolean old;

	public boolean slim;

	// TEXTURE START POSITION
	// HEAD,BODY,RARM,RLEG,LARM,LLEG
	int[] OUT = { 32, 48, 16, 16, 0, 16, 0, 0, 40, 16, 48, 0 };
	int[] IN = { 0, 48, 16, 32, 0, 32, 16, 0, 40, 32, 32, 0 };

	// TEXTURE SIZE
	// WIDTH,HEIGHT,DEPTH
	int[] HEAD = { 8, 8, 8 };
	int[] BODY = { 8, 12, 4 };
	int[] LIMB = { 4, 12, 4 };
	int[] SLIMB = { 3, 12, 4 };

	public SkinType() {
		old = false;
		slim = false;
	}

	public int getLimbWidth() {
		if (!old && slim) {
			return 3;
		} else {
			return 4;
		}
	}
}
