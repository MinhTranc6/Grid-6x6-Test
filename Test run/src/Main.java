public class Main {
    static int gridSize = 7; // Changeable grid size
    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    static long totalPaths = 0;
    static long searchCounter = 0;

    // Check if the cell is valid and unvisited
    static boolean isValid(int m, int n, boolean[][] visited) {
        return m >= 0 && n >= 0 && m < gridSize && n < gridSize && !visited[m][n];
    }

    // Check connectivity of unvisited cells
    static boolean isConnected(boolean[][] visited) {
        boolean[][] tempVisited = new boolean[gridSize][gridSize];
        int startRow = -1, startCol = -1;

        // Find the first unvisited cell
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (!visited[i][j]) {
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
            if (startRow != -1) break;
        }

        if (startRow == -1) return true; // No unvisited cells

        // Perform flood-fill to count connected cells
        int connectedCount = floodFill(visited, tempVisited, startRow, startCol);
        int unvisitedCount = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (!visited[i][j]) unvisitedCount++;
            }
        }
        return connectedCount == unvisitedCount;
    }

    // Flood-fill algorithm to count connected cells
    static int floodFill(boolean[][] visited, boolean[][] tempVisited, int m, int n) {
        if (m < 0 || n < 0 || m >= gridSize || n >= gridSize || visited[m][n] || tempVisited[m][n]) {
            return 0;
        }
        tempVisited[m][n] = true;
        int count = 1;
        for (int[] dir : directions) {
            count += floodFill(visited, tempVisited, m + dir[0], n + dir[1]);
        }
        return count;
    }

    // DFS with backtracking
    static void dfs(int m, int n, boolean[][] visited, int cellsVisited) {
        // Increment the search counter
        searchCounter++;
        if (searchCounter % 10000000 == 0) {
            System.out.println("Search steps: " + searchCounter);
        }

        // Base case: If all cells are visited and we're at the bottom-left corner
        if (cellsVisited == gridSize * gridSize) {
            if (m == gridSize - 1 && n == 0) {
                totalPaths++;
            }
            return;
        }

        // Prune: Check if remaining cells are connected
        if (!isConnected(visited)) {
            return;
        }

        // Mark the current cell as visited
        visited[m][n] = true;

        // Explore all possible directions
        for (int[] dir : directions) {
            int newRow = m + dir[0];
            int newCol = n + dir[1];
            if (isValid(newRow, newCol, visited)) {
                dfs(newRow, newCol, visited, cellsVisited + 1);
            }
        }

        // Backtrack: Unmark the current cell
        visited[m][n] = false;
    }

    public static void main(String[] args) {
        boolean[][] visited = new boolean[gridSize][gridSize];

        // Start timing
        long startTime = System.currentTimeMillis();

        // Start the DFS from the top-left corner
        totalPaths = 0;
        searchCounter = 0;
        dfs(0, 0, visited, 1);

        // End timing
        long endTime = System.currentTimeMillis();

        // Print the result
        System.out.println("Number of unique paths: " + totalPaths);
        System.out.println("Total search steps: " + searchCounter);
        System.out.println("Execution time (ms): " + (endTime - startTime));
    }
}
