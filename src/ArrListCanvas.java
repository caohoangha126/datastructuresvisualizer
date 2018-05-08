import javax.swing.JComponent;
import java.util.*;
import java.util.Timer;
import java.awt.*;

/**
 *  A class to implement a graphical canvas that displays an ArrayList
 *  elements demonstrated by rectangles
 *
 *  @author  Ha Cao Sarah Abowitz
 *  @version May 7th, 2018
 */

public class ArrListCanvas extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> arrList = new ArrayList<Integer>();
	private ArrayList<String> aLAnnotations = new ArrayList<String>();
	boolean addingToAL = false;
	private int access = 10;
	
	/** Constructor */
	public ArrListCanvas() {
		setMinimumSize(new Dimension(500,700));
		setPreferredSize(new Dimension(500,700));
	}
    
    public void setArrList(ArrayList<Integer> aL){
    	arrList = aL;
    }
    
    public ArrayList<Integer> getArrList(){
    	return arrList;
    }
    
    public void arrListSearch(int match){
    	// while this thing isn't found or we still have array in front of  us
    	ArrListCanvas.this.repaint();
    	int j = 0;
    	Timer timer = new Timer();
    	long delay = 2500;
    	//System.err.println("Stuff?" + Arrays.toString(arr));
    	for (j = 0; j <= arrList.size(); j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override
    			public void run(){
    				try {
	    				if (i > arrList.size()){
	    					aLAnnotations.add("");
	    				} else if (arrList.get(i) == match){
	    					aLAnnotations.add("Query found!");
	    					// break;
	    				} else {
	    					aLAnnotations.add("Still searching...");
	    				}
	    				ArrListCanvas.this.repaint();
    				} catch (Throwable t) {
    					t.printStackTrace(System.err);
    				}
    			}
    		}, delay*(j+1));  
    		//System.out.println(Arrays.toString(arr));
    		ArrListCanvas.this.repaint();
    		if (arrList.get(j) == match){
    			break;
    		}
    	}
    	aLAnnotations.clear();
    	//System.out.println(Arrays.toString(arr));
    }
    
    public void arrAccess(int index){
    	access = index;
    	ArrListCanvas.this.repaint();
    }
	
    public void arrListAddition(int num){
    	addingToAL = true;
    	Timer timer = new Timer();
    	int bound = arrList.size();
    	int j;
    	for (j = 0; j <= arrList.size(); j++){
    		timer.schedule(new TimerTask(){
    			@Override 
    			public void run(){
	    	    	aLAnnotations.add("Remains the same");
    	    		ArrListCanvas.this.repaint();
    			}
    			}, (2500*j));
    		
    	}
    	final int k = j+1;
    	timer.schedule(new TimerTask(){
    		@Override
    		public void run(){
    		try{
    				aLAnnotations.add("arrayList.get("+k+") = "+num);
    		} catch (ArrayIndexOutOfBoundsException a){
    			a.printStackTrace();
    		}
    		ArrListCanvas.this.repaint();
    		
    		arrList.add(num);
        	
        	aLAnnotations.clear();
        	ArrListCanvas.this.repaint();
        	
        	}}, 2500*bound);
    }
    
    public void arrListRemoval(int index){
    	addingToAL=false;
    	Timer timer = new Timer();
    	int bound = arrList.size();
    	for (int j = 0; j < arrList.size(); j++){
    		final int i = j;
    		timer.schedule(new TimerTask(){
    			@Override 
    			public void run(){
    				if (i >= index && i < arrList.size()-1){
    	    			aLAnnotations.add("arrList.get("+i+") = "+arrList.get(i+1));
    	    		} else if (i < arrList.size()-1){
    	    			aLAnnotations.add("Remains the same");
    	    		} else {
    	    			aLAnnotations.add("ArrayList shrinks past this");
    	    		}
    				ArrListCanvas.this.repaint();
    			}
    			}, (2500*j));
    		
    	}
    	
    	timer.schedule(new TimerTask(){
    		@Override
    		public void run(){
    		arrList.remove(arrList.get(index));
        	
        	aLAnnotations.clear();
        	ArrListCanvas.this.repaint();
        	
        	}}, 2500*bound);
    }
    
    public void paintComponent(Graphics g) {
		Color c = Color.GREEN;
		for (int i=0; i < arrList.size(); i++){
			if (i == access){
				c = Color.CYAN;
			} else {
				c = Color.GREEN;
			}
			// System.out.println("i: "+i+" access: "+access);
			g.setColor(c);
			g.fillRect(22, 26+(60*i), 400, 50);
			g.setColor(Color.BLACK);
			g.drawString("arrList.get("+i+") = "+arrList.get(i), 30, 50+(60*i));
			if (aLAnnotations.size()>i && aLAnnotations.size()>0){
				g.drawString(aLAnnotations.get(i), 170, 50+(60*i));
				
			}
			if (addingToAL && arrList.size() < aLAnnotations.size()){
				g.drawString(aLAnnotations.get(aLAnnotations.size()-1), 170, 50+(60*arrList.size()));
			}
		}
		
		access = 10;
	
    }
} // end of ArrListCanvas