package shapes;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import effects.EffectsManger;
import main.Main;

public class Picture extends Shape{
	
	BufferedImage img;
	public BufferedImage lastDrawn = null;
	//Picture size
	int width;
	int height;
	//Effects
	EffectsManger effectsManger = new EffectsManger(this);
	public Picture(int x, int y, boolean visible, String name, BufferedImage img, int width, int height) {
		super(x, y, visible, name);
		this.img = img;
		this.width = width;
		this.height = height;
	}
	@Override
	public void draw(Graphics g) {
		if (lastDrawn == null) {
			lastDrawn = getImageToDisplay();
		}
		g.drawImage(lastDrawn, x, y, getWidthOnBoard(), getHeightOnBoard(), null);
	}
	public BufferedImage getImageToDisplay() {
		BufferedImage displayImage = getScaledImage(img, getWidthOnBoard(), getHeightOnBoard());
    	effectsManger.getImage(displayImage);
    	return displayImage;
	}
	@Override
	public int getWidthOnBoard() {
		return (img.getWidth() * width)/100;
	}
	@Override
	public int getHeightOnBoard() {
		return (img.getHeight() * height)/100;
	}
	@Override
	public void edit() {
		JDialog editDialog = new JDialog(Main.f);
		editDialog.setLayout(new GridLayout(4, 1));
		editDialog.setTitle("Edit Picture");
		JPanel positionPanel = new JPanel(new GridLayout(1, 4));
		positionPanel.add(new JLabel("X:"));
		JTextField xField = new JTextField(this.x + "");
		positionPanel.add(xField);
		positionPanel.add(new JLabel("Y:"));
		JTextField yField = new JTextField(this.y + "");
		positionPanel.add(yField);
		editDialog.add(positionPanel);
		JPanel sizePanel = new JPanel(new GridLayout(1, 4));
		sizePanel.add(new JLabel("width:"));
		JTextField widthField = new JTextField(this.width + "");
		sizePanel.add(widthField);
		sizePanel.add(new JLabel("height:"));
		JTextField heightField = new JTextField(this.height + "");
		sizePanel.add(heightField);
		editDialog.add(sizePanel);
		JPanel sourcePanel = new JPanel(new BorderLayout());
		sourcePanel.add(new JLabel("Source:"), BorderLayout.WEST);
		JTextField sourceField = new JTextField("don\'t change");
		sourceField.setEditable(false);
		sourcePanel.add(sourceField);
		JButton browse = new JButton("Browse");
		browse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = sourceField.getText().equals("don\'t change")?
						new JFileChooser():new JFileChooser(new File(sourceField.getText()));
				fc.showOpenDialog(editDialog);
				File f = fc.getSelectedFile();
				sourceField.setText(f.getAbsolutePath());
			}
		});
		sourcePanel.add(browse, BorderLayout.EAST);
		editDialog.add(sourcePanel);
		JButton done = new JButton("done");
		final Picture cur = this;
		done.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lastDrawn = null;
					int x = Integer.parseInt(xField.getText());
					int y = Integer.parseInt(yField.getText());
					int width = Integer.parseInt(widthField.getText());
					int height = Integer.parseInt(heightField.getText());
					if (!sourceField.getText().equals("don\'t change")) {
						File f = new File(sourceField.getText());
						try {
							cur.img = readImage(f);
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(editDialog, "Invalid File Destination",
									"ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
					cur.x = x;
					cur.y = y;
					cur.width = width;
					cur.height = height;
					Main.shapeList.updateImage(cur);
					editDialog.dispose();
					Main.board.repaint();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(Main.f, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		editDialog.add(done);
		editDialog.pack();
		editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editDialog.setVisible(true);
	}
	public void editEffects() {
		effectsManger.edit();
		lastDrawn = null;
	}
	public static BufferedImage getScaledImage(Image srcImg, int width, int height){
	    BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, width, height, null);
	    g2.dispose();
	    return resizedImg;
	}
	public static BufferedImage readImage(File source) {
		try {
			return ImageIO.read(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public BufferedImage getImg() {
		return img;
	}
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	public BufferedImage getLastDrawn() {
		return lastDrawn;
	}
	public void setLastDrawn(BufferedImage lastDrawn) {
		this.lastDrawn = lastDrawn;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public EffectsManger getEffectsManger() {
		return effectsManger;
	}
	public void setEffectsManger(EffectsManger effectsManger) {
		this.effectsManger = effectsManger;
	}
}