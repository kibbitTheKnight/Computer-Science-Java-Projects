import java.util.*;

/*
 * Keira Taylor
 * 04/03/18
 * Countseq attempts to find the amount of occurrences of a subsequence in
 * a given sequence.
*/

public class countseq
{
	//declare global variables
	public static String seq, subSeq;
	
	public static long [][] savedRes;
	
	public static void main(String [] args)
	{
		//declare and initialize variables
		Scanner in = new Scanner(System.in);
		int numCases = 0;
		
		numCases = in.nextInt();
		
		//loop through cases
		for(int i = 0; i < numCases; i++)
		{
			//read in input
			seq = in.next();
			subSeq = in.next();
			
			//create array for saved results
			savedRes = new long[seq.length() + 1][subSeq.length() + 1];
			
			//initialize the array as all -1s
			for(int j = 0; j < seq.length(); j++)
			{
				Arrays.fill(savedRes[j], -1);
			}
			//solve the sequence
			for(int j = seq.length(); j >= 0; j--)
			{
				for(int k = subSeq.length(); k >= 0; k--)
				{
					savedRes[j][k] = match(j, k);
				}
			}
			
			//print output
			System.out.println(savedRes[0][0]);
		}
	}
	
	public static long match(int seqIndex, int subSeqIndex)
	{
		//base cases
		if(subSeqIndex >= subSeq.length()) //the subsequence is found
		{
			return 1;
		}
		if(seqIndex >= seq.length()) //the subsequence is not found
		{
			return 0;
		}
		if(savedRes[seqIndex][subSeqIndex] != -1) //this subsequence has been found before
		{
			return savedRes[seqIndex][subSeqIndex];
		}
		
		//create return value
		long res = 0;
		
		//add matches from the sequence's next index onwards
		res += match(seqIndex + 1, subSeqIndex);
		
		//if the characters at the indices match, continue on that path
		if(seq.charAt(seqIndex) == subSeq.charAt(subSeqIndex))
		{
			res += match(seqIndex + 1, subSeqIndex + 1);
		}
		
		//return result
		return res;
	}
}