package com.gmail.badfalcon610.SkinViewer;

import static com.jogamp.opengl.GL.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Player {

	// TEXTURE START POSITION
	// HEAD,BODY,RARM,RLEG,LARM,LLEG
	final int[] OUT = { 32, 0, 16, 32, 0, 32, 40, 32, 0, 48, 48, 48 };
	final int[] IN = { 0, 0, 16, 16, 0, 16, 40, 16, 16, 48, 32, 48 };

	// TEXTURE SIZE
	// WIDTH,HEIGHT,DEPTH
	final int[] HEAD = { 8, 8, 8 };
	final int[] BODY = { 8, 12, 4 };
	final int[] LIMB = { 4, 12, 4 };
	final int[] SLIMB = { 3, 12, 4 };

	// MODEL CLASSES OF PLAYER
	ModelBox headModel;
	ModelBox bodyModel;
	ModelBox rArmModel;
	ModelBox lArmModel;
	ModelBox rLegModel;
	ModelBox lLegModel;

	Texture skin;

	SkinType type;

	boolean showHead = true;
	boolean showBody = true;
	boolean showArms = true;
	boolean showLegs = true;
	boolean showOuter = true;
	boolean showInner = true;

	public Player() {

		type = new SkinType();

		headModel = new ModelBox(0, 4, 0, HEAD[0], HEAD[1], HEAD[2]);
		bodyModel = new ModelBox(0, 0, 0, BODY[0], BODY[1], BODY[2]);
		rArmModel = new ModelBox(0, -4, 0, LIMB[0], LIMB[1], LIMB[2]);
		lArmModel = new ModelBox(0, -4, 0, LIMB[0], LIMB[1], LIMB[2]);
		rLegModel = new ModelBox(0, -6, 0, LIMB[0], LIMB[1], LIMB[2]);
		lLegModel = new ModelBox(0, -6, 0, LIMB[0], LIMB[1], LIMB[2]);
	}

	public void drawPlayer(GL2 gl, float scale) {
		skin.enable(gl);
		skin.bind(gl);

		// テクスチャの拡大縮小の指定
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		gl.glPolygonMode(GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();

		gl.glTranslatef(0, scale * (LIMB[1] + BODY[1] / 2), 0);

		if (showInner) {
			gl.glDisable(GL_BLEND);
			drawPlayer(gl, scale, 1.0f, IN, type.getLimbWidth());
		}

		if (showOuter && !type.old) {
			gl.glEnable(GL_BLEND);
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			drawPlayer(gl, scale, 1.1f, OUT, type.getLimbWidth());
		}

		gl.glPopMatrix();

		skin.disable(gl);
	}

	void drawPlayer(GL2 gl, float scale, float rate, int[] offset, int limbWidth) {
		// 頭
		if (showHead) {
			gl.glPushMatrix();
			move(gl, scale, 0, BODY[1] / 2, 0);
			headModel.drawModel(gl, scale, rate, offset[0], offset[1]);
			gl.glPopMatrix();
		}
		// 体
		if (showBody) {
			gl.glPushMatrix();
			bodyModel.drawModel(gl, scale, rate, offset[2], offset[3]);
			gl.glPopMatrix();
		}
		// 右足
		if (showLegs) {
			gl.glPushMatrix();
			move(gl, scale, -(LIMB[0] / 2), -(BODY[1] / 2), 0);
			rLegModel.drawModel(gl, scale, rate, offset[4], offset[5]);
			gl.glPopMatrix();
		}
		// 右手
		if (showArms) {
			rArmModel.whd[0] = limbWidth;
			gl.glPushMatrix();
			move(gl, scale, -(limbWidth + BODY[0]) / 2.0f, LIMB[1] / 2 - 2, 0);
			rArmModel.drawModel(gl, scale, rate, offset[6], offset[7]);
			gl.glPopMatrix();
		}
		// 左足
		if (showLegs) {
			gl.glPushMatrix();
			move(gl, scale, LIMB[0] / 2, -(BODY[1] / 2), 0);
			lLegModel.drawModel(gl, scale, rate, offset[8], offset[9]);
			gl.glPopMatrix();
		}
		// 左手
		if (showArms) {
			lArmModel.whd[0] = limbWidth;
			gl.glPushMatrix();
			move(gl, scale, (limbWidth + BODY[0]) / 2.0f, LIMB[1] / 2 - 2, 0);
			lArmModel.drawModel(gl, scale, rate, offset[10], offset[11]);
			gl.glPopMatrix();
		}
	}

	void move(GL2 gl, float scale, float x, float y, float z) {
		gl.glTranslatef(scale * x, scale * y, scale * z);
	}
}
