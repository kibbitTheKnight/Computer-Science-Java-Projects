/*
 * 2018
 */

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Sudoku Solver
 * Solves Sudoku puzzles using backtracking
 * @author Keira Taylor
 * @date 01/17/18
 */
public class Sudoku {

    private static int [] [] puzzle = new int [9] [9];
    private static boolean [] [] usedPuzzle = new boolean [9][9];
    private static final int NUM_ROWS = 9, NUM_BOXES = 3;
    private static Scanner in = new Scanner(System.in);
    public static void main(String[] args) {
        //declare and initliaze varaibles
        int numCases = 0;
        
        //take input
        numCases = in.nextInt();
        
        //loop through cases
        for(int i = 0; i < numCases; i++)
        {
            readCase();
            System.out.println("Test case " + (i + 1) + ":\n");
            processCase();
            
            //print results
            if(processCase())
            {
                printBoard();
            }
            else
            {
                System.out.println("No solution possible.\n");
            }
                System.out.println();
        }
    }
    public static void readCase()
    {
        //loop through rows and columns to initialize puzzle[][] and used[][]
        for(int r = 0; r < NUM_ROWS; r++)
        {
            for(int c = 0; c < NUM_ROWS; c++)
            {
                int num = in.nextInt();
                puzzle[r][c] = num;
                if(puzzle[r][c] == 0)
                {
                    usedPuzzle[r][c] = false;
                }
                else
                {
                    usedPuzzle[r][c] = true;
                }
            }
        }
    }
    public static boolean processCase()
    {
        //shell for solve
        return solve(0, 0, 0);
    }
    public static boolean solve(int row, int col, int counter)
    {
        //base cases
        
        //puzzle has been run through completely
        if(counter >= NUM_ROWS * NUM_ROWS)
        {
            return false;
        }
        
        //the current setup doesn't work
        if(!check())
        {
            return false;
        }
        
        //the current square is not open
        if(usedPuzzle[row][col])
        {
            while(row < NUM_ROWS && col < NUM_ROWS && usedPuzzle[row][col]&& col < NUM_ROWS)
            {
                row = (counter + 1)/NUM_ROWS;
                col = (counter + 1)%NUM_ROWS;
                counter += 1;
            }
        }
        
        //the puzzle is solved
        if(counter >= 80 && isDone())
        {
            return true;
        }
        
        //the row/col are out of bounds
        if(row >= NUM_ROWS || col >= NUM_ROWS)
        {
            return false;
        }
        
        //loop through all permutations
        for(int k = 1; k <= NUM_ROWS; k++)
        {
            //fill current square
            puzzle[row][col] = k;
            
            //check current square
            if(!check(row, col))
            {
                puzzle[row][col] = 0;
                continue;
            }
            
            //set square to used
            usedPuzzle[row][col] = true;
            
            //solve
            if(solve((counter + 1)/NUM_ROWS, (counter + 1)%NUM_ROWS, counter + 1))
            {
                return true;
            }
            
            //check if it is done
            if(counter >= 80 && isDone())
            {
                return true;
            }
            
            //reset values
            puzzle[row][col] = 0;
            usedPuzzle[row][col] = false;
        }
        
        //no solution found
        return false;
    }
    public static boolean isDone()
    {
        //if it is not correct, return false
        if(!check())
        {
            return false;
        }
        
        //if any values are left unfilled, return false
        for(int r = 0; r < NUM_ROWS; r++)
        {
            for(int c = 0; c < NUM_ROWS; c++)
            {
                if(puzzle[r][c] == 0)
                {
                    return false;
                }
            }
        }
        
        //return true if it has not been proven false
        return true;
    }
    public static boolean check()
    {
        boolean isValid;
        
        //check rows and columns
        for(int i = 0; i < NUM_ROWS; i++)
        {
            isValid = isValidRow(i);
            if(!isValid)
            {
                return false;
            }
            isValid = isValidColumn(i);
            if(!isValid)
            {
                return false;
            }
        }
        
        //check each of the 9 boxes
        for(int r = 0; r < NUM_BOXES; r++)
        {
            for(int c = 0; c < NUM_BOXES; c++)
            {
                isValid = isValidBox(r, c);
                if(!isValid)
                {
                    return false;
                }
            }
        }
        
        //if isValid has never been false, the solution is valid
        return true;
    }
    public static boolean check(int r, int c)
    {
        //check row
        if(!isValidRow(r))
        {
            return false;
        }
        //check column
        if(!isValidColumn(c))
        {
            return false;
        }
        //check box
        if(!isValidBox(r/NUM_BOXES, c/NUM_BOXES))
        {
            return false;
        }
        
        //return true if it has not been proven false
        return true;
    }
    public static boolean isValidColumn(int c)
    {
        //create ArrayList to store already used values
        ArrayList<Integer> used = new ArrayList<>();
        
        for(int i = 0; i < NUM_ROWS; i++)
        {
            //if the number has already been used, it is not valid
            if(used.contains(puzzle[i][c]))
            {
                return false;
            }
            //add the number to used if it has been chosen
            if(puzzle[i][c] != 0)
            {
                used.add(puzzle[i][c]);
            }
        }
        return true;
    }
    public static boolean isValidRow(int r)
    {
        //create ArrayList to store already used values
        ArrayList<Integer> used = new ArrayList<>();
        
        for(int i = 0; i < NUM_ROWS; i++)
        {
            //if the number has already been used, it is not valid
            if(used.contains(puzzle[r][i]))
            {
                return false;
            }
            //add the number to used if it has been chosen
            if(puzzle[r][i] != 0)
            {
                used.add(puzzle[r][i]);
            }
        }
        return true;
    }
    public static boolean isValidBox(int boxR, int boxC)
    {
        //create ArrayList to store already used values
        ArrayList<Integer> used = new ArrayList<>();
        
        //loop through each box
        for(int r = 0; r < NUM_BOXES; r++)
        {
            for(int c = 0; c < NUM_BOXES; c++)
            {
                //store current square in value
                int value = puzzle[(boxR * NUM_BOXES) + r][(boxC * NUM_BOXES) + c];
                
                //if the value has already been used in the box, return false
                if(used.contains(value))
                {
                    return false;
                }
                //add the value to the box
                if(value != 0)
                {
                    used.add(value);
                }
            }
        }
        
        //if it has not been proven false, return true
        return true;
    }
    
    //used for testing
    public static void printBoard()
    {
        for(int r = 0; r < NUM_ROWS; r++)
        {
            for(int c = 0; c < NUM_ROWS; c++)
            {
                System.out.print(puzzle[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    //used for testing
    public static void printUsed()
    {
        for(int r = 0; r < NUM_ROWS; r++)
        {
            for(int c = 0; c < NUM_ROWS; c++)
            {
                System.out.print(usedPuzzle[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
