import java.util.*;

public class TST {
    private TSTNode root = new TSTNode();

    private static class TSTNode {
        private char value;
        private boolean terminal;
        private TSTNode[] next = new TSTNode[3];

        private String stopId;
        private String stopCode;
        //stop name is already defined by the TST
        private String stopDesc;
        private String stopLat;
        private String stopLon;
        private String zoneId;
        private String stopUrl;
        private String locationType;
        private String parentStation;
    }

    public void put(String key, ArrayList<String> stopInfo) {
        //System.out.println("\n~~~~~~~~~~~~~~~Putting "+key+" starting from root~~~~~~~~~~~~~~~");
        root = put(root, key, 0, stopInfo);
    }

    private TSTNode put(TSTNode x, String key, int d, ArrayList<String> stopInfo) {
        //System.out.println("-----Putting "+key+" starting from "+d+"-----");
        char c = key.charAt(d);

        //this bit updates the TST if a previously added word is added
        if ((x!=null)&&(x.value == c)&&(d == key.length()-1))
        {
            //System.out.println("Key "+key+" already in TST, current character is"+key.charAt(d));
            return x;
        }

        else if ((x == null||(int) x.value == 0)&&(d == key.length()-1)) {
            //System.out.println("Putting "+key+", at end, adding "+key.charAt(d));
            x = new TSTNode();
            x.value = key.charAt(d);
            x.terminal = true;

            x.stopDesc = ((stopInfo.get(0).equals(" "))?"N/A":stopInfo.get(0));
            x.stopId = ((stopInfo.get(1).equals(" "))?"N/A":stopInfo.get(1));
            x.stopCode = ((stopInfo.get(2).equals(" "))?"N/A":stopInfo.get(2));
            x.stopLat = ((stopInfo.get(3).equals(" "))?"N/A":stopInfo.get(3));
            x.stopLon = ((stopInfo.get(4).equals(" "))?"N/A":stopInfo.get(4));
            x.zoneId = ((stopInfo.get(5).equals(" "))?"N/A":stopInfo.get(5));
            x.stopUrl = ((stopInfo.get(6).equals(" "))?"N/A":stopInfo.get(6));
            x.locationType = ((stopInfo.get(7).equals(" "))?"N/A":stopInfo.get(7));
            x.parentStation = ((stopInfo.get(8).equals(" "))?"N/A":stopInfo.get(8));
            return x;
        }
        if (x == null||(int) x.value == 0)
        {
            //System.out.println("Putting "+key+", at "+d+", making new node, then heading to middle child (key.length() = "+key.length()+", adding "+key.charAt(d)+")");
            x = new TSTNode();
            x.terminal = false;
            x.value = key.charAt(d);
            x.next[1] = put(x.next[1], key, d + 1, stopInfo);
        }

        else if (x.value > c)
        {
            //System.out.println("Putting "+key+", at "+d+", taking left branch (x.value = "+x.value+", c = "+c+")");
            x.next[0] = put(x.next[0], key, d, stopInfo);
        }
        else if (x.value < c)
        {
            //System.out.println("Putting "+key+", at "+d+", taking right branch (x.value = "+x.value+", c = "+c+")");
            x.next[2] = put(x.next[2], key, d, stopInfo);
        }
        else
        {
            //System.out.println("Putting "+key+", at "+d+", taking existing centre branch (x.value = "+x.value+", c = "+c+")");
            x.next[1] = put(x.next[1], key, d+1, stopInfo);
        }
        return x;
    }

    public boolean contains(String key) {
        return get(key);
    }

    public boolean get(String key) {
        //System.out.println("Getting "+key+", starting from root");
        TSTNode x = get(root,key,0);

        return (x != null);
    }

