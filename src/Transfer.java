public class Transfer
{
    int fromStopId, toStopId, transferType, minTransferTime;

    Transfer (int from, int to, int type, int time)
    {
        fromStopId = from;
        toStopId = to;
        transferType = type;
        minTransferTime = time;
    }
}