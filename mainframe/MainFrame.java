package mainframe;

import ide.ConflictException;
import ide.Module;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;


public class MainFrame implements Runnable{
	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	private JFrame frame;
	private JMenuBar menubar;
	private JToolBar buttonPanel;
	private JSplitPane leftAndOthers;
	private JSplitPane rightAndCenter;
	private JSplitPane topAndBottom;
	private JTabbedPane left;
	private JTabbedPane right;
	private JTabbedPane top;
	private JTabbedPane bottom;
	private int menuIndex = 0;
	private int buttonIndex = 0;
	
	public MainFrame() {
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		frame = new JFrame("SmallIDE");
		frame.setVisible(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900,600);
		frame.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel(new BorderLayout());
		
		menubar = new JMenuBar();
		northPanel.add(menubar,BorderLayout.NORTH);
		
		/*JMenu m1 = new JMenu("sdfsdf");
		menubar.add(m1);
		m1.add(new JMenuItem("sfssdfsgggggdf"));*/
		
		buttonPanel = new JToolBar("toolbar");
		//buttonPanel.add(new JButton("fuck"));
		northPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		frame.add(northPanel, BorderLayout.NORTH);
		
		leftAndOthers = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		leftAndOthers.setDividerSize(5);
		leftAndOthers.setDividerLocation(0.2);
		leftAndOthers.setResizeWeight(0.2);
		leftAndOthers.setContinuousLayout(true);
		rightAndCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		rightAndCenter.setDividerSize(5);
		rightAndCenter.setDividerLocation(1-0.2/0.8);
		rightAndCenter.setResizeWeight(1-0.2/0.8);
		rightAndCenter.setContinuousLayout(true);
		topAndBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		topAndBottom.setDividerSize(5);
		topAndBottom.setDividerLocation(0.8);
		topAndBottom.setResizeWeight(0.8);
		topAndBottom.setContinuousLayout(true);
		top = new JTabbedPane();
		bottom = new JTabbedPane();
		left = new JTabbedPane();
		right = new JTabbedPane();
		
		topAndBottom.setTopComponent(top);
		topAndBottom.setBottomComponent(bottom);
		rightAndCenter.setLeftComponent(topAndBottom);
		rightAndCenter.setRightComponent(right);
		leftAndOthers.setLeftComponent(left);
		leftAndOthers.setRightComponent(rightAndCenter);
		
		//top.addTab("top1", new JLabel("fuck1"));
		//bottom.addTab("bottom1", new JLabel("fuck2"));
		//right.addTab("right1", new JLabel("fuck3"));
		//left.addTab("left1", new JLabel("fuck4"));
		
		frame.add(leftAndOthers,BorderLayout.CENTER);
		
		frame.setVisible(true);
		System.out.println("done");
	}
	
	public int addMenu(String path, Module m) throws ConflictException{
		//System.out.println("fuck");
		menubar.setVisible(false);
		String[] ss = path.split("/");
		if(ss.length == 1) return -1;
		JMenu jm = null;
		int i, l = menubar.getMenuCount();
		for(i = 0; i < l; i++){
			if(menubar.getMenu(i).getText().equalsIgnoreCase(ss[0])){
				jm = menubar.getMenu(i);
				break;
			}
		}
		if(i == l){
			jm = new JMenu(ss[0]);
			menubar.add(jm);
		}
		for(int j = 1; j < ss.length - 1; j++){
			Component[] c = jm.getMenuComponents();
			l = c.length;
			for(i = 0; i < l; i++){
				if(c[i] instanceof JMenu){
					if(((JMenu)c[i]).getText().equalsIgnoreCase(ss[j])){
						jm = (JMenu)c[i];
						break;
					}
				}else if(c[i] instanceof MenuItem){
					if(((MenuItem)c[i]).getText().equalsIgnoreCase(ss[j])){
						menubar.setVisible(true);
						throw new ConflictException(((MenuItem)c[i]).getModule(), m);
					}
				}
			}
			if(i == l){
				JMenu njm = new JMenu(ss[j]);
				jm.add(njm);
				jm = njm;
			}
		}
		Component[] c = jm.getMenuComponents();
		l = c.length;
		for(i = 0; i < l; i++){
			if(c[i] instanceof JMenu){
				if(((JMenu)c[i]).getText().equalsIgnoreCase(ss[ss.length-1])){
					menubar.setVisible(true);
					throw new ConflictException(m, m); //TODO norm exception for jmenu
				}
			}else if(c[i] instanceof MenuItem){
				if(((MenuItem)c[i]).getText().equalsIgnoreCase(ss[ss.length-1])){
					menubar.setVisible(true);
					throw new ConflictException(((MenuItem)c[i]).getModule(), m);
				}
			}
		}
		if(i == l){
			MenuItem nmi = new MenuItem(ss[ss.length-1], menuIndex, m);
			jm.add(nmi);
		}
		menubar.setVisible(true);
		return menuIndex++;
	}
	
	public int addButton(Icon icon, Module m){
		//System.out.println("add butt");
		ToolButton b = new ToolButton(icon, buttonIndex, m);
		buttonPanel.setVisible(false);
		buttonPanel.add(b);
		buttonPanel.setVisible(true);
		return buttonIndex++;
	}
	
	public JPanel addGraphicsModule(GraphicsModule gm, int where, String name){
		JTabbedPane z;
		switch(where){
		case TOP:
			z = top;
			break;
		case BOTTOM:
			z = bottom;
			break;
		case LEFT:
			z = left;
			break;
		case RIGHT:
			z = right;
			break;
		default:
			z = bottom;
			break;
		}
		JPanel tc = new ButtonTabComponent(z, gm);
		JPanel res = new JPanel();
		z.addTab(name, res);
		z.setTabComponentAt(z.indexOfComponent(res), tc);
		return res;
	}
}