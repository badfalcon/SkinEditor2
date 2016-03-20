package com.gmail.badfalcon610.SkinViewer;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

public class PreviewMouseListener extends MouseAdapter {

	PreviewPanel pp;

	public PreviewMouseListener(PreviewPanel pp) {
		this.pp = pp;
	}

	public void mousePressed(MouseEvent e) {
		int btn = e.getButton();

		if (btn == MouseEvent.BUTTON1) {
			pp.prevMouseX = e.getX();
			pp.prevMouseY = e.getY();
		} else if (btn == MouseEvent.BUTTON2) {
			pp.preview.zoom = 0.0f;
			pp.preview.angleX = 0.0f;
			pp.preview.angleY = 0.0f;
		} else if (btn == MouseEvent.BUTTON3) {
		}
	}

	public void mouseClicked(MouseEvent e) {
		int btn = e.getButton();

		if (btn == MouseEvent.BUTTON1) {
			JCheckBox box = pp.main.aPanel.walkCheckBox;
			box.setSelected(!box.isSelected());
		} else if (btn == MouseEvent.BUTTON2) {
			pp.preview.zoom = 0.0f;
			pp.preview.angleX = 0.0f;
			pp.preview.angleY = 0.0f;
		} else if (btn == MouseEvent.BUTTON3) {
		}

	}

	public void mouseReleased(MouseEvent e) {
		int btn = e.getButton();

		if (btn == MouseEvent.BUTTON1) {
		} else if (btn == MouseEvent.BUTTON2) {
		} else if (btn == MouseEvent.BUTTON3) {
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			int x = e.getX();
			int y = e.getY();
			Dimension size = e.getComponent().getSize();

			float thetaY = 360.0f * ((float) (x - pp.prevMouseX) / size.width);
			float thetaX = 360.0f * ((float) (pp.prevMouseY - y) / size.height);

			pp.preview.rotate(thetaX, thetaY);

			pp.prevMouseX = x;
			pp.prevMouseY = y;
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
		} else if (SwingUtilities.isRightMouseButton(e)) {
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		pp.preview.zoom += (e.getWheelRotation() * -3);
	}
}
