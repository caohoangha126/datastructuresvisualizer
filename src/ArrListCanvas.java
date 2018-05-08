import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.*;
import java.awt.*;

/**
 *  A class to implement a graphical canvas that displays an ArrayList
 *  elements demonstrated by rectangles
 *
 *  @author  Ha Cao Sarah Abowitz
 *  @version May 7th, 2018
 */

public class ArrListCanvas extends JComponent{
	private static final long serialVersionUID = 1L;
	/** The ArrayList */
	protected ArrayList<Integer> arrList = new ArrayList<Integer>();
	/** The annotations for ArrayList*/
	protected ArrayList<String> aLAnnotations = new ArrayList<String>();
	boolean addingToAL = false;
	private int access = 10;
	
	/** Constructor */
	public ArrListCanvas() {
		setMinimumSize(new Dimension(500, 700));
		setPreferredSize(new Dimension(500, 700));
	}
    
	/** A method to set the values of the ArrayList to a given ArrayList*/
    public void setArrList(ArrayList<Integer> aL){
    	arrList = aL;
    }
    
    /** A method that returns the ArrayList */
    public ArrayList<Integer> getArrList(){
    	return arrList;
    }
    
    /**
     * A method to search an element in the ArrayList
     * @param match the value that user wants to look for in the ArrayList
     */
    public void arrListSearch(int match){
    	// While this thing isn't found and we still have array in front of  us
    	ArrListCanvas.this.repaint();
    	int j = 0;
    	Timer timer = new Timer();
    	long delay = 1500;
    	boolean found = false; 
    	for (j = 0; j < arrList.size(); j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override
    			public void run(){
    				try {
	    				if (i > arrList.size()){
	    					aLAnnotations.add("");
	    				} else if (arrList.get(i) == match){
	    					aLAnnotations.add("Query found!");
	    				} else {
	    					aLAnnotations.add("Still searching ...");
	    				}
	    				ArrListCanvas.this.repaint();
    				} catch (Throwable t) {
    					t.printStackTrace(System.err);
    				}
    			}
    		}, delay*(j+1));  
    		ArrListCanvas.this.repaint();
    		if (arrList.get(j) == match) {
    			found = true;
    			break;
    		}
    	}
    	aLAnnotations.clear();
    	if (!found) {
    		timer.schedule(new TimerTask(){
    			@Override
    			public void run(){
    				JFrame frame = new JFrame("");
    				// Result
    				JOptionPane.showMessageDialog(frame,
    						"The value doesn't exist.",
    						"No Such Value Found",
    						JOptionPane.INFORMATION_MESSAGE);
    			}
    		}, delay*(arrList.size()+1));   			
    	}
    }
    
    /**
     * A method to access a specified element in the ArrayList
     * @param index The index of the element that user wants to access
     */
    public void arrAccess(int index){
    	access = index;
    	ArrListCanvas.this.repaint();
    }
	
    /**
     * A method to add a new element to the end of the ArrayList
     * @param num The new value to add to the end of the ArrayList
     */
    public void arrListAddition(int num){
    	addingToAL = true;
    	Timer timer = new Timer();
    	int bound = arrList.size();
    	int j;
    	for (j = 0; j < arrList.size(); j++){
    		timer.schedule(new TimerTask(){
    			@Override 
    			public void run(){
	    	    	aLAnnotations.add("Remains the same");
    	    		ArrListCanvas.this.repaint();
    			}
    			}, (1500*j));
    		
    	}
    	final int k = j+1;
    	timer.schedule(new TimerTask(){
    		@Override
    		public void run(){
    		try {
    			aLAnnotations.add("arrayList.get(" + k + ") = " + num);
    		} catch (ArrayIndexOutOfBoundsException a){
    			a.printStackTrace();
    		}
    		ArrListCanvas.this.repaint();
    		
    		arrList.add(num);
        	
        	ArrListCanvas.this.repaint();
        	aLAnnotations.clear();
        	
        	}}, 1500*bound);
    }
    
    /**
     * A method to remove a specified element from the ArrayList
     * @param index The index of the element that user wants to remove
     */
    public void arrListRemoval(int index){
    	addingToAL = false;
    	Timer timer = new Timer();
    	int bound = arrList.size();
    	for (int j = 0; j < arrList.size(); j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override 
    			public void run(){
    				if (i >= index && i < arrList.size()-1){
    	    			aLAnnotations.add("arrList.get(" + i + ") = " + arrList.get(i+1));
    	    		} else if (i < arrList.size()-1){
    	    			aLAnnotations.add("Remains the same");
    	    		} else {
    	    			aLAnnotations.add("ArrayList shrinks past this");
    	    		}
    				ArrListCanvas.this.repaint();
    			}
    		}, (1500*j));
    		
    	}
    	
    	timer.schedule(new TimerTask(){
    		@Override
    		public void run(){
    		arrList.remove(arrList.get(index));
        	
        	aLAnnotations.clear();
        	ArrListCanvas.this.repaint();
        	
        	}}, 1500*bound);
    }
    
    /**
	 *  Paint a blue rectangle with width of 200 pixels and height of 50 pixels,
	 *  each rectangle representing an element in the ArrayList
	 *  @param g The graphics object to draw with
	 */
    public void paintComponent(Graphics g) {
		Color c = Color.BLUE;
		for (int i = 0; i < arrList.size(); i++){
			if (i == access){
				c = Color.RED;
			} else {
				c = Color.BLUE;
			}
			g.setColor(c);
			g.fillRect(22, 26+(60*i), 350, 50);
			g.setColor(Color.WHITE);
			g.drawString("arrList.get(" + i + ") = " + arrList.get(i), 30, 50+(60*i));
			if (aLAnnotations.size()>i && aLAnnotations.size()>0){
				g.drawString(aLAnnotations.get(i), 150, 50+(60*i));				
			}
			if (addingToAL && arrList.size() < aLAnnotations.size()){
				g.drawString(aLAnnotations.get(aLAnnotations.size()-1), 150, 50+(60*arrList.size()));
			}
		}		
		access = 10;	
    }
} // end of ArrListCanvas