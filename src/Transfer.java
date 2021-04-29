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

    Transfer (int from, int to, int type, int time, int id, String arrTime) {
        fromStopId = from;
        toStopId = to;
        transferType = type;
        minTransferTime = time;
        tripId = id;
        arrivalTime = arrTime;
    }

    // Compares fromStopId for 2 Transfers, used to sort transfers ArrayList so they can be added to the graph quicker
    public int compareFrom(Transfer o) {
        return this.fromStopId - o.fromStopId;
    }

    // Compares toStopId for 2 Transfers, used to sort transfers ArrayList so they can be added to the graph quicker
    public int compareTo(Transfer o) { return this.toStopId - o.toStopId; }

    public int compareId(Transfer o) { return this.tripId - o.tripId; }

    public String toString() {
        return "(" + this.fromStopId + "," + this.toStopId + "," + this.transferType + "," + this.minTransferTime + ")";
    }
}