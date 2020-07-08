package solver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Complex {
    public final static Complex ONE = new Complex(1);
    public final static Complex ZERO = new Complex(0);
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    public Complex(double real) {
        this.real = real;
    }

    public Complex(String xyi) {
        xyi = xyi.replace("-i","-1i");
        Matcher matcherImaginary = Pattern.compile("([\\+\\-]?\\d+(\\.\\d+)?)?i").matcher(xyi);
        String realPart;
        if (matcherImaginary.find()) {
            String imagPart = matcherImaginary.group();
            if (imagPart.replace("i", "").length() == 0) {
                this.imaginary = 1;
            } else {
                this.imaginary = Double.parseDouble(imagPart.replace("i", ""));
            }
            realPart = xyi.replace(imagPart, "").replace("+","");
        } else {
            this.imaginary = 0;
            realPart = xyi;
        }
        if (realPart.length() == 0) {
            this.real = 0;
        } else {
            this.real = Double.parseDouble(realPart);
        }
    }

    public Complex add(Complex number) {
        return new Complex(this.real + number.real, this.imaginary + number.imaginary);
    }

    public Complex subtract(Complex number) {
        return new Complex(this.real - number.real, this.imaginary - number.imaginary);
    }

    public Complex multiply(Complex number) {
        double realPart = this.real * number.real - this.imaginary * number.imaginary;
        double imagPart = this.real * number.imaginary + this.imaginary * number.real;
        return new Complex(realPart, imagPart);
    }

    public Complex conjugate() {
        return new Complex(this.real, -1 * this.imaginary);
    }

    public Complex divide(Complex number) {
        double a = number.real;
        double b = number.imaginary;
        double realPart = a / (a * a + b * b);
        double imagPart = - b / (a * a + b * b);
        return this.multiply(new Complex(realPart, imagPart));
    }

    @Override
    public String toString() {
        if (imaginary != 0) {
            StringBuilder sb = new StringBuilder();
            if (real == 0) {
                if (imaginary == -1) {
                    sb.append("-");
                }
            } else {
                sb.append(real);
                if (imaginary > 0) {
                    sb.append("+");
                } else if (imaginary == -1) {
                    sb.append("-");
                }
            }
            if (Math.abs(imaginary) != 1) {
                sb.append(imaginary);
            }
            sb.append("i");
            return sb.toString();
        } else {
            return Double.toString(real);
        }
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public boolean equals(Complex obj) {
        return this.real == obj.real && this.imaginary == obj.imaginary;
    }
}
