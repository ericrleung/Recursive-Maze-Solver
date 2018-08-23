import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze {
    private static final byte OPEN = 0;
    private static final byte WALL = 1;
    private static final byte VISITED = 2;

    private int rows, columns;
    private byte[][] grid;

    private ArrayList<Point2D> pathFrom = new ArrayList<Point2D>();
    private ArrayList<Point2D> biggestSoFarFrom = new ArrayList<Point2D>();

    // A constructor that makes a maze of the given size
    public Maze(int r, int c) {
        rows = r;
        columns = c;
        grid = new byte[r][c];
        for (r = 0; r < rows; r++) {
            for (c = 0; c < columns; c++) {
                grid[r][c] = WALL;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    // Return true if a wall is at the given location, otherwise false
    public boolean wallAt(int r, int c) {
        return grid[r][c] == WALL;
    }

    // Return true if this location has been visited, otherwise false
    public boolean visitedAt(int r, int c) {
        return grid[r][c] == VISITED;
    }

    // Put a visit marker at the given location
    public void placeVisitAt(int r, int c) {
        grid[r][c] = VISITED;
    }

    // Remove a visit marker from the given location
    public void removeVisitAt(int r, int c) {
        grid[r][c] = OPEN;
    }

    // Put a wall at the given location
    public void placeWallAt(int r, int c) {
        grid[r][c] = WALL;
    }

    // Remove a wall from the given location
    public void removeWallAt(int r, int c) {
        grid[r][c] = 0;
    }

    // Carve out a maze
    public void carve() {
        int startRow = (int) (Math.random() * (rows - 2)) + 1;
        int startCol = (int) (Math.random() * (columns - 2)) + 1;
        carve(startRow, startCol);
    }

    // Directly recursive method to carve out the maze
    public void carve(int r, int c) {
        // Write your code here
        // simple check to make sure we leave the borders in tact
        if ((r == 0) || (r == rows - 1) || (c == 0) || (c == columns - 1)) {
            // do nothing
        } else if (!wallAt(r, c)) {
            // if we don't have a wall (already carved)
            // do nothing
        } else {
            // create an initial variable in order to store the condition checks
            int i = 0;
            if (wallAt(r - 1, c)) {
                i++;
            }
            if (wallAt(r + 1, c)) {
                i++;
            }
            if (wallAt(r, c - 1)) {
                i++;
            }
            if (wallAt(r, c + 1)) {
                i++;
            }
            // if we counter at least 3 walls, remove wall at current position
            if (i >= 3) {
                removeWallAt(r, c);
                // arraylists for offsets
                ArrayList<Integer> rowOffsets = new ArrayList<Integer>(Arrays.asList(-1, 1, 0, 0));
                ArrayList<Integer> colOffsets = new ArrayList<Integer>(Arrays.asList(0, 0, -1, 1));

                // random variable
                Random rand = new Random();

                // generate random ints from 0-3 and make sure they are not equal
                int rand1 = rand.nextInt(4);
                int rand2 = rand.nextInt(4);
                while (rand1 == rand2) {
                    rand2 = rand.nextInt(4);
                }
                int rand3 = rand.nextInt(4);
                while ((rand1 == rand3) || (rand2 == rand3)) {
                    rand3 = rand.nextInt(4);
                }
                int rand4 = rand.nextInt(4);
                while ((rand1 == rand4) || (rand2 == rand4) || (rand3 == rand4)) {
                    rand4 = rand.nextInt(4);
                }
                // recursively call the carve function with the offsets
                carve(r + rowOffsets.get(rand1), c + colOffsets.get(rand1));
                carve(r + rowOffsets.get(rand2), c + colOffsets.get(rand2));
                carve(r + rowOffsets.get(rand3), c + colOffsets.get(rand3));
                carve(r + rowOffsets.get(rand4), c + colOffsets.get(rand4));

            }
        }
    }

    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPath() {
        // at first, call longestPathFrom(1,1) as a basis for the size to compare to
        int size = longestPathFrom(1, 1).size();
        // create an arrayList to hold the largest path so far
        ArrayList<Point2D> biggestSoFar = new ArrayList<Point2D>();

        // loop through every point (excludes borders)
        for (int r = 1; r < rows; r ++) {
            for (int c = 1; c < columns; c++) {
                // if a wall is not present (path can't start at wall)
                if (!wallAt(r,c)) {
                    // if the current path size > the currently stored size then replace the path with the current path
                    if (longestPathFrom(r, c).size() > size) {
                        biggestSoFar = new ArrayList<>(longestPathFrom(r, c));
                    }
                }
            }
        }
        // return biggest path found
        return biggestSoFar;
    }

    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPathFrom(int r, int c) {
        // at first, check if there is a wall at the current location
        // only go through algorithm if not wallAt that point, otherwise return the path
        if (!wallAt(r, c)) {
            // place a 'bread crumb' at the current location
            placeVisitAt(r, c);
            // add the current location to the path
            pathFrom.add(new Point2D(r, c));

            // base case
            // check if there is a wall or that each point around the current point is visited
            // if this is met (simplest case)
            // then we simply set the longestPath ArrayList = path
            if ((wallAt(r+1, c) || visitedAt(r+1, c)) && (wallAt(r-1, c) || visitedAt(r-1, c)) && (wallAt(r, c+1) || visitedAt(r, c+1)) && (wallAt(r, c-1) || visitedAt(r, c-1))) {
                if (pathFrom.size() > biggestSoFarFrom.size())
                    biggestSoFarFrom = new ArrayList<>(pathFrom);
            }

            // recursive case
            // check if it's even possible / should we call the function
            // checks if r > 1 because otherwise, we'd end up with a negative r which would cause problems
            // make sure we haven't already visited the point, or that there's a wall there
            // if all conditions are met, then we call the recursive function
            if (r > 1 && !visitedAt(r-1, c) && !wallAt(r-1, c))
                longestPathFrom(r-1, c);

            if (r < rows && !visitedAt(r+1, c) && !wallAt(r+1, c))
                longestPathFrom(r+1, c);

            if (c > 1 && !visitedAt(r, c-1) && !wallAt(r, c-1))
                longestPathFrom(r, c-1);

            if (c < columns && !visitedAt(r, c+1) && !wallAt(r, c+1))
                longestPathFrom(r, c+1);

            // after doing our recursive calls, we remove the current point from the path (in order to not be a destructive algorithm)
            pathFrom.remove(new Point2D(r, c));
            // remove visited at the current point
            removeVisitAt(r, c);
        }
        // in the case where we are at a wall, simply return longest path
        return biggestSoFarFrom;
    }
}