    private TSTNode get(TSTNode x, String key, int d) {
        //System.out.println("----------");
        if (x == null) {
            System.out.println("Getting "+key+", not found");
            return null;
        }

        if (d == key.length()-1&&x.terminal) {
            System.out.println("Getting "+key+", found");
            return x;
        }

        char c = key.charAt(d);

        //System.out.println("x.value = "+x.value);

        if (x.value > c)
        {
            //System.out.println("Getting "+key+", at "+d+", taking left branch (x.value = "+x.value+", c = "+c+")");
            return get(x.next[0], key,d);
        }
        else if (x.value < c)
        {
            //System.out.println("Getting "+key+", at "+d+", taking right branch (x.value = "+x.value+", c = "+c+")");
            return get(x.next[2], key,d);
        }
        else
        {
            /*System.out.println("Getting "+key+", at "+d+", taking existing centre branch, x.value = "
            /        +x.value+", x.terminal = "+x.terminal+", c = "+c+")");*/
            return get(x.next[1], key, d + 1);
        }
    }

    public TSTNode getNodeFromKey(String key) {
        //System.out.println("Getting "+key+", starting from root");
        TSTNode x = getNodeFromKey(root,key,0);

        return (x);
    }

    private TSTNode getNodeFromKey(TSTNode x, String key, int d) {
        //System.out.println("----------");
        if (x == null) {
            System.out.println("No stops found with that name.");
            return null;
        }
        char c = key.charAt(d);

        if (d == key.length()-1&&x.value==c) {
            //System.out.println("Getting "+key+", found, x.value = " + x.value);
            return x;
        }
        /*else if (d == key.length()-1) {
            System.out.println("Getting "+key+", not found, x.value = "+x.value);
            return null;
        }*/

        //System.out.println("key = "+key+", d = "+d);

        //System.out.println("x.value = "+x.value);

        if (x.value > c)
        {
            //System.out.println("Getting "+key+", at "+d+", taking left branch (x.value = "+x.value+", c = "+c+")");
            return getNodeFromKey(x.next[0], key,d);
        }
        else if (x.value < c)
        {
            //System.out.println("Getting "+key+", at "+d+", taking right branch (x.value = "+x.value+", c = "+c+")");
            return getNodeFromKey(x.next[2], key,d);
        }
        else
        {
            /*System.out.println("Getting "+key+", at "+d+", taking existing centre branch, x.value = "
                    +x.value+", x.terminal = "+x.terminal+", c = "+c+")");*/
            return getNodeFromKey(x.next[1], key, d + 1);
        }
    }

    public ArrayList<String> getSubtree() {
        ArrayList<String> subtree = new ArrayList<String>();
        subtree.addAll(getSubtree(root, "", true));
        return subtree;
    }
    public ArrayList<String> getSubtree(TSTNode x, String currentPath, boolean isStart) {
        ArrayList<String> subtree = new ArrayList<String>();
        if (x==null) return null;
        String newPath = currentPath;
        if (x.terminal)
        {
            //newPath = currentPath + x.value;
            subtree.add(newPath);

            System.out.println("Stop: " + newPath);
            getStopInfo(x);
            //System.out.println("adding current path "+newPath);
        }

        //System.out.println("current x.value = "+x.value+", newPath = "+newPath+" isStart = "+isStart);

        for (int i=0;i<3;i++)
        {
            try
            {
                if ((i==1)&&(!isStart)) newPath = currentPath + x.value;
                else newPath = currentPath;

                if (!isStart) subtree.addAll(getSubtree(x.next[i], newPath, false));
                else subtree.addAll(getSubtree(x.next[1], newPath, false));
            }
            catch (NullPointerException e)
            {

            }
        }
        return subtree;
    }

    public void getStopInfo(TSTNode x)
    {
        if (x!=null&&x.terminal)
        {
            System.out.println("\tDescription: " + x.stopDesc +
                    "\n\tID: " + x.stopId +
                    "\n\tCode: " + x.stopCode +
                    "\n\tCo-ordinates: " + x.stopLat + ", " + x.stopLon +
                    "\n\tZone id: " + x.zoneId +
                    "\n\tURL: " + x.stopUrl +
                    "\n\tLocation type: " + x.locationType +
                    "\n\tParent station: " + x.parentStation + "\n--------------------"
            );
        }
    }


}
