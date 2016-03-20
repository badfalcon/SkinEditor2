
package com.gmail.badfalcon610.SkinEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.gmail.badfalcon610.SkinViewer.ViewerMain;

public class MyMenuBar extends JMenuBar {
	final String[] menuName = { "file", "edit", "settings", "Skin", "view", "Help" };

	final String[] fileItemName = { "New", "Open", "Save", "Save as" };

	final String[] fileItemIdentifier = { "new", "open", "overwrite", "save" };

	JMenuItem[] editItemList;

	final String[] editItemName = { "Undo", "Redo", "Copy", "Cut", "Paste" };

	final String[] editItemIdentifier = { "undo", "redo", "copy", "cut", "paste" };

	JMenuItem[] settingItemList;

	final String[] settingItemName = { "change background color", "change format color", "reset color settings",
			"toggle format model" };
	final String[] settingItemIdentifier = { "change background color", "change format color", "reset color settings",
			"toggle format model" };

	final String[] formatName = { "Outer Head", "Outer Body", "Outer Arm(R)", "Outer Arm(L)", "Outer Leg(R)",
			"Outer Leg(L)", "Inner Head", "Inner Body", "Inner Arm(R)", "Inner Arm(L)", "Inner Leg(R)", "Inner Leg(L)",
			"Outer", "Inner", "All" };
	final String[] formatIdentifier = { "Outer Head", "Outer Body", "Outer Arm(R)", "Outer Arm(L)", "Outer Leg(R)",
			"Outer Leg(L)", "Inner Head", "Inner Body", "Inner Arm(R)", "Inner Arm(L)", "Inner Leg(R)", "Inner Leg(L)",
			"Outer", "Inner", "All" };

	JCheckBoxMenuItem[] skinItemList;

	final String[] toolItemName = { "preview skin", "upload and use in Minecraft" };

	final String[] toolItemIdentifier = { "preview", "upload" };

	final String[] skinItemName = { "older preview", "Slim Skin", "upload and use in Minecraft" };
	final String[] skinItemIdentifier = { "old", "slim", "upload" };

	JCheckBoxMenuItem[] formatItemList = new JCheckBoxMenuItem[formatName.length];

	JMenuItem[] displayItemList;

	final String[] displayItemName = { "hidepallet", "preview" };
	final String[] displayItemIdentifier = { "hidepallet", "showPrev" };

	JMenuItem[] fileItemList;

	private EditorMain main;

	public MyMenuBar(EditorMain main) {
		this.main = main;

		JMenu[] menuList = new JMenu[menuName.length];

		for (int i = 0; i < menuName.length; i++) {
			menuList[i] = new JMenu(menuName[i]);
			add(menuList[i]);
		}

		FileActionListener fal = new FileActionListener();

		fileItemList = new JMenuItem[fileItemName.length];

		for (int i = 0; i < fileItemName.length; i++) {
			fileItemList[i] = new JMenuItem(fileItemName[i]);
			menuList[0].add(fileItemList[i]);
			fileItemList[i].addActionListener(fal);
			fileItemList[i].setActionCommand(fileItemIdentifier[i]);
		}
		fileItemList[0].setMnemonic(KeyEvent.VK_N);
		fileItemList[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		fileItemList[1].setMnemonic(KeyEvent.VK_O);
		fileItemList[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		fileItemList[2].setMnemonic(KeyEvent.VK_S);
		fileItemList[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

		editItemList = new JMenuItem[editItemName.length];

		EditActionListener eal = new EditActionListener();

		for (int i = 0; i < editItemName.length; i++) {
			editItemList[i] = new JMenuItem(editItemName[i]);
			menuList[1].add(editItemList[i]);
			editItemList[i].addActionListener(eal);
			editItemList[i].setActionCommand(editItemIdentifier[i]);
			editItemList[i].setEnabled(false);
		}

		editItemList[0].setMnemonic(KeyEvent.VK_Z);
		editItemList[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		editItemList[1].setMnemonic(KeyEvent.VK_Y);
		editItemList[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
		editItemList[2].setMnemonic(KeyEvent.VK_C);
		editItemList[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		editItemList[3].setMnemonic(KeyEvent.VK_X);
		editItemList[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		editItemList[4].setMnemonic(KeyEvent.VK_V);
		editItemList[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));

		SettingActionListener seal = new SettingActionListener();

		settingItemList = new JMenuItem[settingItemName.length];
		for (int i = 0; i < settingItemName.length; i++) {
			if (i == 3) {
				menuList[2].addSeparator();
				settingItemList[i] = new JCheckBoxMenuItem(settingItemName[i]);
			} else {
				settingItemList[i] = new JMenuItem(settingItemName[i]);
			}
			menuList[2].add(settingItemList[i]);
			settingItemList[i].addActionListener(seal);
			settingItemList[i].setActionCommand(settingItemIdentifier[i]);
		}

		SkinActionListener skal = new SkinActionListener();

		skinItemList = new JCheckBoxMenuItem[skinItemName.length];
		for (int i = 0; i < skinItemName.length; i++) {
			if (i == 1) {
				menuList[3].addSeparator();
			}
			skinItemList[i] = new JCheckBoxMenuItem(skinItemName[i]);
			menuList[3].add(skinItemList[i]);
			skinItemList[i].addActionListener(skal);
			skinItemList[i].setActionCommand(skinItemIdentifier[i]);
		}

		DisplayActionListener dal = new DisplayActionListener();

		displayItemList = new JMenuItem[displayItemName.length];
		for (int i = 0; i < displayItemName.length; i++) {
			if (i == 1) {
				displayItemList[i] = new JMenuItem(displayItemName[i]);
			} else {
				displayItemList[i] = new JCheckBoxMenuItem(displayItemName[i]);
			}
			menuList[4].add(displayItemList[i]);
			displayItemList[i].addActionListener(dal);
			displayItemList[i].setActionCommand(displayItemIdentifier[i]);
		}

		HelpActionListener hal = new HelpActionListener();

		JMenuItem helpitem1 = new JMenuItem("About");
		helpitem1.addActionListener(hal);
		helpitem1.setActionCommand("about");
		menuList[5].add(helpitem1);

	}

	public class FileActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String act = event.getActionCommand();
			if (act == null) {
				System.out.println("null");
			} else {
				switch (act) {
				case "new":
					main.fm.newAction();
					break;
				case "open":
					main.fm.openAction();
					break;
				case "overwrite":
					main.fm.overWriteAction();
					break;
				case "save":
					main.fm.savenew();
					break;
				}
			}
		}
	}

	public class EditActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String act = event.getActionCommand();

		}

	}

	public class SettingActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String act = event.getActionCommand();

		}

	}

	public class SkinActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String act = event.getActionCommand();

		}

	}

	public class DisplayActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String act = event.getActionCommand();
			if (act == null) {
				System.out.println("null");
			} else {
				switch (act) {
				case "showPrev":
					if (main.hoge == null) {
						try {
							main.hoge = (ViewerMain) main.c.newInstance();
							main.hoge.open(main.fm.image);
						} catch (InstantiationException | IllegalAccessException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
					} else {
						main.hoge.setVisible(true);
					}
					break;
				}
			}
		}

	}

	public class HelpActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String act = event.getActionCommand();

		}

	}

}
