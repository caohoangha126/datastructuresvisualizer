import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * A class to implement a graphical panel to display an ArrayList
 * 
 * @author Ha Cao and Sarah Abowitz
 * @version May 7th, 2018
 */

public class ArrListPanel {
	private static ArrListCanvas canvas; 
	
	/** Label for the input mode instructions */
	private JLabel instr;

	/** The input mode */
	public InputMode mode = InputMode.ACCESS_ENTRY;
	
	/** Graph display fields */
	private JPanel panel1;
	private ArrListMouseListener alml;
	
	/** Control field */
	private JPanel panel2;
	
	/** Constructor */
	public ArrListPanel(JComponent panel) {
		// Initialize the graph display and control fields
		canvas = new ArrListCanvas();
		panel1 = new JPanel();
		alml = new ArrListMouseListener();
		instr = new JLabel("Click the element that you want to access. It will change to red.");
		panel2 = new JPanel();	
		createComponents(panel);		
		ArrayList<Integer> nums = new ArrayList<Integer>();
		nums.add(100);
		nums.add(111);
		nums.add(122);
		nums.add(133);
		nums.add(144);
		nums.add(155);
		canvas.setArrList(nums);
	}
	
	/**
	 * A method to default the display of the canvas
	 * to return to the original display condition when changing modes
	 */
	public void defaultVar(ArrListCanvas canvas) {		
		canvas.aLAnnotations.clear();
		canvas.repaint();
	}
	
	/** Puts content in the GUI window */
	public void createComponents(JComponent panel) {
		// Graph display
		panel.setLayout(new FlowLayout());
		panel1.setLayout(new BorderLayout());
		canvas.addMouseListener(alml);
		canvas.addMouseMotionListener(alml);
		panel1.add(canvas);
		panel1.add(instr, BorderLayout.NORTH);		

		panel.add(panel1);
		
		panel2.setLayout(new GridLayout(10, 1));
		
		JButton createArrayButton = new JButton("Create Customized ArrayList");
		panel2.add(createArrayButton);
		createArrayButton.addActionListener(new createArrayListener());

		JButton addElementButton = new JButton("Add Element");
		panel2.add(addElementButton);
		addElementButton.addActionListener(new AddElementListener());

		JButton rmvElementButton = new JButton("Remove Element");
		panel2.add(rmvElementButton);
		rmvElementButton.addActionListener(new RmvElementListener());

		JButton accessButton = new JButton("Access Element");
		panel2.add(accessButton);
		accessButton.addActionListener(new GetElementListener());

		JButton searchButton = new JButton("Search Element");
		panel2.add(searchButton);
		searchButton.addActionListener(new SearchArrListListener());	

		JButton editButton = new JButton("Change Element Value");
		panel2.add(editButton);
		editButton.addActionListener(new EditArrListListener());
		
		JButton clearButton = new JButton("Clear Previous Content");
		panel2.add(clearButton);
		clearButton.addActionListener(new ClearListener());	
		
		panel.add(panel2);	
	}
	
	/** Constants for recording the input mode */
	enum InputMode {
		RMV_ENTRY, ACCESS_ENTRY, EDIT_ARRLIST
	}
	
	/**
	 * A method to test if an ArrayList input is in correct format
	 */
	public static boolean isInRightFormat(String arrListData) {
		if (arrListData != null) {
			if (arrListData.trim().length() == 0) {
				return false;
			} else {
				boolean previousIsSpace = false;
				for (int i = 0; i < arrListData.length(); i++) {
					if ((arrListData.charAt(i) < '0' || arrListData.charAt(i) > '9') && (arrListData.charAt(i) != ' ')) {
						return false;
					} else {
						if (previousIsSpace && arrListData.charAt(i) == ' ') {
							return false;
						} else if (arrListData.charAt(i) == ' ') {
							previousIsSpace = true;
						} else if (arrListData.charAt(i) != ' ') {
							previousIsSpace = false;
						}
					}
				} 
			}
		}
		return true;
	}
	
