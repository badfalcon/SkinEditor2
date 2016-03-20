package com.gmail.badfalcon610.SkinEditor;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.math.BigDecimal;

public class CanvasMouseListener extends MouseAdapter {

	CanvasMainPanel c;
	Point mouseCurrent;
	Point mousePrevious;
	Point dotCurrent;
	Point dotPrevious;

	Point shapeStart;
	int shapeWidth;
	int shapeHeight;

	public CanvasMouseListener(CanvasMainPanel canvas) {
		this.c = canvas;

		initMousePoint();
	}

	public void initMousePoint() {
		mousePrevious = new Point(-1, -1);
		mouseCurrent = new Point(-1, -1);
		dotPrevious = new Point(-1, -1);
		dotCurrent = new Point(-1, -1);
	}

	public void newShape() {
		shapeStart = new Point(-1, -1);
		shapeHeight = 0;
		shapeWidth = 0;
	}

	public void mousePressed(MouseEvent e) {
		// todo
		initMousePoint();
		newShape();

		mouseCurrent = getCurrentPoint(e);
		dotCurrent = getCurrentDot();
		mousePrevious = new Point(mouseCurrent);
		dotPrevious = new Point(dotCurrent);

		int btn = e.getButton();

		if (btn == MouseEvent.BUTTON1) {
			shapeStart = new Point(dotCurrent);

			switch (c.cp.main.tp.tool) {
			case 0:
				c.canvas.drawLine(dotPrevious, dotCurrent);
				c.cp.main.pallet.addToHistory();
			case 1:
				break;
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				// start.x = dotCurrent.x;
				// start.y = dotCurrent.y;
				c.update = true;
				break;
			case 10:
				// start.x = dotCurrent.x;
				// start.y = dotCurrent.y;
				// if (insideSelected()) {
				//
				// if (!grabbing) {
				//
				// grab = true;
				// }
				// } else {
				//
				// if (grabbing) {
				// release = true;
				// }
				// }
				c.update = true;
				break;
			default:
				break;
			}
			c.repaint();
			// SkinEditor.preview.retex();
		} else if (btn == MouseEvent.BUTTON2) {
		} else if (btn == MouseEvent.BUTTON3) {

			c.cp.main.tp.togglebuttons[2].doClick();
			c.repaint();
		}

	}

	public void mouseDragged(MouseEvent e) {

		shiftPoint(mousePrevious, mouseCurrent);

		mouseCurrent = getCurrentPoint(e);
		dotCurrent = getCurrentDot();

		if (!dotCurrent.equals(dotPrevious)) {

			switch (c.cp.main.tp.tool) {
			case 0:
				c.canvas.drawLine(dotPrevious, dotCurrent);
				c.cp.main.pallet.addToHistory();
			case 1:
				if (dotPrevious.x != -1 || dotPrevious.y != -1) {
					addLine(dotPrevious, dotCurrent);
				}
				break;
			case 4:
				c.canvas.drawSingleLine(shapeStart, dotCurrent);
				break;
			case 10:
				// if (start.x != -1) {
				// if (grabbing) {
				// getShapeBounds(false);
				// // inside
				// // select.setLocation(dotCurrent);
				// if (moved) {
				// moveselect();
				// }
				// } else {
				// getShapeBounds(true);
				// // outside
				// if (shapeWidth > 0 || shapeHeight > 0) {
				// select = new Rectangle(shapeStart.x, shapeStart.y, shapeWidth
				// +
				// 1, shapeHeight + 1);
				// SkinEditor.editItemList[2].setEnabled(true);
				// SkinEditor.editItemList[3].setEnabled(true);
				// selected = true;
				// } else {
				// SkinEditor.editItemList[2].setEnabled(false);
				// SkinEditor.editItemList[3].setEnabled(false);
				// selected = false;
				// }
				// }
				// }
				break;
			default:
				break;
			}

			shiftPoint(dotPrevious, dotCurrent);

			c.repaint();
			// c.canvas.main.preview.retex();
		}

	}

