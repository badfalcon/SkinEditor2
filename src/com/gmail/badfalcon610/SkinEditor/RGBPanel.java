package com.gmail.badfalcon610.SkinEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class RGBPanel extends PalletPanel {

	public static void main(String[] args) {
		new TestFrame(new RGBPanel(null));
	}

	ColorSelectPanel parent;

	Color color;

	GBPanel gb;
	RPanel r;

	public RGBPanel(ColorSelectPanel pp) {
		this.parent = pp;
		color = Color.RED;

		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		JPanel hspanel = new JPanel();
		gb = new GBPanel(this);
		hspanel.add(gb);
		JPanel bpanel = new JPanel();
		r = new RPanel(this);
		bpanel.add(r);
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
		r.red = c.getRed();
		gb.green = c.getGreen();
		gb.blue = c.getBlue();
		colorChanged();
	}

	public void colorChanged() {
		this.color = new Color(r.red, gb.green, gb.blue);
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		System.out.println(hsb[0] + " , " + hsb[1] + " , " + hsb[2]);
		r.repaint();
		gb.repaint();
	}

	public class GBPanel extends JPanel {

		RGBPanel parent;

		final int greenMax = 255;
		final int blueMax = 255;
		Dimension size = new Dimension(greenMax, blueMax);

		int green;
		int blue;

		public GBPanel(RGBPanel parent) {
			this.parent = parent;
			green = 0;
			blue = 0;
			GBListener gbl = new GBListener();
			addMouseListener(gbl);
			addMouseMotionListener(gbl);
			setPreferredSize(size);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D display = (Graphics2D) g.create();

			for (int y = 0; y <= blueMax; y++) {
				for (int x = 0; x <= greenMax; x++) {
					display.setPaint(new Color(parent.color.getRed(), x, y));
					display.fillRect(x, y, 1, 1);
				}
			}

			display.setPaint(new Color(parent.color.getRed(), green, blue));
			display.fillOval(green - 5, blue - 5, 10, 10);
			display.setPaint(Color.BLACK);
			display.drawOval(green - 5, blue - 5, 10, 10);

			display.dispose();
		}

		public class GBListener extends MouseAdapter {
			public void mouseClicked(MouseEvent e) {
				updateColor(e);
			}

			public void mouseDragged(MouseEvent e) {
				updateColor(e);
			}

			public void updateColor(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				green = roundUp(x, 0, greenMax);
				blue = roundUp(y, 0, blueMax);
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

	public class RPanel extends JPanel {

		RGBPanel parent;

		final int redMax = 255;
		Dimension size = new Dimension(20, redMax);

		int red;

		public RPanel(RGBPanel parent) {
			this.parent = parent;
			red = 255;
			RListener rl = new RListener();
			addMouseListener(rl);
			addMouseMotionListener(rl);
			// setBorder(new BevelBorder(BevelBorder.LOWERED));
			setPreferredSize(size);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D display = (Graphics2D) g.create();
			for (int i = 0; i <= redMax; i++) {
				display.setPaint(new Color(i, parent.color.getGreen(), parent.color.getBlue()));
				display.drawLine(0, i, getWidth(), i);
			}
			display.setPaint(new Color(255, 255, 255));
			display.drawLine(0, red, getWidth(), red);
			display.dispose();
		}

		public class RListener extends MouseAdapter {
			public void mouseClicked(MouseEvent e) {
				updateColor(e);
			}

			public void mouseDragged(MouseEvent e) {
				updateColor(e);
			}

			public void updateColor(MouseEvent e) {
				int y = e.getY();
				red = roundUp(y, 0, redMax);
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
