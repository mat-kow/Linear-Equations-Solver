package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
//    private static Deque<int[]> stack = new ArrayDeque<>();

    public static void main(String[] args) {
        String importFileName = "in.txt";
        String exportFileName = "out.txt";

        int eqNo = 0;
        int varNo = 0;
        Complex[][] matrix = new Complex[1][1];
        for (int i = 0; i < args.length; i += 2) {
            String arg = args[i];
            if (arg.equals("-in")) {
                importFileName = args[i + 1];
            } else if (arg.equals("-out")) {
                exportFileName = args[i + 1];
            }
        }
        File file = new File(importFileName);

        try (Scanner scanner = new Scanner(file)) {
            varNo = scanner.nextInt();
            eqNo = scanner.nextInt();
            scanner.nextLine();
            matrix = new Complex[eqNo][varNo + 1];
            for (int i = 0; i < eqNo; i++) {
                String[] line = scanner.nextLine().trim().split(" ");
                for (int j = 0; j < varNo + 1; j++) {
                    matrix[i][j] = new Complex(line[j]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found\n");
        }

        printMatrixComplex(matrix);
        System.out.println("\n\n\n");


        for (int i = 0; i < Math.min(varNo, eqNo); i++) {
            int colNo = Math.min(i, varNo - 1);
            if (matrix[i][colNo].equals(new Complex(0))) {
                if (!findAndSwapRow(matrix, i)) { // no row with no-zero
                    if (!findAndSwapCol(matrix, colNo)) { // no col with no-zero
                        for (int k = i + 1; k < eqNo; k++) {
                            swapRows(matrix, i, k);
                            if (findAndSwapCol(matrix, colNo)) {
                                break;
                            }
                            swapRows(matrix, i, k);
                        }
                        for (int m = i; m < matrix.length; m++) {
                            if (!matrix[m][matrix[m].length - 1].equals(Complex.ZERO)){
                                saveNoSolution(exportFileName);
                                System.out.println("zero");
                                return;
                            }
                        }
                        saveInfiniteSolutions(exportFileName);
                        System.out.println("infi");
                        return;
                    }
                }
            }
            makeOneOnDiagon(matrix, i);
            for (int j = i + 1; j < eqNo; j++) {
                subtract(i, j, matrix);
            }
        }

        for (int i = Math.min(varNo, eqNo) - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                subtract(i, j, matrix);
            }
        }

        if (eqNo > varNo) {
            for (int i = varNo; i < eqNo; i++) {
                if (!matrix[i][matrix[i].length - 1].equals(Complex.ZERO)) {
                    saveNoSolution(exportFileName);
                    System.out.println("No solution");
                    return;
                }
            }
        }
        if (varNo > eqNo) {
            System.out.println("Infinitely many solutions");
            saveInfiniteSolutions(exportFileName);
            return;
        }


        printMatrixComplex(matrix);
//        while (!stack.isEmpty()) {
//            System.out.println(Arrays.toString(stack.pollLast()));
//        }

        //1 solution
        saveToFile(exportFileName, matrix);

    }

    static void subtract(int rowWith1, int targetRow, Complex[][] matrix) {
        Complex multiplier = matrix[targetRow][rowWith1];
        for (int i = 0; i < matrix[rowWith1].length; i++) {
            matrix[targetRow][i] = matrix[targetRow][i].subtract(matrix[rowWith1][i].multiply(multiplier));
        }
    }

    static void makeOneOnDiagon(Complex[][] matrix, int rowIndex) {
        Complex multiplier = Complex.ONE.divide(matrix[rowIndex][rowIndex]);
        for (int i = 0; i < matrix[rowIndex].length; i++) {
            matrix[rowIndex][i] = multiplier.multiply(matrix[rowIndex][i]);
        }
    }

    static void saveToFile(String fileName, Complex[][] matrix) {
        File file = new File(fileName);
        int freeIndex = matrix[0].length - 1;
        int noVar = Math.min(matrix.length, matrix[0].length - 1);
        try (FileWriter writer = new FileWriter(file, false)) {
            for (int i = 0; i < noVar; i++) {
                String line = matrix[i][freeIndex].toString() + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            System.out.printf("\nAn exception occurs %s\n", e.getMessage());
        }
    }

    static void saveNoSolution(String fileName) {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file, false)) {
            String line = "No solutions";
            writer.write(line);
        } catch (IOException e) {
            System.out.printf("\nAn exception occurs %s\n", e.getMessage());
        }
    }

    static void saveInfiniteSolutions(String fileName) {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file, false)) {
            String line = "Infinitely many solutions";
            writer.write(line);
        } catch (IOException e) {
            System.out.printf("\nAn exception occurs %s\n", e.getMessage());
        }
    }

    static boolean findAndSwapRow(Complex[][] matrix, int targetRowIndex) {
        for (int i = targetRowIndex + 1; i < matrix.length; i++) {
            int colInd = Math.min(targetRowIndex, matrix[i].length - 1);
            if (!matrix[i][colInd].equals(Complex.ZERO)) {
                swapRows(matrix, colInd, i);
                return true;
            }
        }
        return false;
    }

    static boolean findAndSwapCol(Complex[][] matrix, int targetColIndex) {
        for (int i = targetColIndex + 1; i < matrix[targetColIndex].length - 1; i++) {
            if (!matrix[targetColIndex][i].equals(Complex.ZERO)) {
                swapCol(matrix, targetColIndex, i);
//                stack.addLast(new int[]{targetColIndex, i});
                return true;
            }
        }
        return false;
    }

    static void swapRows (Complex[][] matrix, int row1, int row2) {
        Complex[] tempRow = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = tempRow;
    }

    static void swapCol(Complex[][] matrix, int col1, int col2) {
        for (int i = 0; i < matrix.length; i++) {
            Complex tempVal = matrix[i][col1];
            matrix[i][col1] = matrix[i][col2];
            matrix[i][col2] = tempVal;
        }
    }

    static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    static void printMatrixComplex(Complex[][] matrix) {
        for (Complex[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