	/** Listener for Create Array button */
	private class createArrayListener implements ActionListener {
		/** Event handler for Create Array button */
		public void actionPerformed(ActionEvent e) {		
			instr.setText("Input the elements of the ArrayList, separated by a single white space, and ended with no more than one white space.");
			defaultVar(canvas);
			JFrame frame = new JFrame("User's input of the ArrayList");
			// Prompt the user to enter the data of the array 
			String arrListData = JOptionPane.showInputDialog(frame, "List the elements of array separated by whitespace.");
			if (arrListData != null && arrListData.trim().length() != 0) {
				while (!isInRightFormat(arrListData)) {
					arrListData = JOptionPane.showInputDialog(frame, "Elements of array need to be separated by whitespace and be integers. Please re-try.");
				}
				if (arrListData != null) {
					String[] elements = arrListData.split(" ");
					if (elements.length > 10) {
						Toolkit.getDefaultToolkit().beep();
						// Warning
						JOptionPane.showMessageDialog(frame,
								"Input was in the wrong format. Please re-click the button.",
								"Input Warning",
								JOptionPane.WARNING_MESSAGE);
					} else if (arrListData.split(" ") != null) {
						elements = arrListData.split(" ");
						canvas.arrList = new ArrayList<Integer>();
						for (int i = 0; i < elements.length; i++) {
							canvas.arrList.add(Integer.parseInt(elements[i]));
						}
						canvas.repaint();
					}
				}
			}
		}
	}
	
