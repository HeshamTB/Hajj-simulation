import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class GUI_ViewBuses {

	private static JFrame frame;
	private static ArrayList<Vehicle> vehicles = new ArrayList<>();
	private static ArrayList<Vehicle> vehiclesAlazizya = new ArrayList<>();
	private static ArrayList<Vehicle> vehiclesAlhijra = new ArrayList<>();
	private static ArrayList<Vehicle> vehiclesAlmansoor = new ArrayList<>();
	private static PDate currenttimeManager;
	private static JTable table;
	private static Object[][] busData;
	private static String[] busColNames = {"ID", "Street", "location","Progress", "trip time",  "Arrive Time"};
	private static JButton Alazizya;
	private static JButton Alhijra;
	private static JButton Almansoor;
	private static JLabel lblTime;
	private static JLabel lblDate;
	JLabel lblStatus;
	private JLabel lblDestination;
	private JLabel lblDistrict;
	private JLabel lblDistrictValue;
	
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
		frame = new JFrame("Buses");
		busData = new Object[vehicles.size()][6];
		
	
		for (int i = 0; i < vehicles.size(); i++) {
			busData[i][0] = vehicles.get(i).getUID();
			if (vehicles.get(i).isMoving())
				busData[i][1] = vehicles.get(i).getCurrentStreet().getName();
			else busData[i][1] = "Not Moving";
			busData[i][2] = vehicles.get(i).getCurrentLocation();
			busData[i][3] = vehicles.get(i).getProgress();
			busData[i][4] = vehicles.get(i).getTripTime();
			if (vehicles.get(i).isArrivedToDest())
				busData[i][5] = vehicles.get(i).getTimeOfArrival();//Formula of time
			else busData[i][5] = "NOT Arrived";
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
		scrollPane.setBounds(20, 24, 754, 236);
		
		frame.getContentPane().setBackground(new Color(70, 70, 70));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100,100,814,454);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(scrollPane);
		
		Alazizya = new JButton("Alazizya");
		Alazizya.setBounds(30, 271, 99, 23);
		Alazizya.addActionListener(e -> {
			updateVehicles(vehiclesAlazizya);
		});
		frame.getContentPane().add(Alazizya);
		
		Alhijra = new JButton("Alhijra");
		Alhijra.setBounds(30, 317, 99, 23);
		Alhijra.addActionListener(e -> {
			updateVehicles(vehiclesAlhijra);
		});
		frame.getContentPane().add(Alhijra);
		
		Almansoor = new JButton("Almansoor");
		Almansoor.setBounds(30, 366, 99, 23);
		Almansoor.addActionListener(e -> {
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

		lblDate = new JLabel(currenttimeManager.getCurrentTime().toString());
		lblDate.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDate.setForeground(Color.WHITE);
		lblDate.setBounds(235, 270, 326, 21);
		frame.getContentPane().add(lblDate);
		
		lblDestination = new JLabel();
		lblDestination.setForeground(Color.WHITE);
		lblDestination.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDestination.setBounds(235, 317, 184, 18);
		frame.getContentPane().add(lblDestination);
    	if (currenttimeManager.getCurrentCalendar().get(Calendar.DAY_OF_MONTH) == 9) 
			 lblDestination.setText("Heading to Arafat");
		else lblDestination.setText("Heading to Hotels");
    	
    	lblDistrict = new JLabel("District: ");
		lblDistrict.setForeground(Color.WHITE);
		lblDistrict.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDistrict.setBackground(Color.LIGHT_GRAY);
		lblDistrict.setBounds(180, 370, 72, 14);
		frame.getContentPane().add(lblDistrict);
		
		lblDistrictValue = new JLabel();
		lblDistrictValue.setText("0");
		lblDistrictValue.setForeground(Color.WHITE);
		lblDistrictValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDistrictValue.setBackground(Color.BLACK);
		lblDistrictValue.setBounds(247, 371, 90, 12);
		frame.getContentPane().add(lblDistrictValue);
		
		
		
		frame.setVisible(true);
	}
	
	public void setData(ArrayList<Campaign> campaign , PDate currenttimeManager) {
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
	
	public void updateTable() {
		busData = new Object[vehicles.size()][6];
		for (int i = 0; i < vehicles.size(); i++) {
			busData[i][0] = vehicles.get(i).getUID();
			if (vehicles.get(i).isMoving())
				busData[i][1] = vehicles.get(i).getCurrentStreet().getName();
			else busData[i][1] = "Not Moving";
			busData[i][2] = vehicles.get(i).getCurrentLocation();
			busData[i][3] = vehicles.get(i).getProgress();
			busData[i][4] = vehicles.get(i).getTripTime();
			if (vehicles.get(i).isArrivedToDest())
				busData[i][5] = vehicles.get(i).getTimeOfArrival().toString();//Formula of time
			else busData[i][5] = "NOT Arrived";
		}
		table.setModel(new DefaultTableModel(busData ,busColNames));
	}
	
	public void updateVehicles(ArrayList<Vehicle> vehiclesDistrict) {
		vehicles.clear();
		vehicles.addAll(vehiclesDistrict);
		lblDistrictValue.setText(((Bus)vehiclesDistrict.get(0)).getCampaign().getHotelDistrict().name());
		updateTable();
	}
}
