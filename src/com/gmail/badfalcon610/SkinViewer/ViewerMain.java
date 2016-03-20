package com.gmail.badfalcon610.SkinViewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ViewerMain extends JFrame {

	public class MyTransferHandler extends TransferHandler {

		@Override
		public boolean canImport(TransferSupport support) {
			if (!support.isDrop()) {
				// ドロップ操作でない場合は受け取らない
				return false;
			}

			if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				// ドロップされたのがファイルでない場合は受け取らない
				return false;
			}

			return true;
		}

		/**
		 * ドロップされたファイルを受け取る
		 */
		@Override
		public boolean importData(TransferSupport support) {
			// 受け取っていいものか確認する
			if (!canImport(support)) {
				return false;
			}

			// ドロップ処理
			Transferable t = support.getTransferable();
			try {
				// ファイルを受け取る
				@SuppressWarnings("unchecked")
				List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

				pPanel.open(files.get(0));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	public static void main(String[] args) {
		ViewerMain main = new ViewerMain();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	PreviewPanel pPanel;
	AdjustPanel aPanel;
	BufferedImage loadedImage = null;

	public ViewerMain() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			//
		} finally {
			SwingUtilities.updateComponentTreeUI(this);
		}

		try {
			loadedImage = ImageIO.read(getClass().getResource("img/new.png"));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		setTransferHandler(new MyTransferHandler());

		// menu = new MyMenuBar(this);
		// setJMenuBar(menu);

		Container pane = getContentPane();

		pPanel = new PreviewPanel(this);
		pane.add(pPanel, BorderLayout.CENTER);

		aPanel = new AdjustPanel(this);
		pane.add(aPanel, BorderLayout.EAST);

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setTitle("Skin Viewer");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/stevehead2d.png")));
		// setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void open(BufferedImage image) {
		pPanel.open(image);
	}

}
