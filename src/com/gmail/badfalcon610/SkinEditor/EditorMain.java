package com.gmail.badfalcon610.SkinEditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.gmail.badfalcon610.SkinViewer.ViewerMain;

public class EditorMain extends JFrame {
	public static void main(String[] args) {
		new EditorMain();

	}

	MyTransferHandler th;

	MyMenuBar menu;
	CanvasPanel canvas;
	ToolPanel tp;
	ColorSelectPanel pallet;

	FileManager fm;

	ResourceBundle resources;
	Properties configuration;

	Class<ViewerMain> c = com.gmail.badfalcon610.SkinViewer.ViewerMain.class;
	public ViewerMain hoge = null;

	public EditorMain() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			//
		} finally {
			SwingUtilities.updateComponentTreeUI(this);
		}

		th = new MyTransferHandler(this);
		setTransferHandler(th);

		menu = new MyMenuBar(this);
		setJMenuBar(menu);

		Container pane = getContentPane();

		fm = new FileManager(this);

		tp = new ToolPanel(this);
		pane.add(tp, BorderLayout.WEST);

		canvas = new CanvasPanel(this);
		pane.add(canvas, BorderLayout.CENTER);

		pallet = new ColorSelectPanel(this);
		pane.add(pallet, BorderLayout.EAST);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Skin Editor");
		// setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		try {
			InputStream inputStream = new FileInputStream(new File("SkinEditor.properties"));
			configuration.load(inputStream);
		} catch (FileNotFoundException e) {
			configuration = new Properties();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			loadConfig();
		}
	}

	void loadConfig() {
		// TODO 自動生成されたメソッド・スタブ

	}

	void saveConfig() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
