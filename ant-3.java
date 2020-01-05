/*
* Keira Taylor
* 01/26/18
* ant
*/

import java.util.*;

public class ant
{
	private static Scanner in = new Scanner(System.in);
	
	public static void main (String [] args)
	{
		//declare and initialize variables
		int numCases = in.nextInt();
		int answer = 0;
		
		//loop through the cases
		for(int i = 0; i < numCases; i++)
		{
			//print out output
			System.out.print("Campus #" + (i + 1) + ": ");
			//solve the case
			answer = solveCase();
			//output solution
			if(answer != -1)
			{
				System.out.println(answer);
			}
			else
			{
				System.out.println("I'm a programmer, not a miracle worker!");
			}
		}
	}
	
	public static int solveCase()
	{
		//declare and initialize variables
		int numHills = in.nextInt();
		int numTunnels = in.nextInt();
		PriorityQueue<Edge> path = new PriorityQueue<>();
		ArrayList<Edge> allEdges = new ArrayList<>();
		ArrayList<Integer> set = new ArrayList<>();
		int numToComplete = numHills;
		int totalCost = 0;
		
		//if there is only 1 hill and no tunnels
		if(numHills == 1 && numTunnels == 0)
		{
			return 0;
		}
		//if there are no tunnels and multiple hills, it is impossible
		else if(numHills > 1 && numTunnels == 0)
		{
			return -1;
		}
		
		//add each path from the input
		for(int k = 0; k < numTunnels; k++)
		{
			allEdges.add(new Edge(in.nextInt(), in.nextInt(), in.nextInt()));
		}
		
		//add first hill to path
		set.add(1);
		
		//add all edges connected to the first hill
		for(int k = 0; k < allEdges.size(); k++)
		{
			if(allEdges.get(k).isIncidentTo(set.get(0)))
			{
				Edge onPath = allEdges.get(k);
				path.add(onPath);
				allEdges.remove(onPath);
				k--;
			}
		}
		int i = 0;
		//loop for completing ant's journey
		while(!(set.size() == numHills) && path.size() > 0)
		{
			//get first edge on priority queue
			Edge e = path.poll();
			
			//increment i
			i++;
			
			//if the edge exists
			if(e != null)
			{
				
				//if this edge would complete a cycle, continue
				if(set.contains(e.getP1()) && set.contains(e.getP2()))
				{
					continue;
				}
				
				//add points  to set
				if(!set.contains(e.getP1()))
					set.add(e.getP1());
				if(!set.contains(e.getP2()))
					set.add(e.getP2());
			
				//add to total cost
				totalCost += e.getWeight();
			
				//add all edges incident to the points on the newest edge added
				int k = 0;
				while(allEdges.size() > k)
				{
					Edge e2 = allEdges.get(k);
					//if the new edge is incident to the first edge on the pq add it to the path
					if(e2.isIncidentTo(e.getP1()) || e2.isIncidentTo(e.getP2()))
					{
						path.add(e2);
						allEdges.remove(e2);
						k--; //because an element was removed, k must be decremented
					}
					//increment k
					k++;
				}
			}
		}
		
		//checks to see if solution was discovered
		if(set.size() == numHills)
		{
			return totalCost;
		}
		//if solution was not discovered, return -1
		else
		{
			return -1;
		}
	}
}
//class to hold paths
class Edge implements Comparable<Edge>
{
	//declare global variables
	private int point1;
	private int point2;
	private int weight;
	
	//Edge's constructor
	public Edge(int p1, int p2, int w)
	{
		point1 = p1;
		point2 = p2;
		weight = w;
	}
	
	//returns <0 if this.weight < e.weight, >0 if this.weight > e.weight, and 0 if equal
	public int compareTo(Edge e)
	{
		return weight - e.weight;
	}
	
	public boolean isIncidentTo(int point)
	{
		if(point == point1 || point == point2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//returns point1
	public int getP1()
	{
		return point1;
	}
	//returns point2
	public int getP2()
	{
		return point2;
	}
	//returns weight
	public int getWeight()
	{
		return weight;
	}
	//for testing: produces string form of class
	public String toString()
	{
		return point1 + ", " + point2 + ", " + weight;
	}
}