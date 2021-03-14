package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.LinkedList;

public class ListLayout implements LayoutManager2{

	public LinkedList<Component> components = new LinkedList<Component>();
	public int hgap;
	public int vgap;
	public ListLayout() {
		this(0, 0);
	}
	public ListLayout(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}
	@Override
	public void addLayoutComponent(String name, Component comp) {
		components.add(comp);
	}
	
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		components.add(comp);
	}

	@Override
	public void layoutContainer(Container parent) {
		int totalHeight = 0;
		for (int i = 0; i < components.size(); i++) {
			components.get(i).setBounds(0, totalHeight, parent.getWidth(), 30);
			totalHeight += 30 + vgap;
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(150, parent.getHeight());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		// TODO Auto-generated method stub
		return null;
	}
}