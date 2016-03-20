package com.gmail.badfalcon610.SkinViewer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdjustPanel extends JTabbedPane {

	ViewerMain main;

	JCheckBox alexCheckBox;
	JCheckBox formerCheckBox;
	JCheckBox walkCheckBox;

	SliderPanel headPanel;
	SliderPanel lArmPanel;
	SliderPanel rArmPanel;
	SliderPanel lLegPanel;
	SliderPanel rLegPanel;

	String[] sPart = new String[] { "head", "body", "arms", "legs", "outer" };

	public AdjustPanel(ViewerMain main) {
		this.main = main;

		JPanel tabPanel1 = new JPanel();

		BoxLayout layout1 = new BoxLayout(tabPanel1, BoxLayout.Y_AXIS);
		tabPanel1.setLayout(layout1);

		tabPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		checkBoxActionListener cal = new checkBoxActionListener();
		JCheckBox[] checkBoxes = new JCheckBox[sPart.length];

		for (int i = 0; i < checkBoxes.length; i++) {
			checkBoxes[i] = new JCheckBox(sPart[i], true);
			checkBoxes[i].setActionCommand(sPart[i]);
			checkBoxes[i].addActionListener(cal);
			if (i == 4) {
				tabPanel1.add(Box.createRigidArea(new Dimension(1, 10)));
			}
			tabPanel1.add(checkBoxes[i]);
		}

		JPanel tabPanel2 = new JPanel();

		headPanel = new SliderPanel("head", main.pPanel.preview.player.headModel.degree);
		lArmPanel = new SliderPanel("left arm", main.pPanel.preview.player.lArmModel.degree);
		rArmPanel = new SliderPanel("right arm", main.pPanel.preview.player.rArmModel.degree);
		lLegPanel = new SliderPanel("left leg", main.pPanel.preview.player.lLegModel.degree);
		rLegPanel = new SliderPanel("right leg", main.pPanel.preview.player.rLegModel.degree);

		BoxLayout layout2 = new BoxLayout(tabPanel2, BoxLayout.Y_AXIS);
		tabPanel2.setLayout(layout2);

		tabPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		tabPanel2.add(headPanel);
		tabPanel2.add(lArmPanel);
		tabPanel2.add(rArmPanel);
		tabPanel2.add(lLegPanel);
		tabPanel2.add(rLegPanel);

		JPanel tabPanel3 = new JPanel();

		JPanel groundPanel = new JPanel();

		JLabel groundLabel = new JLabel("ground texture");

		String[] groundTypes = new String[] { "grass", "dirt", "sand", "cobblestone", "white wool" };
		JComboBox<String> groundComboBox = new JComboBox<String>(groundTypes);
		groundComboBox.setMaximumSize(groundComboBox.getPreferredSize());
		groundComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		groundComboBox.addItemListener(new groundItemListener());

		BoxLayout groundLayout = new BoxLayout(groundPanel, BoxLayout.X_AXIS);
		groundPanel.setLayout(groundLayout);

		groundPanel.add(groundLabel);
		groundPanel.add(groundComboBox);

		groundPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		// groundComboBox.setBorder(new LineBorder(Color.RED));

		alexCheckBox = new JCheckBox("Alex (small arms)", false);
		// alexCheckBox.setBorder(new LineBorder(Color.BLUE));
		alexCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		alexCheckBox.setActionCommand("alex");
		alexCheckBox.addActionListener(cal);
		formerCheckBox = new JCheckBox("old look(~1.7)", false);
		formerCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		formerCheckBox.setActionCommand("old");
		formerCheckBox.addActionListener(cal);
		walkCheckBox = new JCheckBox("walk", false);
		walkCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		walkCheckBox.setActionCommand("walk");
		walkCheckBox.addItemListener(new walkItemListener());
		// walkCheckBox.addActionListener(cal);

		BoxLayout layout3 = new BoxLayout(tabPanel3, BoxLayout.Y_AXIS);
		tabPanel3.setLayout(layout3);

		tabPanel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		tabPanel3.add(alexCheckBox);
		tabPanel3.add(formerCheckBox);
		tabPanel3.add(walkCheckBox);
		tabPanel3.add(groundPanel);

		this.addTab("show/hide", tabPanel1);
		this.addTab("rotation", tabPanel2);
		this.addTab("etc", tabPanel3);

		// setPreferredSize(new Dimension(200, 300));
	}

	public class SliderPanel extends JPanel {

		JSlider[] sliders;

		public SliderPanel(String title, float[] f) {
			BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
			setLayout(layout);

			JLabel label = new JLabel(title);
			add(label);

			sliders = new JSlider[3];
			SliderMouseListener sml = new SliderMouseListener();
			for (int i = 0; i < 3; i++) {
				sliders[i] = new JSlider(-180, 180, 0);
				sliders[i].addChangeListener(new SliderListener(f, i));
				sliders[i].addMouseListener(sml);
				add(sliders[i]);
			}

		}

		public class SliderMouseListener extends MouseAdapter {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				int button = e.getButton();
				if (button == MouseEvent.BUTTON2) {
					((JSlider) e.getSource()).setValue(0);
				}
			}
		}

		public class SliderListener implements ChangeListener {

			float[] f;
			int i;

			public SliderListener(float[] f, int i) {
				this.f = f;
				this.i = i;
			}

			@Override
			public void stateChanged(ChangeEvent e) {
				float value = ((JSlider) e.getSource()).getValue();
				if (i != 0) {
					value = -value;
				}
				f[i] = value;
			}
		}

	}

	public class groundItemListener implements ItemListener {
		final String[] groundImagePaths = new String[] { "img/grass.png", "img/dirt.png", "img/sand.png",
				"img/cobblestone.png", "img/w_wool.png" };

		@Override
		public void itemStateChanged(ItemEvent e) {
			int index = ((JComboBox) e.getSource()).getSelectedIndex();
			main.pPanel.preview.reGround(groundImagePaths[index]);
		}

	}

	public class walkItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			System.out.println("item listener");
			boolean b = ((JCheckBox) e.getSource()).isSelected();
			main.pPanel.preview.walk = b;
		}

	}

	public class checkBoxActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("action performed!!!");
			String command = e.getActionCommand();
			boolean b = ((JCheckBox) e.getSource()).isSelected();
			switch (command) {
			case "head":
				main.pPanel.preview.player.showHead = b;
				break;
			case "body":
				main.pPanel.preview.player.showBody = b;
				break;
			case "arms":
				main.pPanel.preview.player.showArms = b;
				break;
			case "legs":
				main.pPanel.preview.player.showLegs = b;
				break;
			case "outer":
				main.pPanel.preview.player.showOuter = b;
				break;
			case "inner":
				main.pPanel.preview.player.showInner = b;
				break;
			case "alex":
				main.pPanel.preview.player.type.slim = b;
				formerCheckBox.setEnabled(!b);
				break;
			case "old":
				if (b) {
					main.pPanel.preview.reSkin(Util.convert(main.loadedImage, main.pPanel));
				} else {
					main.pPanel.preview.reSkin(main.loadedImage);
				}
				main.pPanel.preview.player.type.old = b;
				alexCheckBox.setEnabled(!b);
				break;
			case "walk":
				main.pPanel.preview.walk = b;
				break;
			}
			// main.pPanel.preview.reSkin();
		}
	}
}
