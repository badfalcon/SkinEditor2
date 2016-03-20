package com.gmail.badfalcon610.SkinEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class Canvas {

	CanvasMainPanel parent;

	UndoManager undoManager;

	BufferedImage saved;
	BufferedImage latest;

	Color c;

	public Canvas(CanvasMainPanel parent, Dimension dimension) {
		this.parent = parent;
		init(dimension);
		c = Color.BLACK;
	}

	private void test() {
		Graphics2D g = drawStart();
		g.setColor(Color.black);
		g.drawRect(0, 0, 16, 16);
		drawEnd(g);
		apply();
		sendUpdate();
	}

	public void init(Dimension dimension) {
		load(new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB_PRE));
	}

	public void load(BufferedImage image) {
		saved = image;
		latest = image;
		undoManager = new UndoManager();
	}

	public Graphics2D drawStart() {
		Graphics2D canvas = latest.createGraphics();
		return canvas;
	}

	public void drawEnd(Graphics2D g2) {
		g2.dispose();
	}

	public void apply() {
		getDiff();
		saved = deepCopy(latest);
		// undoManager.undoableEditHappened(new UndoableEditEvent(this, new
		// SetValueUndo(change.b, change.a)));
		// SkinEditor.editItemList[0].setEnabled(undoManager.canUndo());
		// SkinEditor.editItemList[1].setEnabled(undoManager.canRedo());

	}

	public void getDiff() {
		ArrayList<Unit> changes = new ArrayList<Unit>();
		ArrayList<Unit> Unitsb = new ArrayList<Unit>();
		ArrayList<Unit> Unitsa = new ArrayList<Unit>();
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				int rgbold = saved.getRGB(j, i);
				int rgbnew = latest.getRGB(j, i);
				if (rgbold != rgbnew) {
					System.out.println("old" + rgbold + ":new" + rgbnew);
					// Unitsb.add(new Unit(j, i, rgbold));
					// Unitsa.add(new Unit(j, i, rgbnew));
				}
			}
		}
		// return new Changes(Unitsb, Unitsa);
	}

	public void sendUpdate() {
		parent.repaint();
	}

	public void reset() {
		latest = deepCopy(saved);
	}

	public void drawSingleLine(Point p1, Point p2) {
		reset();
		drawLine(p1, p2);
	}

	public void clearLine(Point p1, Point p2) {
		drawLine(p1, p2, new Color(0f, 0f, 0f, 0f));
	}

	public void drawLine(Point p1, Point p2) {
		drawLine(p1, p2, parent.cp.main.pallet.getMainColor());
	}

	public void drawLine(Point p1, Point p2, Color c) {
		int color = c.getRGB();

		if ((p2.x - p1.x) == 0 || Math.abs((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())) >= 1) {
			if (p2.y - p1.y == 0) {
				latest.setRGB(p1.x, p1.y, color);
			} else {
				if (p1.y > p2.y) {
					Point temp = new Point(p1);
					p1 = new Point(p2);
					p2 = new Point(temp);
				}
				for (int y = p1.y; y <= p2.y; y++) {
					int x = (p2.x - p1.x) * (y - p1.y) / (p2.y - p1.y) + p1.x;
					if (isInside(x, y)) {
						latest.setRGB(x, y, color);
					}
				}
			}
		} else {
			if (p1.x > p2.x) {
				Point temp = new Point(p1);
				p1 = new Point(p2);
				p2 = new Point(temp);
			}
			for (int x = p1.x; x <= p2.x; x++) {
				int y = (p2.y - p1.y) * (x - p1.x) / (p2.x - p1.x) + p1.y;
				if (isInside(x, y)) {
					latest.setRGB(x, y, color);
				}
			}
		}

		// drawEnd(canvas);
		sendUpdate();
	}

	public boolean isInside(int x, int y) {
		Rectangle rect = new Rectangle(new Dimension(64, 64));
		if (rect.contains(x, y)) {
			return true;
		} else {
			return false;
		}
	}

	class SetValueUndo extends AbstractUndoableEdit {
		List<Unit> oldValues;
		List<Unit> newValues;

		int num;

		/** コンストラクター */
		protected SetValueUndo(List<Unit> before, List<Unit> after) {
			oldValues = before;
			newValues = after;
		}

		@Override
		public void undo() throws CannotUndoException {
			super.undo();
			for (int i = 0; i < oldValues.size(); i++) {
				saved.setRGB(oldValues.get(i).x, oldValues.get(i).y, oldValues.get(i).rgba);
			}
			latest = deepCopy(saved);
		}

		@Override
		public void redo() throws CannotRedoException {
			super.redo();
			for (int i = 0; i < newValues.size(); i++) {
				saved.setRGB(newValues.get(i).x, newValues.get(i).y, newValues.get(i).rgba);
			}
			latest = deepCopy(saved);
		}
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
