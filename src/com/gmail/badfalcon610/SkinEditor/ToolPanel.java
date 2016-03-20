package com.gmail.badfalcon610.SkinEditor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class ToolPanel extends JPanel {
	/**
	 *
	 */
	private EditorMain main;

	int tool;
	int previousTool;

	public int[] mnemonic = { KeyEvent.VK_B, KeyEvent.VK_E, KeyEvent.VK_I, KeyEvent.VK_F, KeyEvent.VK_L, KeyEvent.VK_R,
			KeyEvent.VK_R, KeyEvent.VK_C, KeyEvent.VK_C, KeyEvent.VK_E, KeyEvent.VK_S };

	ButtonGroup buttongroup;

	JToggleButton[] togglebuttons;

	static final String[] imgpass = { "img/brush.png", "img/eraser.png", "img/color_dropper.png",
			"img/paint_bucket.png", "img/line.png", "img/rectangle.png", "img/frectangle.png", "img/ellipse.png",
			"img/fellipse.png", "img/feraser.png", "img/select.png" };

	final String[] commandName = { "brush", "eraser", "dropper", "bucket", "line", "square", "fsquare", "ellipse",
			"fellipse", "feraser", "select" };

	public ToolPanel(EditorMain main) {

		this.main = main;
		JToolBar toolbar = new JToolBar(null, JToolBar.VERTICAL);
		buttongroup = new ButtonGroup();
		togglebuttons = new JToggleButton[imgpass.length];
		toolbar.setFloatable(false);
		toolbar.setBorderPainted(false);
		toolbar.setPreferredSize(new Dimension(40, 40 * imgpass.length));
		// setMaximumSize(new Dimension(40, 40 * imgpass.length));
		// setMinimumSize(new Dimension(40, 40 * imgpass.length));
		for (int y = 0; y < imgpass.length; y++) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			togglebuttons[y] = new JToggleButton(new ImageIcon(tk.getImage(getClass().getResource(imgpass[y]))));
			togglebuttons[y].setActionCommand(String.valueOf(y));
			togglebuttons[y].addActionListener(new ToolListener());
			togglebuttons[y].setMnemonic(mnemonic[y]);
			togglebuttons[y].setFocusable(false);
			buttongroup.add(togglebuttons[y]);
			toolbar.add(togglebuttons[y]);
			togglebuttons[y].setToolTipText(commandName[y]);
		}
		buttongroup.setSelected(togglebuttons[0].getModel(), true);
		add(toolbar);
	}


	public class ToolListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			changeTool(e.getActionCommand());
		}
	}

	public void changeTool(String actionCommand) {
		if (actionCommand.equals("2")) {
			previousTool = tool;
		}
		tool = Integer.parseInt(actionCommand);
		main.configuration.setProperty("selectedTool", String.valueOf(tool));

	}

}