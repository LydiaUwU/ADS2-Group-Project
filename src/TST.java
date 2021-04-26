public class TST {
    private TSTNode root = new TSTNode();

    private static class TSTNode {
        private char value;
        private boolean terminal;
        private TSTNode[] next = new TSTNode[3];
    }

    public void put(String key) {
        System.out.println("Putting "+key+" starting from root");
        root = put(root, key, 0);
    }

    private TSTNode put(TSTNode x, String key, int d) {
        System.out.println("Putting "+key+" starting from "+d);
        char c = key.charAt(d);

        if (d == key.length()-1) {
            System.out.println("Putting "+key+", at end, adding "+key.charAt(d)+")");
            x = new TSTNode();
            x.value = key.charAt(d);
            x.terminal = true;
            return x;
        }
        if (x == null||(int) x.value == 0)
        {
            System.out.println("Putting "+key+", at "+d+", making new node (key.length() = "+key.length()+", adding "+key.charAt(d)+")");
            x = new TSTNode();
            x.terminal = false;
            x.value = key.charAt(d);
            x.next[1] = put(x.next[1], key, d + 1);
        }

        else if (x.value > c)
        {
            System.out.println("Putting "+key+", at "+d+", taking left branch (x.value = "+x.value+", c = "+c+")");
            x.next[0] = put(x.next[0], key, d);
        }
        else if (x.value < c)
        {
            System.out.println("Putting "+key+", at "+d+", taking right branch (x.value = "+x.value+", c = "+c+")");
            x.next[2] = put(x.next[2], key, d);
        }
        else
        {
            System.out.println("Putting "+key+", at "+d+", taking existing centre branch (x.value = "+x.value+", c = "+c+")");
            x.next[1] = put(x.next[1], key, d+1);
        }
        return x;
    }

    public boolean contains(String key) {
        return get(key);
    }

    public boolean get(String key) {
        System.out.println("Getting "+key+", starting from root");
        TSTNode x = get(root,key,0);

        return (x != null);
    }

    private TSTNode get(TSTNode x, String key, int d) {
        System.out.println("----------");
        if (x == null) {
            System.out.println("Getting "+key+", not found");
            return null;
        }

        if (d == key.length()-1&&x.terminal) {
            System.out.println("Getting "+key+", found");
            return x;
        }
        char c = key.charAt(d);

        System.out.println("x.value = "+x.value);

        if (x.value > c)
        {
            System.out.println("Getting "+key+", at "+d+", taking left branch (x.value = "+x.value+", c = "+c+")");
            return get(x.next[0], key,d);
        }
        else if (x.value < c)
        {
            System.out.println("Getting "+key+", at "+d+", taking right branch (x.value = "+x.value+", c = "+c+")");
            return get(x.next[2], key,d);
        }
        else
        {
            System.out.println("Getting "+key+", at "+d+", taking existing centre branch, x.value = "
                    +x.value+", x.terminal = "+x.terminal+", c = "+c+")");
            return get(x.next[1], key, d + 1);
        }
    }
}
