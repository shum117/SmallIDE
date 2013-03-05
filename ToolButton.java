import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class ToolButton extends JButton implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Module m;
	private int index;
	
	public ToolButton(String text, int index, Module m) {
		super(text);
		this.m = m;
		this.index= index;
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		m.buttonClick(index);
	}
	
	public Module getModule() {
		return m;
	}
}
