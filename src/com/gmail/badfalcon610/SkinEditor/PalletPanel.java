package com.gmail.badfalcon610.SkinEditor;

import java.awt.Color;

import javax.swing.JPanel;

public abstract class PalletPanel extends JPanel {
	Color color;

	public abstract void update(Color c);
}
