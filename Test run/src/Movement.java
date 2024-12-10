///The envision process is parseDirection() -> getNextPosition() -> isValidMove() -> true -> tryMove()
///                        /\                                                      -> false -||
///                        ||                                                                ||
///                        ====================================================================

//===================================================================================================//

/// This does not yet check if it reaches the end prematurely
/// Will have to look into it.

public class Movement {
    //This variable will be global to handle position.
    //Initial position.
    int[] currentPosition = {0, 0};


    public static int[] parseDirection(char direction) {
        switch (direction) {
            case 'U':
                return new int[]{-1, 0};
            case 'D':
                return new int[]{1, 0};
            case 'L':
                return new int[]{0, -1};
            case 'R':
                return new int[]{0, 1};
            case '*':
                // Randomly choose a direction or iterate over all four. Will decide later
                // ...
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }

    public static int[] getNextPosition(int currentRow, int currentCol, char direction) {
        int[] movementOffset = parseDirection(direction);
        return new int[]{currentRow + movementOffset[0], currentCol + movementOffset[1]};
    }


    ///The purpose of this is to check if the value return by getNextPosition() is a valid move or not
    ///Implementation of this method will most likely belong to the pathfinding algorithm.
    public static boolean isValidMove(int newRow, int newCol) {
        return newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8;
    }

    ///Example
    /*
    int[] newPosition = getNextPosition(currentRow, currentCol, direction);
    if (isValidMove(newPosition[0], newPosition[1])) {
        // The move is valid, proceed with the move i.e. tryMove()
    } else {
        // The move is invalid, handle accordingly (e.g., backtrack or explore other directions)
    }
     */

    public static boolean tryMove(char direction, int[] currentPosition /*, Grid grid [Call if needed]*/) {
        int[] offsets = parseDirection(direction);
        int newRow = currentPosition[0] + offsets[0];
        int newCol = currentPosition[1] + offsets[1];

        if (isValidMove(newRow, newCol)) {
            currentPosition[0] = newRow;
            currentPosition[1] = newCol;
            ///Call the method to mark the coordinate on the grid if needed
            return true;
        }
        return false;
    }

}
