import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class Polynomial {
	
	double[] coefficients;
	int[] exponents;
	
	
	public Polynomial() {
		
		this.coefficients  = new double[0];
		this.exponents = new int[0];
	}
	
	
	public Polynomial(double[] input) {
		
		int count = 0;
		for (double curr : input) {
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

	    while (indexThis < this.exponents.length) {
	        tempCoefficients[indexResult] = this.coefficients[indexThis];
	        tempExponents[indexResult] = this.exponents[indexThis];
	        indexResult++;
	        indexThis++;
	    }

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

	    if (this.coefficients.length == 0 || A.coefficients.length == 0) {
	        return new Polynomial();
	    }

	    int maxTerms = this.coefficients.length * A.coefficients.length;
	    double[] tempCoeffs = new double[maxTerms];
	    int[] tempExps = new int[maxTerms];
	    int termCnt = 0;

	    for (int i = 0; i < this.coefficients.length; i++) {
	        for (int j = 0; j < A.coefficients.length; j++) {

	            double prodCoeff = this.coefficients[i] * A.coefficients[j];
	            if (prodCoeff == 0) continue;

	            int prodExp = this.exponents[i] + A.exponents[j];
	            int idxFound = findIndex(tempExps, termCnt, prodExp);

	            if (idxFound >= 0) {
	                tempCoeffs[idxFound] += prodCoeff;
	                
	                if (tempCoeffs[idxFound] == 0) {
	                	
	                    for (int jShift = idxFound; jShift < termCnt - 1; jShift++) {
	                        tempCoeffs[jShift] = tempCoeffs[jShift + 1];
	                        tempExps[jShift] = tempExps[jShift + 1];
	                    }
	                    
	                    termCnt--;
	                }
	                
	            } else {
	                tempCoeffs[termCnt] = prodCoeff;
	                tempExps[termCnt] = prodExp;
	                termCnt++;
	            }
	        }
	    }

	    for (int i = 1; i < termCnt; i++) {
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

	    double[] resCoeffs = new double[termCnt];
	    int[] resExps = new int[termCnt];
	    
	    for (int i = 0; i < termCnt; i++) {
	        resCoeffs[i] = tempCoeffs[i];
	        resExps[i] = tempExps[i];
	    }

	    return new Polynomial(resCoeffs, resExps);
	}

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
	    
	    String line = scanner.nextLine().replace(" ", "");
	    scanner.close();
	    
	    if (line.isEmpty()) {
	    	
	        this.coefficients = new double[0];
	        this.exponents = new int[0];
	        return;
	    }
	    
	    line = line.replace("-", "+-");
	    
	    if (line.startsWith("+")) { 
	    	line = line.substring(1); 
	    }

	    String[] rawTerms = line.split("\\+");
	    double[] tempCoeffs = new double[rawTerms.length];
	    int[] tempExps = new int[rawTerms.length];
	    int termCount = 0;

	    for (int t = 0; t < rawTerms.length; t++) {
	    	
	        String term = rawTerms[t].trim();
	        if (term.isEmpty()) {
	        	continue;
	        }

	        double coeff;
	        int exponent;

	        if (term.contains("x")) {
	            String[] parts = term.split("x", 2);
	            String coeffPart = parts[0];
	            
	            if (coeffPart.equals("") || coeffPart.equals("+")) {
	            	coeff = 1.0;
	            }
	            
	            else if (coeffPart.equals("-")) {
	            	coeff = -1.0;
	            }
	            else {
	            	coeff = Double.parseDouble(coeffPart);
	            }

	            if (parts.length == 2 && !parts[1].isEmpty()) {
	            	exponent = Integer.parseInt(parts[1]);
	            }
	            else {
	            	exponent = 1;
	            }
	            
	        } else {
	            coeff = Double.parseDouble(term);
	            exponent = 0;
	        }

	        int position = findIndex(tempExps, termCount, exponent);
	        
	        if (position >= 0) {
	        	
	            tempCoeffs[position] += coeff;
	            if (tempCoeffs[position] == 0) {
	            	
	                for (int shift = position; shift < termCount - 1; shift++) {
	                    tempCoeffs[shift] = tempCoeffs[shift + 1];
	                    tempExps[shift] = tempExps[shift + 1];
	                }
	                
	                termCount--;
	            }
	            
	        } else {
	            if (coeff != 0) {
	                tempCoeffs[termCount] = coeff;
	                tempExps[termCount] = exponent;
	                termCount++;
	            }
	        }
	    }

	    for (int i = 1; i < termCount; i++) {
	    	
	        double keyCoeff = tempCoeffs[i];
	        int keyExp = tempExps[i];
	        int index = i - 1;
	        
	        while (index >= 0 && tempExps[index] > keyExp) {
	            tempCoeffs[index + 1] = tempCoeffs[index];
	            tempExps[index + 1] = tempExps[index];
	            index--;
	        }
	        
	        tempCoeffs[index + 1] = keyCoeff;
	        tempExps[index + 1] = keyExp;
	    }

	    this.coefficients = new double[termCount];
	    this.exponents = new int[termCount];
	    
	    for (int i = 0; i < termCount; i++) {
	        this.coefficients[i] = tempCoeffs[i];
	        this.exponents[i] = tempExps[i];
	    }
	    
	}
	
	// step E
	
	public void saveToFile(String fileName) throws IOException {
	    FileWriter writer = new FileWriter(fileName);
	    
	    if (coefficients.length == 0) {
	        writer.write("0");
	        writer.close();
	        return;
	    }

	    StringBuilder text = new StringBuilder();
	    
	    for (int i = 0; i < coefficients.length; i++) {
	    	
	        double coeff = coefficients[i];
	        int exp = exponents[i];

	        if (i == 0) {
	            if (coeff < 0) {
	            	text.append('-');
	            }
	        } else {
	            if (coeff < 0) {
	            	text.append('-');
	            }
	            else {
	            	text.append('+');
	            }
	        }

	        double absCoeff = Math.abs(coeff);

	        if (exp == 0) {
	            text.append(format(absCoeff));

	        } else {
	            if (absCoeff != 1.0) {
	                text.append(format(absCoeff));
	            }
	            text.append('x');
	            if (exp != 1) {
	            	text.append(exp);
	            }
	        }
	    }

	    writer.write(text.toString());
	    writer.close();

	}

	private String format(double value) {
	    int intVal = (int) value;
	    
	    if (value == intVal) {
	    	
	        return Integer.toString(intVal);
	    } else {
	        return Double.toString(value);
	    }
	}


	
	
}