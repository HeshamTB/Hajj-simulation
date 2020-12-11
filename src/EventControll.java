import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;

public class EventControll {

	private JFrame frame;
	//private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EventControll window = new EventControll();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EventControll() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		for (int i = 0; i<10 ; i++) {
			JTextField textField = new JTextField();
			int x =(int)(((double)i/9.0)*255);
			textField.setBackground(new Color(255,x,0));
			frame.getContentPane().add(textField);
			textField.setColumns(10);
		}
	}

	public void setData(Object A) {
		//lblNewLabel.setText(A.toString());
	}
}
