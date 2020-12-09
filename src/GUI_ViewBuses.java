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
		//makeFrame();
	}

	private void makeFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 1000);
		GridBagLayout gridBagLayout = new GridBagLayout();
//		JScrollPane scrollPane = new JScrollPane();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
//		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
//		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		for (int i = 0; i<100 ; i++) {
			JButton btnNewButton = new JButton(vehicles.get(i).getUID());
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
			gbc_btnNewButton.gridx = i%5;
			gbc_btnNewButton.gridy = i/5 +1;
			frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
			frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
			}

//		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
//		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
//		gbc_scrollPane.fill = GridBagConstraints.BOTH;
//		gbc_scrollPane.gridx = 0;
//		gbc_scrollPane.gridy = 1;
		//scrollPane.setViewportView(gbc_scrollPane);
		//frame.getContentPane().add(scrollPane, gbc_scrollPane);
		frame.setVisible(true);
	}
	
	public void  setData(ArrayList<Campaign> campaign) {
		for (Campaign camp : campaign)
			vehicles.add(camp.getVehicles().get(0));
		makeFrame();
	}

	/**
	 * Object[][] vehicleData = new Object[50][4]; String[] vehicleColNames =
	 * {"ID","Street", "Location", "Distance covered"};
	 * 
	 * for (int i = 0; i < 50 ; i++) { vehicleData[i][0] = vehicles.get(i).getUID();
	 * vehicleData[i][1] = vehicles.get(i).getCurrentStreet(); vehicleData[i][2] =
	 * vehicles.get(i).getCurrentLocation(); vehicleData[i][3] =
	 * vehicles.get(i).getTotalDistanceTraveled(); //vehicleData[i][4] =
	 * vehicles.get(i).getTimeOfArrival(); }
	 * 
	 * table = new JTable(vehicleData,vehicleColNames);
	 * frame.getContentPane().add(table, BorderLayout.CENTER);
	 * table.setEnabled(false); DefaultTableModel model = new DefaultTableModel();
	 * model.setColumnIdentifiers(vehicleData);
	 * table.getTableHeader().setBackground(new Color(17,17,17));
	 * table.getTableHeader().setForeground(Color.WHITE);
	 * table.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
	 * table.setBackground(new Color(17,17,17)); table.setForeground(Color.white);
	 * table.setSelectionBackground(Color.RED); table.setGridColor(new Color(102,
	 * 102, 102)); table.setSelectionForeground(Color.white); table.setFont(new
	 * Font("Rockwell", Font.PLAIN, 18)); table.setRowHeight(25);
	 * table.setLocation(700, 200); table.revalidate();
	 */
	/**
	 * @wbp.parser.entryPoint
	 */


}
