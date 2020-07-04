package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        MySet<Integer> set = new MySet<>();

        int a;
        System.out.println("Введите целые числа. В конце ввода поставьте точку: ");
        do{
            a = scanner.nextInt();
            set.add(a);
        } while (scanner.hasNextInt());
        scanner.next();

        System.out.println("Результат :");
        ArrayList<Integer> listOutput = set.getArrayList();
        for (int item : listOutput) {
            System.out.print(item + "; ");
        }
    }
}
