package effects;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import components.LSlider;
import languages.Translator;
import main.Main;

public class GreenScreenEffect extends Effect{
	int colorRed = 0;
	int colorGreen = 200;
	int colorBlue = 0;
	
	int accuracy = 50;
	@Override
	public BufferedImage getImage(BufferedImage bf) {
		int width = bf.getWidth();
		int height = bf.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				bf.setRGB(i, j, getColor(bf.getRGB(i, j)));
			}
		}
		return bf;
	}
	public int getColor(int rgb) {
		Color c = new Color(rgb);
		if (compareColors(c, colorRed, colorGreen, colorBlue) < accuracy) {
			return 0;
		}
		return rgb;
	}
	public static int compareColors(Color c1, int r, int g, int b) {
		int redDiff = Math.abs(c1.getRed() - r);
		int greenDiff = Math.abs(c1.getGreen() - g);
		int blueDiff = Math.abs(c1.getBlue() - b);
		return (redDiff + greenDiff + blueDiff)/3;
	}
	@Override
	public void edit() {
		Color greenScreenColor = new Color(colorRed, colorGreen, colorBlue);
		JDialog editGreenScreenDialog = new JDialog(Main.f);
		editGreenScreenDialog.setTitle("Green Screen");
		editGreenScreenDialog.setLayout(new GridLayout(3, 1));
		JPanel colorPanel = new JPanel(new BorderLayout());
		colorPanel.add(new JLabel("color:"), BorderLayout.WEST);
		JLabel colorLabel = new JLabel();
		colorLabel.setOpaque(true);
		colorLabel.setBackground(greenScreenColor);
		colorPanel.add(colorLabel);
		colorPanel.add(colorLabel);
		JPanel setColorPanel = new JPanel(new GridLayout(1, 2));
		JButton chooseColorButton = new JButton("choose color");
		chooseColorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				colorLabel.setBackground(JColorChooser.showDialog(
						editGreenScreenDialog, "Choose Green Screen color", greenScreenColor));
			}
		});
		setColorPanel.add(chooseColorButton);
		JButton pickColorButton = new JButton("pick Color");
		pickColorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(editGreenScreenDialog,
						"Put the mouse on the color and press space");
				try {
					Robot r = new Robot();
					colorLabel.setBackground(r.getPixelColor(
							MouseInfo.getPointerInfo().getLocation().x, 
							MouseInfo.getPointerInfo().getLocation().y));
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		setColorPanel.add(pickColorButton);
		colorPanel.add(setColorPanel, Translator.getAfterTextBorder());
		editGreenScreenDialog.add(colorPanel);
		LSlider accuracySlider = new LSlider("Accuracy:", 0, 255, accuracy);
		editGreenScreenDialog.add(accuracySlider);
		JButton done = new JButton("done");
		done.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				colorRed = colorLabel.getBackground().getRed();
				colorGreen = colorLabel.getBackground().getGreen();
				colorBlue = colorLabel.getBackground().getBlue();
				accuracy = accuracySlider.getValue();
				editGreenScreenDialog.dispose();
				Main.board.repaint();
			}
		});
		editGreenScreenDialog.add(done);
		editGreenScreenDialog.pack();
		editGreenScreenDialog.setSize(editGreenScreenDialog.getWidth() + 100, editGreenScreenDialog.getHeight());
		editGreenScreenDialog.setVisible(true);
	}
}