import java.util.*;
import java.util.Timer;
import java.awt.*;
import javax.swing.*;  
/**
 *  Implement a graphical canvas that displays an array
 *  elements demonstrated by rectangles
 *
 *  @author  Ha Cao (modded by Sarah Abowitz)
 *  @version May 7th, 2018
 */

public class ArrayCanvas extends JComponent {	
	private static final long serialVersionUID = 1L;
	/** The array */
	protected int[] arr;
	/** The annotations that show the indices and contents of the elements */
	protected ArrayList<String> annotations;
	boolean addingToArr = false;
	private int access = 8;
	
	/** Constructor */
	public ArrayCanvas() {
		arr = new int[8];
		annotations = new ArrayList<String>();
		setMinimumSize(new Dimension(500, 700));
		setPreferredSize(new Dimension(500, 700));
	}
	
	/**
	 * A method to set the array to a given array
	 * @param array the given array that user wants to display
	 */
	public void setArr(int[] array){
    	for(int i = 0; i < array.length; i++){
    		arr[i] = array[i];
    	}
    }
    
	/**
	 * A method to get the array
	 * @return arr the array on the canvas
	 */
    public int[] getArr(){
    	return arr;
    }
    
    /**
     * A method to search an element in the array
     * @param match the value that user wants to look for in the array
     */
    public void arrSearch(int match){
    	// While this thing isn't found and we still have array in front of us
    	ArrayCanvas.this.repaint();
    	int j = 0;
    	Timer timer = new Timer();
    	long delay = 1500;
    	for (j = 0; j <= arr.length; j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override
    			public void run(){
    				try {
	    				if (i > arr.length){
	    					annotations.add("");
	    				} else if (arr[i] == match){
	    					annotations.add("Query found!");
	    					// break;
	    				} else {
	    					annotations.add("Still searching ...");
	    				}
	    				ArrayCanvas.this.repaint();
    				} catch (Throwable t) {
    					t.printStackTrace(System.err);
    				}
    			}
    		}, delay*(j+1));  
    		ArrayCanvas.this.repaint();
    		if (arr[j] == match){
    			break;
    		}
    	}
    	annotations.clear();
    }
    
    public void arrAccess(int index){
    	access = index;
    	ArrayCanvas.this.repaint();
    }    
    
	/**
	 *  Paint a blue rectangle with width of 200 pixels and height of 50 pixels,
	 *  each rectangle representing an element in the array
	 *  @param g The graphics object to draw with
	 */
	public void paintComponent(Graphics g) {
			Color c = Color.BLUE;
			for (int i = 0; i < arr.length; i++){
				// Change the color to RED if the element is what user is looking for
				if (i == access){
					c = Color.RED;
				} else {
					c = Color.BLUE;
				}
				g.setColor(c);
				g.fillRect(22, 26+(60*i), 200, 50);
				g.setColor(Color.WHITE);
				g.drawString("array[" + i + "] = " + arr[i], 30, 50+(60*i));
				// Draw an annotation only if there is an annotation for element i
				if (annotations.size() > i){
					g.drawString(annotations.get(i), 100, 50+(60*i));					
				}
			}			
			access = 8;		
	}
} // end of ArrayCanvas