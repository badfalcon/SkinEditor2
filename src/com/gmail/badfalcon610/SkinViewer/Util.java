package com.gmail.badfalcon610.SkinViewer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import com.jogamp.opengl.GL2;

public class Util {
	public static BufferedImage convert(BufferedImage source, ImageObserver io) {
		BufferedImage tempImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D gtemp = tempImage.createGraphics();
		gtemp.drawImage(source, 0, 0, io);

		reverse(source, gtemp, 0, 16, 16, 48, io);
		reverse(source, gtemp, 40, 16, 32, 48, io);

		gtemp.dispose();
		source = tempImage;
		return source;
	}

	public static void reverse(BufferedImage source, Graphics2D gtemp, int x, int y, int dx, int dy, ImageObserver io) {
		gtemp.drawImage(source, dx + 12, dy + 4, dx + 8, dy + 16, x + 0, y + 4, x + 4, y + 16, io);
		gtemp.drawImage(source, dx + 8, dy + 0, dx + 4, dy + 16, x + 4, y + 0, x + 8, y + 16, io);
		gtemp.drawImage(source, dx + 4, dy + 4, dx + 0, dy + 16, x + 8, y + 4, x + 12, y + 16, io);
		gtemp.drawImage(source, dx + 12, dy + 0, dx + 8, dy + 4, x + 8, y + 0, x + 12, y + 4, io);
		gtemp.drawImage(source, dx + 16, dy + 4, dx + 12, dy + 16, x + 12, y + 4, x + 16, y + 16, io);
	}

	public static void drawNormal(GL2 gl, float x, float y, float z) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(x, y, z);
		gl.glEnd();
	}

}
