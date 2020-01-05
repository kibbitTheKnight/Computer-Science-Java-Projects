/*
 * Keira Taylor
 * 03/07/18
 * Calc
 * This program simulates a very terrible calculator that
 * can perform only 3 operations
*/

import java.util.*;

public class Calc
{
	public static void main (String [] args)
	{
		//declare and initialize variables
		int numCases = 0;
		Scanner in = new Scanner(System.in);
		
		//scan in numCases
		numCases = in.nextInt();
		
		//loop through each case
		for(int i = 0; i < numCases; i++)
		{
			int a = in.nextInt();
			int b = in.nextInt();
			int c = in.nextInt();
			int num = in.nextInt();
			
			//search through each calc tree and print the answer
			System.out.println(bfs(a, b, c, num));
		}
		
		
	}
	public static int bfs(int a, int b, int c, int num)
	{
		//declare and initialize variables
		//array that acts like a queue
		int [] q = new int[1000001];
		//array to keep track of used numbers
		int [] seen = new int[1000000];
		//integer to set distance
		int distance = 0;
		//integer to keep track of beginning of queue
		int pointer = 0;
		//integer to keep track of end of queue
		int end = 0;
		//integer to keep track of size of queue
		int size = 0;
		
		//add first element to queue
		seen[pointer] = distance;
		end++;
		size = end - pointer;
		
		//return 0 if num == 0
		if(num == 0) return 0;
		
		//search through all possible values
		while(size > 0 && pointer < 1000000)
		{
			//get first item on queue
			int polled = q[pointer];
			pointer++;
			
			//add its relatives to the queue
			
			//update changed
			int changed = add(a, polled);
			//return the distance if the target is reached
			if(changed == num) return seen[polled] + 1;
			//if the the number is valid and not used, add it to the queue
			if(changed > 0 && seen[changed] == 0)
			{	
				q[end] = changed;
				end++;
				seen[changed] = seen[polled] + 1;
			}
			
			//update changed
			changed = multiply(b , polled);
			//return the distance if the target is reached
			if(changed == num) return seen[polled] + 1;
			//if the the number is valid and not used, add it to the queue
			if(changed > 0 && seen[changed] == 0)
			{
				q[end] = changed;
				end++;
				seen[changed] = seen[polled] + 1;
			}

			//update changed
			changed = divide(c, polled);
			//return the distance if the target is reached
			if(changed == num) return seen[polled] + 1;
			//if the the number is valid and not used, add it to the queue
			if(changed > 0 && seen[changed] == 0)
			{
				q[end] = changed;
				end++;
				seen[changed] = seen[polled] + 1;
			}
			
			//update size of queue
			size = end - pointer;
		}
		//no valid answer was found. Return -1
		return -1;
	}
	
	public static int lastSix(long number)
	{
		return (int)number % 1000000;
	}
	
	public static int add(int a, long num)
	{
		if(num + a > 2e30) return (int)num + a;
		return lastSix(num + a);
	}
	
	public static int multiply(int b, long num)
	{
		if(num * b > 2e30) return (int)num*b;
		return lastSix(num * b);
	}
	
	public static int divide(int c, long num)
	{
		if(num / c > 2e30) return (int)num / c;
		return lastSix(num / c);
	}
}