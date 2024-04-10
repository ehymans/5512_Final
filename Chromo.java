/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/
	public List<Point> chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;


	// Restricts the city coordinates to a 100x100 grid.
	public static double gridX = 100.0;
	public static double gridY = 100.0;
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	// Random initialization of cities.
	public static List<Point> cities = generateCities(Parameters.geneSize);

	// For the TSP, a valid route must start from the origin and end at the origin.
	public static Point origin = new Point(Search.r.nextDouble() * gridX, Search.r.nextDouble() * gridY);

	// Point class for a coordinate system.
	public static class Point{
		public double x;
		public double y;

		public Point(double x, double y){
			this.x = x; 
			this.y = y;			
		}
	}

	// Generates n (n is correlated directly to the geneSize parameter) cities for the Traveling Salesman Problem.
	public static List<Point> generateCities(int n){
		List<Point> cities = new ArrayList<>();

		for (int i = 0; i < n; i++){
			double x = Search.r.nextDouble() * gridX;
			double y = Search.r.nextDouble() * gridY;
			cities.add(new Point(x,y));
		}
		return cities;
	}

	public Chromo(){

		List<Point> route = new ArrayList<>();
		List<Point> temp = new ArrayList<>(cities);

		// Randomly choose cities to route.
		for (int i = 0; i < Parameters.geneSize; i++) {
			if (!temp.isEmpty()){
				int index = Search.r.nextInt(temp.size());
				route.add(temp.get(index));
				temp.remove(index);
			}
		}

		this.chromo = route;
		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		switch (Parameters.mutationType){

		case 1:     // Bit-Swap; Swaps two cities
			List<Point> swap = new ArrayList<>(this.chromo);

			for (int j = 0; j < this.chromo.size(); j++){
				randnum = Search.r.nextDouble();
				int index1 = Search.r.nextInt(this.chromo.size());
				int index2 = Search.r.nextInt(this.chromo.size());

				if (randnum < Parameters.mutationRate){
					Point temp = swap.get(index1);
					swap.set(index1, swap.get(index2));
					swap.set(index2, temp);
				}
			}
			this.chromo = swap;
			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		double best = 0;
		int index = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:  	//  Tournament Selection
			int tournamentSize = Parameters.popSize;
			for (int i = 0; i < tournamentSize; i++){
				k = Search.r.nextInt(Parameters.popSize);
				if (Search.member[k].proFitness > best) {
					best = Search.member[k].proFitness;
					index = k;
				}
			}
			return index;   

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
	
		switch (Parameters.xoverType){

		case 1:     //  Ordered Crossover for Permutation Representation
			
			// Choose two crossover points at random.
			int start = Search.r.nextInt(Parameters.geneSize);
			int end = Search.r.nextInt(Parameters.geneSize);

			if (start > end) {
				int temp = start;
				start = end;
				end = temp;
			}

			// Generate temp arrays for both children.
			List<Point> childTemp1 = new ArrayList<>(Parameters.geneSize);
			List<Point> childTemp2 = new ArrayList<>(Parameters.geneSize);

			// Fill in the random crossover point-section from the respective parent.
			for (int i = start; i < end; i++) {
				childTemp1.add(parent1.chromo.get(i));
				childTemp2.add(parent2.chromo.get(i));
			}
			
			// Fill in the remaining indexes with the other parents indexes.
			for (Point city : parent1.chromo) {
				if (!childTemp2.contains(city)) childTemp2.add(city);
			}

			for (Point city : parent2.chromo) {
				if (!childTemp1.contains(city)) childTemp1.add(city);
			}

			child1.chromo = childTemp1;
			child2.chromo = childTemp2;

			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
