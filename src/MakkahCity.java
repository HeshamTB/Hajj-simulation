import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class MakkahCity {

	private static final ArrayList<Campaign> listOfCampaigns = new ArrayList<>();
	private static final ArrayList<Vehicle> listOfVehicles = new ArrayList<>();
	private static final ArrayList<Campaign>[] campPerDistrict = new ArrayList[District.values().length];
	private static final Route[] stdRoutes = new Route[RouteName.values().length];
	private static final Street[] stdStreet = new Street[StreetNames.values().length];
	private static Date allArrivedToArafatTime;
	private static Date allArrivedToHotelsTime;

	private static final PDate firstDayTimeMan = new PDate(
		new GregorianCalendar(1442, Calendar.JANUARY, 9, 4, 0, 0),
		new GregorianCalendar(1442, Calendar.JANUARY, 9, 18, 0, 0)
	);

	private static final PDate lastDayTimeMan = new PDate(
			new GregorianCalendar(1442, Calendar.JANUARY, 12, 12, 0, 0),
			new GregorianCalendar(1442, Calendar.JANUARY, 12, 23, 0, 0)
	);

	private static PDate currenttimeManager = firstDayTimeMan;

	private static final InputListener inputListener = new InputListener();
	private static final Thread t = new Thread(inputListener,"InputThread-Makkah");
	private static boolean isAllRoutSet;
	private static final DataManeger dataManeger = new DataManeger();
	//GUI
	private static boolean exit_flag;
	private static boolean pause_flag;
	private static Checkbox autoModeCheckBox;
	private static JFrame makkahFrame;
	private static JTable streetTable;
	private static JTable districtTable;
	private static JLabel lblDate;
	private static JLabel lblDestination;
	private static JLabel lblNumOfBuses;
	private static JLabel lblNumOfDonebuses;
	private static JLabel lblMaximumTripValue;
	private static JLabel lblMinimumTripValue;
    private static JLabel lblBusesArrivedInTheLastHourValue;
    private static JLabel lblAverageTripForLastHourValue;
    private static JButton btnPause;
	private static JLabel lblAverageTimeForTheTrip;
	private static JLabel lblArrivedToArafatTime;
	private static JLabel lblArrivedToHotelsTime;
	

	public static void main(String[] args) {

		//TODO: View camp >> View Report
		//TODO: History view
		//TODO: Arrival dates (Arafat, Mina)
		//TODO: fix state errors
		t.start();
		//Gen Camp
		campPerDistrict[District.ALMANSOOR.ordinal()] = new ArrayList<>();
		campPerDistrict[District.ALAZIZIYA.ordinal()] = new ArrayList<>();
		campPerDistrict[District.ALHIJRA.ordinal()] = new ArrayList<>();
		generateCamps(District.ALAZIZIYA, (int)getRandom(1200, 1400));
		generateCamps(District.ALMANSOOR, (int)getRandom(1600, 1800));
		generateCamps(District.ALHIJRA, (int)getRandom(1400, 1600));
		
		Collections.shuffle(listOfCampaigns);

		fillBusesToList();
		
		makeStreets();

		addCivilVehicleNoise();

		makeRoutes();

		//GUI
		autoModeCheckBox = new Checkbox();
		makkahFrame = new JFrame("Hajj Simulation");
		
		//Street data and district for GUI table
		
		//Street data
		Object[][] streetData = new Object[stdStreet.length][6];
		String[] streetColNames = {"Street Name", "Street Load %", "Total", "Buses",
				"Local Vehicles","Avg. Time"};

		for (int i = 0; i < stdStreet.length; i++) {
			streetData[i][0] = stdStreet[i].getName().name();
			streetData[i][1] = stdStreet[i].getPercentRemainingCapacity();
			streetData[i][2] = stdStreet[i].getVehicles().size();
			streetData[i][3] = stdStreet[i].getNumberOfBuses();
			streetData[i][4] = stdStreet[i].getNumberOfLocalCars();
			streetData[i][5] = avgTimeOnStreet(stdStreet[i]);
		}
		//District data
		Object[][] districtData = new Object[campPerDistrict.length][7];
		String[] districtColNames = {"District", "Campaigns", "Busses", "Arrival %",
				"Avg. Time", "Best time to Arafat", "Best time to District"};
		
		for (int i = 0; i < campPerDistrict.length; i++) {
			districtData[i][0] = campPerDistrict[i].get(0).getHotelDistrict().name();
			districtData[i][1] = campPerDistrict[i].size();
			districtData[i][2] = busesInDistrict(District.values()[i]);
			districtData[i][3] = getPercentArrival(District.values()[i]);
			districtData[i][4] = getAvgTimeOfTrip(District.values()[i]);
			districtData[i][5] = getShortestRoute(campPerDistrict[i].get(0), Mashier.ARAFAT).getFastestTimeOfTravel(new Bus());
			districtData[i][6] = getShortestRoute(campPerDistrict[i].get(0), Mashier.MINA).getFastestTimeOfTravel(new Bus());
		}
		
		//tables
		
		//Street table
		streetTable = new JTable(streetData,streetColNames);
		streetTable.setEnabled(false);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(streetColNames);
		streetTable.getTableHeader().setBackground(new Color(17,17,17));
		streetTable.getTableHeader().setForeground(Color.WHITE);
		streetTable.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		streetTable.setBackground(new Color(17,17,17));
		streetTable.setForeground(Color.white);
		streetTable.setSelectionBackground(Color.RED);
		streetTable.setGridColor(new Color(102, 102, 102));
		streetTable.setSelectionForeground(Color.white);
		streetTable.setFont(new Font("Rockwell", Font.PLAIN, 18));
		streetTable.setRowHeight(25);
		streetTable.setAutoCreateRowSorter(true);
		JScrollPane streetScroll = new JScrollPane(streetTable);
		streetScroll.setBounds(50,100,1216,329);
		
		//District table
		districtTable = new JTable(districtData,districtColNames);
		districtTable.setEnabled(false);
		districtTable.setForeground(new Color(255, 255, 255));
		districtTable.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		districtTable.getTableHeader().setBackground(new Color(17,17,17));
		districtTable.getTableHeader().setForeground(Color.WHITE);
		districtTable.setBackground(new Color(17,17,17));
		districtTable.setSelectionBackground(Color.RED);
		model.setColumnIdentifiers(districtColNames);
		districtTable.setSelectionForeground(Color.white);
		districtTable.setFont(new Font("Rockwell", Font.PLAIN, 18));
		districtTable.setGridColor(new Color(102, 102, 102));
		JScrollPane districtScroll = new JScrollPane(districtTable);
		districtScroll.setEnabled(false);
		districtTable.setAutoCreateRowSorter(true);
		districtTable.setRowHeight(25);
		districtTable.revalidate();
		districtScroll.setBounds(50,478,1216,105);
		
		//Buttons
		JButton btnViewRoutes = new JButton("View Routes");
		btnViewRoutes.setBounds(1307, 33, 166, 29);
		btnViewRoutes.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnViewRoutes.setBackground(new Color(9,9,9));
		btnViewRoutes.setForeground(Color.white);
		btnViewRoutes.addActionListener(e -> {
			GUI_ViewRoute r = new GUI_ViewRoute(stdRoutes ,listOfCampaigns, currenttimeManager);
			pause_flag = true;
			btnPause.setText("Unpause");
		});
		JButton btnViewBuses = new JButton("View Buses");
		btnViewBuses.setBounds(1307, 76, 166, 29);
		btnViewBuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnViewBuses.setBackground(new Color(9,9,9));
		btnViewBuses.setForeground(Color.white);
		btnViewBuses.addActionListener(e -> {
			GUI_ViewBuses t =  new GUI_ViewBuses(listOfCampaigns , currenttimeManager);
			pause_flag = true;
			btnPause.setText("Unpause");
		});
		
		JButton btnViewCampaigns = new JButton("View Campaigns");
		btnViewCampaigns.setBounds(1307, 119, 166, 29);
		btnViewCampaigns.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnViewCampaigns.setBackground(new Color(9,9,9));
		btnViewCampaigns.setForeground(Color.white);
		
		JButton btnViewStreet = new JButton("View Street");
		btnViewStreet.setBounds(1307, 159, 166, 29);
		btnViewStreet.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnViewStreet.setBackground(new Color(9,9,9));
		btnViewStreet.setForeground(Color.white);
		btnViewStreet.addActionListener(e -> {
			GUI_ViewStreet t =  new GUI_ViewStreet(stdStreet,currenttimeManager);
			pause_flag = true;
			btnPause.setText("Unpause");
		});

		JButton btnExit = new JButton("Exit");
		btnExit.setBackground(new Color(9,9,9));
		btnExit.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnExit.setForeground(Color.white);
		btnExit.setBounds(1307, 623, 166, 29);
		btnExit.addActionListener(actionEvent -> exit_flag = true);

		btnPause = new JButton("Pause");
		btnPause.setBackground(new Color(9,9,9));
		btnPause.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnPause.setForeground(Color.white);
		btnPause.setBounds(1307, 240, 166, 29);
		btnPause.addActionListener(actionEvent -> {
			if (!pause_flag) {
				pause_flag = true;
				btnPause.setText("Unpause");
			}
			else {
				pause_flag = false;
				btnPause.setText("Pause");
			}
		});
		
		JButton btnBrowseHistory = new JButton("Browse History");
		btnBrowseHistory.setBounds(1307, 199, 166, 29);
		btnBrowseHistory.setBackground(new Color(9,9,9));
		btnBrowseHistory.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnBrowseHistory.setForeground(Color.white);
		btnBrowseHistory.addActionListener(e -> {
			GUI_History hist = new GUI_History(dataManeger.getStates());
		});
		
		//Label 
		JLabel lblStreets = new JLabel("Streets");
		lblStreets.setFont(new Font("Rockwell", Font.PLAIN, 24));
		lblStreets.setForeground(new Color(255, 255, 255));
		lblStreets.setBounds(49, 59, 208, 30);
		
		JLabel lblDistrict = new JLabel("District");
		lblDistrict.setFont(new Font("Rockwell", Font.PLAIN, 24));
		lblDistrict.setForeground(new Color(255, 255, 255));
		lblDistrict.setBounds(49, 438, 166, 29);
		
		JLabel lblTime = new JLabel("Time:");
		lblTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblTime.setForeground(new Color(255, 255, 255));
		lblTime.setBounds(50, 11, 72, 14);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setForeground(new Color(255, 255, 255));
		lblStatus.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblStatus.setBounds(423, 9, 72, 18);
		
		lblDestination = new JLabel();
		lblDestination.setForeground(new Color(255, 255, 255));
		lblDestination.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDestination.setBounds(477, 9, 184, 18);

		lblDate = new JLabel(currenttimeManager.getCurrentTime().toString());
		lblDate.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDate.setForeground(Color.WHITE);
		lblDate.setBounds(100, 8, 326, 21);
		
		JLabel lblBuses = new JLabel("Buses: ");
		lblBuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBuses.setForeground(new Color(255, 255, 255));
		lblBuses.setBackground(new Color(192, 192, 192));
		lblBuses.setBounds(49, 605, 56, 14);
		
		lblNumOfBuses = new JLabel();
		lblNumOfBuses.setText("0");
		lblNumOfBuses.setBackground(new Color(0, 0, 0));
		lblNumOfBuses.setForeground(new Color(255, 255, 255));
		lblNumOfBuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblNumOfBuses.setBounds(100, 606, 90, 12);
		
		JLabel lblBusesDone = new JLabel("Buses Done:");
		lblBusesDone.setForeground(new Color(255, 255, 255));
		lblBusesDone.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBusesDone.setBounds(199, 606, 101, 12);
		
		lblNumOfDonebuses = new JLabel();
		lblNumOfDonebuses.setText("0");
		lblNumOfDonebuses.setForeground(new Color(255, 255, 255));
		lblNumOfDonebuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblNumOfDonebuses.setBounds(293, 604, 80, 16);
		
		JLabel lblMaximumTrip = new JLabel("Maximum Trip:");
		lblMaximumTrip.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMaximumTrip.setForeground(new Color(255, 255, 255));
		lblMaximumTrip.setBounds(49, 666, 112, 22);
		
		lblMaximumTripValue = new JLabel();
		lblMaximumTripValue.setText("-:--");
		lblMaximumTripValue.setForeground(new Color(255, 255, 255));
		lblMaximumTripValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMaximumTripValue.setBounds(169, 668, 46, 18);
		
		JLabel lblMinimumTrip = new JLabel("Minimum Trip:");
		lblMinimumTrip.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMinimumTrip.setForeground(Color.WHITE);
		lblMinimumTrip.setBounds(49, 699, 112, 18);
		
		lblMinimumTripValue = new JLabel("-:--");
		lblMinimumTripValue.setForeground(Color.WHITE);
		lblMinimumTripValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMinimumTripValue.setBounds(167, 701, 90, 14);
		
		JLabel lblBusesArrivedInTheLastHour = new JLabel("Buses Arrived In The Last Hour:");
		lblBusesArrivedInTheLastHour.setForeground(Color.WHITE);
		lblBusesArrivedInTheLastHour.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBusesArrivedInTheLastHour.setBounds(395, 605, 237, 14);
		
		lblBusesArrivedInTheLastHourValue = new JLabel();
		lblBusesArrivedInTheLastHourValue.setText("0");
		lblBusesArrivedInTheLastHourValue.setForeground(Color.WHITE);
		lblBusesArrivedInTheLastHourValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBusesArrivedInTheLastHourValue.setBounds(628, 605, 90, 14);
		
		JLabel lblAverageTripForLastHour = new JLabel("Average Trip For Last Hour:");
		lblAverageTripForLastHour.setForeground(Color.WHITE);
		lblAverageTripForLastHour.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAverageTripForLastHour.setBackground(Color.BLACK);
		lblAverageTripForLastHour.setBounds(287, 668, 208, 18);
		
		lblAverageTripForLastHourValue = new JLabel("(No Arrivals) In Last Hour");
		lblAverageTripForLastHourValue.setForeground(Color.WHITE);
		lblAverageTripForLastHourValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAverageTripForLastHourValue.setBounds(502, 668, 216, 18);
		
		JLabel lblAvgTime = new JLabel("Average Time For The Trip:");
		lblAvgTime.setForeground(Color.WHITE);
		lblAvgTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAvgTime.setBounds(287, 694, 208, 29);
		
		lblAverageTimeForTheTrip = new JLabel("-:--");
		lblAverageTimeForTheTrip.setForeground(Color.WHITE);
		lblAverageTimeForTheTrip.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAverageTimeForTheTrip.setBounds(502, 701, 101, 14);
		
		JLabel lblArrivedToArafat = new JLabel("All Arrived To Arafat At:");
		lblArrivedToArafat.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblArrivedToArafat.setForeground(Color.WHITE);
		lblArrivedToArafat.setBounds(724, 670, 216, 14);
		makkahFrame.getContentPane().add(lblArrivedToArafat);
		
		JLabel lblArrivedToHotels = new JLabel("All Arrived To Hotels At:");
		lblArrivedToHotels.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblArrivedToHotels.setForeground(Color.WHITE);
		lblArrivedToHotels.setBounds(724, 701, 184, 14);
		makkahFrame.getContentPane().add(lblArrivedToHotels);
		
		lblArrivedToArafatTime = new JLabel("N/A");
		lblArrivedToArafatTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblArrivedToArafatTime.setForeground(Color.WHITE);
		lblArrivedToArafatTime.setBounds(908, 670, 358, 14);
		makkahFrame.getContentPane().add(lblArrivedToArafatTime);
		
		lblArrivedToHotelsTime = new JLabel("N/A");
		lblArrivedToHotelsTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblArrivedToHotelsTime.setForeground(Color.WHITE);
		lblArrivedToHotelsTime.setBounds(908, 701, 358, 14);
		
		//Add Elements
		makkahFrame.getContentPane().add(streetScroll);
		makkahFrame.getContentPane().add(lblAverageTimeForTheTrip);
		makkahFrame.getContentPane().add(lblAverageTripForLastHourValue);
		makkahFrame.getContentPane().add(lblBusesArrivedInTheLastHourValue);
		makkahFrame.getContentPane().add(lblStatus);
		makkahFrame.getContentPane().add(lblDestination);
		makkahFrame.getContentPane().add(lblBuses);
		makkahFrame.getContentPane().add(lblNumOfBuses);
		makkahFrame.getContentPane().add(lblBusesDone);
		makkahFrame.getContentPane().add(lblNumOfDonebuses);
		makkahFrame.getContentPane().add(btnExit);
		makkahFrame.getContentPane().add(btnViewStreet);
		makkahFrame.getContentPane().add(btnViewCampaigns);
		makkahFrame.getContentPane().add(btnViewBuses);
		makkahFrame.getContentPane().add(btnViewRoutes);
		makkahFrame.getContentPane().add(lblAverageTripForLastHour);
		makkahFrame.getContentPane().add(lblAvgTime);
		makkahFrame.getContentPane().add(districtScroll);
		makkahFrame.getContentPane().add(lblDistrict);
		makkahFrame.getContentPane().add(lblStreets);
		makkahFrame.getContentPane().add(lblTime);
		makkahFrame.getContentPane().add(lblDate);
		makkahFrame.getContentPane().add(lblBusesArrivedInTheLastHour);
		makkahFrame.getContentPane().add(lblMaximumTripValue);
		makkahFrame.getContentPane().add(lblMinimumTripValue);
		makkahFrame.getContentPane().add(lblMaximumTrip);
		makkahFrame.getContentPane().add(lblMinimumTrip);
		makkahFrame.getContentPane().add(btnBrowseHistory);
		makkahFrame.getContentPane().add(btnPause);
		
		//Frame Settings
		makkahFrame.getContentPane().setBackground(new Color(70, 70, 70));
		makkahFrame.getContentPane().setForeground(new Color(0, 0, 0));
		makkahFrame.setBounds(100,100,1519,777);
		makkahFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		makkahFrame.getContentPane().setLayout(null);
		makkahFrame.getContentPane().add(lblArrivedToHotelsTime);
		makkahFrame.setLocationRelativeTo(null);
		makkahFrame.revalidate();
		makkahFrame.setLocation(200,150);
		makkahFrame.setAutoRequestFocus(false);
		makkahFrame.setVisible(true);
		
		//Set Routes for Campaigns
		while(!firstDayTimeMan.isEnded()) {
			checkInput();
			//Start of Every hour
			if (firstDayTimeMan.getCurrentCalendar().get(Calendar.MINUTE) == 0){
				System.out.println("\n\n" + getStreetsReport());
				updateStreetFrame();
				saveState();
			}
			else System.out.print(".");
			
			if (!isAllRoutSet) {
				isAllRoutSet = true;
				setRoutesForCampaigns(Mashier.ARAFAT);
				}
			clearDoneCivilVehicles();
			addCivilVehicleNoise();
			for (Vehicle vehicle : listOfVehicles) {
				if (vehicle.getRoute() == null)
					continue;
				Route route = vehicle.getRoute();
				double currentLocation = vehicle.getCurrentLocation();
				if (vehicle.getCurrentStreet() == null &&
				firstDayTimeMan.getCurrentCalendar().get(Calendar.MINUTE) % 2 == 0 &&
				route.getStreets()[0].capcityPoint(0,1000) < 1) {
					vehicle.setCurrentStreet(route.getStreets()[0]);
				}
				 if (vehicle.getCurrentStreet() != null && vehicle.getCurrentStreet().capcityPoint(currentLocation,
						currentLocation+1000) < 1 ) {

					if (currentLocation >= vehicle.getCurrentStreet().getLength()) {
						//Move to next street
						vehicle.moveToNextStreet();
					}
					if (!vehicle.isArrivedToDest()) {
						double factor = 1-(vehicle.getCurrentStreet().capcityPoint(vehicle.getCurrentLocation(),
										vehicle.getCurrentLocation()+1000,vehicle)) ;
						if (vehicle instanceof Bus) vehicle.move(Bus.MAX_FORWARD * factor );
						else if (vehicle instanceof Sedan) vehicle.move(Sedan.MAX_FORWARD * factor );
						else if (vehicle instanceof SUV) vehicle.move(SUV.MAX_FORWARD * factor );
						else if (vehicle instanceof Truck) vehicle.move(Bus.MAX_FORWARD * factor );
					}
				}
			}
			if (isAllArrived() && allArrivedToArafatTime == null) allArrivedToArafatTime = (Date)currenttimeManager.getCurrentTime().clone();
			firstDayTimeMan.step(Calendar.MINUTE, 1);
			lblDate.setText(currenttimeManager.getCurrentTime().toString());
			lblArrivedToArafatTime.setText(getDistTimeForLbl());
		}
		
		currenttimeManager = lastDayTimeMan;
		System.out.println("\n***************FINSHIED ARAFAT DAY***************");
		//Collections.shuffle(listOfVehicles);
		isAllRoutSet = false;
		for (Vehicle vehicle : listOfVehicles) {
			vehicle.setCurrentStreet(null);
		}
		Vehicle.resetMaxArrived();
		Vehicle.resetMinArrived();
		addCivilVehicleNoise();

		System.out.println("***************STARTING LAST DAY***************");
		setRoutesForCampaigns(Mashier.MINA);
		while(!lastDayTimeMan.isEnded()) {
			checkInput();
			//Start of Every hour
			if (lastDayTimeMan.getCurrentCalendar().get(Calendar.MINUTE) == 0){
				System.out.println("\n\n" + getStreetsReport());
				updateStreetFrame();
				saveState();
			}
			else System.out.print(".");
			
			
			if (!isAllRoutSet) {
				isAllRoutSet = true;
				setRoutesForCampaigns(Mashier.MINA);
				}
			clearDoneCivilVehicles();
			addCivilVehicleNoise();
			for (Vehicle vehicle : listOfVehicles) {
				if (vehicle.getRoute() == null)
					continue;
				Route route = vehicle.getRoute();
				double currentLocation = vehicle.getCurrentLocation();
				if (vehicle.getCurrentStreet() == null &&
						route.getStreets()[0].capcityPoint(0,1000) < 1) {
					vehicle.setCurrentStreet(route.getStreets()[0]);
				}
				if (vehicle.getCurrentStreet() != null && vehicle.getCurrentStreet().capcityPoint(currentLocation,
						currentLocation+1000) < 1 ) {

					if (currentLocation >= vehicle.getCurrentStreet().getLength()) {
						//Move to next street
						vehicle.moveToNextStreet();
					}
					if (!vehicle.isArrivedToDest()) {
						double factor = 1-(vehicle.getCurrentStreet().capcityPoint(vehicle.getCurrentLocation(),
								vehicle.getCurrentLocation()+1000,vehicle)) ;
						if (vehicle instanceof Bus) vehicle.move(Bus.MAX_FORWARD * factor );
						else if (vehicle instanceof Sedan) vehicle.move(Sedan.MAX_FORWARD * factor );
						else if (vehicle instanceof SUV) vehicle.move(SUV.MAX_FORWARD * factor );
						else if (vehicle instanceof Truck) vehicle.move(Bus.MAX_FORWARD * factor );
					}
				}
			}
			if (isAllArrived() && allArrivedToHotelsTime == null) allArrivedToHotelsTime = (Date)currenttimeManager.getCurrentTime().clone();
			lastDayTimeMan.step(Calendar.MINUTE, 1);
			lblDate.setText(currenttimeManager.getCurrentTime().toString());
			lblArrivedToHotelsTime.setText(getDistTimeForLbl());
		}
		//When done show menu to analyze. Exit from menu too.
		inputListener.pause();
		startMenu();
	}

	private static void checkInput() {
		String input = "";
		if (exit_flag) System.exit(0);
		if (pause_flag) {
			while (pause_flag) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (inputListener.hasNew()){
			input = inputListener.getInput();
			if (input.equals("m")){
				System.out.println("PAUSED");
				inputListener.pause();
				startMenu();
				inputListener.unpause();
			}
		}
	}

	private static void startMenu() {
		Scanner in = new Scanner(System.in);
		System.out.println("\n"+currenttimeManager.getCurrentTime()+"\n"+
							"---------------------------\n" +
							"[1] View Buses\n" +
							"[2] View Streets\n" +
							"[3] View Campaigns\n" +
							"[4] View Routes\n" +
							"[5] Print report\n" +
							"[6] Browse History\n" +
							"[7] Continue\n" +
							"[8] Exit");
		String choice = in.next();
		//Split into methods?
		if (choice.equals("1")){
			ArrayList<Vehicle> buses = new ArrayList<>();
			for (Campaign campaign : listOfCampaigns)
				buses.addAll(campaign.getVehicles());
			System.out.printf("choose from 0 to %d\n", buses.size()-1);
			String c = in.next();
			Vehicle v = buses.get(Integer.parseInt(c));
			showVehicle(v);
			//meybe add option here to go to members (Campaign, Street ...)
		}
		if (choice.equals("2")){
			for (int i = 0; i < stdStreet.length; i++) {
				System.out.printf("[%d] %s (%%%d)\n",i, stdStreet[i].getName().name(),
						stdStreet[i].getPercentRemainingCapacity());
			}
			String input = in.next();
			int index = Integer.parseInt(input);//TODO: unhandled ex
			showStreet(stdStreet[index]);
		}
		if (choice.equals("4")){
			for (int i = 0; i < stdRoutes.length; i++){
				int count = 0;
				for (Campaign campaign : listOfCampaigns)
					if (campaign.getRoute() == stdRoutes[i])
						count += campaign.getVehicles().size();

				System.out.printf("[%d] %sUsed By %d buses\n\n", i, stdRoutes[i], count);
			}
		}
		if (choice.equals("5")) System.out.println(getStreetsReport());
		if (choice.equals("6")) browseHistory();
		if (choice.equals("7")) return;
		if (choice.equals("8")) {
			inputListener.stop();
			t.interrupt();
			System.exit(0);
		}
		startMenu();
	}

	private static void showVehicle(Vehicle vehicle) {
		String choice = "";
		Scanner in = new Scanner(System.in);
		StringBuilder menu = new StringBuilder();
		int opCount = 0;
		menu.append("\n\n")
				.append(vehicle.getUID())
				.append("\n")
				.append("---------------------------\n")
				.append(String.format("[%d] Details\n",++opCount));
		if (vehicle instanceof Bus) {
			menu.append(String.format("[%d] Get Campaign\n", ++opCount));
		}
		menu.append(String.format("[%d] Return", ++opCount));
		System.out.println(menu.toString());
		choice = in.next();
		if (choice.equals("1")) System.out.println(vehicle.toString());
		if (choice.equals("2") && opCount == 3) showCampaign(((Bus)vehicle).getCampaign());
		else if (choice.equals("2")) return;
		else if (choice.equals("3")) return;
		showVehicle(vehicle);
	}

	private static void showCampaign(Campaign campaign){
		String choice = "";
		Scanner in = new Scanner(System.in);
		System.out.println("\n"+campaign.toString());
		System.out.println("\n"+campaign.getUID());
		System.out.println("---------------------------\n" +
				"[1] Details\n" +
				"[2] Select bus\n" +
				"[3] Get current Route\n" +
				"[4] Return");
		choice = in.next();
		if (choice.equals("1")) System.out.println(campaign.toString());
		if (choice.equals("2")){
			System.out.printf("Choose from 0 to %d", campaign.getNumberOfBusses());
			choice = in.next();
			showVehicle(campaign.getVehicles().get(Integer.parseInt(choice)));
		}
		if (choice.equals("3")) showRoute(campaign.getRoute());
		if (choice.equals("4")) return;
		showCampaign(campaign);
	}

	private static void showStreet(Street street) {
		String choice = "";
		Scanner in = new Scanner(System.in);
		System.out.println("\n"+street.getName().name());
		System.out.println("---------------------------\n"+
				"[1] Details\n" +
				"[2] Select vehicle\n" +
				"[3] Get history for vehicle (NOT IMPLEMENTED YET)\n" +
				"[4] Return");
		choice = in.next();
		if (choice.equals("1")) System.out.println("\n\n"+street.toString());
		if (choice.equals("2")){
			System.out.printf("Select from 0 to %d\n", street.getVehicles().size());
			choice = in.next();
			showVehicle(street.getVehicles().get(Integer.parseInt(choice)));
		}
		if (choice.equals("4")) return;
		showStreet(street);
	}

	private static void showRoute(Route route){
		System.out.println(route.toString());
	}

	private static void browseHistory() {
		Calendar dummyCal = new GregorianCalendar();
		dummyCal.setTime(currenttimeManager.getCurrentTime());
		//dummyCal.roll(Calendar.HOUR, -1);
		dummyCal.set(Calendar.MINUTE, 0);//Go down to last hour.
		Scanner in = new Scanner(System.in);
		boolean selected = false;
		while (!selected){
			System.out.println(new HijriDate(dummyCal.getTimeInMillis()));
			System.out.print("[1] Forward\n" +
							"[2] Backward\n" +
							"[3] Select\n" +
							"[4] Return\n");
			String choice = in.next();
			if (choice.equals("1")) dummyCal.roll(Calendar.HOUR, 1);
			if (choice.equals("2")) dummyCal.roll(Calendar.HOUR, -1);
			if (choice.equals("3")) {
				selected = true;
				DataManeger dataManeger = new DataManeger();
				if (dataManeger.stateAvailable(dummyCal.getTime())) {
					State state = dataManeger.loadState(dummyCal.getTime());
					System.out.print(dummyCal.getTime() + " (History)\n");
					System.out.println("\n"+
							"---------------------------\n" +
							"[1] View Buses\n" +
							"[2] View Streets\n" +
							"[3] View Campaigns\n" +
							"[4] View Routes\n");
					choice = in.next();
					if (choice.equals("1")){
						System.out.print("0 - "+state.getListOfVehicles().size());
						choice = in.next();
						showVehicle(state.getListOfVehicles().get(Integer.parseInt(choice)));
					}
				} else System.out.print("Not saved");
			}
		}

	}

	private static void clearDoneCivilVehicles() {
		//Clear civil cars from list
		for (int i = 0; i < listOfVehicles.size();){
			Vehicle vehicle = listOfVehicles.get(i);
			if (!(vehicle instanceof Bus) && vehicle.isArrivedToDest())
				listOfVehicles.remove(vehicle);
			else i++;

		}
	}

	private static void setRoutesForCampaigns(Mashier mashier) {
		for (Campaign camp : listOfCampaigns){
			if(camp.getVehicles().get(0).getCurrentStreet() == null) {
				isAllRoutSet = false;
				camp.setRoute(getBestRoute(camp, mashier));
			}
		}
	}

	private static double getRandom(double min, double max) {
		return (Math.random() * (max - min) + min);
	}

	private static void generateCamps(District area, int count) {
		for (int i = 0; i < count; i++){
			Campaign camp = new Campaign(area, (int)getRandom(10, 15));
			listOfCampaigns.add(camp);
			campPerDistrict[area.ordinal()].add(camp);
		}
	}

	private static void makeStreets(){
		stdStreet[StreetNames.KA_STREET.ordinal()] = new Street(4700,3, StreetNames.KA_STREET);
		stdStreet[StreetNames.FOURTH_HIGHWAY1.ordinal()] = new Street(4850,4, StreetNames.FOURTH_HIGHWAY1);
		stdStreet[StreetNames.FOURTH_HIGHWAY2.ordinal()] = new Street(4850,4, StreetNames.FOURTH_HIGHWAY2);
		stdStreet[StreetNames.FOURTH_HIGHWAY3.ordinal()] = new Street(4500,4, StreetNames.FOURTH_HIGHWAY3);
		stdStreet[StreetNames.THIRD_HIGHWAY.ordinal()] = new Street(8200,3, StreetNames.THIRD_HIGHWAY);
		stdStreet[StreetNames.STREET1.ordinal()] = new Street(7800,4, StreetNames.STREET1);
		stdStreet[StreetNames.STREET2.ordinal()] = new Street(2400,3,StreetNames.STREET2);
		stdStreet[StreetNames.STREET3.ordinal()] = new Street(4800,2, StreetNames.STREET3);
		stdStreet[StreetNames.JABAL_THAWR_STREET.ordinal()] = new Street(3800,3,StreetNames.JABAL_THAWR_STREET);
		stdStreet[StreetNames.IBRAHIM_ALKHALIL2.ordinal()] = new Street(3200,2, StreetNames.IBRAHIM_ALKHALIL2);
		stdStreet[StreetNames.IBRAHIM_ALKHALIL1.ordinal()] = new Street(4500,3, StreetNames.IBRAHIM_ALKHALIL1);
		stdStreet[StreetNames.KKH_STREET.ordinal()] = new Street(3300,3, StreetNames.KKH_STREET);

	}

	private static void makeRoutes() {

		//******Arafat day
		stdRoutes[RouteName.AlHijraToArafat1.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.THIRD_HIGHWAY.ordinal()],
						stdStreet[StreetNames.STREET2.ordinal()],
						stdStreet[StreetNames.STREET1.ordinal()]},
				District.ALHIJRA, Mashier.ARAFAT);

		stdRoutes[RouteName.AlHijraToArafat2.ordinal()] = new Route(new Street[]{
						stdStreet[StreetNames.JABAL_THAWR_STREET.ordinal()],
						stdStreet[StreetNames.FOURTH_HIGHWAY2.ordinal()],
						stdStreet[StreetNames.STREET1.ordinal()]
		},District.ALHIJRA, Mashier.ARAFAT);

		stdRoutes[RouteName.AlMansoorToArafat1.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.STREET3.ordinal()],
						stdStreet[StreetNames.KA_STREET.ordinal()],
						stdStreet[StreetNames.STREET2.ordinal()],
						stdStreet[StreetNames.STREET1.ordinal()]
				},District.ALMANSOOR, Mashier.ARAFAT);

		stdRoutes[RouteName.AlMansoorToArafat2.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.IBRAHIM_ALKHALIL2.ordinal()],
						stdStreet[StreetNames.THIRD_HIGHWAY.ordinal()],
						stdStreet[StreetNames.STREET2.ordinal()],
						stdStreet[StreetNames.STREET1.ordinal()]
				},District.ALMANSOOR, Mashier.ARAFAT);

		//Optimal for Almansoor
		stdRoutes[RouteName.AlMansoorToArafat3.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.IBRAHIM_ALKHALIL2.ordinal()],
						stdStreet[StreetNames.IBRAHIM_ALKHALIL1.ordinal()],
						stdStreet[StreetNames.FOURTH_HIGHWAY1.ordinal()],
						stdStreet[StreetNames.FOURTH_HIGHWAY2.ordinal()],
						stdStreet[StreetNames.STREET1.ordinal()]
				},District.ALMANSOOR, Mashier.ARAFAT);

		stdRoutes[RouteName.AlAziziToArafat1.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.KA_STREET.ordinal()],
						stdStreet[StreetNames.STREET2.ordinal()],
						stdStreet[StreetNames.STREET1.ordinal()]
		},District.ALAZIZIYA, Mashier.ARAFAT);

		//******Arafat day end

		//******Mina Leave
		stdRoutes[RouteName.MinaToAlMansoor1.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.KKH_STREET.ordinal()],
						stdStreet[StreetNames.KA_STREET.ordinal()],
						stdStreet[StreetNames.STREET3.ordinal()]
				},District.ALMANSOOR, Mashier.MINA);

		stdRoutes[RouteName.MinaToAlMansoor2.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.FOURTH_HIGHWAY3.ordinal()],
						stdStreet[StreetNames.STREET2.ordinal()],
						stdStreet[StreetNames.THIRD_HIGHWAY.ordinal()],
						stdStreet[StreetNames.IBRAHIM_ALKHALIL2.ordinal()]
				},District.ALMANSOOR, Mashier.MINA);

		stdRoutes[RouteName.MinaToAlMansoor3.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.FOURTH_HIGHWAY3.ordinal()],
						stdStreet[StreetNames.FOURTH_HIGHWAY2.ordinal()],
						stdStreet[StreetNames.FOURTH_HIGHWAY1.ordinal()],
						stdStreet[StreetNames.IBRAHIM_ALKHALIL1.ordinal()],
						stdStreet[StreetNames.IBRAHIM_ALKHALIL2.ordinal()]
				},District.ALMANSOOR, Mashier.MINA);

		stdRoutes[RouteName.MinaToAlhijra1.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.FOURTH_HIGHWAY3.ordinal()],
						stdStreet[StreetNames.STREET2.ordinal()],
						stdStreet[StreetNames.THIRD_HIGHWAY.ordinal()]
				},District.ALHIJRA, Mashier.MINA);

		stdRoutes[RouteName.MinaToAlhijra2.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.FOURTH_HIGHWAY3.ordinal()],
						stdStreet[StreetNames.FOURTH_HIGHWAY2.ordinal()],
						stdStreet[StreetNames.JABAL_THAWR_STREET.ordinal()]
				},District.ALHIJRA, Mashier.MINA);

		stdRoutes[RouteName.MinaToAlhijra3.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.KKH_STREET.ordinal()],
						stdStreet[StreetNames.THIRD_HIGHWAY.ordinal()]
				},District.ALHIJRA, Mashier.MINA);

		stdRoutes[RouteName.MinaToAlaziziya1.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.KKH_STREET.ordinal()],
						stdStreet[StreetNames.KA_STREET.ordinal()],
				},District.ALAZIZIYA, Mashier.MINA);

		stdRoutes[RouteName.MinaToAlaziziya2.ordinal()] = new Route(
				new Street[]{
						stdStreet[StreetNames.FOURTH_HIGHWAY3.ordinal()],
						stdStreet[StreetNames.STREET2.ordinal()],
						stdStreet[StreetNames.KA_STREET.ordinal()],
				},District.ALAZIZIYA, Mashier.MINA);
		//******Mina Leave end

	}

	private static void fillBusesToList() {
		for (Campaign camp : listOfCampaigns) {
			listOfVehicles.addAll(camp.getVehicles());
		}
	}

	private static void addCivilVehicleNoise() {

		for (Street street: stdStreet) {
			if (street.getPercentRemainingCapacity() >= 80) 
				continue;
			
			int numOfSedan = (int)getRandom(10,15);
			int numOfSUV = (int)getRandom(5,9);
			int numOfTruck = (int)getRandom(3,6);
			if (!street.isContainsBuses()) {
				numOfSedan *= 5;
				numOfSUV *= 5;
				numOfTruck *= 2;
			}
			if (street.getName() == StreetNames.FOURTH_HIGHWAY1) numOfSedan = (int) (numOfSedan * 0.5);
			if (street.getName() == StreetNames.FOURTH_HIGHWAY2) numOfSedan = (int) (numOfSedan * 0.5);
			if (street.getName() == StreetNames.STREET3) numOfSedan = (int) (numOfSedan * 1.5);
			if (street.getName() == StreetNames.IBRAHIM_ALKHALIL2) numOfSedan = (int) (numOfSedan * 1.2);
			for (int x = 0; x < numOfSedan; x++) {
				Sedan car = new Sedan(getRandom(4, 5));
				double pointOfEntry = getRandom(0, street.getLength());//TODO: consider getLength - x
				if (street.capcityPoint(pointOfEntry, pointOfEntry+1500) < 1){
					listOfVehicles.add(car);
					car.setCurrentLocation(pointOfEntry);
					car.setRoute(new Route(street));
					car.setCurrentStreet(street);
				}

			}

			if (street.getName() == StreetNames.FOURTH_HIGHWAY1) numOfTruck = (int) (numOfTruck * 0.5);
			if (street.getName() == StreetNames.FOURTH_HIGHWAY2) numOfTruck = (int) (numOfTruck * 0.5);
			if (street.getName() == StreetNames.STREET3) numOfTruck = (int) (numOfTruck * 1.5);
			if (street.getName() == StreetNames.IBRAHIM_ALKHALIL2) numOfSedan = (int) (numOfSedan * 1.2);
			for (int x = 0; x < numOfTruck; x++) {
				Truck car = new Truck(getRandom(4, 5));
				double pointOfEntry = getRandom(0, street.getLength());
				if (street.capcityPoint(pointOfEntry, pointOfEntry+1500) < 1){
					listOfVehicles.add(car);
					car.setCurrentLocation(pointOfEntry);
					car.setRoute(new Route(street));
					car.setCurrentStreet(street);
				}
			}

			if (street.getName() == StreetNames.FOURTH_HIGHWAY1) numOfSUV = (int) (numOfSUV * 0.5);
			if (street.getName() == StreetNames.FOURTH_HIGHWAY2) numOfSUV = (int) (numOfSUV * 0.5);
			if (street.getName() == StreetNames.STREET3) numOfSUV = (int) (numOfSUV * 1.5);
			if (street.getName() == StreetNames.IBRAHIM_ALKHALIL2) numOfSUV = (int) (numOfSedan * 1.2);
			for (int x = 0; x < numOfSUV; x++) {
				SUV car = new SUV(getRandom(4, 5));
				double pointOfEntry = getRandom(0, street.getLength());
				if (street.capcityPoint(pointOfEntry, pointOfEntry+1500) < 1){
					listOfVehicles.add(car);
					car.setCurrentLocation(pointOfEntry);
					car.setRoute(new Route(street));
					car.setCurrentStreet(street);
				}
			}

		}
	}

	public static PDate getTimeMan() {
		return currenttimeManager;
	}

	/**
	 * Find shortest path without respect to traffic
	 * @param campaign
	 * @return
	 */
	
	private static Route getBestRoute(Campaign campaign , Mashier mashier) {
		//ArrayList<Route> routes = (ArrayList<Route>) Arrays.asList(getRoutesToDistrict(campaign.getHotelDistrict()));
		Route[] routes = getRoutesToDistrict(campaign.getHotelDistrict());
		routes = sortRoutes(routes);
		for (Route r : routes) {
			if(r.getMashier() == Mashier.ARAFAT && r.getHotelArea() == District.ALHIJRA) {
				return r;
			}
			if (r.getMashier() == mashier){
				if (r.capcity() < 70) {
					return r;
				}
				else if (r.getHotelArea() == District.ALAZIZIYA)
					return r;
			}
		}
		return null;
	}
	
	private static Route[] sortRoutes(Route[] routes) {
		Route[] sortingRoute = new Route[routes.length];
		double[] lengthes = new double[routes.length];
		
		for (int i = 0; i < lengthes.length ; i++) 
			lengthes[i] = routes[i].getTotalLength();
		
		Arrays.sort(lengthes);
		for (int i = 0; i < lengthes.length ; i++) {
			for (Route r : routes)
				if (lengthes[i] == r.getTotalLength())
					sortingRoute[i] = r;
		}
		return sortingRoute;
	}
	
	private static Route getShortestRoute(Campaign campaign, Mashier mashier) {
		Route[] routes = getRoutesToDistrict(campaign.getHotelDistrict());
		Route route = null;
		double min = Double.MAX_VALUE;
		for (Route r : routes) {
			if (r.getMashier() == mashier){
				if (r.getTotalLength() < min) {
					min = r.getTotalLength();
					route = r;
				}
			}
		}
		return route;
	}

	/**
	 * Find routes that connect to a certain district.
	 * @param district
	 * @return Array of routes that connect to 'district'
	 */
	private static Route[] getRoutesToDistrict(District district) {
		ArrayList<Route> routes = new ArrayList<>();
		for (Route route : stdRoutes) {
			if (route.getHotelArea() == district) {
				routes.add(route);
			}
		}
		Route[] routesArray = new Route[routes.size()];
		return routes.toArray(routesArray);
	}
	
	private static String getStreetsReport() {
		String status = "";
		if (currenttimeManager == firstDayTimeMan) status = "   Status: Heading to Arafat";
		else status = "   Status: Heading to hotels";
		String headerFormat = "******Streets report*****\n" +
						"Time: %s%s\n" +
						"    Street name    |Street Load| Total | Buses | Local Vehicles | Avg. Time |\n";

		StringBuilder report = new StringBuilder();
		report.append(String.format(headerFormat, currenttimeManager.getCurrentTime(), status));
		String streetFormat = "%-18s | %%%-8s | %5d | %5d | %14d | %-10s|";

		for (int i = 0; i < stdStreet.length; i++) {
			int cap = stdStreet[i].getPercentRemainingCapacity();
			report.append(String.format(streetFormat,
					stdStreet[i].getName().name(),
					cap,
					stdStreet[i].getVehicles().size(),
					stdStreet[i].getNumberOfBuses(),
					stdStreet[i].getNumberOfLocalCars(),
					avgTimeOnStreet(stdStreet[i])));
			report.append("\n");
		}
		report.append("\n").append(getFinalRep()).append("\n");
		report.append(preSimulationReport()).append("Type m+Enter to view menu");
		return report.toString();
	}
	
	private static String getFinalRep() {
		StringBuilder s = new StringBuilder();
		int numberOfBusses = 0;
		int numberOfArrivedBuses = getNumberOfArrivedBusses();
		//Redundant loops slow down execution. find better sol.
		for (Campaign campaign : listOfCampaigns) {
			numberOfBusses += campaign.getNumberOfBusses();
		} 
		String fFormat = "All arrived to %s at: %s";
		boolean arr = isAllArrived();//since it has looping. use once.
		if (arr && allArrivedToArafatTime != null)
			s.append(String.format(fFormat,"Arafat",allArrivedToArafatTime)).append("\n");
		if (arr && allArrivedToHotelsTime != null)
			s.append(String.format(fFormat,"Hotels",allArrivedToHotelsTime)).append("\n");
		s.append(String.format("Buses: %d, Buses done: %d %s\nBuses arrived in the last hour: %d, Average trip in last hour: %s\n",
				numberOfBusses, numberOfArrivedBuses, minMaxRep() , getNumberOfArrivedBussesPerHour(), avgTimeOfTrip()));
		return s.toString();
	}
	
	private static String minMaxRep() {
		StringBuilder report = new StringBuilder();
		if (Vehicle.getMaxArrived() != null && Vehicle.getMinArrived() != null) {
			report.append(String.format(" Maximum trip %s,", Vehicle.getMaxArrived().timeToString()));
			report.append(String.format(" Minimum trip %s", Vehicle.getMinArrived().timeToString()));
			}		
		return report.toString();
	}

	/**
	 * Calculate average trip time for last 10 minutes
	 * @return "hh:mm"
	 */
	private static String avgTimeOfTrip() {
		//TODO: does output diff value even after all have arrived.
		Calendar now = currenttimeManager.getCurrentCalendar();
		Calendar from = (GregorianCalendar)now.clone();
		from.roll(Calendar.HOUR, -1);
		int counter = 1;
		int sum = 0;
		for (Campaign campaign : listOfCampaigns){
			for (Vehicle bus : campaign.getVehicles()){
				if (bus.isArrivedToDest() && bus.getTimeOfArrival().before(now.getTime())
				&& bus.getTimeOfArrival().after(from.getTime())) {
					long minutes = (bus.getTimeOfArrival().getTime() - bus.getTimeStartedMoving().getTime())/60000;
					sum+= minutes;
					counter++;
				}
			}
		}
		sum = sum /counter;
		int hours = sum / 60;
		int minutes = sum % 60;
		if (hours == 0 && minutes == 0) return "(No Arrivals) In Last Hour";
		return String.format("%2d:%02d", hours, minutes);
	}

	private static int getPercentArrival(District district) {
		int sum = 0;
		for (Campaign campaign : campPerDistrict[district.ordinal()]) {
			sum += campaign.getPercentArrived();
		}
		return sum/campPerDistrict[district.ordinal()].size();
	}

	private static String getAvgTimeOfTrip(District district){
		int sum = 0;
		int counter = 1;
		for (Campaign campaign : campPerDistrict[district.ordinal()]) {
			for (Vehicle vehicle : campaign.getVehicles()) {
				if (vehicle.isArrivedToDest()) {
					long minutes = (vehicle.getTimeOfArrival().getTime() - vehicle.getTimeStartedMoving().getTime())/60000;
					sum+= minutes;
					counter++;
				}
			}
		}//Make the following a method since it is the same other method
		sum = sum /counter;
		int hours = sum / 60;
		int minutes = sum % 60;
		if (hours == 0 && minutes == 0) return " n/a";
		return String.format("%2d:%02d", hours,minutes);
	}

	private static int getNumberOfArrivedBusses() {
		int num = 0;
		for (Campaign campaign : listOfCampaigns) {
			for (Vehicle vehicle : campaign.getVehicles()){
				if (vehicle instanceof  Bus &&
				vehicle.isArrivedToDest()) num++;
			}
		}
		return num;
	}

	private static int getNumberOfArrivedBussesPerHour() {
		Calendar now = currenttimeManager.getCurrentCalendar();
		Calendar from = (GregorianCalendar)now.clone();
		from.roll(Calendar.HOUR, -1);
		int num = 0;
		for (Campaign campaign : listOfCampaigns){
			for (Vehicle bus : campaign.getVehicles()){
				if (bus.isArrivedToDest() && bus.getTimeOfArrival().before(now.getTime())
				&& bus.getTimeOfArrival().after(from.getTime())) {
					num++;
				}
			}
		}
		return num;
	}
	
	private static boolean isAllArrived() {
		for (Campaign campaign : listOfCampaigns)
			if (!campaign.isDone())
				return false;
		
		return true;
	}

	private static String preSimulationReport() {
		StringBuilder report = new StringBuilder();
		report.append("******************************District details******************************\n");
		report.append(" District | Campaigns | Busses | Arrival | Avg Time | Best time to Arafat | Best time to District |\n");
		for (int i = 0; i < campPerDistrict.length; i++) {
			//Per District, i denotes district index
			report.append(String.format("%-10s|",campPerDistrict[i].get(0).getHotelDistrict().name()));

			report.append(String.format(" %-10d|",campPerDistrict[i].size()));
			report.append(String.format(" %-7d|", busesInDistrict(District.values()[i])));
			report.append(String.format(" %%%-7d|", getPercentArrival(District.values()[i])));
			report.append(String.format(" %-9s|", getAvgTimeOfTrip(District.values()[i])));
			report.append(String.format(" %-20s|", getShortestRoute(campPerDistrict[i].get(0), Mashier.ARAFAT).getFastestTimeOfTravel(new Bus())));
			report.append(String.format(" %-22s|", getShortestRoute(campPerDistrict[i].get(0), Mashier.MINA).getFastestTimeOfTravel(new Bus())));
			//Calc values per dist here.
			//TODO: add arrived buses colum (NO NEED)
			report.append("\n");
		}
		return report.toString();
	}

	//This is for ALL vehicles, should make it for last hour to be consistent with the report.
	public static String avgTimeOnStreet(Street street) {
		int sum = 0;
		int counter = 1;
		for (Campaign campaign : listOfCampaigns)
			for (Vehicle vehicle : campaign.getArrivedVehicles())
				if (vehicle.hasCrossedStreet(street)){
					sum += vehicle.getTimeOnStreet(street);
					counter++;
				}
		sum /= counter;
		int hours = sum / 60;
		int minutes = sum % 60;
		if (hours == 0 && minutes == 0) return street.getFastestTimeOfTravel(new Bus());
		return String.format("%02d:%02d", hours, minutes);
	}

	private static int busesInDistrict(District district){
		int buses = 0;
		for (Campaign campaign : campPerDistrict[district.ordinal()]){
			buses += campaign.getNumberOfBusses();
		}
		return buses;
	}

	private static void saveState(){
		State s = new State(listOfCampaigns,
				listOfVehicles,
				stdRoutes,
				stdStreet,
				allArrivedToArafatTime,
				allArrivedToHotelsTime,
				currenttimeManager.getCurrentTime());
		dataManeger.saveState(s, currenttimeManager.getCurrentTime());

		boolean result = dataManeger.saveState(s, currenttimeManager.getCurrentTime());
		if (!result) System.out.println("Could not save state "+currenttimeManager.getCurrentTime().getTime());
	}

	 private static void updateStreetFrame() {
		Object[][] streetData = new Object[stdStreet.length][6];
		for (int i = 0; i < stdStreet.length; i++) {
			streetData[i][0] = stdStreet[i].getName().name();
			streetData[i][1] = stdStreet[i].getPercentRemainingCapacity();
			streetData[i][2] = stdStreet[i].getVehicles().size();
			streetData[i][3] = stdStreet[i].getNumberOfBuses();
			streetData[i][4] = stdStreet[i].getNumberOfLocalCars();
			streetData[i][5] = avgTimeOnStreet(stdStreet[i]);
		}
		for (int i = 0; i < streetData.length; i++) {
			for (int j = 0; j < streetData[i].length; j++) {
				streetTable.setValueAt(streetData[i][j], i, j);
			}
		}
		Object[][] districtData = new Object[campPerDistrict.length][7];
		for (int i = 0; i < campPerDistrict.length; i++) {
			districtData[i][0] = campPerDistrict[i].get(0).getHotelDistrict().name();
			districtData[i][1] = campPerDistrict[i].size();
			districtData[i][2] = busesInDistrict(District.values()[i]);
			districtData[i][3] = getPercentArrival(District.values()[i]);
			districtData[i][4] = getAvgTimeOfTrip(District.values()[i]);
			districtData[i][5] = getShortestRoute(campPerDistrict[i].get(0), Mashier.ARAFAT).getFastestTimeOfTravel(new Bus());
			districtData[i][6] = getShortestRoute(campPerDistrict[i].get(0), Mashier.MINA).getFastestTimeOfTravel(new Bus());
		}
		for (int i = 0; i < districtData.length; i++) {
			for (int j = 0; j < districtData[i].length; j++) {
				districtTable.setValueAt(districtData[i][j], i, j);
			}
		}
		lblDate.setText(currenttimeManager.getCurrentTime().toString());
		
		 String status = "";
		 if (currenttimeManager == firstDayTimeMan) {
			 status = "Heading to Arafat";
		 }
			else{
				status = "Heading to Hotels";
			}
		 	lblDestination.setText(status);
		 	
		 int numberOfBusses = 0;
		 for (Campaign campaign : listOfCampaigns) {
				numberOfBusses += campaign.getNumberOfBusses();
			} 
		 String bus = String.format("%d", numberOfBusses);
		 lblNumOfBuses.setText(bus);
		
		String numOfdoneBuses = String.format("%d",getNumberOfArrivedBusses());
		lblNumOfDonebuses.setText(numOfdoneBuses);
		
		if (Vehicle.getMaxArrived() != null && Vehicle.getMinArrived() != null) {
			lblMaximumTripValue.setText(Vehicle.getMaxArrived().getTripTime().toString());
			lblMinimumTripValue.setText(Vehicle.getMinArrived().getTripTime().toString());
		}
		
		String NumberOfBussPerHour = String.format("%d", getNumberOfArrivedBussesPerHour());
		lblBusesArrivedInTheLastHourValue.setText(NumberOfBussPerHour);
		
		lblAverageTripForLastHourValue.setText(avgTimeOfTrip());
		lblAverageTimeForTheTrip.setText(getAvgTripForAllDis());
		
	 }
	 
	 public static String getAvgTripForAllDis() {
		 int sum = 0;
		 int counter = 1;
		 for (Campaign campaign : listOfCampaigns) {
			 for (Vehicle vehicle : campaign.getVehicles()) {
				 if (vehicle.isArrivedToDest()) {
					 long minutes = (vehicle.getTimeOfArrival().getTime() - vehicle.getTimeStartedMoving().getTime())/60000;
					 sum+= minutes;
					 counter++;
				 }
			 }
		 }//Make the following a method since it is the same other method
		 sum = sum /counter;
		 int hours = sum / 60;
		 int minutes = sum % 60;
		 if (hours == 0 && minutes == 0) return "-:--";
		 return String.format("%2d:%02d", hours,minutes);
	 }

	 private static String getDistTimeForLbl() {
			int numberOfBusses = 0;
			int numberOfArrivedBuses = getNumberOfArrivedBusses();
			
			for (Campaign campaign : listOfCampaigns) {
				numberOfBusses += campaign.getNumberOfBusses();
			} 
			
			boolean Done = isAllArrived();
			if (Done && allArrivedToArafatTime != null) {
				return String.format("%s", allArrivedToArafatTime);
			}
			if (Done && allArrivedToHotelsTime != null) {
				return String.format("%s",allArrivedToHotelsTime);
			}
			return "N/A";
	 }
			
}


