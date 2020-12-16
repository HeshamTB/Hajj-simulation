import java.awt.Color;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class GUI_History {

	private static JFrame frame;
	private List<State> states;
	
	public GUI_History(List<State> states) {
		this.states = states;
		initialize();
	}

	private void initialize() {
		frame = new JFrame("History");
		frame.getContentPane().setBackground(new Color(70, 70, 70));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 1228, 727);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JComboBox<Date> comboBox = new JComboBox<Date>();
		comboBox.setBounds(453, 46, 302, 42);
		for (State state : states) {
			comboBox.addItem(state.getStateTime());
		}
		panel.add(comboBox);
		frame.setLocationRelativeTo(null);
		frame.revalidate();
		frame.setLocation(200,150);
		frame.setBounds(200, 150, 1400, 720);
		frame.setAutoRequestFocus(false);
		frame.setVisible(true);
	}
}
