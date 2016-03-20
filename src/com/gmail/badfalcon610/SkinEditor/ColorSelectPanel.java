package com.gmail.badfalcon610.SkinEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorSelectPanel extends JPanel {

	public static void main(String[] args) {
		JPanel panel = new JPanel();
		ColorSelectPanel t = new ColorSelectPanel(null);
		panel.add(t);
		new TestFrame(panel);
		t.addToHistory();
	}

	Color colormain;
	Color colorsub;

	JLayeredPane preview;

	PreviewBox ppanelmain;
	PreviewBox ppanelsub;
	HistoryPanel historypanel;

	JButton exchangebutton;
	JTabbedPane tabbedPane;

	HSBPanel hsb;
	RGBSlider rgb;
	RGBPanel rgbpanel;

	EditorMain main;

	public ColorSelectPanel(EditorMain mf) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			//
		} catch (InstantiationException e) {
			//
		} catch (IllegalAccessException e) {
			//
		} catch (UnsupportedLookAndFeelException e) {
			//
		}
		SwingUtilities.updateComponentTreeUI(this);

		this.main = mf;
		colormain = Color.RED;
		colorsub = Color.WHITE;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setFocusable(false);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addChangeListener(new TabListener());

		hsb = new HSBPanel(this);
		rgb = new RGBSlider(this);
		rgbpanel = new RGBPanel(this);

		tabbedPane.addTab("HSB", null, hsb, null);
		tabbedPane.addTab("RGBPanel", null, rgbpanel, null);
		tabbedPane.addTab("RGBSlider", null, rgb, null);

		add(tabbedPane);

		// JPanel Sample = new JPanel();
		// tabbedPane.addTab("Sample", null, Sample, null);

		preview = new JLayeredPane();
		SpringLayout layout = new SpringLayout();
		preview.setLayout(layout);
		// preview.setLayout(new GridLayout(0, 3, 10, 10));

		ppanelmain = new PreviewBox(colormain, 60, 60);
		ppanelsub = new PreviewBox(colorsub, 60, 60);

		exchangebutton = new JButton();
		exchangebutton.setPreferredSize(new Dimension(30, 30));
		exchangebutton.setBorderPainted(false);
		exchangebutton.addActionListener(new ChangeActionListener());
		historypanel = new HistoryPanel(5, 2);

		layout.putConstraint(SpringLayout.NORTH, ppanelmain, 0, SpringLayout.NORTH, tabbedPane);
		layout.putConstraint(SpringLayout.WEST, ppanelmain, 0, SpringLayout.WEST, preview);

		layout.putConstraint(SpringLayout.NORTH, ppanelsub, -30, SpringLayout.SOUTH, ppanelmain);
		layout.putConstraint(SpringLayout.WEST, ppanelsub, -30, SpringLayout.EAST, ppanelmain);

		layout.putConstraint(SpringLayout.NORTH, exchangebutton, 0, SpringLayout.SOUTH, ppanelmain);
		layout.putConstraint(SpringLayout.WEST, exchangebutton, 0, SpringLayout.WEST, ppanelmain);

		layout.putConstraint(SpringLayout.NORTH, historypanel, 0, SpringLayout.NORTH, tabbedPane);
		layout.putConstraint(SpringLayout.EAST, historypanel, 0, SpringLayout.EAST, this);

		preview.add(ppanelsub);

		preview.add(ppanelmain);

		preview.add(exchangebutton);

		preview.add(historypanel);

		// this.setMinimumSize(new Dimension(300, 403));

		add(preview);

		// setMainColor(new
		// Color(Integer.parseInt(main.configuration.getProperty("primaryColor",
		// String.valueOf(new Color(Color.HSBtoRGB(0.0f, 1.0f,
		// 1.0f)).getRGB())))));

		setPreferredSize(new Dimension(310, 372));

		preview.moveToFront(ppanelmain);
	}

	class TabListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
			PalletPanel pp = (PalletPanel) tabbedPane.getSelectedComponent();
			pp.update(colormain);
		}

	}

	public void addTo(Component c) {
		tabbedPane.add("preview", c);
	}

	public void setMainColor(Color c) {
		colormain = c;
		ppanelmain.setColor(c);
		// main.configuration.setProperty("primaryColor",
		// String.valueOf(c.getRGB()));
		System.out.println(System.currentTimeMillis() + " , " + ppanelmain.color.getRed());
		preview.moveToFront(ppanelmain);
	}

	public void setMainColor(float[] hsbc) {
		Color c = new Color(Color.HSBtoRGB(hsbc[0], hsbc[1], hsbc[2]));
		setMainColor(c);
	}

	public void setMainColor(int[] rgbvalue) {
		Color c = new Color(rgbvalue[0], rgbvalue[1], rgbvalue[2]);
		setMainColor(c);
	}

	public Color getMainColor() {
		return colormain;
	}

	public Color getSubColor() {
		return colorsub;
	}

	public void addToHistory() {
		historypanel.addColor(colormain);
		historypanel.repaint();
	}

	public class PreviewBox extends JPanel {
		private Color color;

		private final int WIDTH;
		private final int HEIGHT;

		public PreviewBox(Color c, int w, int h) {
			color = c;
			WIDTH = w;
			HEIGHT = h;
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			LineBorder lb1 = new LineBorder(Color.LIGHT_GRAY, 5);
			LineBorder lb2 = new LineBorder(Color.GRAY);
			CompoundBorder cb = new CompoundBorder(lb2, lb1);

			setBorder(cb);
		}

		public void setColor(Color c) {
			color = c;
			repaint();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D display = (Graphics2D) g.create();
			display.setPaint(color);
			display.fillRect(0, 0, WIDTH, HEIGHT);
			display.dispose();
		}

	}

	public class HistoryPanel extends JPanel {

		final int x;
		final int y;

		LinkedList<Color> colorhistory;

		HistorySinglePanel[][] history;

		HistoryPanel(int x, int y) {
			this.x = x;
			this.y = y;
			colorhistory = new LinkedList<Color>();
			LineBorder lb = new LineBorder(Color.BLACK);
			TitledBorder tb = new TitledBorder(lb, "History");
			setLayout(new GridLayout(y, x));
			// mainpreview.setAlignmentY(CENTER_ALIGNMENT);
			history = new HistorySinglePanel[y][x];
			for (int j = 0; j < y; j++) {
				for (int i = 0; i < x; i++) {
					history[j][i] = new HistorySinglePanel(35, 35);
					add(history[j][i]);
				}
			}
			setBorder(tb);
		}

		void addColor(Color c) {
			if (colorhistory.contains(c)) {
				colorhistory.remove(c);
			}
			colorhistory.addFirst(c);
			if (colorhistory.size() > x * y) {
				colorhistory.removeLast();
			}
			for (int i = 0; i < colorhistory.size(); i++) {
				history[i / x][i % x].setColor(colorhistory.get(i));

			}
		}

		class HistorySinglePanel extends JPanel {
			private Color color;

			private final int WIDTH;
			private final int HEIGHT;

			HistorySinglePanel(int w, int h) {
				WIDTH = w;
				HEIGHT = h;
				setPreferredSize(new Dimension(WIDTH, HEIGHT));
				setBorder(new BevelBorder(BevelBorder.LOWERED));
				addMouseListener(new historyMouseListener());
			}

			void setColor(Color c) {
				color = c;
				repaint();
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D display = (Graphics2D) g.create();
				if (color != null) {
					display.setPaint(color);
				} else {
					display.setPaint(new Color(0, 0, 0, 0));
				}
				display.fillRect(0, 0, WIDTH, HEIGHT);
				display.dispose();
			}

			class historyMouseListener extends MouseAdapter {
				public void mouseClicked(MouseEvent e) {
					if (color != null) {
						setMainColor(color);
					}

				}
			}

		}

	}

	class ChangeActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Color temp = new Color(colormain.getRGB());
			setMainColor(colorsub);
			colorsub = new Color(temp.getRGB());
			ppanelsub.setColor(colorsub);
		}
	}

}
