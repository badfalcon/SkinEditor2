package com.gmail.badfalcon610.SkinViewer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

public class PreviewPanel extends GLJPanel {

	FPSAnimator animator;
	Preview preview;

	int prevMouseX;
	int prevMouseY;

	ViewerMain main;

	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		frame.setSize(new Dimension(300, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PreviewPanel panel = new PreviewPanel(null);
		frame.add(panel);
		frame.setVisible(true);

	}

	void start() {
		animator = new FPSAnimator(this, 60);
		animator.start();
	}

	public PreviewPanel(ViewerMain main) {
		this.main = main;

		preview = new Preview(this, this);

		start();

		PreviewMouseListener pml = new PreviewMouseListener(this);

		addMouseListener(pml);

		addMouseMotionListener(pml);

		addMouseWheelListener(pml);

		setPreferredSize(new Dimension(300, 500));

	}

	public void open(File file) {
		BufferedImage temp = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		try {
			temp = ImageIO.read(file);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		open(temp);
	}

	public void open(BufferedImage image) {
		int width = image.getWidth();
		if (width == 64) {
			int height = image.getHeight();
			if (height == 32) {
				main.loadedImage = Util.convert(image, this);
				preview.reSkin(Util.convert(image, this));
			} else if (height == 64) {
				main.loadedImage = image;
				preview.reSkin(image);
			} else {
				JOptionPane.showMessageDialog(this, "unsupported size");
			}
		} else {
			JOptionPane.showMessageDialog(this, "unsupported size");
		}
	}

}
