import java.util.*;

/*
 * Keira Taylor
 * 04/10/18
 * This program calculates the number of paths, maximum score, and most
 * alphabetical path Pac Man can take.
*/

public class pacman
{
	public static int rows, cols;
	public static int startR, startC;
	public static final int BEG = -1, END = -2;
	public static final long MOD = 1000000007;
	
	public static String [][] alpha;
	public static long [][] count;
	
	public static int [] dx = {0, -1};
	public static int [] dy = {-1, 0};
	public static String [] addAlpha = {"R", "D"};
	
	public static void main(String [] args)
	{
		//declare and initialize variables
		Scanner in = new Scanner(System.in);
		
		rows = in.nextInt();
		cols = in.nextInt();
		
		int [][] board = new int[rows][cols];
		int [][] weight = new int[rows][cols];
		alpha = new String[rows][cols];
		count = new long[rows][cols];
		
		//read in input
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				String ch = in.next();
				if(ch.equals("P"))
				{
					board[r][c] = 0;
					weight[r][c] = 0;
					startR = r;
					startC = c;
				}
				else if(ch.equals("E"))
				{
					board[r][c] = 0;
					weight[r][c] = 0;
				}
				else
				{
					board[r][c] = Integer.parseInt(ch);
					weight[r][c] = Integer.parseInt(ch);
				}
				alpha[r][c] = "";
				count[r][c] = 1;
			}
		}
		
		//loop through each spot, looking at the cell above and to the left
		for(int r = startR; r < rows; r++)
		{
			for(int c = startC; c < cols; c++)
			{
				//check if the left and upper cell are both in bounds
				if(inBounds(r + dx[0], c + dy[0]) && inBounds(r + dx[1], c + dy[1]))
				{
					//if the weight of the cell to the left and above are equal, they are both valid paths
					if(weight[r + dx[0]][c + dy[0]] == weight[r + dx[1]][c + dy[1]])
					{
						//copy the weight of the left cell
						weight[r][c] = weight[r + dx[0]][c + dy[0]] + board[r][c];
						//add the path count of both cells together
						count[r][c] = ((long)count[r + dx[0]][c + dy[0]] + (long)count[r + dx[1]][c + dy[1]]) % MOD;
						//take the more alphabetical string path
						if(alpha[r + dx[1]][c + dy[1]].compareTo(alpha[r + dx[0]][c + dy[0]]) < 0)
						{
							alpha[r][c] = alpha[r + dx[1]][c + dy[1]] + addAlpha[1];
						}
						else
						{
							alpha[r][c] = alpha[r + dx[0]][c + dy[0]] + addAlpha[0];
						}
					}
					//if the left weight is greater than the upper weight
					else if(weight[r + dx[0]][c + dy[0]] > weight[r + dx[1]][c + dy[1]])
					{
						//copy the weight of the left cell
						weight[r][c] = weight[r + dx[0]][c + dy[0]] + board[r][c];
						//copy the path count of the left cell
						count[r][c] = (long)count[r + dx[0]][c + dy[0]] % MOD;
						//copy the left cell's string and add "R" to it
						alpha[r][c] = alpha[r + dx[0]][c + dy[0]] + addAlpha[0];
					}
					else
					{
						//copy the weight of the upper cell
						weight[r][c] = weight[r + dx[1]][c + dy[1]] + board[r][c];
						//copy the path count of the upper cell
						count[r][c] = (long)count[r + dx[1]][c + dy[1]] % MOD;
						//copy the left cell's string and add "D" to it
						alpha[r][c] = alpha[r + dx[1]][c + dy[1]] + addAlpha[1];
					}
				}
				//if just the left cell is in bounds
				else if(inBounds(r + dx[0], c + dy[0]))
				{
					//copy the weight of the left cell
					weight[r][c] = weight[r + dx[0]][c + dy[0]] + board[r][c];
					//copy the path count of the left cell
					count[r][c] = (long)count[r + dx[0]][c + dy[0]] % MOD;
					//copy the left cell's string and add "R" to it
					alpha[r][c] = alpha[r + dx[0]][c + dy[0]] + addAlpha[0];
				}
				//if just the upper cell is in bounds
				else if(inBounds(r + dx[1], c + dy[1]))
				{
					//copy the weight of the upper cell
					weight[r][c] = weight[r + dx[1]][c + dy[1]] + board[r][c];
					//copy the path count of the upper cell
					count[r][c] = (long)count[r + dx[1]][c + dy[1]] % MOD;
					//copy the left cell's string and add "D" to it
					alpha[r][c] = alpha[r + dx[1]][c + dy[1]] + addAlpha[1];
				}
				//if neither the upper cell nor the left cell is in bounds (at position P)
				else
				{
					weight[r][c] = 0;
					count[r][c] = 1;
					alpha[r][c] = "";
				}
			}
		}
		
		//print results
		System.out.println(weight[rows - 1][cols - 1] + " " + count[rows - 1][cols - 1] % MOD);
		System.out.println(alpha[rows - 1][cols - 1]);
	}
	
	public static boolean inBounds(int r, int c)
	{
		return (r >= 0 && r < rows && c >= 0 && c < cols);
	}

}