import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.DropMode;
import javax.swing.JTextPane;

public class GUI_ViewBuses {

	private JFrame frame;
	private ArrayList<Vehicle> vehicles = new ArrayList<>();
	private ArrayList<Vehicle> vehiclesAlazizya = new ArrayList<>();
	private ArrayList<Vehicle> vehiclesAlhijra = new ArrayList<>();
	private ArrayList<Vehicle> vehiclesAlmansoor = new ArrayList<>();
	private Date currenttimeManager;
	private JTable table;
	private Object[][] busData;
	private String[] busColNames = {"ID", "Street", "location","Progress", "trip time",  "Arrive Time"};
	private JButton Alazizya;
	private JButton Alhijra;
	private JButton Almansoor;
	private JLabel lblTime;
	private JLabel lblDate;
	private JLabel lblStatus;
	private JLabel lblDestination;
	private JLabel lblDistrict;
	private JLabel lblDistrictValue;
	private JTextPane textPane;
	
	
	public GUI_ViewBuses(ArrayList<Campaign> campaign , Date currenttimeManager) {
		for (Campaign camp : campaign) {
			switch (camp.getHotelDistrict()) {
			case ALAZIZIYA:
				vehiclesAlazizya.add(camp.getVehicles().get(0));
				break;
			case ALHIJRA:
				vehiclesAlhijra.add(camp.getVehicles().get(0));
				break;
			case ALMANSOOR:
				vehiclesAlmansoor.add(camp.getVehicles().get(0));
				break;
			default:
				break;
			}
		}
		this.currenttimeManager = currenttimeManager;
		makeFrame();
	}
	
	
	private void makeFrame() {
		frame = new JFrame("Buses");
		
		table = new JTable(busData,busColNames);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(busColNames);
		ListSelectionModel model2 = table.getSelectionModel();
		model2.addListSelectionListener(e -> {
				if (!model2.isSelectionEmpty()) {
				int row = model2.getMinSelectionIndex();
				textPane.setText(((Bus)vehicles.get(row)).toString());
			}
			});
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
		scrollPane.getVerticalScrollBar().setBackground(new Color(17,17,17));
		scrollPane.setBounds(20, 24, 887, 236);
		
		frame.getContentPane().setBackground(new Color(70, 70, 70));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100,100,972,454);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(scrollPane);
		
		Alazizya = new JButton("Alazizya");
		Alazizya.setBackground(new Color(9,9,9));
		Alazizya.setFont(new Font("Rockwell", Font.PLAIN, 16));
		Alazizya.setForeground(Color.white);
		Alazizya.setBounds(20, 366, 116, 23);
		Alazizya.addActionListener(e -> {
			if (vehiclesAlazizya.get(0).getRoute() != null)
			updateVehicles(vehiclesAlazizya);
		});
		frame.getContentPane().add(Alazizya);
		
		Alhijra = new JButton("Alhijra");
		Alhijra.setBackground(new Color(9,9,9));
		Alhijra.setFont(new Font("Rockwell", Font.PLAIN, 16));
		Alhijra.setForeground(Color.white);
		Alhijra.setBounds(20, 269, 116, 23);
		Alhijra.addActionListener(e -> {
			if (vehiclesAlhijra.get(0).getRoute() != null)
			updateVehicles(vehiclesAlhijra);
		});
		frame.getContentPane().add(Alhijra);
		
		Almansoor = new JButton("Almansoor");
		Almansoor.setBackground(new Color(9,9,9));
		Almansoor.setFont(new Font("Rockwell", Font.PLAIN, 16));
		Almansoor.setForeground(Color.white);
		Almansoor.setBounds(20, 316, 116, 23);
		Almansoor.addActionListener(e -> {
			if (vehiclesAlmansoor.get(0).getRoute() != null)
			updateVehicles(vehiclesAlmansoor);
		});
		frame.getContentPane().add(Almansoor);
		
		lblTime = new JLabel("Time:");
		lblTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblTime.setForeground(new Color(255, 255, 255));
		lblTime.setBounds(180, 273, 72, 14);
		frame.getContentPane().add(lblTime);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setForeground(new Color(255, 255, 255));
		lblStatus.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblStatus.setBounds(180, 317, 72, 18);
		frame.getContentPane().add(lblStatus);

		lblDate = new JLabel(currenttimeManager.toString());
		lblDate.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDate.setForeground(Color.WHITE);
		lblDate.setBounds(235, 270, 326, 21);
		frame.getContentPane().add(lblDate);
		
		lblDestination = new JLabel();
		lblDestination.setForeground(Color.WHITE);
		lblDestination.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDestination.setBounds(235, 317, 184, 18);
		frame.getContentPane().add(lblDestination);
    	if (currenttimeManager.getMonth() == 9) 
			 lblDestination.setText("Heading to Arafat");
		else lblDestination.setText("Heading to Hotels");
    	
    	lblDistrict = new JLabel("District: ");
		lblDistrict.setForeground(Color.WHITE);
		lblDistrict.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDistrict.setBackground(Color.LIGHT_GRAY);
		lblDistrict.setBounds(180, 370, 72, 14);
		frame.getContentPane().add(lblDistrict);
		
		lblDistrictValue = new JLabel();
		lblDistrictValue.setForeground(Color.WHITE);
		lblDistrictValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDistrictValue.setBackground(Color.BLACK);
		lblDistrictValue.setBounds(247, 371, 129, 12);
		frame.getContentPane().add(lblDistrictValue);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("Rockwell", Font.PLAIN, 16));
		textPane.setForeground(Color.WHITE);
		textPane.setBackground(Color.BLACK);
		
		JScrollPane scrolltext = new JScrollPane(textPane);
		scrolltext.setBounds(523, 271, 384, 133);
		frame.getContentPane().add(scrolltext);

		
		frame.setVisible(true);
	}
	
	
	public void updateTable() {
		busData = new Object[vehicles.size()][6];
		for (int i = 0; i < vehicles.size(); i++) {
			busData[i][0] = vehicles.get(i).getUID();
			if (vehicles.get(i).isMoving())
				busData[i][1] = vehicles.get(i).getCurrentStreet().getName();
			else busData[i][1] = "Not Moving";
			busData[i][2] = vehicles.get(i).getCurrentLocation();
			if (vehicles.get(i).getProgress() != null)
				busData[i][3] = vehicles.get(i).getProgress();
			else busData[i][3] = "%0";
			busData[i][4] = vehicles.get(i).getTripTime();
			if (vehicles.get(i).isArrivedToDest())
				busData[i][5] = getShortTime(vehicles.get(i).getTimeOfArrival());
			else busData[i][5] = "NOT Arrived";
		}
		table.setModel(new DefaultTableModel(busData ,busColNames));
	}
	
	
	public void updateVehicles(ArrayList<Vehicle> vehiclesDistrict) {
		vehicles.clear();
		vehicles.addAll(vehiclesDistrict);
		lblDistrictValue.setText(((Bus)vehiclesDistrict.get(0)).getCampaign().getHotelDistrict().name());
		lblDate.setText(currenttimeManager.toString());
		updateTable();
	}

	private String getShortTime(Date time) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(time);
		return String.format("%02d:%02d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
	}
}
