import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 *  A panel to display array
 *  @author  Ha Cao (modded for Array support by Sarah Abowitz)
 *  @version Apr 7th, 2018
 */

public class ArrayPanel {	
	/** The array to be displayed */
	private static ArrayCanvas canvas;

	/** Label for the input mode instructions */
	private JLabel instr;

	/** The input mode */
	private InputMode mode = InputMode.CREATE_ARRAY;

	/** Graph display fields */
	private JPanel panel1;
	private ArrayMouseListener aml;

	/** Control field */
	private JPanel panel2;

	/** Constructor */
	public ArrayPanel(JComponent panel) {
		// Initialize the array display and control fields
		canvas = new ArrayCanvas();
		panel1 = new JPanel();
		aml = new ArrayMouseListener();
		instr = new JLabel("");
		panel2 = new JPanel();
		createComponents(panel);
	}
	
	/**
	 * A method to default the colors of the nodes, the point under mouse, previous point and twoNodeClick boolean,
	 * to return to the original display condition when changing modes
	 */
	public void defaultVar(ArrayCanvas canvas) {
		// Paint the nodes as green again 
		/*for (int i = 0; i < canvas.colors.size(); i++) {
			canvas.setElementColor(i, Color.GREEN);
		}*/
		// Default these values to so that new modes can begin from scratch
		canvas.repaint();
	}
	
	/** Puts content in the GUI window */
	public void createComponents(JComponent panel) {
		// Array display
		panel.setLayout(new FlowLayout());
		panel1.setLayout(new BorderLayout());
		canvas.addMouseListener(aml);
		canvas.addMouseMotionListener(aml);
		panel1.add(canvas);
		panel1.add(instr, BorderLayout.NORTH);
		
		panel.add(panel1);

		// TODO Controls
		panel2.setLayout(new GridLayout(5,2));	
		
		JButton createArrayButton = new JButton("Create a new customized array");
		panel2.add(createArrayButton);
		createArrayButton.addActionListener(new createArrayListener());

		JButton changeValueButton = new JButton("Change value of an element");
		panel2.add(changeValueButton);
		changeValueButton.addActionListener(new changeValueListener());

		JButton accessButton = new JButton("Access an element by index");
		panel2.add(accessButton);
		accessButton.addActionListener(new accessListener());
		
		JButton findButton = new JButton("Find an element in the array");
		panel2.add(findButton);
		findButton.addActionListener(new findListener());
		
		panel.add(panel2);	
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
	
	/** Constants for recording the input mode */
	enum InputMode {
		CREATE_ARRAY, CHANGE_VALUE, ACCESS, FIND
	}
	
	/** Listener for createArray button */
	private class createArrayListener implements ActionListener {
		/** Event handler for createArray button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.CREATE_ARRAY;			
			instr.setText("Input the size of array and content of array.");
			defaultVar(canvas);
			JFrame frame = new JFrame("User's input of the array");
			// Prompt the user to enter the input the size and data of the array 
			int arrSize = Integer.parseInt(JOptionPane.showInputDialog(frame, "What's the size of this array?"));
			while (arrSize > 10) {
				Toolkit.getDefaultToolkit().beep();
				arrSize = Integer.parseInt(JOptionPane.showInputDialog(frame, 
						  "For demo purpose, array size can't be bigger than 10. Please input the data again!"));
			}
			String arrData = JOptionPane.showInputDialog(frame, "List the elements of array separated by whitespace");
			String[] elements = arrData.split(" ");
			while (elements.length != arrSize) {
				Toolkit.getDefaultToolkit().beep();
				arrData = JOptionPane.showInputDialog(frame, "Please input the data again!");
				elements = arrData.split(" ");
			}
			canvas.arr = new int[arrSize];
			for (int i = 0; i < elements.length; i++) {
				canvas.arr[i] = Integer.parseInt(elements[i]);
			}
			canvas.repaint();
		}
	}

	/** Listener for changeValue button */ 
	private class changeValueListener implements ActionListener {
		/** Event handler for changeValue button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.CHANGE_VALUE;
			instr.setText("Click an array element to change its value.");
			defaultVar(canvas);			
		}
	}
	
	/** Listener for access button */
	private class accessListener implements ActionListener {
		/** Event handler for access button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.ACCESS;
			instr.setText("Click an element to access it.");
			defaultVar(canvas);
			/*JFrame frame = new JFrame("User's input of element index");
			// Prompt the user to enter the index of the element they want to access
			int index = Integer.parseInt(JOptionPane.showInputDialog(frame, "What's the index of the element?"));
			canvas.colors.set(index, Color.CYAN);
			canvas.repaint();*/			
		}
	}

	/** Listener for find button */
	private class findListener implements ActionListener {
		/** Event handler for find button */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.FIND;
			instr.setText("Click an element to open the search dialog.");
			defaultVar(canvas);
			// Prompt the user to enter the value they want to find in the array
			/*int value = Integer.parseInt(JOptionPane.showInputDialog(frame, "What's the value you are looking for?"));
			int index = Arrays.binarySearch(canvas.arr, value);
			canvas.colors.set(index, Color.CYAN);
			canvas.repaint();*/
		}
	}

	/** Mouse listener for ArrayCanvas element */
	private class ArrayMouseListener extends MouseAdapter
	implements MouseMotionListener {

		/** Responds to click event depending on mode */
		public void mouseClicked(MouseEvent e) {
			switch (mode) {
			case CREATE_ARRAY:
				break;
			case CHANGE_VALUE:
				Point accClick2 = new Point((int) e.getX(), (int) e.getY());
				int[] mockArr = canvas.getArr();
				int arrLen = canvas.getArr().length;
				int itemClicked = 10; // if itemClicked stays at 10, no hitbox clicked
				for (int i = 0; i < arrLen; i++){
					int y1 = 26+(60*i);
					int y2 = 76+(60*i);
					if (zoneClicked(22,422,y1,y2,accClick2)) {itemClicked = i;}
				} if(itemClicked < canvas.getArr().length){
					JFrame addQuestion = new JFrame("Add an entry");
					String init = JOptionPane.showInputDialog(addQuestion, "What integer should go in arr["+itemClicked+"]?");
					int i = Integer.valueOf(init);
					mockArr[itemClicked] = i;
					canvas.setArr(mockArr);
					System.out.println(Arrays.toString(canvas.getArr()));
				} 
				
				canvas.repaint();
				break;		
			case ACCESS:
				Point accClick = new Point((int) e.getX(), (int) e.getY());
				
				int arrLen2 = canvas.getArr().length;
				int itemClicked2 = 10; // if itemClicked stays at 10, no hitbox clicked
				for (int i = 0; i < arrLen2; i++){
					int y1 = 26+(60*i);
					int y2 = 76+(60*i);
					if (zoneClicked(22,422,y1,y2,accClick)) {itemClicked2 = i;}
				}
				instr.setText("The item, if accessed, is in cyan.");
				if(itemClicked2 < canvas.getArr().length){
					canvas.arrAccess(itemClicked2);
				} else {
					instr.setText("Called element out of bounds.");
				}
			
				break;		
			case FIND:
				JFrame addQuery = new JFrame("Add an entry");
				String insertPlace = JOptionPane.showInputDialog(addQuery, "What integer are you looking for?");
				int index = Integer.valueOf(insertPlace);
				canvas.arrSearch(index);
				break;
		}
	}
	// Empty but necessary to comply with MouseMotionListener interface
	public void mouseMoved(MouseEvent e) {}
	}
} // end of ArrayPanel