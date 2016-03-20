package com.gmail.badfalcon610.SkinViewer;

import com.jogamp.opengl.GL2;

public class ModelBox {

	float[] xyz;
	int[] whd;
	float[] degree;

	public ModelBox(float x, float y, float z, int width, int height, int depth) {
		xyz = new float[] { x, y, z };
		whd = new int[] { width, height, depth };
		degree = new float[] { 0f, 0f, 0f };
	}

	public void drawModel(GL2 gl, float scale, float rate, int texX, int texY) {
		// setAngle(gl, -limbAngle, 0, -limbzAngle);
		setAngle(gl, degree[0], degree[1], degree[2]);
		move(gl, scale, xyz[0], xyz[1], xyz[2]);
		drawBox(gl, scale, rate, texX, texY, whd[0], whd[1], whd[2]);
	}

	void move(GL2 gl, float scale, float x, float y, float z) {
		gl.glTranslatef(scale * x, scale * y, scale * z);
	}

	void setAngle(GL2 gl, float xAngle, float yAngle, float zAngle) {
		gl.glRotatef(xAngle, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yAngle, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(zAngle, 0.0f, 0.0f, 1.0f);
	}

	void drawBox(GL2 gl, float scale, float rate, int textx, int texty, int width, int height, int depth) {
		// 上面
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, scale * height / 2 * rate, 0.0f);
		gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
		// gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
		drawFace(gl, scale, rate, textx + depth, texty, width, depth, false);
		gl.glPopMatrix();

		// 下面
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, -(scale * height / 2 * rate), 0.0f);
		gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
		drawFace(gl, scale, rate, textx + depth + width, texty + depth, width, -depth, true);
		gl.glPopMatrix();

		// 右面
		gl.glPushMatrix();
		gl.glTranslatef(-(scale * width / 2 * rate), 0.0f, 0.0f);
		gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
		drawFace(gl, scale, rate, textx, texty + depth, depth, height, false);
		gl.glPopMatrix();

		// 正面
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, scale * depth / 2 * rate);
		drawFace(gl, scale, rate, textx + depth, texty + depth, width, height, false);
		gl.glPopMatrix();

		// 左面
		gl.glPushMatrix();
		gl.glTranslatef((scale * width / 2 * rate), 0.0f, 0.0f);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		drawFace(gl, scale, rate, textx + depth + width, texty + depth, depth, height, false);
		gl.glPopMatrix();

		// 背面
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -(scale * depth / 2 * rate));
		gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
		drawFace(gl, scale, rate, textx + depth + width + depth, texty + depth, width, height, false);
		gl.glPopMatrix();

	}

	static void drawFace(GL2 gl, float scale, float rate, int x, int y, int w, int h, boolean reverse) {
		gl.glPushMatrix();

		gl.glBegin(GL2.GL_QUADS);

		if (reverse) {
			gl.glNormal3f(0, 0, -1);
		} else {
			gl.glNormal3f(0, 0, 1);
		}
		// 左上
		gl.glTexCoord2f(1.0f / 64f * x, 1.0f / 64f * y);
		gl.glVertex2f(-scale * w / 2 * rate, scale * h / 2 * rate);
		// 左下
		gl.glTexCoord2f(1.0f / 64f * x, 1.0f / 64f * (y + h));
		gl.glVertex2f(-scale * w / 2 * rate, -scale * h / 2 * rate);
		// 右下
		gl.glTexCoord2f(1.0f / 64f * (x + w), 1.0f / 64f * (y + h));
		gl.glVertex2f(scale * w / 2 * rate, -scale * h / 2 * rate);
		// 右上
		gl.glTexCoord2f(1.0f / 64f * (x + w), 1.0f / 64f * y);
		gl.glVertex2f(scale * w / 2 * rate, scale * h / 2 * rate);

		gl.glEnd();
		gl.glPopMatrix();
	}

}
