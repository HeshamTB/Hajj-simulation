import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class GUI_ViewStreet {
	private static JFrame frame;
	private static Street street;
	private static Street[] stdStreet = new Street[StreetNames.values().length];
	private static PDate currenttimeManager;
	private static Object[][] busData;
	private static String[] busColNames = {"ID", "District", "location","Progress", "trip time"};
	private static JTable table;
	
	public GUI_ViewStreet(Street[] streets, PDate currenttimeManager) {
		this.stdStreet = streets;
		this.currenttimeManager = currenttimeManager;
		makeFrame();
	}
	
	private void makeFrame() {
		street = stdStreet[0];
		frame = new JFrame("Streets");
		frame.getContentPane().setBackground(new Color(70, 70, 70));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100,100,814,454);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
busData = new Object[street.getVehicles().size()][6];
		
		for (int i = 0; i < street.getVehicles().size(); i++) {
			busData[i][0] = street.getVehicles().get(i).getUID();
			if (street.getVehicles().get(i) instanceof Bus)
				busData[i][1] = ((Bus)street.getVehicles().get(i)).getCampaign().getHotelDistrict().name();
			else busData[i][1] = "Local Vehicle";
			busData[i][2] = street.getVehicles().get(i).getCurrentLocation();
			busData[i][3] = street.getVehicles().get(i).getProgress();
			busData[i][4] = street.getVehicles().get(i).getTripTime();
			
		}
		
		table = new JTable(busData,busColNames);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(busColNames);
		table.getTableHeader().setBackground(new Color(17,17,17));
		table.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setModel(new DefaultTableModel(busData ,busColNames));
		table.setBackground(new Color(17,17,17));
		table.setForeground(Color.WHITE);
		table.setGridColor(new Color(102, 102, 102));
		table.setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.setRowHeight(25);
		table.setAutoCreateRowSorter(true);
		table.revalidate();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 168, 754, 236);
		frame.getContentPane().add(scrollPane);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			Street[] streets = stdStreet;
			public int getSize() {
				return stdStreet.length;
			}
			public Object getElementAt(int index) {
				return streets[index].getName().name();
			}
		});
		list.setBounds(10, 11, 118, 146);
		JScrollPane scrollList = new JScrollPane(table);
		scrollList.setBounds(10, 11, 118, 146);
		frame.getContentPane().add(scrollList);
		
		frame.setVisible(true);
	}
}
