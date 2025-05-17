public class Polynomial {
	
	
	double[] coefficients;
	
	/*
	 * ii. It has a no-argument constructor that sets the polynomial to zero (i.e. the
	 * corresponding array would be [0])
	 */
	
	public Polynomial() {
		
		coefficients = new double[] {0};
	}
	
	/*
	 * iii. It has a constructor that takes an array of double as an argument and sets the
	 * coefficients accordingly
	 * */
	
	public Polynomial(double[] input) {
		
		coefficients = new double[input.length];
		
	    for (int i = 0; i < input.length; i++) {
	    	
	        coefficients[i] = input[i];
	    }
	}
	
	/*
	 * iv. It has a method named add that takes one argument of type Polynomial and
	 * returns the polynomial resulting from adding the calling object and the argument
	 * */
	
	public Polynomial add(Polynomial A) {
		
		int max = Math.max(A.coefficients.length, coefficients.length);
				
		double[] sum = new double[max];
		
		for (int i = 0; i < max; i++) {
			
	        double a = 0;
	        double b = 0;

	        if (i < A.coefficients.length) {
	        	
	            a = A.coefficients[i];
	        }

	        if (i < coefficients.length) {
	        	
	            b = coefficients[i];
	        }

	        sum[i] = a + b;
	    }
		
		return new Polynomial(sum);
	}
	
	/*
	 * v. It has a method named evaluate that takes one argument of type double
	 * representing a value of x and evaluates the polynomial accordingly. For example,
	 * if the polynomial is 6 − 2𝑥 + 5𝑥 % and evaluate(-1) is invoked, the result should
	 * be 3
	 * */
	
	public double evaluate(double x) {
		
		double result = 0;
		
        for (int i = 0; i < coefficients.length; i++) {
        	
            result += coefficients[i] * Math.pow(x, i);
        }
        
        return result;
		
	}
	
	/*
	 * vi. It has a method named hasRoot that takes one argument of type double and
	 * determines whether this value is a root of the polynomial or not. Note that a root
	 * is a value of x for which the polynomial evaluates to zero.
	 * */
	
	public boolean hasRoot(double value) {
		
		return evaluate(value) == 0;
	}
	
}