	public void mouseReleased(MouseEvent e) {
		shiftPoint(mousePrevious, mouseCurrent);

		mouseCurrent = getCurrentPoint(e);
		dotCurrent = getCurrentDot();

		if (!dotCurrent.equals(dotPrevious)) {
			shiftPoint(dotPrevious, dotCurrent);

			dotCurrent = getCurrentDot();
			switch (c.cp.main.tp.tool) {
			case 0:
				c.cp.main.pallet.addToHistory();
				c.canvas.drawLine(dotPrevious, dotCurrent);
				break;
			case 1:
				break;
			case 2:
				// int color = c.layer1.getRGB(dotCurrent.x, dotCurrent.y);
				// if (color != 0) {
				// c.canvas.main.pallet.setMainColor(new Color(color, true));
				// }
				// if (c.canvas.main.tp.previousTool != -1) {
				// c.canvas.main.tp.togglebuttons[c.canvas.main.tp.previousTool].doClick();
				// } else {
				// c.canvas.main.tp.togglebuttons[0].doClick();
				// }
				break;
			case 3:
				// Color Current = new Color(change.getRGB(dotCurrent.x,
				// dotCurrent.y),
				// true);
				// if (!Current.equals(primaryColor)) {
				// c.canvas.main.pallet.addToHistory();
				// }
				break;
			case 4:
				// draw line
				c.canvas.drawSingleLine(shapeStart, dotCurrent);
				break;
			case 5:
				// draw rectangle
			case 6:
				// draw filled rectangle
			case 7:
				// draw ellipse
			case 8:
				// draw filled ellipse
				// SkinEditor.colorchooser.addToHistory(primaryColor);
			case 9:
				// erase filled rectangle
				// resetStart();
				// update = false;
				break;
			case 10:
				// rectangle selection
				// if (start.x != -1) {
				// if (grabbing) {
				// getShapeBounds(false);
				// // inside
				// } else {
				// getShapeBounds(true);
				// // outside
				// if (shapeWidth > 1 || shapeHeight > 1) {
				// select = new Rectangle(shapeStart.x, shapeStart.y, shapeWidth
				// +
				// 1,
				// shapeHeight + 1);
				// SkinEditor.editItemList[2].setEnabled(true);
				// SkinEditor.editItemList[3].setEnabled(true);
				// selected = true;
				// } else {
				// SkinEditor.editItemList[2].setEnabled(false);
				// SkinEditor.editItemList[3].setEnabled(false);
				// selected = false;
				// }
				// }
				// }
				// resetStart();
				// update = false;
				break;
			default:
				break;
			}
			//
			// if (!grabbing) {
			// // changes
			// updateUndo(layer1);
			// }
			//

			// SkinEditor.preview.retex();
		}
		c.canvas.apply();
		c.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		shiftPoint(mousePrevious, mouseCurrent);

		mouseCurrent = getCurrentPoint(e);
		dotCurrent = getCurrentDot();

		if (dotCurrent != dotPrevious) {

			shiftPoint(dotPrevious, dotCurrent);

			c.repaint();

			// SkinEditor.preview.retex();
		}
	}

	public void mouseEntered(MouseEvent e) {
		// todo
	}

	public void mouseExited(MouseEvent e) {

		// initMousePoint();
		shiftPoint(mousePrevious, mouseCurrent);

		mouseCurrent = getCurrentPoint(e);
		dotCurrent = getCurrentDot();

		c.repaint();
		// todo
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		// todo
	}

	public void shiftPoint(Point to, Point from) {
		to.x = from.x;
		to.y = from.y;
	}

	public void addLine(Point p1, Point p2) {
		// if ((p2.x - p1.x) == 0 || Math.abs((p2.getY() - p1.getY()) /
		// (p2.getX() - p1.getX())) >= 1) {
		// if (p1.y > p2.y) {
		// Point temp = new Point(p1);
		// p1 = new Point(p2);
		// p2 = new Point(temp);
		// }
		// for (int y = p1.y; y <= p2.y; y++) {
		// int x = (p2.x - p1.x) * (y - p1.y) / (p2.y - p1.y) + p1.x;
		// addPoint(new Point(x, y));
		// }
		// } else {
		// if (p1.x > p2.x) {
		// Point temp = new Point(p1);
		// p1 = new Point(p2);
		// p2 = new Point(temp);
		// }
		// for (int x = p1.x; x <= p2.x; x++) {
		// int y = (p2.y - p1.y) * (x - p1.x) / (p2.x - p1.x) + p1.y;
		// addPoint(new Point(x, y));
		// }
		// }
	}

	public Point getCurrentPoint(MouseEvent e) {
		return new Point(e.getX(), e.getY());
	}

	public Point getCurrentDot() {
		return toDot(mouseCurrent);
	}

	public Point toDot(Point p) {
		int x = round(p.x / (double) c.scale);
		int y = round(p.y / (double) c.scale);
		return new Point(x, y);
	}

	public int round(double d) {
		BigDecimal bd = new BigDecimal(d);
		BigDecimal bd1 = bd.setScale(0, BigDecimal.ROUND_FLOOR);
		return bd1.intValue();
	}

	public boolean insideSelected() {
		// todo
		return false;
	}

	public boolean updateMousePlace(MouseEvent e) {
		mouseCurrent = new Point(e.getX(), e.getY());
		if ((int) (mouseCurrent.x / c.scale) != dotCurrent.x || (int) (mouseCurrent.y / c.scale) != dotCurrent.y) {
			return true;
		} else {
			return false;
		}
	}
}