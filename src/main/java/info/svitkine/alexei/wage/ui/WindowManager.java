package info.svitkine.alexei.wage;

import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import lombok.Getter;


public class WindowManager extends JPanel implements ComponentListener {
	@Getter private JComponent modalDialog;
	private MenuBarRenderer menubar;

	public WindowManager() {
		setLayout(null);
		addComponentListener(this);
	}

	public void add(JComponent c) {
		c.setBorder(new WindowBorder());
		// TODO: use chain of events pattern...
		MouseInputListener listener = new CloseBoxListener();
		c.addMouseListener(listener);
		c.addMouseMotionListener(listener);
		listener = new WindowDragListener();
		c.addMouseListener(listener);
		c.addMouseMotionListener(listener);
		super.add(c);
		setComponentZOrder(c, lowestZOrderForWindow());
	}
	
	public void remove(JComponent comp) {
		super.remove(comp);
		if (comp == modalDialog) {
			for (int i = 0; i < getComponentCount(); i++)
				getComponent(i).setEnabled(true);
			modalDialog = null;
		}
	}

	public void addModalDialog(JComponent dialog) {
		if (modalDialog != null)
			throw new IllegalArgumentException();
		for (int i = 0; i < getComponentCount(); i++)
			getComponent(i).setEnabled(false);
		super.add(dialog);
		setComponentZOrder(dialog, 0);
		modalDialog = dialog;
	}
	
	private static void repaintShape(JComponent c, Shape s) {
		Rectangle b = s.getBounds();
		c.repaint(b.x, b.y, b.width, b.height);
	}
	
	public void setMenuBar(MenuBar menubar) {
		if (this.menubar != null)
			remove(this.menubar);
		if (menubar == null) {
			this.menubar = null;
			return;
		}
		this.menubar = new MenuBarRenderer(menubar);
		super.add(this.menubar);
		this.menubar.setBounds(getBounds());
		setComponentZOrder(this.menubar, lowestZOrderForWindow() - 1);
	}

	private int lowestZOrderForWindow() {
		int z = 0;
		if (menubar != null)
			z++;
		if (modalDialog != null)
			z++;
		return z;
	}
	
	private class WindowDragListener extends MouseInputAdapter {
		private Point startPos = null; 
		private Timer scrollTimer;

		@Override
		public void mousePressed(MouseEvent event) {
			JComponent c = (JComponent) event.getComponent();
			if (!c.isEnabled() || !(c.getBorder() instanceof WindowBorder))
				return;
			setComponentZOrder(c, lowestZOrderForWindow());
			WindowBorder border = (WindowBorder) c.getBorder();
			Shape[] borderShapes = border.getBorderShapes(c);
			Shape shape = borderShapes[WindowBorder.BORDER_SHAPE];
			Point p = event.getPoint();
			if (shape != null && shape.contains(p)) {
				Shape closeBoxShape = borderShapes[WindowBorder.CLOSE_BOX];
				if (closeBoxShape == null || !closeBoxShape.contains(p)) {
					startPos = event.getPoint();
				}
				if (border.isScrollable()) {
					if (borderShapes[WindowBorder.SCROLL_UP].contains(p)) {
						scroll(c, -1);
					} else if (borderShapes[WindowBorder.SCROLL_DOWN].contains(p)) { 
						scroll(c, 1);
					}
				}
			}
		}

		private void scroll(JComponent c, final int amount) {
			while (!(c instanceof JScrollPane)) {
				c = (JComponent) c.getComponent(0);
			}
			final JScrollBar bar = ((JScrollPane) c).getVerticalScrollBar();
			scrollTimer = new Timer(3, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					bar.setValue(bar.getValue() + amount);
				}
			});
			scrollTimer.start();
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			if (startPos != null) { 
				int dx = event.getX() - startPos.x;
				int dy = event.getY() - startPos.y;
				JComponent c = (JComponent) event.getComponent();
				Rectangle bounds = c.getBounds();
				bounds.translate(dx, dy); 
				c.setBounds(bounds);
				Container p = c.getParent();
				p.repaint();
				p.invalidate();
				((JComponent)p).revalidate();
			}
		}

		@Override
		public void mouseReleased(MouseEvent event) { 
			startPos = null;
			if (scrollTimer != null) {
				scrollTimer.stop();
				scrollTimer = null;
			}
		}
	}

	private static class CloseBoxListener extends MouseInputAdapter {
		private boolean clickedInCloseBox;

		private void updateCloseBox(MouseEvent event) {
			if (clickedInCloseBox) {
				JComponent c = (JComponent) event.getComponent();
				WindowBorder border = (WindowBorder) c.getBorder();
				Shape closeBox = border.getBorderShapes(c)[WindowBorder.CLOSE_BOX];
				boolean wasPressed = border.isCloseBoxPressed();
				border.setCloseBoxPressed(closeBox.contains(event.getPoint()));
				if (wasPressed != border.isCloseBoxPressed())
					repaintShape(c, closeBox);
			}			
		}
		
		@Override
		public void mouseMoved(MouseEvent event) {
			updateCloseBox(event);
		}
		
		@Override
		public void mousePressed(MouseEvent event) {
			JComponent c = (JComponent) event.getComponent();
			if (!c.isEnabled())
				return;
			WindowBorder border = (WindowBorder) c.getBorder();
			Shape closeBox = border.getBorderShapes(c)[WindowBorder.CLOSE_BOX];
			if (closeBox != null && closeBox.contains(event.getPoint())) {
				clickedInCloseBox = true;
				updateCloseBox(event);
			}
		} 

		@Override
		public void mouseDragged(MouseEvent event) {
			updateCloseBox(event);
		} 

		@Override
		public void mouseReleased(MouseEvent event) {
			updateCloseBox(event);
			clickedInCloseBox = false;
			JComponent c = (JComponent) event.getComponent();
			WindowBorder border = (WindowBorder) c.getBorder();
			if (border.isCloseBoxPressed()) {
				Shape closeBox = border.getBorderShapes(c)[WindowBorder.CLOSE_BOX];
				if (closeBox.contains(event.getPoint())) {
					border.setCloseBoxPressed(false);
					Container p = c.getParent();
					p.remove(c);
					repaintShape((JComponent) p, c.getBounds());
				}	
			}
		} 
	}

	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentResized(ComponentEvent arg0) {
		if (menubar != null) {
			menubar.setBounds(getBounds());
		}
	}

	public void componentShown(ComponentEvent arg0) {
	}
}
