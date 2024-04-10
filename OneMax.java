/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class OneMax extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/


	public OneMax(){
		name = "OneMax Problem";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){

		X.rawFitness = 0;
		double totalDistance = 0;
		int end = X.chromo.size() - 1;

		// A valid route must start at the origin.
		totalDistance = totalDistance + calculateDistance(Chromo.origin.x, X.chromo.get(0).x, Chromo.origin.y, X.chromo.get(0).y);

		// Calculate distance for each city. 
		for (int i = 0; i < end; i++) {
			totalDistance = totalDistance + calculateDistance(X.chromo.get(i).x, X.chromo.get(i+1).x, X.chromo.get(i).y, X.chromo.get(i+1).y);
		}
		
		// A valid route must end at the origin.
		totalDistance = totalDistance + calculateDistance(X.chromo.get(end).x, Chromo.origin.x, X.chromo.get(end).y, Chromo.origin.y);
		
		X.rawFitness = totalDistance;
	}

	private double calculateDistance (double x1, double x2, double y1, double y2) {
		double xDifference = (x2 - x1) * (x2 - x1);
		double yDifference = (y2 - y1) * (y2 - y1);
		double distance = Math.sqrt(xDifference + yDifference);

		return distance;
		
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	// public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

	// 	for (int i=0; i<Parameters.numGenes; i++){
	// 		Hwrite.right(X.getGeneAlpha(i),11,output);
	// 	}
	// 	output.write("   RawFitness");
	// 	output.write("\n        ");
	// 	for (int i=0; i<Parameters.numGenes; i++){
	// 		Hwrite.right(X.getPosIntGeneValue(i),11,output);
	// 	}
	// 	Hwrite.right((int) X.rawFitness,13,output);
	// 	output.write("\n\n");
	// 	return;
	// }

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of OneMax.java ******************************************************

