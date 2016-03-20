package com.gmail.badfalcon610.SkinViewer;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Preview implements GLEventListener {

	private GL2 gl;
	FPSAnimator animator;
	private GLUT glut;

	private float scale = 0.2f;

	float angleX = 0.0f;
	float angleY = 0.0f;

	boolean walk = false;
	private static final int MAX_ANGLE = 30;
	private int limbXAngle;
	private boolean limbMode;
	private static final double MAX_Z_ANGLE = 1.0;
	private boolean limbzMode;
	private double rad;

	float zoom;

	private Texture ground;
	TextureData newSkin = null;
	URL newGround = null;

	private final String groundPass = "img/grass.png";

	Player player;

	PreviewPanel pp;

	public Preview(GLAutoDrawable canvas, PreviewPanel pp) {
		this.pp = pp;
		player = new Player();

		canvas.addGLEventListener(this);

		canvas.getAnimator();
	}

	public void reSkin(BufferedImage skinImage) {
		newSkin = AWTTextureIO.newTextureData(GLProfile.getDefault(), skinImage, true);

	}

	public void reGround(String pass) {
		newGround = getClass().getResource(pass);
	}

	@Override
	public void display(GLAutoDrawable drawable) {

		// background colorS
		gl.glClearColor(0.0f, 0.5f, 1.0f, 0.0f);

		if (newSkin != null) {
			player.skin = TextureIO.newTexture(newSkin);
			// texture.enable(gl);
			// texture.bind(gl);
			newSkin = null;

		}
		if (newGround != null) {
			try {
				ground = TextureIO.newTexture(newGround, false, "png");
			} catch (GLException | IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} finally {
				newGround = null;
			}
		}

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		float[] lightPos = { 0, 5, 0, 0 };
		float[] lightAmbient = { 1f, 1f, 1f, 1f }; // low ambient light
		float[] lightSpecular = { 1f, 1f, 1f, 1f }; // low ambient light
		float[] lightDiffuse = { 1f, 1f, 1f, 1f };
		float[] lightDirection = { 0, -1, 0 };

		gl.glPushMatrix();

		gl.glTranslatef(0.0f, 0.0f, scale * zoom);
		gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0, scale * -(player.LIMB[1] + player.BODY[1] / 2), 0);

		// properties of the light
		// 環境光
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_AMBIENT, lightAmbient, 0);
		// 鏡面光
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_SPECULAR, lightSpecular, 0);
		// 拡散光
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, lightDiffuse, 0);
		// 照明の位置
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, lightPos, 0);
		// 照明の方向
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_SPOT_DIRECTION, lightDirection, 0);
		gl.glEnable(GLLightingFunc.GL_LIGHT0);

		// grass
		drawGrass(10);

		// player

		float[] playerAmbient = { 0.45f, 0.45f, 0.45f, 1f }; // low ambient
																// light
		gl.glMaterialfv(GL_FRONT, GL2.GL_AMBIENT, playerAmbient, 0);

		player.drawPlayer(gl, scale);

		gl.glPopMatrix();

		if (walk) {
			if (limbMode) {
				limbXAngle -= 1;
				if (limbXAngle <= -MAX_ANGLE) {
					limbMode = false;
				}
			} else {
				limbXAngle += 1;
				if (limbXAngle >= MAX_ANGLE) {
					limbMode = true;
				}
			}
			pp.main.aPanel.rArmPanel.sliders[0].setValue(limbXAngle);
			pp.main.aPanel.lArmPanel.sliders[0].setValue(-limbXAngle);
			pp.main.aPanel.rLegPanel.sliders[0].setValue(-limbXAngle);
			pp.main.aPanel.lLegPanel.sliders[0].setValue(limbXAngle);
		}

	}

	int toAngle(int n) {
		while (n < 0) {
			n += 360;
		}
		while (n > 360) {
			n -= 360;
		}
		return n;
	}

	void drawGrass(int n) {
		ground.enable(gl);
		ground.bind(gl);

		// テクスチャのリピートの設定
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

		gl.glPushMatrix();

		gl.glRotatef(270f, 1.0f, 0.0f, 0.0f);

		gl.glPolygonMode(GL_FRONT, GL2.GL_FILL);
		gl.glPolygonMode(GL_BACK, GL2.GL_POINT);

		gl.glBegin(GL2.GL_QUADS);

		gl.glNormal3f(0, 0, 1);

		// 左下
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(-n, -n, 0);
		// 左上
		gl.glTexCoord2f(n, 0);
		gl.glVertex3f(n, -n, 0);
		// 右上
		gl.glTexCoord2f(n, n);
		gl.glVertex3f(n, n, 0);
		// 右下
		gl.glTexCoord2f(0, n);
		gl.glVertex3f(-n, n, 0);

		gl.glEnd();

		gl.glPopMatrix();

		ground.disable(gl);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		zoom = 0.0f;
		gl = drawable.getGL().getGL2();
		glut = new GLUT();

		try {
			ground = TextureIO.newTexture(getClass().getResource(groundPass), true, "PNG");
			player.skin = AWTTextureIO.newTexture(GLProfile.getDefault(),
					ImageIO.read(getClass().getResource("img/new.png")), true);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// テクスチャの拡大方法の指定
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_LIGHTING);
		// 深さ設定
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);

		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		float ratio = (float) height / (float) width;
		gl.glViewport(0, 0, width, height);

		// 定数はGLではなく、GL2にあります。
		// （正確にはjavax.media.opengl.fixedfunc.GLMatrixFuncみたい）
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -ratio, ratio, 5.0f, 40.0f);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -20.0f);

	}

	void rotate(float thetaX, float thetaY) {
		if (-90 < angleX - thetaX && angleX - thetaX < 90) {
			angleX -= thetaX;
		} else if (-90 >= angleX - thetaX) {
			angleX = -90;
		} else if (angleX - thetaX >= 90) {
			angleX = 90;
		}
		angleY += thetaY;
		if (angleY > 180f) {
			angleY -= 360f;
		} else if (angleY < -180f) {
			angleY += 360f;
		}

	}
}