	/** Listener for Add Element button */
	public class AddElementListener implements ActionListener {
		/** Event handler for Add Element button */
		public void actionPerformed(ActionEvent e) {
			instr.setText("Enter the value you want to add to the ArrayList.");
			defaultVar(canvas);
			if (canvas.getArrList().size() < 10){
				JFrame addQuery = new JFrame("Add an element");
				String number = JOptionPane.showInputDialog(addQuery, "Type the integer value of your element.");
				if (number != null) {
					try {
						int num = Integer.valueOf(number);
						canvas.arrListAddition(num);	
					} catch(NumberFormatException error) {
						JFrame frame2 = new JFrame("");
						// Warning
						JOptionPane.showMessageDialog(frame2,
								"Input value is not a valid number",
								"Input Warning",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			} else {
				Toolkit.getDefaultToolkit().beep();
				JFrame frame = new JFrame("");
				// Warning
				JOptionPane.showMessageDialog(frame,
						"For demo purpose, there isn't enough space on the canvas to add more elements.",
						"Limit Warning",
						JOptionPane.WARNING_MESSAGE);
			}
			canvas.repaint();
		}
	}

	/** Listener for Remove Element button */
	public class RmvElementListener implements ActionListener {
		/** Event handler for Remove Node button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.RMV_ENTRY;
			instr.setText("Click an ArrayList element to remove it.");
			defaultVar(canvas);	
		}
	}

	/** Listener for Access button */
	public class GetElementListener implements ActionListener {
		/** Event handler for Access button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.ACCESS_ENTRY;
			instr.setText("Click an ArrayList element to access it. It will change to red.");
			defaultVar(canvas);	
		}
	}

	/** Listener for Search button */
	public class SearchArrListListener implements ActionListener {
		/** Event handler for Search button */
		public void actionPerformed(ActionEvent e) {
			instr.setText("Enter the value you want to search in the ArrayList.");
			defaultVar(canvas);
			JFrame addQuery = new JFrame("Find a value");
			String insertPlace = JOptionPane.showInputDialog(addQuery, "What integer are you looking for?");
			if (insertPlace != null) {
				try {
					int index = Integer.valueOf(insertPlace);
					canvas.arrListSearch(index);
				} catch(NumberFormatException error) {
					JFrame frame2 = new JFrame("");
					// Warning
					JOptionPane.showMessageDialog(frame2,
							"Searched value is not a valid number",
							"Input Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	/** Listener for Edit button */
	public class EditArrListListener implements ActionListener {
		/** Event handler for Edit button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.EDIT_ARRLIST;
			instr.setText("Click an entry to edit.");
			defaultVar(canvas);	
		}
	}
	
	/** Listener for Clear button */
	private class ClearListener implements ActionListener {
		/** Event handler for Clear button */
		public void actionPerformed(ActionEvent e) {
			instr.setText("Previous content is removed.");
			ArrayList<Integer> nums = new ArrayList<Integer>();
			nums.add(100);
			nums.add(111);
			nums.add(122);
			nums.add(133);
			nums.add(144);
			nums.add(155);
			canvas.setArrList(nums);
			canvas.aLAnnotations.clear();
			canvas.repaint();
		}
	}
	
	/** A method to find which element is clicked on */
	public boolean zoneClicked(int x1, int x2, int y1, int y2, Point pt){
		int xPrime = (int) pt.getX();
		int yPrime = (int) pt.getY();
		boolean horiz = false, vert = false;
		if (x1 < xPrime && x2 > xPrime){
			horiz = true;
		}
		if (y1 < yPrime && y2 > yPrime){
			vert = true;
		}
		if (horiz && vert){
			return true;
		}
		return false;
	}
	
	/** Mouse listener for ArrListCanvas element */
	private class ArrListMouseListener extends MouseAdapter
	implements MouseMotionListener {
		public void mouseClicked(MouseEvent e) {
			switch (mode) {
			case RMV_ENTRY:
				Point rmvClick = new Point((int) e.getX(), (int) e.getY());
				if (canvas.getArrList().size() > 0){
					int arrLen = canvas.getArrList().size();
					int itemClicked = canvas.getArrList().size();
					for (int i = 0; i < arrLen; i++){
						int y1 = 26+(60*i);
						int y2 = 76+(60*i);
						if (zoneClicked(22, 372, y1, y2, rmvClick)) itemClicked = i;
					}					
					if (itemClicked < 10){
						instr.setText("Remove the clicked element from the ArrayList.");
						canvas.arrListRemoval(itemClicked);
					}					
				} else {
					instr.setText("Failed click on elements. Click again.");
				}	
				canvas.repaint();
				break;
			case ACCESS_ENTRY:
				Point accClick = new Point((int) e.getX(), (int) e.getY());
				int arrLen = canvas.getArrList().size();
				int itemClicked = canvas.getArrList().size();
				for (int i = 0; i < arrLen; i++){
					int y1 = 26+(60*i);
					int y2 = 76+(60*i);
					if (zoneClicked(22, 372, y1, y2, accClick)) itemClicked = i;
				}
				instr.setText("The accessed item is in red.");
				if (itemClicked < canvas.getArrList().size()){
					canvas.arrAccess(itemClicked);
				} else {
					instr.setText("Failed click on elements. Click again.");
				}
				break;
			case EDIT_ARRLIST:
				Point accClick2 = new Point((int) e.getX(), (int) e.getY());
				ArrayList<Integer> mockArrList = canvas.getArrList();
				int arrListLen = canvas.getArrList().size();
				int thingClicked = canvas.getArrList().size(); 
				for (int i = 0; i < arrListLen; i++){
					int y1 = 26+(60*i);
					int y2 = 76+(60*i);
					if (zoneClicked(22, 372, y1, y2, accClick2)) thingClicked = i;
				} if (thingClicked < canvas.getArrList().size()){
					JFrame addQuestion = new JFrame("Change an element value");
					String init = JOptionPane.showInputDialog(addQuestion, "What integer should go in arrList.get("+thingClicked+")?");
					if (init != null) {
						try {
							int i = Integer.valueOf(init);
							mockArrList.set(thingClicked,i);
							canvas.setArrList(mockArrList);
						} catch(NumberFormatException error) {
							JFrame frame2 = new JFrame("");
							// Warning
							JOptionPane.showMessageDialog(frame2,
									"Input value is not a valid number",
									"Input Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				} else {
					instr.setText("Failed click on elements. Click again.");
				}				
				canvas.repaint();
				break;
			}
		}
		
		// Empty but necessary to comply with MouseMotionListener interface
		public void mouseMoved(MouseEvent e) {}
	}
} // end of ArrListPanel