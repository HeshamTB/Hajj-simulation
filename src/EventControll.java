import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Font;

public class EventControll {

	private JFrame frame;
	private JLabel lblNewLabel;
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		 lblNewLabel = new JLabel();
		lblNewLabel.setFont(new Font("Rockwell", Font.PLAIN, 16));
		panel.add(lblNewLabel);
		frame.setVisible(true);
	}

	public void setData(Object A) {
		lblNewLabel.setText(A.toString());
	}
}
