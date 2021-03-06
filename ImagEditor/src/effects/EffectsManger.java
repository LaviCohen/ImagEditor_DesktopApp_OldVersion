package effects;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import languages.Translator;
import main.Main;
import shapes.Picture;

public class EffectsManger extends Effect{
	public Picture parent;
	public HashMap<Effect, Boolean> effects = new HashMap<Effect, Boolean>();
	public EffectsManger(Picture picture) {
		this.parent = picture;
	}
	public BufferedImage getImage(BufferedImage bufferedImage) {
		for(Effect effect:effects.keySet()) {
			if (effect instanceof GreenScreenEffect && effects.get(effect)) {
				bufferedImage = effect.getImage(bufferedImage);
			}
		}
		for(Effect effect:effects.keySet()) {
			if (!(effect instanceof GreenScreenEffect) && effects.get(effect)) {
				bufferedImage = effect.getImage(bufferedImage);
			}
		}
		return bufferedImage;
	}
	@Override
	public void edit() {
		JDialog effectManagerDialog = new JDialog(Main.f);
		effectManagerDialog.setTitle("Effects Manager");
		effectManagerDialog.setLayout(new BorderLayout());
		GridLayout effectsLayout = new GridLayout(effects.size(), 1);
		JPanel allEffects = new JPanel(effectsLayout);
		for (Effect effect : effects.keySet()) {
			allEffects.add(getPanelForEffect(effect, effectManagerDialog));
		}
		effectManagerDialog.add(allEffects);
		JButton addEffect = new JButton("Add Effect");
		addEffect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String ans = JOptionPane.showInputDialog(null, "Choose Effect:", "Add Effect", JOptionPane.PLAIN_MESSAGE, null,
						new String[] {"Green Screen", "Blur", "Black & White"}, null).toString();
				Effect effect = null;
				if(ans.equals("Green Screen")) {
					effect = new GreenScreenEffect();
				}else if(ans.equals("Blur")) {
					effect = new BlurEffect();
				}else if(ans.equals("Black & White")) {
					effect = new BlackAndWhiteEffect();
				}
				if (effect == null) {
					return;
				}
				effects.put(effect, true);
				effectsLayout.setRows(effectsLayout.getRows() + 1);
				allEffects.add(getPanelForEffect(effect, effectManagerDialog));
				effectManagerDialog.pack();
				effect.edit();
				parent.lastDrawn = null;
			}
		});
		effectManagerDialog.add(addEffect, BorderLayout.SOUTH);
		effectManagerDialog.pack();
		effectManagerDialog.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				parent.lastDrawn = null;
				Main.board.repaint();
			}
		});
		effectManagerDialog.setVisible(true);
	}
	public JPanel getPanelForEffect(Effect effect, JDialog dialog) {
		JPanel panel = new JPanel(new BorderLayout());
		JCheckBox active = new JCheckBox();
		active.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				effects.put(effect, active.isSelected());
			}
		});
		active.setSelected(effects.get(effect));
		panel.add(active, Translator.getBeforeTextBorder());
		panel.add(new JLabel(effect.getClass().getSimpleName()));
		JPanel actionsPanel = new JPanel(new GridLayout(1, 2));
		JButton edit = new JButton("Edit Effect");
		edit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				effect.edit();
			}
		});
		actionsPanel.add(edit);
		JButton remove = new JButton("Remove Effect");
		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				effects.remove(effect);
				dialog.dispose();
				parent.lastDrawn = null;
				Main.board.repaint();
			}
		});
		actionsPanel.add(remove);
		panel.add(actionsPanel, Translator.getAfterTextBorder());
		return panel;
	}
}