package com.gmail.badfalcon610.SkinEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class RGBSlider extends PalletPanel {

	ColorSelectPanel parent;

	Color color;

	static final int R = 0;
	static final int G = 1;
	static final int B = 2;

	RGBLine red;
	RGBLine green;
	RGBLine blue;

	public static void main(String[] args) {
		RGBSlider rgbslider = new RGBSlider(null);
		TestFrame tf = new TestFrame(rgbslider);
		tf.pack();
	}

	public RGBSlider(ColorSelectPanel parent) {
		this.parent = parent;
		color = Color.RED;

		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		JPanel p = new JPanel();
		p.setMinimumSize(new Dimension(20, 20));
		p.setMaximumSize(new Dimension(20, 20));
		JPanel p0 = new JPanel();
		p0.setMinimumSize(new Dimension(20, 20));
		p0.setMaximumSize(new Dimension(20, 20));
		JPanel p1 = new JPanel();
		p1.setMinimumSize(new Dimension(20, 20));
		p1.setMaximumSize(new Dimension(20, 20));
		red = new RGBLine(this, "R", R, Color.RED);
		green = new RGBLine(this, "G", G, Color.GREEN);
		blue = new RGBLine(this, "B", B, Color.BLUE);
		add(p);
		add(red);
		add(p0);
		add(green);
		add(p1);
		add(blue);
		// add(table);

	}

	public void sendColor() {
		parent.setMainColor(color);
	}

	@Override
	public void update(Color c) {
		red.value = c.getRed();
		green.value = c.getGreen();
		blue.value = c.getBlue();
		red.update();
		green.update();
		blue.update();
		colorChanged();
	}

	public void colorChanged() {
		this.color = new Color(red.value, green.value, blue.value);
	}

	public class RGBLine extends JPanel {

		RGBSlider parent;

		JLabel label;
		JPanel slider;
		TextField field;

		int index;
		int value;

		public RGBLine(RGBSlider parent, String s, int index, Color c) {
			this.parent = parent;

			this.index = index;
			this.value = 0;

			BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
			setLayout(layout);

			label = new JLabel(s);
			slider = new Slider(this, index, c);
			field = new TextField(this);

			add(label);
			add(slider);
			add(field);
			setPreferredSize(new Dimension(289, 75));
		}

		public void update() {
			slider.repaint();
			System.out.println(String.valueOf(value));
			field.setText(String.valueOf(value));
		}

		public class TextField extends JTextField {

			RGBLine parent;

			public TextField(RGBLine parent) {
				this.parent = parent;
				setText(String.valueOf(parent.value));
				setHorizontalAlignment(JTextField.RIGHT);
				setInputVerifier(new IntegerInputVerifier());
				setBorder(new BevelBorder(BevelBorder.LOWERED));
				setMaximumSize(new Dimension(30, 20));
			}

			public class IntegerInputVerifier extends InputVerifier {
				@Override
				public boolean verify(JComponent c) {
					boolean verified = false;
					JTextField textField = (JTextField) c;
					try {
						int i = Integer.parseInt(textField.getText());
						if (0 <= i && i <= 255) {
							verified = true;
							parent.value = i;
							parent.parent.colorChanged();
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
						// UIManager.getLookAndFeel().provideErrorFeedback(c);
						// Toolkit.getDefaultToolkit().beep();
					}
					return verified;
				}
			}

		}

		public class Slider extends JPanel {

			RGBLine parent;
			int index;

			int max = 255;
			Dimension size = new Dimension(max, 20);

			Color[] color;

			public Slider(RGBLine parent, int index, Color color) {
				this.parent = parent;
				this.index = index;
				this.color = new Color[] { Color.BLACK, color };
				SliderListener sl = new SliderListener();
				addMouseListener(sl);
				addMouseMotionListener(sl);
				setMaximumSize(size);
				// setBorder(new BevelBorder(BevelBorder.LOWERED));
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D display = (Graphics2D) g.create();

				float[] dist = new float[] { 0f, 1f };
				LinearGradientPaint gradient = new LinearGradientPaint(new Point2D.Double(0, 0),
						new Point2D.Double(max, 0), dist, color, MultipleGradientPaint.CycleMethod.NO_CYCLE);

				display.setPaint(gradient);

				display.fill(new Rectangle2D.Double(0, 0, max, getHeight()));

				display.setPaint(new Color(255, 255, 255));
				display.drawLine(parent.value, 0, parent.value, getHeight());
				display.dispose();
			}

			public class SliderListener extends MouseAdapter {

				public void mouseClicked(MouseEvent e) {
					updateColor(e);
				}

				public void mouseDragged(MouseEvent e) {
					updateColor(e);
				}

				public void updateColor(MouseEvent e) {
					int x = e.getX();
					parent.value = roundUp(x, 0, max);
					parent.update();

					parent.parent.colorChanged();
					parent.parent.sendColor();
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
}
