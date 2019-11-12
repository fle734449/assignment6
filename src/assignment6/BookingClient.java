/* MULTITHREADING BookingClient.java
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Frank Le
 * fpl227
 * 16185
 * Slip days used: <0>
 * Fall 2019
 */
package assignment6;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Thread;

public class BookingClient {
	
	private Theater theater;
	private Map<String, Integer> office;
	
    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater theater) {
        this.theater = theater;
        this.office = office;
    }
    	/**
    	 * 
    	 * Inner class of BookingClient used to run threads/BoxOffice IDs
    	 *
    	 */
    	public class BookClientThread implements Runnable {
    		private String id;
    		private int numClients;
    		private int client;
    		
    		/**
    		 * 
    		 * @param id is the BoxOffice ID
    		 * @param numClients is the number of clients in line
    		 * @param client is the current client being processed in line
    		 */
    		public BookClientThread(String id, int numClients, int client) {
    			this.id = id;
    			this.numClients = numClients + client;
    			this.client = client;
    		}
    	
    		/**
    		 * Runs the threads
    		 */
    		@Override
    		public void run() {
    			Theater.Ticket ticket = null; 
    			Theater.Seat seat = null;
    			while (client < numClients) {	
    				synchronized(theater.getTransactionLog()) {
    					seat = theater.bestAvailableSeat();
    					ticket = theater.printTicket(id, seat, client);		
    				}	
    				try {
    					Thread.sleep(theater.getPrintDelay());
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    				client++;
    			}
    		}
    	}
    	
    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
        ArrayList<Thread> t = new ArrayList<Thread>();
        int client = 1;
        for(Map.Entry<String, Integer> e : office.entrySet()) {
        	BookClientThread b = new BookClientThread(e.getKey(), e.getValue(), client);
        	client += e.getValue();
        	Thread thread = new Thread(b);
        	t.add(thread);
        }
        for (Thread thread : t) {
        	thread.start();
        }
        
        return t;
    }

    /**
     * Initialize Booking CLient with parameters
     * 
     */
    public static void main(String[] args) {
        Map<String, Integer> boxOffice = new HashMap<String, Integer>();
        boxOffice.put("BX1", 3);
        boxOffice.put("BX3", 3);
        boxOffice.put("BX2", 4);
        boxOffice.put("BX5", 3);
        boxOffice.put("BX4", 3);
        
        Theater cinemark = new Theater(3, 5, "Ouija");
        BookingClient fandango = new BookingClient(boxOffice, cinemark);
        fandango.simulate();
    }
}
