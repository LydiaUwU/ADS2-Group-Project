import java.util.*;

/**
 * Class for handling the Ternary Search Tree of stop names.
 *
 * @Authors: David King
 */

public class TST {
    private TSTNode root = new TSTNode();

    /**
     * @param value: The letter represented by the TSTNode.
     * @param terminal: Whether the TSTNode represents the end of a station name.
     * @param next: The children of the current TSTNode.
     * @param stopId: The stop id of the TSTNode.
     * @param stopCode: The stop code of the TSTNode.
     * @param stopDesc: The stop description of the TSTNode.
     * @param stopLat: The stop latitude of the TSTNode.
     * @param stopLon:  The stop longitude of the TSTNode.
     * @param stopUrl:  The stop URL of the TSTNode.
     * @param locationType:  The location type of the TSTNode.
     * @param parentStation:  The parent station of the TSTNode.
     * @Authors: David King
     */

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

    /**
     * Adds the given stop to the TST, starting from the root.
     *
     * @param key: The name of the stop to input.
     * @param stopInfo: Information about the stop to input.
     * @Authors: David King
     */
    public void put(String key, ArrayList<String> stopInfo) {
        root = put(root, key, 0, stopInfo);
    }

    /**
     * Adds the given stop to the TST, starting from a given TSTNode.
     *
     *  @param x: The node to input.
     * @param key: The name of the stop to input.
     * @param d: The index of the current character being input.
     * @param stopInfo: Information about the stop to input.
     * @Authors: David King
     */
    private TSTNode put(TSTNode x, String key, int d, ArrayList<String> stopInfo) {
        char c = key.charAt(d);

        //this bit updates the TST if a previously added word is added
        if ((x != null) && (x.value == c) && (d == key.length() - 1)) {
            return x;
        } else if ((x == null || (int) x.value == 0) && (d == key.length() - 1)) {
            x = new TSTNode();
            x.value = key.charAt(d);
            x.terminal = true;

            x.stopDesc = ((stopInfo.get(0).equals(" ")) ? "N/A" : stopInfo.get(0));
            x.stopId = ((stopInfo.get(1).equals(" ")) ? "N/A" : stopInfo.get(1));
            x.stopCode = ((stopInfo.get(2).equals(" ")) ? "N/A" : stopInfo.get(2));
            x.stopLat = ((stopInfo.get(3).equals(" ")) ? "N/A" : stopInfo.get(3));
            x.stopLon = ((stopInfo.get(4).equals(" ")) ? "N/A" : stopInfo.get(4));
            x.zoneId = ((stopInfo.get(5).equals(" ")) ? "N/A" : stopInfo.get(5));
            x.stopUrl = ((stopInfo.get(6).equals(" ")) ? "N/A" : stopInfo.get(6));
            x.locationType = ((stopInfo.get(7).equals(" ")) ? "N/A" : stopInfo.get(7));
            x.parentStation = ((stopInfo.get(8).equals(" ")) ? "N/A" : stopInfo.get(8));
            return x;
        }
        if (x == null || (int) x.value == 0) {
            x = new TSTNode();
            x.terminal = false;
            x.value = key.charAt(d);
            x.next[1] = put(x.next[1], key, d + 1, stopInfo);
        } else if (x.value > c) {
            x.next[0] = put(x.next[0], key, d, stopInfo);
        } else if (x.value < c) {
            x.next[2] = put(x.next[2], key, d, stopInfo);
        } else {
            x.next[1] = put(x.next[1], key, d + 1, stopInfo);
        }
        return x;
    }

    /**
     * Checks if the given name is the name of a stop, starting at the root.
     *
     * @param key: The name of the stop to check.
     * @return TSTNode: The node that matches the given key.
     * @Authors: David King
     */

    public TSTNode getNodeFromKey(String key) {
        TSTNode x = getNodeFromKey(root, key, 0);

        return (x);
    }

    /**
     * Checks if the given name is the name of a stop, starting from a given TSTNode.
     *
     * @param x: The current node.
     * @param key: The name of the stop to input.
     * @param d: The index of the current character being input.
     * @return TSTNode: The node that matches the given key.
     * @Authors: David King
     */

    private TSTNode getNodeFromKey(TSTNode x, String key, int d) {
        if (x == null) {
            System.out.println("No stops found with that name.");
            return null;
        }
        char c = key.charAt(d);

        if (d == key.length() - 1 && x.value == c) {
            return x;
        }
        /*else if (d == key.length()-1) {
            System.out.println("Getting "+key+", not found, x.value = "+x.value);
            return null;
        }*/

        if (x.value > c) {
            return getNodeFromKey(x.next[0], key, d);
        } else if (x.value < c) {
            return getNodeFromKey(x.next[2], key, d);
        } else {
            return getNodeFromKey(x.next[1], key, d + 1);
        }
    }

    /**
     * Gets all nodes below the root.
     *
     * @return ArrayList<String>: THe list of nodes below the root.
     * @Authors: David King
     */

    public ArrayList<String> getSubtree() {
        ArrayList<String> subtree = new ArrayList<String>();
        subtree.addAll(getSubtree(root, "", true));
        return subtree;
    }

    /**
     * Gets all nodes below the root.
     * @param x: The current node.
     * @param currentPath: The path of nodes from the root to the current node.
     * @param isStart: Whether the current node is the first node checked.
     * @return ArrayList<String>: THe list of nodes below the root.
     * @Authors: David King
     */

    public ArrayList<String> getSubtree(TSTNode x, String currentPath, boolean isStart) {
        ArrayList<String> subtree = new ArrayList<String>();
        if (x == null) {
            return null;
        }
        String newPath = currentPath;
        if (x.terminal) {
            //newPath = currentPath + x.value;
            subtree.add(newPath);

            System.out.println("Stop: " + newPath);
            getStopInfo(x);
        }


        for (int i = 0; i < 3; i++) {
            try {
                if ((i == 1) && (!isStart)) {
                    newPath = currentPath + x.value;
                } else {
                    newPath = currentPath;
                }

                if (!isStart) {
                    subtree.addAll(getSubtree(x.next[i], newPath, false));
                } else {
                    subtree.addAll(getSubtree(x.next[1], newPath, false));
                }
            } catch (NullPointerException e) {

            }
        }
        return subtree;
    }

    /**
     * Prints out all the information for the given stop.
     * @param x: The stop to check.
     * @Authors: David King
     */

    public void getStopInfo(TSTNode x) {
        if (x != null && x.terminal) {
            System.out.println("\tDescription: " + x.stopDesc + "\n\tID: " + x.stopId + "\n\tCode: " + x.stopCode + "\n\tCo-ordinates: " + x.stopLat + ", " + x.stopLon + "\n\tZone id: " + x.zoneId + "\n\tURL: " + x.stopUrl + "\n\tLocation type: " + x.locationType + "\n\tParent station: " + x.parentStation + "\n--------------------");
        }
    }


}
