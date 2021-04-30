# List of things to include in the demo, please add/remove examples to show in the demo

## Part 1 (shortest path between stops):

# Valid paths

1477, 1866

1866, 1477 (same both ways)

1477, 1863 (first part of first example)

1863, 1866 (second part of first example)

1477, 1477 (same stop)

1817, 1819

1477, 1377 (not a whole number cost, truncated)

646, 1278

841, 842


# Invalid paths

test, test

(press ENTER for first stop without typing anything)

1477, (press ENTER for second stop without typing anything)


## Part 2 (search for stops by name):

# Valid names
kootenay loop bay 2

KOOTENAY LOOP BAY 2

Kootenay Loop Bay 2

halifax

ewen ave ns

ewen ave fs

# Invalid names

(press ENTER without typing anything)

(1 space)

(2 spaces)

!!!!!!!!

aaaaaaaa

zzzzzzzz

~~~~~~~~

aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

kootenay loop bay 2a

## Part 3 (searching for trips with a given arrival time):

# Valid times 

00:00:00

12:00:00

12:34:56

## Invalid times
(press ENTER without typing anything)

(1 space)

(2 spaces)

123456

abcdef

25:00:00

00:60:00

00:00:60

00:00:00:00
