import java.util.Scanner;

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
    // DFS with constraints from input string
    static void dfs(int m, int n, boolean[][] visited, int cellsVisited, String input) {
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

        // Get the allowed direction for this move
        char allowedMove = input.charAt(cellsVisited - 1); // 0-based index

        // Explore all possible directions
        for (int[] dir : directions) {
            int newRow = m + dir[0];
            int newCol = n + dir[1];

            // Determine the direction char for this move
            char directionChar = getDirectionChar(dir);

            // If the move is valid and allowed by the input string, proceed
            if (isValid(newRow, newCol, visited) && (allowedMove == '*' || allowedMove == directionChar)) {
                dfs(newRow, newCol, visited, cellsVisited + 1, input);
            }
        }

        // Backtrack: Unmark the current cell
        visited[m][n] = false;
    }

    // Utility method to map direction vector to its corresponding char
    static char getDirectionChar(int[] dir) {
        if (dir[0] == -1 && dir[1] == 0) return 'U';
        if (dir[0] == 1 && dir[1] == 0) return 'D';
        if (dir[0] == 0 && dir[1] == -1) return 'L';
        if (dir[0] == 0 && dir[1] == 1) return 'R';
        return '*'; // Should never reach here
    }

    // Updated userInterface method
    private static void userInterface() {
        boolean[][] visited = new boolean[gridSize][gridSize];
        Scanner userIP = new Scanner(System.in);
        boolean runtime = true;
        System.out.print("================================================ \n" +
                         "Disclaimer: The time shown will be the time it took for the algorithm to run \n");
        while (runtime) {
            System.out.print("================================================ \n" +
                             "Enter 48-character path constraints (U/D/L/R/*), or type 'exit' to quit: \n");
            String pathInput = userIP.nextLine();

            // Exit condition
            if (pathInput.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program. Goodbye!");
                runtime = false; // Break out of the loop
                continue;
            }

            // Validate input length
            if (pathInput.length() != gridSize * gridSize - 1) {
                System.out.println("Invalid input. Must be 48 characters long.");
                continue;
            }

            // Start timing
            long startTime = System.currentTimeMillis();

            // Reset variables for a new computation
            totalPaths = 0;
            searchCounter = 0;

            // Run the pathfinding process
            dfs(0, 0, visited, 1, pathInput);

            // End timing
            long endTime = System.currentTimeMillis();

            // Display results
            System.out.print("================================================ \n");
            System.out.println("Input: " + pathInput);
            System.out.println("Total search steps: " + searchCounter);
            System.out.println("Number of unique paths: " + totalPaths);
            System.out.println("Execution time (ms): " + (endTime - startTime));
        }

        userIP.close(); // Close the scanner resource
    }

    public static void main(String[] args) {
        userInterface();
    }
}
