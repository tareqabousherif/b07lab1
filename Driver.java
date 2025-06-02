import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Driver {

    public static void main(String[] args) {

        Polynomial zero = new Polynomial();
        double[] d1 = {6, 0, 0, 5};
        double[] d2 = {0, -2, 0, 0, -9};
        Polynomial p1 = new Polynomial(d1);
        Polynomial p2 = new Polynomial(d2);

        System.out.println("p1(2)=" + p1.evaluate(2));
        System.out.println("p2(-1)=" + p2.evaluate(-1));
        System.out.println("zero(10)=" + zero.evaluate(10));

        if (p1.hasRoot(-6.0 / 5.0)) {
            System.out.println("-6/5 is a root of p1");
        } else {
            System.out.println("-6/5 is NOT a root of p1");
        }

        Polynomial sum = p1.add(p2);
        System.out.println("sum(0.1)=" + sum.evaluate(0.1));

        Polynomial prod = p1.multiply(p2);
        System.out.println("prod(1)=" + prod.evaluate(1));

        try {
            String outFile = "poly.txt";
            prod.saveToFile(outFile);
            Polynomial fromFile = new Polynomial(new File(outFile));
            System.out.println("fromFile(1)=" + fromFile.evaluate(1));
            double[] xs = {-2, -1, 0, 1, 2};

            for (double x : xs) {

                double a = prod.evaluate(x);
                double b = fromFile.evaluate(x);

                if (Math.abs(a - b) < 1e-9) {
                    System.out.println("OK at x=" + x);
                } else {
                    System.out.println("Mismatch at x=" + x);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not read the file: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}

/*
 * Expected output:
 *      p1(2)=46.0
 *      p2(-1)=-7.0
 *      zero(10)=0.0
 *      -6/5 is NOT a root of p1
 *      sum(0.1)=5.8041
 *      prod(1)=-121.0
 *      Saved product to poly.txt
 *      fromFile(1)=-121.0
 *      OK at x=-2.0
 *      OK at x=-1.0
 *      OK at x=0.0
 *      OK at x=1.0
 *      OK at x=2.0
 */