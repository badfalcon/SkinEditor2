package com.gmail.badfalcon610.SkinEditor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {

	EditorMain main;

	BufferedImage image;

	File currentDirectory;

	File loadedFile;

	String filename;

	public FileManager(EditorMain main) {
		this.main = main;

		image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB_PRE);
		filename = "untitled";
		loadedFile = null;
		currentDirectory = null;
	}

	public void open(File file) {
		BufferedImage temp = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB_PRE);
		try {
			newsource();
			temp = ImageIO.read(file);
		} catch (Exception er) {
			er.printStackTrace();
		}

		int width = temp.getWidth();
		if (width == 64) {
			int height = temp.getHeight();
			if (height == 32 || height == 64) {
				if (height == 32) {
					temp = convert(temp, null);
				}
				image = temp;
				loadedFile = file;
				currentDirectory = new File(loadedFile.getPath());
				filename = loadedFile.getName();
				main.menu.fileItemList[3].setEnabled(true);
				main.canvas.canvasMain.update = true;
				main.canvas.canvasMain.repaint();
				if(main.hoge != null){
					main.hoge.open(image);
				}
			} else {
				JOptionPane.showMessageDialog(main, "unsupported size");
			}
		} else {
			JOptionPane.showMessageDialog(main, "unsupported size");
		}
	}

	public void overwrite(File file) {
		BufferedImage image = main.canvas.canvasMain.getImage();
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (main.canvas.canvasMain.edited) {
			main.canvas.canvasMain.edited = false;
		}
	}

	public void savenew() {
		JFileChooser filechooser = new JFileChooser();

		FileFilter filter = new FileNameExtensionFilter(".png", "png");

		if (loadedFile == null) {
			filechooser.setCurrentDirectory(
					new File(EditorMain.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
			filechooser.setSelectedFile(new File("skin.png"));
		} else {
			filechooser.setCurrentDirectory(loadedFile);
			filechooser.setSelectedFile(loadedFile);
		}

		filechooser.setFileFilter(filter);
		int selected = filechooser.showSaveDialog(main);
		if (selected == JFileChooser.APPROVE_OPTION) {
			File file = filechooser.getSelectedFile();
			if (file.exists()) {
				int select = JOptionPane.showConfirmDialog(main, "The file exists.Overwrite?", "existing file",
						JOptionPane.OK_CANCEL_OPTION);
				if (select == JOptionPane.OK_OPTION) {
					overwrite(file);
				}
			}
			try {
				BufferedImage image = main.canvas.canvasMain.getImage();
				ImageIO.write(image, "png", file);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			loadedFile = file;
			currentDirectory = new File(loadedFile.getPath());
			filename = loadedFile.getName();
			if (main.canvas.canvasMain.edited) {
				main.canvas.canvasMain.edited = false;
			}
		} else if (selected == JFileChooser.CANCEL_OPTION) {
		} else if (selected == JFileChooser.ERROR_OPTION) {
		}
	}

	public void newsource() {
		image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB_PRE);
		filename = "untitled";
		main.menu.fileItemList[3].setEnabled(true);
		main.canvas.canvasMain.init();
		main.canvas.canvasMain.edited = false;
	}

	public boolean saveBeforeQuit() {
		if (main.canvas.canvasMain.edited) {
			int save = JOptionPane.showConfirmDialog(main, "You have unsaved changes\n\nSave before closing?",
					"unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION);
			if (save == JOptionPane.YES_OPTION) {
				if (loadedFile != null) {
					overwrite(loadedFile);
					main.saveConfig();
					return true;
				} else {
					savenew();
					if (!main.canvas.canvasMain.edited) {
						main.saveConfig();
						return true;
					}
					return false;
				}
			} else if (save == JOptionPane.NO_OPTION) {
				main.saveConfig();
				return true;
			} else {
				return false;
			}
		} else {
			main.saveConfig();
			return true;
		}

	}

	public static BufferedImage convert(BufferedImage source, ImageObserver io) {
		BufferedImage tempImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D gtemp = tempImage.createGraphics();
		gtemp.drawImage(source, 0, 0, io);

		reverse(source, gtemp, 0, 16, 16, 48, io);
		reverse(source, gtemp, 40, 16, 32, 48, io);

		gtemp.dispose();
		source = tempImage;
		return source;
	}

	public static void reverse(BufferedImage source, Graphics2D gtemp, int x, int y, int dx, int dy, ImageObserver io) {
		gtemp.drawImage(source, dx + 12, dy + 4, dx + 8, dy + 16, x + 0, y + 4, x + 4, y + 16, io);
		gtemp.drawImage(source, dx + 8, dy + 0, dx + 4, dy + 16, x + 4, y + 0, x + 8, y + 16, io);
		gtemp.drawImage(source, dx + 4, dy + 4, dx + 0, dy + 16, x + 8, y + 4, x + 12, y + 16, io);
		gtemp.drawImage(source, dx + 12, dy + 0, dx + 8, dy + 4, x + 8, y + 0, x + 12, y + 4, io);
		gtemp.drawImage(source, dx + 16, dy + 4, dx + 12, dy + 16, x + 12, y + 4, x + 16, y + 16, io);
	}

	public void openAction() {
		boolean openflag = saveBeforeQuit();
		if (openflag) {
			JFileChooser rc = new JFileChooser();
			rc.setAccessory(new ImagePreview(rc));

			FileFilter rf = new FileNameExtensionFilter(".png", "png");

			rc.setCurrentDirectory(currentDirectory);
			rc.setFileFilter(rf);

			int rs = rc.showOpenDialog(main);

			if (rs == JFileChooser.APPROVE_OPTION) {

				File rfile = rc.getSelectedFile();

				open(rfile);
			}
		}
	}

	public void overWriteAction() {
		if (loadedFile != null) {
			overwrite(loadedFile);
		} else {
			savenew();
		}
	}

	public void newAction() {
		String[] options = { "continue", "cancel" };
		int conf = JOptionPane.showOptionDialog(main, "All unsaved changes will be lost", "Create new skin",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (conf == JOptionPane.OK_OPTION) {
			loadedFile = null;
			// newsource();
			// main.menu.editItemList[0].setEnabled(main.canvas.undoManager.canUndo());
			// main.menu.editItemList[1].setEnabled(main.canvas.undoManager.canRedo());
			// updateTitle();
		}
	}
}
