package com.gmail.badfalcon610.SkinEditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CanvasMainPanel extends JPanel {

	CanvasPanel cp;
	CanvasMouseListener cml;
	Canvas canvas;

	int scale;

	int width;
	int height;
	Color FGColor;
	Color BGColor;

	boolean update;
	boolean grabbing;
	boolean grab;
	boolean slim;
	boolean edited;
	boolean hideTemplate;

	static Rectangle rect = new Rectangle(64, 64);

	public static void main(String[] args) {
		new TestFrame(new CanvasMainPanel(null));
	}

	public CanvasMainPanel(CanvasPanel cp) {
		this.cp = cp;
		canvas = new Canvas(this, new Dimension(64, 64));
		cml = new CanvasMouseListener(this);
		addMouseListener(cml);
		addMouseMotionListener(cml);
		addMouseWheelListener(cml);

		setPreferredSize(new Dimension(128, 128));
		setMinimumSize(new Dimension(128, 128));

		init();
		resetColor();
	}

	public void setSize() {
		width = scale * 64;
		height = scale * 64;
	}

	public void init() {
		slim = false;
		hideTemplate = false;
		canvas.init(new Dimension(64, 64));
	}

	public void resetColor() {
		BGColor = new Color(128, 128, 128, 255);
		FGColor = new Color(255, 255, 255, 70);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D display = (Graphics2D) g.create();
		display.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D gbuffer = buffer.createGraphics();
		gbuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		Color primaryColor = cp.main.pallet.getMainColor();

		// メインの背景
		gbuffer.setPaint(BGColor);
		gbuffer.fill(new Rectangle(0, 0, getWidth(), getHeight()));

		gbuffer.drawImage(canvas.latest, 0, 0, 64 * scale, 64 * scale, this);

		Cursor c;

		if (grabbing) {
			// マウスを十字矢印に
			c = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
		} else {
			BufferedImage customCursor = new BufferedImage(17, 17, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D gCC = customCursor.createGraphics();
			gCC.setColor(new Color(0, 0, 0, 0));
			gCC.fillRect(0, 0, 16, 16);
			gCC.setColor(new Color(0, 0, 0, 255));
			gCC.fillRect(8, 0, 1, 17);
			gCC.fillRect(0, 8, 17, 1);

			gCC.dispose();
			c = Toolkit.getDefaultToolkit().createCustomCursor(customCursor, new Point(16, 16), "null_cursor");
			// c = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
		}
		setCursor(c);

		/*
		 * gbuffer.drawImage(layer1, 0, 0, 64 * scale, dotheight * scale, this);
		 */
		Point dotCurrent = cml.dotCurrent;
		if (rect.contains(dotCurrent)) {
			// マウス位置に現在色を描画
			if (cp.main.tp.tool != 1 && cp.main.tp.tool != 2 && cp.main.tp.tool != 9 && cp.main.tp.tool != 10) {
				gbuffer.setPaint(primaryColor);
				gbuffer.fill(new Rectangle(dotCurrent.x * scale, dotCurrent.y * scale, scale, scale));
			}
			if (cp.main.tp.tool != 10) {
				// マウス位置の周りに現在色の補色を描画
				Color reverse = new Color(255 - primaryColor.getRed(), 255 - primaryColor.getGreen(),
						255 - primaryColor.getBlue(), primaryColor.getAlpha());
				gbuffer.setPaint(reverse);
				gbuffer.draw(new Rectangle(dotCurrent.x * scale - 1, dotCurrent.y * scale - 1, scale + 1, scale + 1));
			}
		}else{
		}
		//
		// // selected area
		// if (selected) {
		// float[] dash = { 6.0f, 6.0f };
		// BasicStroke dashStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_MITER, 10.0f, dash,
		// 0.0f);
		// gbuffer.setPaint(Color.WHITE);
		// gbuffer.setStroke(dashStroke);
		//
		// gbuffer.draw(new Rectangle(select.x * scale,
		// select.y * scale,
		// select.width * scale, select.height * scale));
		// }
		//
		// overlay template
		if (!hideTemplate) {
			gbuffer.setPaint(FGColor);

			drawTemplate(gbuffer);

		}

		gbuffer.dispose();

		// 画面に描画
		display.drawImage(buffer, 0, 0, this);
		display.dispose();

	}

	public void draw(Graphics2D g, ArrayList<Point> points, Color c) {
		for (Point p : points) {
			g.setPaint(c);
			g.drawLine(p.x, p.y, p.x, p.y);
		}
	}

	public void draw(BufferedImage bi, ArrayList<Point> points, int c) {
		for (Point p : points) {
			bi.setRGB(p.x, p.y, c);
		}
	}

	BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public BufferedImage getImage() {
		return canvas.latest;
	}

	private final int[] headRatio = new int[] { 8, 8, 8 };
	private final int[] bodyRatio = new int[] { 8, 12, 4 };
	private final int[] limbRatio = new int[] { 4, 12, 4 };
	private final int[] slimRatio = new int[] { 3, 12, 4 };

	String[] partsName = { "OUTER", "INNER", "HEAD", "BODY", "ARM(R)", "ARM(L)", "LEG(R)", "LEG(L)" };

	void drawTemplate(Graphics2D g2) {

		int[] arm;
		if (slim) {
			arm = slimRatio;
		} else {
			arm = limbRatio;
		}

		// inner head
		drawExpansion(g2, new Point(0, 0), headRatio, partsName[1], partsName[2]);
		// inner body
		drawExpansion(g2, new Point(16 * scale, 16 * scale), bodyRatio, partsName[1], partsName[3]);
		// inner arm(R)
		drawExpansion(g2, new Point(40 * scale, 16 * scale), arm, partsName[1], partsName[4]);
		// inner arm(L)
		drawExpansion(g2, new Point(32 * scale, 48 * scale), arm, partsName[1], partsName[5]);
		// inner leg(R)
		drawExpansion(g2, new Point(0, 16 * scale), limbRatio, partsName[1], partsName[6]);
		// inner leg(L)
		drawExpansion(g2, new Point(16 * scale, 48 * scale), limbRatio, partsName[1], partsName[7]);

		// outer head
		drawExpansion(g2, new Point(32 * scale, 0), headRatio, partsName[0], partsName[2]);
		// outer body
		drawExpansion(g2, new Point(16 * scale, 32 * scale), bodyRatio, partsName[0], partsName[3]);
		// outer arm(R)
		drawExpansion(g2, new Point(40 * scale, 32 * scale), arm, partsName[0], partsName[4]);
		// outer arm(L)
		drawExpansion(g2, new Point(48 * scale, 48 * scale), arm, partsName[0], partsName[5]);
		// outer leg(R)
		drawExpansion(g2, new Point(0, 32 * scale), limbRatio, partsName[0], partsName[6]);
		// outer leg(L)
		drawExpansion(g2, new Point(0, 48 * scale), limbRatio, partsName[0], partsName[7]);
	}

	void drawExpansion(Graphics2D g2, Point p, int[] size, String inout, String partName) {
		Point p2 = new Point(p.x + 1, p.y + 1);
		int width = size[0] * scale;
		int height = size[1] * scale;
		int depth = size[2] * scale;

		Font font = new Font(g2.getFont().getFamily(), Font.PLAIN, scale * 2 - 1);
		g2.setFont(font);
		drawWord(g2, inout, p2, size[2], size[2] + 2);
		drawWord(g2, partName, p2, size[2], size[2] + 4);

		g2.setStroke(new BasicStroke(2));
		font = new Font(g2.getFont().getFamily(), Font.PLAIN, scale * 2 - 3);
		g2.setFont(font);

		// up
		drawWord(g2, "u", p2, size[2], size[2]);
		g2.draw(new Rectangle(p2.x + depth, p2.y, width - 2, depth - 2));
		// down
		drawWord(g2, "d", p2, size[2] + size[0], size[2]);
		g2.draw(new Rectangle(p2.x + depth + width, p2.y, width - 2, depth - 2));
		// right
		drawWord(g2, "r", p2, 0, size[2] + size[1]);
		g2.draw(new Rectangle(p2.x, p2.y + depth, depth - 2, height - 2));
		// front
		drawWord(g2, "f", p2, size[2], size[2] + size[1]);
		g2.draw(new Rectangle(p2.x + depth, p2.y + depth, width - 2, height - 2));
		// left
		drawWord(g2, "l", p2, size[2] + size[0], size[2] + size[1]);
		g2.draw(new Rectangle(p2.x + depth + width, p2.y + depth, depth - 2, height - 2));
		// back
		drawWord(g2, "b", p2, size[2] * 2 + size[0], size[2] + size[1]);
		g2.draw(new Rectangle(p2.x + depth * 2 + width, p2.y + depth, width - 2, height - 2));

		// reset
		g2.setStroke(new BasicStroke());
	}

	void drawWord(Graphics2D g2, String str, Point p, int xadd, int yadd) {
		g2.drawString(str, (float) (p.x + (xadd + 0.5) * scale),
				(float) (p.y + (yadd - 1.5) * scale + (g2.getFontMetrics().getAscent() / 2)));
	}

}
