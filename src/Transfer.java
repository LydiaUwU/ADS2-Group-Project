//THIS CLASS IS ALSO USED WITH stop_times.txt, NOT JUST FOR TRANSFERS!

/**
 * @Authors: David King
 */
public class Transfer {
    /* listed as from_stop_id,to_stop_id,transfer_type,min_transfer_time at the start of transfers.txt,
       i have adapted the variable names to fit standard java practice. also, time is defined (as 0) even when
       transfer type is 0 */
    int fromStopId, toStopId, transferType, minTransferTime, tripId;
    String arrivalTime;

    /**
     * @param from: The source stop of the transfer.
     * @param to: The destination stop of the transfer.
     * @param type: The type of the transfer.
     * @param time: The minimum time taken for the transfer.
     * @param id: The trip id of the transfer.
     * @param arrTime: The arrival time of the transfer.
     * @Authors: David King
     */

    Transfer(int from, int to, int type, int time, int id, String arrTime) {
        fromStopId = from;
        toStopId = to;
        transferType = type;
        minTransferTime = time;
        tripId = id;
        arrivalTime = arrTime;
    }

    /**
     * Compares the source stopIds of the 2 nodes.
     *
     * @param o: The node to compare to.
     * @return int: The result of the comparison.
     * @Authors: David King
     */
    public int compareFrom(Transfer o) {
        return this.fromStopId - o.fromStopId;
    }

    /**
     * Compares the destination stopIds of the 2 nodes.
     *
     * @param o: The node to compare to.
     * @return int: The result of the comparison
     * @Authors: David King
     */
    public int compareTo(Transfer o) {
        return this.toStopId - o.toStopId;
    }

    /**
     * Compares the tripIds of the 2 nodes.
     *
     * @param o: The node to compare to.
     * @return int: The result of the comparison
     * @Authors: David King
     */
    public int compareId(Transfer o) {
        return this.tripId - o.tripId;
    }

    /**
     * Generates a string representation of the transfer.
     *
     * @return String: The string representation of the transfer.
     * @Authors: David King
     */

    public String toString() {
        return "(" + this.fromStopId + "," + this.toStopId + "," + this.transferType + "," + this.minTransferTime + ")";
    }
}