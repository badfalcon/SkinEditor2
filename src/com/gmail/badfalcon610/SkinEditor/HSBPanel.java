package com.gmail.badfalcon610.SkinEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class HSBPanel extends PalletPanel {

	public static void main(String[] args) {
		new TestFrame(new HSBPanel(null));
	}

	ColorSelectPanel parent;

	Color color;

	HSPanel hs;
	BPanel b;

	public HSBPanel(ColorSelectPanel pallet) {
		this.parent = pallet;
		color = Color.RED;

		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		JPanel hspanel = new JPanel();
		hs = new HSPanel(this);
		hspanel.add(hs);
		JPanel bpanel = new JPanel();
		b = new BPanel(this);
		bpanel.add(b);
		layout.putConstraint(SpringLayout.NORTH, hspanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, hspanel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, bpanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, bpanel, 0, SpringLayout.EAST, hspanel);
		add(hspanel);
		add(bpanel);
		setPreferredSize(new Dimension(295, 265));
	}

	public void sendColor() {
		parent.setMainColor(color);
	}

	@Override
	public void update(Color c) {
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		hs.hue = (int) (hsb[0] * hs.hueMax);
		hs.sauration = (int) (hsb[1] * hs.saurationMax);
		b.brightness = (int) (hsb[2] * b.brightnessMax);
		colorChanged();
	}

	public void colorChanged() {
		this.color = getColor();
		hs.repaint();
		b.repaint();
	}

	public Color getColor() {
		return Color.getHSBColor(hs.getHue(), hs.getSauration(), b.getBrightness());
	}

	public float getHue() {
		return hs.getHue();
	}

	public float getSauration() {
		return hs.getSauration();
	}

	public float getBrightness() {
		return b.getBrightness();
	}

	public class HSPanel extends JPanel {

		HSBPanel parent;

		int hueMax = 255;
		int saurationMax = 255;
		Dimension size = new Dimension(hueMax, saurationMax);

		int hue;
		int sauration;

		public HSPanel(HSBPanel parent) {
			this.parent = parent;
			hue = 0;
			sauration = 255;
			addMouseListener(new HSListener());
			addMouseMotionListener(new HSListener());
//			setBorder(new BevelBorder(BevelBorder.LOWERED));
			setPreferredSize(size);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D display = (Graphics2D) g.create();
			for (int j = 0; j < saurationMax; j++) {
				for (int i = 0; i < hueMax; i++) {
					display.setPaint(Color.getHSBColor(i / (float) hueMax, j / (float) saurationMax, 1f));
					display.fillRect(i, j, 1, 1);
				}
			}
			display.setPaint(parent.getColor());
			display.drawOval(hue - 5, sauration - 5, 10, 10);
			display.setPaint(Color.BLACK);
			display.drawOval(hue - 5, sauration - 5, 10, 10);
			// display.drawOval((int) (hue * hueMax - 6), (int) ((1.0f -
			// sauration) * saurationMax - 6), 12, 12);
			display.dispose();
		}

		public float getHue() {
			return (hue / (float) hueMax);
		}

		public float getSauration() {
			return (sauration / (float) saurationMax);
		}

		public class HSListener extends MouseAdapter {
			public void mouseClicked(MouseEvent e) {
				updateColor(e);
			}

			public void mouseDragged(MouseEvent e) {
				updateColor(e);
			}

			public void updateColor(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				hue = roundUp(x, 0, hueMax);
				sauration = roundUp(y, 0, saurationMax);
				repaint();

				parent.colorChanged();
				parent.sendColor();
			}

			public int roundUp(int value, int min, int max) {
				if (value < min) {
					return min;
				} else if (max < value) {
					return max;
				} else {
					return value;
				}
			}
		}
	}

	public class BPanel extends JPanel {

		HSBPanel parent;

		final int brightnessMax = 255;
		Dimension size = new Dimension(20, brightnessMax);

		int brightness;

		public BPanel(HSBPanel parent) {
			this.parent = parent;
			brightness = 255;
			ColorSliderBListener bl = new ColorSliderBListener();
			addMouseListener(bl);
			addMouseMotionListener(bl);
			// setBorder(new BevelBorder(BevelBorder.LOWERED));
			setPreferredSize(size);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D display = (Graphics2D) g.create();
			for (int i = 0; i < brightnessMax; i++) {
				display.setPaint(Color.getHSBColor(parent.getHue(), parent.getSauration(), i / (float) brightnessMax));
				display.drawLine(0, i, getWidth(), i);
			}
			display.setPaint(new Color(255, 255, 255));
			display.drawLine(0, brightness, getWidth(), brightness);
			display.dispose();
		}

		public float getBrightness() {
			return (brightness / (float) brightnessMax);
		}

		public class ColorSliderBListener extends MouseAdapter {
			public void mouseClicked(MouseEvent e) {
				updateColor(e);
			}

			public void mouseDragged(MouseEvent e) {
				updateColor(e);
			}

			public void updateColor(MouseEvent e) {
				int y = e.getY();
				brightness = roundUp(y, 0, brightnessMax);
				repaint();

				parent.colorChanged();
				parent.sendColor();
			}

			public int roundUp(int value, int min, int max) {
				if (value < min) {
					return min;
				} else if (max < value) {
					return max;
				} else {
					return value;
				}
			}
		}

	}

}
