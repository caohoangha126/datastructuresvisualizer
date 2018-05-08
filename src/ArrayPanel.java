import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  A class to implement a graphical panel to display array
 *  
 *  @author Ha Cao, Sarah Abowitz
 *  @version May 7th, 2018
 */

public class ArrayPanel {	
	/** The array to be displayed */
	private static ArrayCanvas canvas;

	/** Label for the input mode instructions */
	private JLabel instr;

	/** The input mode */
	private InputMode mode = InputMode.ACCESS;

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
		instr = new JLabel("Click the element that you want to access. It will change to red.");
		panel2 = new JPanel();
		createComponents(panel);
	}
	
	/**
	 * A method to default the display of the canvas
	 * to return to the original display condition when changing modes
	 */
	public void defaultVar(ArrayCanvas canvas) {		
		canvas.annotations.clear();
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

		// Controls
		panel2.setLayout(new GridLayout(5, 2));	
		
		JButton createArrayButton = new JButton("Create a customized array");
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
		
		JButton clearButton = new JButton("Clear Previous Content");
		panel2.add(clearButton);
		clearButton.addActionListener(new ClearListener());	
		
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
		CREATE_ARRAY, CHANGE_VALUE, ACCESS
	}
	
	/**
	 * A method to test if an array input is in correct format
	 */
	public static boolean isInRightFormat(String arrData) {
		boolean isCorrect = true;
		for (int i = 0; i < arrData.length(); i++) {
			if ((arrData.charAt(i) < '0' || arrData.charAt(i) > '9') && (arrData.charAt(i) != ' ')) {
				isCorrect = false;
				break;
			}
		}
		return isCorrect;
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
			String arrSizeString = JOptionPane.showInputDialog(frame, "What's the size of this array?");
			if (arrSizeString != null) {
				try {
					int arrSize = Integer.parseInt(arrSizeString);
					while (arrSize > 10) {
						Toolkit.getDefaultToolkit().beep();
						arrSize = Integer.parseInt(JOptionPane.showInputDialog(frame, 
								  "For demo purpose, array size can't be bigger than 10. Please input the data again!"));
					}
					String arrData = JOptionPane.showInputDialog(frame, "List the elements of array separated by whitespace.");
					while (!isInRightFormat(arrData)) {
						arrData = JOptionPane.showInputDialog(frame, "Elements of array need to be separated by whitespace. Please re-try.");
					}
					String[] elements = arrData.split(" ");
					while (elements.length != arrSize) {
						Toolkit.getDefaultToolkit().beep();
						arrData = JOptionPane.showInputDialog(frame, "You didn't input the same number of elements as the assigned array size. Please input the data again!");
						elements = arrData.split(" ");
					}
					canvas.arr = new int[arrSize];
					for (int i = 0; i < elements.length; i++) {
						canvas.arr[i] = Integer.parseInt(elements[i]);
					}
					canvas.repaint();
				} catch(NumberFormatException error) {	
					JFrame frame2 = new JFrame("");
					// Warning
					JOptionPane.showMessageDialog(frame2,
							"Input size is not a valid number",
							"Input Warning",
							JOptionPane.WARNING_MESSAGE);
				} 
			}
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
		}
	}

	/** Listener for find button */
	private class findListener implements ActionListener {
		/** Event handler for find button */
		public void actionPerformed(ActionEvent e) {
			instr.setText("Click anywhere on the canvas to open the search dialog.");
			defaultVar(canvas);
			JFrame addQuery = new JFrame("Find a value.");
			String insertPlace = JOptionPane.showInputDialog(addQuery, "What integer are you looking for?");
			if (insertPlace != null) {
				try {
					int index = Integer.valueOf(insertPlace);
					canvas.arrSearch(index);
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
	
	/** Listener for Clear button */
	private class ClearListener implements ActionListener {
		/** Event handler for Clear button */
		public void actionPerformed(ActionEvent e) {
			instr.setText("Previous content is removed.");
			canvas.arr = new int[8];
			canvas.annotations.clear();
			canvas.repaint();
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
					if (zoneClicked(22, 222, y1, y2, accClick2)) itemClicked = i;
				} if(itemClicked < canvas.getArr().length){
					JFrame addQuestion = new JFrame("Change an element");
					String init = JOptionPane.showInputDialog(addQuestion, "What integer should go in arr[" + itemClicked + "]?");
					if (init != null) {
						try {
							int i = Integer.valueOf(init);
							mockArr[itemClicked] = i;
							canvas.setArr(mockArr);
						} catch(NumberFormatException error) {
							JFrame frame2 = new JFrame("");
							// Warning
							JOptionPane.showMessageDialog(frame2,
									"Input value is not a valid number",
									"Input Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					}
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
					if (zoneClicked(22, 222, y1, y2, accClick)) itemClicked2 = i;
				}
				instr.setText("The item, if accessed, is in red.");
				if(itemClicked2 < canvas.getArr().length){
					canvas.arrAccess(itemClicked2);
				} else {
					instr.setText("Failed click on elements. Click again.");
				}			
				break;			
		}
	}
	// Empty but necessary to comply with MouseMotionListener interface
	public void mouseMoved(MouseEvent e) {}
	}
} // end of ArrayPanel