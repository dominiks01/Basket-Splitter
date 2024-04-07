package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;


/**
 * ILP Solver for MinSetCover Problem
 * Reduction of MinSetCover problem to ILP can be found in Read.me
 * Optimizer used for this service is based on Simplex algorithm for linear programming
 */
public class MinSetCoverILP {
    public static List<Integer> solveSetCoverILP(List<Set<Integer>> sets, Set<Integer> universe) {
        int numSets = sets.size();

        // Coefficients stands for used Sets. Initially we use all sets.
        double[] coefficients = new double[numSets];
        Arrays.fill(coefficients, 1.0);

        // Objective is to minimize number or used sets
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(coefficients, 0);

        // it-h constraint represents it-h item delivery options
        List<LinearConstraint> constraints = new ArrayList<>();

        // Populate delivery options list for all items
        for (int element : universe) {
            double[] constraintCoefficients = new double[numSets];

            for (int i = 0; i < numSets; i++)
                if (sets.get(i).contains(element))
                    constraintCoefficients[i] = 1.0;

            constraints.add(new LinearConstraint(constraintCoefficients, Relationship.GEQ, 1));
        }

        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver solver = new SimplexSolver();

        // Use Simplex algorithm to find optimized solution
        PointValuePair solution = solver.optimize(objectiveFunction, constraintSet, new NonNegativeConstraint(true));

        double[] solutionArray = solution.getPoint();
        List<Integer> chosenSets = new ArrayList<>();

        // Retrieve sets used in our solution
        for (int i = 0; i < solutionArray.length; i++)
            if (solutionArray[i] >= 0.5)
                chosenSets.add(i);

        return chosenSets;
    }
}