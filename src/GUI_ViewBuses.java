import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JTable;

public class GUI_ViewBuses {

	private static JFrame frame;
	private static ArrayList<Vehicle> vehicles = new ArrayList<>();
	private static JTable table;
//	private JLabel lblNewLabel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_ViewBuses window = new GUI_ViewBuses();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI_ViewBuses() {
		makeFrame();
	}
	private void makeFrame() {
		frame = new JFrame("Buses");
		Object[][] busData = new Object[vehicles.size()][5];
		String[] busColNames = {"ID", "trip time", "location", "Street", "district"
				};
		
		table = new JTable(busData,busColNames);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(busColNames);
		table.getTableHeader().setBackground(new Color(17,17,17));
		table.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setModel(model);
		table.setBackground(new Color(17,17,17));
		table.setForeground(Color.WHITE);
		table.setGridColor(new Color(102, 102, 102));
		table.setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.setRowHeight(25);
		table.setAutoCreateRowSorter(true);
		table.revalidate();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setEnabled(false);
		scrollPane.setBounds(20, 24, 668, 236);
		
		frame.getContentPane().add(scrollPane);
		frame.getContentPane().setBackground(new Color(70, 70, 70));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100,100,814,454);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(scrollPane);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(89, 306, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(222, 306, 89, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		btnNewButton_2.setBounds(341, 306, 89, 23);
		frame.getContentPane().add(btnNewButton_2);
		frame.setVisible(true);
		

			}
}
