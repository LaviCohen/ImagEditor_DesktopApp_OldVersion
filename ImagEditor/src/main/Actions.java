package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import components.LTextArea;
import components.LTextField;
import install.Install;
import languages.Translator;
import log.Logger;
import shapes.Picture;
import shapes.Rectangle;
import shapes.Text;

public class Actions {
	public static void action(String command) {
		if (command.equals("Save")) {
			save();
		}else if (command.equals("Set Paper Size")) {
			Main.board.setPaperSize(
					Integer.parseInt(JOptionPane.showInputDialog("Enter Width:")),
					Integer.parseInt(JOptionPane.showInputDialog("Enter Height:")));
		}else if (command.equals("Set Language")) {
			setLanguage();
		}else if (command.equals("Rectangle")) {
			addRectagle();
		}else if (command.equals("Text")) {
			addText();
		}else if (command.equals("Picture")) {
			addPicture();
		}else if (command.equals("Edit")) {
			edit();
		}else if (command.equals("Refresh")) {
			Main.board.repaint();
			Main.updateShapeList();
		}else if(command.equals("Profile")) {
			Main.myAccount.showAccount();
		}else if (command.equals("Visit Website")) {
			Main.website.openInBrowser();
		}else if (command.equals("Send Report")) {
			sendReport();
		}else if (command.equals("Log")) {
			openLog();
		}
	}
	/**
	 * Method that create two GUI dialogs, one for general log and one for error log.
	 * @see Logger
	 * */
	public static void openLog() {
		//General log
		JDialog generalLog = new JDialog(Main.f);
		generalLog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		generalLog.setTitle(Translator.get("General Log"));
		generalLog.setLayout(new BorderLayout());
		generalLog.add(new JLabel("<html><b>" + Translator.get("General Log") + "</b></html>"), BorderLayout.NORTH);
		String styledText = "<html>" + Logger.getLog().replaceAll("\n", "<br/>") + "</html>";
		generalLog.add(new JScrollPane(new JLabel(styledText)));
		JButton copyGeneralLog = new JButton("Copy");
		copyGeneralLog.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(Logger.getLog()), null);
			}
		});
		generalLog.add(copyGeneralLog, BorderLayout.SOUTH);
		generalLog.pack();
		generalLog.setSize(generalLog.getWidth() > 500 ? 500 : generalLog.getWidth(),
				generalLog.getHeight() > 500 ? 500 : generalLog.getHeight());
		generalLog.setVisible(true);
		//Error log
		JDialog errorLog = new JDialog(Main.f);
		errorLog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		errorLog.setTitle(Translator.get("Error Log"));
		errorLog.setLayout(new BorderLayout());
		errorLog.add(new JLabel("<html><b>" + Translator.get("Error Log") + "</b></html>"), BorderLayout.NORTH);
		styledText = "<html>" + Logger.getErrorLog().replaceAll("\n", "<br/>") + "</html>";
		errorLog.add(new JScrollPane(new JLabel(styledText)));
		JButton copyErrorLog = new JButton("Copy");
		copyErrorLog.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(Logger.getErrorLog()), null);
			}
		});
		errorLog.add(copyErrorLog, BorderLayout.SOUTH);
		errorLog.pack();
		errorLog.setBounds(generalLog.getWidth(), 0,
				errorLog.getWidth() > 500 ? 500 : errorLog.getWidth(), errorLog.getHeight() > 500 ? 500 : errorLog.getHeight());
		errorLog.setVisible(true);
	}
	private static void sendReport() {
		JDialog reportDialog = new JDialog(Main.f);
		reportDialog.setTitle("Report");
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(new JLabel(Translator.get("Title") + ":"), Translator.getBeforeTextBorder());
		LTextField titleField = new LTextField("Your report title here");
		headerPanel.add(titleField);
		reportDialog.add(headerPanel, BorderLayout.NORTH);
		LTextArea contentArea = new LTextArea("Your report content here");
		reportDialog.add(contentArea);
		JButton done = new JButton("done");
		reportDialog.add(done, BorderLayout.SOUTH);
		done.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(reportDialog,
					Main.website.sendReport(Main.myAccount.userName, 
						titleField.getText(), contentArea.getText()));
				reportDialog.dispose();
			}
		});
		reportDialog.pack();
		reportDialog.setVisible(true);
		reportDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	private static void setLanguage() {
		Translator.showChangeLanguageDialog();
		Install.default_setting.put("language", Translator.getLanguageName());
	}
	public static void save() {
		JDialog saveDialog = new JDialog(Main.f);
		saveDialog.setTitle("Save");
		saveDialog.setLayout(new GridLayout(3, 1));
		JPanel dirPanel = new JPanel(new BorderLayout());
		dirPanel.add(new JLabel("Directory:"), Translator.getBeforeTextBorder());
		JTextField dirField = new JTextField();
		dirField.setEditable(false);
		dirPanel.add(dirField);
		saveDialog.add(dirPanel);
		JButton browse = new JButton("Browse");
		browse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = dirField.getText().equals("")?
						new JFileChooser(Install.getPath("Gallery")):new JFileChooser(new File(dirField.getText()));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.showOpenDialog(saveDialog);
				File f = fc.getSelectedFile();
				dirField.setText(f.getAbsolutePath());
			}
		});
		dirPanel.add(browse, Translator.getAfterTextBorder());
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.add(new JLabel("Name"), Translator.getBeforeTextBorder());
		JTextField nameField = new JTextField("picture");
		namePanel.add(nameField);
		JComboBox<String> typeBox = new JComboBox<String>(new String[] {".png", ".jpg"});
		namePanel.add(typeBox, Translator.getAfterTextBorder());
		saveDialog.add(namePanel);
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = new File(
						dirField.getText() + "\\" + nameField.getText() + typeBox.getSelectedItem());
				System.out.println("Exporting image to " + f);
				try {
					BufferedImage bf = new BufferedImage(Main.board.getWidth(), Main.board.getHeight(), 
							typeBox.getSelectedItem().equals(".png")?
									BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB);
					Main.board.paintShapes(bf.getGraphics());
					JDialog d = new JDialog();
					d.add(new JLabel(new ImageIcon(bf)));
					d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					d.pack();
					d.setVisible(true);
					ImageIO.write(bf.getSubimage(0, 0, bf.getWidth(), bf.getHeight()), 
							typeBox.getSelectedItem().toString().substring(1), f);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				saveDialog.dispose();
			}
		});
		saveDialog.add(save);
		saveDialog.pack();
		saveDialog.setVisible(true);
	}
	public static void addRectagle() {
		Rectangle r = new Rectangle(0, 0, true, null, 100, 100, Color.BLUE);
		Main.board.addShape(r);
		r.edit();
	}
	public static void addText() {
		Text t = new Text(
				0, 0, true, null, Color.BLACK, new Font("Arial", Font.PLAIN, 20), "text");
		Main.board.addShape(t);
		t.edit();
	}
	public static void addPicture() {
		Picture p = new Picture(0, 0, true, null,
				new BufferedImage(150, 50, BufferedImage.TYPE_INT_RGB), 100, 100);
		Main.board.addShape(p);
		p.edit();
	}
	public static void edit() {
		if (Main.getShapeList().getSelectedShape() == null) {
			return;
		}
		Main.getShapeList().getSelectedShape().edit();
	}
	public static void remove() {
		if (Main.getShapeList().getSelectedShape() == null) {
			return;
		}
		if (JOptionPane.showConfirmDialog(Main.f, "Are you sure?") == JOptionPane.YES_OPTION) {
			Main.board.getShapesList().remove(Main.getShapeList().getSelectedShape());
			Main.board.repaint();
			Main.updateShapeList();
		}
	}
}