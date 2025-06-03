import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Polynomial {
	
	double[] coefficients;
	int[] exponents;
	
	
	public Polynomial() {
		
		this.coefficients  = new double[0];
		this.exponents = new int[0];
	}
	
	
	public Polynomial(double[] input) {
		
		int count = 0;
		for (int i = 0; i < input.length; i++) {
		    double curr = input[i];
		    if (curr != 0) {
		        count++;
		    }
		
		}
		
		if (count == 0) {
	        this.coefficients = new double[0];
	        this.exponents = new int[0];
	        return;
	    }
		
		this.coefficients = new double[count];
		this.exponents = new int[count];
		
		
		int j = 0;
	    for (int i = 0; i < input.length; i++) {
	    	if (input[i] != 0) {
	        	coefficients[j] = input[i];
	        	exponents[j] = i;
	        	j++;
	    	}
	    }
	}
	
	
	private Polynomial(double[] coeffs, int[] exps) {
	    this.coefficients = coeffs;
	    this.exponents = exps;
	}
	
	
	public Polynomial add(Polynomial A) {
		
	    int maxTerms = this.coefficients.length + A.coefficients.length;
	    double[] tempCoefficients = new double[maxTerms];
	    int[] tempExponents = new int[maxTerms];

	    int indexThis = 0;
	    int indexA = 0;
	    int indexResult = 0;
	    
	    // Traverse through both polynomials. Add terms with the same exponent or copy over the smaller one
	    // until one list runs out
	    
	    while (indexThis < this.exponents.length && indexA < A.exponents.length) {

	        int expThis = this.exponents[indexThis];
	        int expA = A.exponents[indexA];

	        if (expThis == expA) {
	            double combined = this.coefficients[indexThis] + A.coefficients[indexA];
	            if (combined != 0) {
	                tempCoefficients[indexResult] = combined;
	                tempExponents[indexResult] = expThis;
	                indexResult++;
	            }
	            
	            indexThis++;
	            indexA++;

	        } else if (expThis < expA) {
	            tempCoefficients[indexResult] = this.coefficients[indexThis];
	            tempExponents[indexResult] = expThis;
	            indexResult++;
	            indexThis++;

	        } else {
	            tempCoefficients[indexResult] = A.coefficients[indexA];
	            tempExponents[indexResult] = expA;
	            indexResult++;
	            indexA++;
	        }
	    }

	    // Copy any remaining terms from this polynomial into the result
	    
	    while (indexThis < this.exponents.length) {
	        tempCoefficients[indexResult] = this.coefficients[indexThis];
	        tempExponents[indexResult] = this.exponents[indexThis];
	        indexResult++;
	        indexThis++;
	    }
	    
	    // Copy any leftover terms from polynomial A into the result

	    while (indexA < A.coefficients.length) {
	        tempCoefficients[indexResult] = A.coefficients[indexA];
	        tempExponents[indexResult] = A.exponents[indexA];
	        indexResult++;
	        indexA++;
	    }

	    double[] resultCoefficients = new double[indexResult];
	    int[] resultExponents = new int[indexResult];

	    for (int i = 0; i < indexResult; i++) {
	        resultCoefficients[i] = tempCoefficients[i];
	        resultExponents[i] = tempExponents[i];
	    }

	    return new Polynomial(resultCoefficients, resultExponents);
	}
	
	
	public double evaluate(double x) {
		
		double result = 0;
		
        for (int i = 0; i < coefficients.length; i++) {
        	result += coefficients[i] * Math.pow(x, exponents[i]);
        }
        
        return result;
	}
	
	
	public boolean hasRoot(double value) {
		
		return evaluate(value) == 0;
	}
	
	// step c
	
	public Polynomial multiply(Polynomial A) {

		
		// If either polynomial is empty return the zero polynomial

	    if (this.coefficients.length == 0 || A.coefficients.length == 0) {
	        return new Polynomial();
	    }

	    int maxTerms = this.coefficients.length * A.coefficients.length;
	    double[] tempCoeffs = new double[maxTerms];
	    int[] tempExps = new int[maxTerms];
	    int termCount = 0;

	    
	    // Use this for loop to multiply every term from both polynomials, merge results
	    // that have the same exponent and drop any that cancel to zero
	    
	    for (int i = 0; i < this.coefficients.length; i++) {
	        for (int j = 0; j < A.coefficients.length; j++) {

	            double prodCoeff = this.coefficients[i] * A.coefficients[j];
	            if (prodCoeff == 0) continue;

	            int prodExp = this.exponents[i] + A.exponents[j];
	            int indexFound = findIndex(tempExps, termCount, prodExp);

	            if (indexFound >= 0) {
	                tempCoeffs[indexFound] += prodCoeff;
	                
	                if (tempCoeffs[indexFound] == 0) {
	                	
	                    for (int shiftIndex = indexFound; shiftIndex < termCount - 1; shiftIndex++) {
	                        tempCoeffs[shiftIndex] = tempCoeffs[shiftIndex + 1];
	                        tempExps[shiftIndex] = tempExps[shiftIndex + 1];
	                    }
	                    
	                    termCount--;
	                }
	                
	            } else {
	                tempCoeffs[termCount] = prodCoeff;
	                tempExps[termCount] = prodExp;
	                termCount++;
	            }
	        }
	    }

	    
	    // Sort the terms in ascending order wrt exponent

	    for (int i = 1; i < termCount; i++) {
	        int keyExp = tempExps[i];
	        double keyCoeff = tempCoeffs[i];
	        int p = i - 1;
	        
	        while (p >= 0 && tempExps[p] > keyExp) {
	            tempExps[p + 1] = tempExps[p];
	            tempCoeffs[p + 1] = tempCoeffs[p];
	            p--;
	        }
	        
	        tempExps[p + 1] = keyExp;
	        tempCoeffs[p + 1] = keyCoeff;
	    }

	    double[] resultCoeffs = new double[termCount];
	    int[] resultExps = new int[termCount];
	    
	    // Copy from temp arrays to result arrays
	    
	    for (int i = 0; i < termCount; i++) {
	        resultCoeffs[i] = tempCoeffs[i];
	        resultExps[i] = tempExps[i];
	    }

	    return new Polynomial(resultCoeffs, resultExps);
	}

	// Helper:
	// Return the index of targetExp in array[0 .. n-1], or -1 if not found


	private int findIndex(int[] array, int len, int targetExp) {
	    for (int i = 0; i < len; i++) {
	        if (array[i] == targetExp) return i;
	    }
	    return -1;
	}

	
	// step d
	

	public Polynomial(File file) throws FileNotFoundException {
		
	    Scanner scanner = new Scanner(file);
	    if (!scanner.hasNextLine()) {
	    	
	        this.coefficients = new double[0];
	        this.exponents = new int[0];
	        scanner.close();
	        return;
	    }
	    
	    String line = scanner.nextLine();
	    scanner.close();
	    
	    if (line.equals("")) {
	    	
	        this.coefficients = new double[0];
	        this.exponents = new int[0];
	        return;
	    }

	    String[] strings = line.split("(?=[+-])");
	    Map<Integer, Double> map = new HashMap<>();

	    for (int i = 0; i < strings.length; i++) {
	        String str = strings[i];
	        
	        
	        double coefficient;
	        int exponent;
	        
	        if (str.contains("x")) {
	        	
	            String[] parts = str.split("x");
	            String coeffPart = parts[0];
	            
	            if (coeffPart.equals("+") || coeffPart.equals("")) {
	            	coefficient = 1.0;
	            }
	            else if (coeffPart.equals("-")) {
	            	coefficient = -1.0;
	            }
	            else {
	            	coefficient = Double.parseDouble(coeffPart);
	            }
	            if (parts.length > 1 && !parts[1].equals("")) {
	            	exponent = Integer.parseInt(parts[1]);
	            }
	            else {
	            	exponent = 1;
	            }
	        } else {
	            coefficient = Double.parseDouble(str);
	            exponent = 0;
	        }
	        
	        double keyExists = 0.0;
	        if (map.containsKey(exponent)) {
	            keyExists = map.get(exponent);
	        }
	        
	        double sum = keyExists + coefficient;
	        if (sum == 0.0) {
	        	map.remove(exponent);
	        }
	        else {
	        	map.put(exponent, sum);
	        }
	    }

	    if (map.isEmpty()) {
	        this.coefficients = new double[0];
	        this.exponents = new int[0];
	        return;
	    }

	    List<Integer> sortedExps = new ArrayList<>(map.keySet());
	    Collections.sort(sortedExps);

	    int n = sortedExps.size();
	    this.coefficients = new double[n];
	    this.exponents = new int[n];
	    
	    for (int i = 0; i < n; i++) {
	        int e = sortedExps.get(i);
	        this.exponents[i] = e;
	        this.coefficients[i] = map.get(e);
	    }
	    
	}

	
	// step E
	
	public void saveToFile(String givenFile) throws IOException {
		
	    FileWriter file = new FileWriter(givenFile); // File being written to
	    
	    // If empty, return
	    
	    if (coefficients.length == 0) {
	        file.write("0");
	        file.close();
	        return;
	    }
	    
	    String output = "";
	    for (int i = 0; i < coefficients.length; i++) {
	    	
	        double coeff = coefficients[i];
	        int exp = exponents[i];
	        if (i == 0) {
	        	
	        	// If first term is negative
	        	
	            if (coeff < 0) { 
	                output = output + "-";
	            }
	            
	          // If subsquent terms are negative
	            
	        } else { 
	        	
	            if (coeff < 0) {
	                output = output + "-";
	                
	                // If positive
	                
	            } else { 
	                output = output + "+";
	            }
	        }
	        
	        double absCoeff = Math.abs(coeff);
	        if (exp == 0) {
	        	
	            int intValue = (int) absCoeff;
	            if (absCoeff == intValue) {
	                output = output + intValue;
	                
	            } else {
	                output = output + absCoeff;
	            }
	            
	            // Else a term with x
	            
	        } else { 
	            if (absCoeff != 1.0) {
	                int intValue = (int) absCoeff;
	                
	                if (absCoeff == intValue) {
	                    output = output + intValue;
	                } else {
	                    output = output + absCoeff;
	                }
	            }
	            
	            output = output + "x"; 
	            if (exp != 1) {
	                output = output + exp;
	            }
	        }
	    }
	    
	    file.write(output);
	    file.close();
	}

}