/* MULTITHREADING Theater.java
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Frank Le
 * fpl227
 * 16185
 * Slip days used: <0>
 * Fall 2019
 */
package assignment6;

import java.util.ArrayList;
import java.util.List;

public class Theater {

    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms.  Use it in your Thread.sleep()

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Represents a seat in the theater
     * A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
    }

    /**
     * Represents a ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;


        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol +
                    showLine + eol +
                    boxLine + eol +
                    seatLine + eol +
                    clientLine + eol +
                    dashLine;

            return result;
        }
    }

    private int numRows;
    private int seatsPerRow;
    private int bestSeat;
    private String show;
    private ArrayList<Ticket> transactionLog;
    private boolean soldOut;
    
    public Theater(int numRows, int seatsPerRow, String show) {
        this.numRows = numRows;
        this.seatsPerRow = seatsPerRow;
        this.bestSeat = 0;
        this.show = show;
        this.transactionLog = new ArrayList<Ticket>();
        this.soldOut = false;
    }

    /**
     * Calculates the best seat not yet reserved
     *
     * @return the best seat or null if theater is full
     */
    public Seat bestAvailableSeat() {
    	synchronized(this) {
    		Seat seat = new Seat(bestSeat / seatsPerRow, (bestSeat % seatsPerRow) + 1);
        	if(bestSeat == numRows*seatsPerRow) {
    			return null;
    		} else {
    			bestSeat++;
    			return seat;
    		}
    	}
    }

    /**
     * Prints a ticket for the client after they reserve a seat
     * Also prints the ticket to the console
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        
    	if (seat == null) {
        	if(!soldOut) {
        		System.out.println("Sorry, we are sold out!");
        	}
        	soldOut = true;
        	return null;
        }
        
        Ticket ticket = new Ticket(show, boxOfficeId, seat, client);
        transactionLog.add(ticket);
        System.out.println(ticket);
        try {
			Thread.sleep(printDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return ticket;  
    }

    /**
     * Lists all tickets sold for this theater in order of purchase
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return this.transactionLog;
    }
}
