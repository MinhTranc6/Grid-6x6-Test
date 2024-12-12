public class Dynamic {

    private static final int GRID_SIZE = 8;
    private static final int TOTAL_CELLS = GRID_SIZE * GRID_SIZE;

    // Memoization table: dp[x][y][visited] (reduced scale to fit bitmask range)
    private static long[][][] dp;

    // Counter to track progress
    private static long pathCounter = 0;

    public static void main(String[] args) {
        // Initialize memoization table with appropriate size
        int bitmaskStates = 1 << TOTAL_CELLS;
        if (bitmaskStates <= 0 || bitmaskStates > Integer.MAX_VALUE / 8) {
            throw new IllegalStateException("Bitmask size exceeds memory limits. Reduce GRID_SIZE.");
        }

        dp = new long[GRID_SIZE][GRID_SIZE][bitmaskStates];

        // Fill the table with -1 to indicate uncomputed states
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                for (int k = 0; k < bitmaskStates; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }

        // Start pathfinding from the top-left corner with only the first cell visited
        int startVisited = 1; // Bitmask: Only the top-left cell is visited
        long result = findPaths(0, 0, startVisited);

        System.out.println("Total number of valid paths: " + result);
    }

    private static long findPaths(int x, int y, int visited) {
        // Update and print progress every 1,000,000 calls
        pathCounter++;
        if (pathCounter % 1_000_000 == 0) {
            System.out.println("Paths tried: " + pathCounter);
        }

        // Base case: If all cells are visited and we're at the bottom-left corner, count as a valid path
        if (visited == (1 << TOTAL_CELLS) - 1) {
            return (x == GRID_SIZE - 1 && y == 0) ? 1 : 0;
        }

        // If already computed, return the cached result
        if (dp[x][y][visited] != -1) {
            return dp[x][y][visited];
        }

        long count = 0;

        // Possible moves: Up, Down, Left, Right
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            // Ensure bounds are checked before accessing bitmask or dp table
            if (isValidMove(nx, ny, visited)) {
                int nextBit = nx * GRID_SIZE + ny;
                int nextVisited = visited | (1 << nextBit); // Mark the next cell as visited
                count += findPaths(nx, ny, nextVisited);
            }
        }

        // Cache the result for the current state
        dp[x][y][visited] = count;

        return count;
    }

    private static boolean isValidMove(int x, int y, int visited) {
        // Check if the move is within bounds
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) {
            return false;
        }
        // Check if the cell has already been visited
        int bit = x * GRID_SIZE + y;
        return (bit >= 0 && bit < TOTAL_CELLS && (visited & (1 << bit)) == 0);
    }
}
