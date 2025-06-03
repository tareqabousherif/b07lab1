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

        Polynomial sum  = p1.add(p2);
        System.out.println("sum(0.1)=" + sum.evaluate(0.1));

        Polynomial prod = p1.multiply(p2);
        System.out.println("prod(1)="  + prod.evaluate(1));

    }
}