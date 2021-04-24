//THIS CLASS IS ALSO USED WITH stop_times.txt, NOT JUST FOR TRANSFERS!
public class Transfer
{
    /* listed as from_stop_id,to_stop_id,transfer_type,min_transfer_time at the start of transfers.txt,
       i have adapted the variable names to fit standard java practice. also, time is defined (as 0) even when
       transfer type is 0 */
    int fromStopId, toStopId, transferType, minTransferTime;

    Transfer (int from, int to, int type, int time)
    {
        fromStopId = from;
        toStopId = to;
        transferType = type;
        minTransferTime = time;
    }

    //compares fromStopId for 2 Transfers, used to sort transfers ArrayList so they can be added to the graph quicker
    public int compareFrom(Transfer o) {
        return this.fromStopId - o.fromStopId;
    }
    //compares toStopId for 2 Transfers, used to sort transfers ArrayList so they can be added to the graph quicker
    public int compareTo(Transfer o) { return this.toStopId - o.toStopId; }

    public String toString()
    {
        return "("+this.fromStopId+","+this.toStopId+","+this.transferType+","+this.minTransferTime+")";
    }
}