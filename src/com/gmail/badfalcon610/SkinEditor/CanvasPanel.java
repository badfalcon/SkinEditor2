package com.gmail.badfalcon610.SkinEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

public class CanvasPanel extends JPanel {

	public static void main(String[] args) {
		new TestFrame(new CanvasPanel());
	}

	EditorMain main;
	CanvasMainPanel canvasMain;

	public CanvasPanel(EditorMain main) {
		this();

		this.main = main;
	}

	public CanvasPanel() {

		addComponentListener(new ResizeAdapter());

		setLayout(null);

		setPreferredSize(new Dimension(300, 300));

		setMinimumSize(new Dimension(100, 100));

		canvasMain = new CanvasMainPanel(this);
		add(canvasMain);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D display = (Graphics2D) g.create();

		display.setPaint(Color.LIGHT_GRAY);
		display.fill(new Rectangle(0, 0, getWidth(), getHeight()));
		display.dispose();
	}

	public class ResizeAdapter extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			int width = e.getComponent().getWidth();
			int height = e.getComponent().getHeight();
			rescale();

			int a = (width - 64 * canvasMain.scale) / 2;
			int b = (height - 64 * canvasMain.scale) / 2;
			int c = 64 * canvasMain.scale;
			int d = 64 * canvasMain.scale;
			System.out.println(a + " : " + b + " : " + c + " : " + d);

			canvasMain.setBounds(a, b, c, d);
			repaint();
		}
	}

	public void rescale() {

		int width = this.getWidth();
		int height = this.getHeight();

		double widthscale = width / 72;
		double heightscale = height / (64 + 8);

		if (widthscale < heightscale) {
			canvasMain.scale = (int) widthscale;
		} else {
			canvasMain.scale = (int) heightscale;
		}
		System.out.println("canvas scale set to " + canvasMain.scale);

		canvasMain.setSize();
		setPreferredSize(new Dimension(canvasMain.scale * 72, canvasMain.scale * (64 + 8)));

		repaint();
	}
}
