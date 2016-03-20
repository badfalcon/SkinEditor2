package com.gmail.badfalcon610.SkinEditor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.TransferHandler;

public class MyTransferHandler extends TransferHandler {

	EditorMain main;

	public MyTransferHandler(EditorMain main) {
		this.main = main;
	}

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

			boolean openflag = main.fm.saveBeforeQuit();
			if (openflag) {
				main.fm.open(files.get(0));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		}
		return true;
	}
}