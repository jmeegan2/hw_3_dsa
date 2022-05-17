import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class B1_red_black_tree {
    public static void main(String[] args) throws IOException {
        Path filePath = Paths.get("src/positive_integers.txt");
        Scanner scanner = new Scanner(filePath);
        List<Integer> integers = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                integers.add(scanner.nextInt());
            } else {
                scanner.next();
            }

        }
        System.out.println(integers);
    }
}
