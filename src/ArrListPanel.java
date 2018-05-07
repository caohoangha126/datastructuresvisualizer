import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.util.*;
import java.util.Queue;
import javax.swing.*;

/**
 * A display panel for ArrayList simulation.
 * @author Ha Cao and Sarah Abowitz
 * @version Apr 8th, 2018
 */

public class ArrListPanel {
	private static ArrListCanvas canvas; 
	
	/** Label for the input mode instructions */
	private JLabel instr;

	/** The input mode */
	public InputMode mode = InputMode.ADD_ENTRY;
	
	/** Remember point where last mouse-down event occurred */
	private Point pointUnderMouse;
	
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
		instr = new JLabel("Click to add new nodes; drag to move.");
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

		JButton addElementButton = new JButton("Add Element");
		panel2.add(addElementButton);
		addElementButton.addActionListener(new AddElementListener());

		JButton rmvElementButton = new JButton("Remove Element");
		panel2.add(rmvElementButton);
		rmvElementButton.addActionListener(new RmvElementListener());

		JButton accessButton = new JButton("Access Element");
		panel2.add(accessButton);
		accessButton.addActionListener(new GetElementListener());

		JButton searchButton = new JButton("Search ArrayList");
		panel2.add(searchButton);
		searchButton.addActionListener(new SearchArrListListener());	

		JButton editButton = new JButton("Edit ArrayList");
		panel2.add(editButton);
		editButton.addActionListener(new EditArrListListener());	
		
		panel.add(panel2);	
	}
	
	/** Constants for recording the input mode */
	enum InputMode {
		ADD_ENTRY, RMV_ENTRY, ACCESS_ENTRY, SEARCH_ARRLIST, EDIT_ARRLIST
	}
	
	/** Listener for addElement button */
	public class AddElementListener implements ActionListener {
		/** Event handler for AddPoint button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.ADD_ENTRY;
			instr.setText("Click anywhere to add an element.");
		}
	}

	/** Listener for RmvElement button */
	public class RmvElementListener implements ActionListener {
		/** Event handler for RmvNode button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.RMV_ENTRY;
			instr.setText("Click an ArrayList element to remove it.");
		}
	}

	/** Listener for accessButton */
	public class GetElementListener implements ActionListener {
		/** Event handler for accessButton */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.ACCESS_ENTRY;
			instr.setText("Click an ArrayList element to access it.");
		}
	}

	/** Listener for searchButton */
	public class SearchArrListListener implements ActionListener {
		/** Event handler for searchButton */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.SEARCH_ARRLIST;
			instr.setText("Click anywhere to start a search.");
		}
	}

	/** Listener for editButton */
	public class EditArrListListener implements ActionListener {
		/** Event handler for editButton */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.EDIT_ARRLIST;
			instr.setText("Click an entry to edit.");
		}
	}
	
	public boolean zoneClicked(int x1, int x2, int y1, int y2, Point pt){
		int xPrime = (int) pt.getX();
		int yPrime = (int) pt.getY();
		boolean horiz = false, vert = false;
		if (x1 < xPrime && x2 > xPrime){
			horiz = true;
		    //	System.out.println(horiz);
		}
		if (y1 < yPrime && y2 > yPrime){
			vert = true;
		}
		if (horiz && vert){
			return true;
		}
		return false;
	}
	
	public class ArrListMouseListener extends MouseAdapter
	implements MouseMotionListener {
		public void mouseClicked(MouseEvent e) {
			switch (mode) {
			case ADD_ENTRY:
				if(canvas.getArrList().size() < 10){
					JFrame addQuery = new JFrame("Add an entry");
					String number = JOptionPane.showInputDialog(addQuery, "Type the integer value of your element.");
					int num = Integer.valueOf(number);
					instr.setText("Copying array into new array with new item.");
					canvas.arrListAddition(num);
					
				}else{
							Toolkit.getDefaultToolkit().beep();
							JFrame frame = new JFrame("");
							// Warning
							JOptionPane.showMessageDialog(frame,
									"Failed click on empty space. Start adding nodes again.",
									"Click Warning",
									JOptionPane.WARNING_MESSAGE);
						
					}
				canvas.repaint();				
				break;
			case RMV_ENTRY:
				Point rmvClick = new Point((int) e.getX(), (int) e.getY());
				if (canvas.getArrList().size() > 0){
					int arrLen = canvas.getArrList().size();
					int itemClicked = 10; // if itemClicked stays at 10, no hitbox clicked
					for (int i = 0; i < arrLen; i++){
						int y1 = 26+(60*i);
						int y2 = 76+(60*i);
						if (zoneClicked(22,422,y1,y2,rmvClick)) {itemClicked = i;}
					}
					
					if (itemClicked < 10){
						instr.setText("Copying array into new array without item.");
						canvas.arrListRemoval(itemClicked);
					}
					
				}
				canvas.repaint();
				break;
			case ACCESS_ENTRY:
				Point accClick = new Point((int) e.getX(), (int) e.getY());
				int arrLen = canvas.getArrList().size();
				int itemClicked = 10; // if itemClicked stays at 10, no hitbox clicked
				for (int i = 0; i < arrLen; i++){
					int y1 = 26+(60*i);
					int y2 = 76+(60*i);
					if (zoneClicked(22,422,y1,y2,accClick)) {itemClicked = i;}
				}
				instr.setText("The item is in cyan.");
				if(itemClicked < canvas.getArrList().size()){
					canvas.arrAccess(itemClicked);
				} else {
					instr.setText("Called element out of bounds.");
				}
				break;
			case SEARCH_ARRLIST:
				JFrame addQuery = new JFrame("Add an entry");
				String insertPlace = JOptionPane.showInputDialog(addQuery, "What integer are you looking for?");
				int index = Integer.valueOf(insertPlace);
				canvas.arrListSearch(index);
				break;
			case EDIT_ARRLIST:
				Point accClick2 = new Point((int) e.getX(), (int) e.getY());
				ArrayList<Integer> mockArrList = canvas.getArrList();
				int arrListLen = canvas.getArrList().size();
				int thingClicked = 10; // if itemClicked stays at 10, no hitbox clicked
				for (int i = 0; i < arrListLen; i++){
					int y1 = 26+(60*i);
					int y2 = 76+(60*i);
					if (zoneClicked(22,422,y1,y2,accClick2)) {thingClicked = i;}
				} if(thingClicked < canvas.getArrList().size()){
					JFrame addQuestion = new JFrame("Add an entry");
					String init = JOptionPane.showInputDialog(addQuestion, "What integer should go in arrList.get("+thingClicked+")?");
					int i = Integer.valueOf(init);
					mockArrList.set(thingClicked,i);
					canvas.setArrList(mockArrList);
				} 
				
				canvas.repaint();
				break;
			}
		}
		
		/** Records point under mouse-down event in anticipation of possible drag */
		public void mousePressed(MouseEvent e) {} 
		
		/** Responds to mouse-up event */
		public void mouseReleased(MouseEvent e) {pointUnderMouse = null;}
		
		/** Responds to mouse drag event */
		public void mouseDragged(MouseEvent e) {
			// If mode allows node motion, and there is a point under the mouse, 
			// then change its coordinates to the current mouse coordinates & update display
			if ((mode == InputMode.ADD_ENTRY) && (pointUnderMouse!=null)) {
				pointUnderMouse.setLocation((int) e.getX(), (int) e.getY());
				// Loop through the edge list of this particular node to update the distances
				
			canvas.repaint();
		}
	}
		
		// Empty but necessary to comply with MouseMotionListener interface
		public void mouseMoved(MouseEvent e) {}
	}
} // end of ArrListPanel