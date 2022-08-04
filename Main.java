package numbers;

import java.util.Collections;
import numbers.Number.*;

public class Main {
    public static void main(String[] args) {
        //write your code here
        String welcome = "Welcome to Amazing Numbers!\n";
        String description = "Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.";

        UserInput userInput = new UserInput();

        Number number;

        System.out.println(welcome);
        System.out.println(description);

        do {
            if (userInput.scan().isValid()) {
                if (userInput.getNumber() == 0) {
                    System.out.println("Goodbye!");
                    userInput.close();
                } else {
                    number = new Number(userInput.getNumber());
                    number.setProperties();
                    if (userInput.getSize() < 2) {
                        System.out.println(listPropertiesVertically(number));
                    } else if (userInput.getSize() < 3) {
                        for (int i = 0; i < userInput.getQuantity(); i++) {
                            System.out.println(listPropertiesInRow(number));
                            number = new Number(number.VALUE + 1).setProperties();
                        }
                    } else if (userInput.getSize() >= 3) {
                        int count = 0;
                        while (count < userInput.getQuantity()) {
                            // test whether all inclusive properties of the request are included in the number's properties
                            boolean containsAll = number.getProperties().containsAll(userInput.getInclusiveProperties());
                            // test whether all exclusive properties of the request are not present int the number's properties
                            boolean excludesAll = Collections.disjoint(number.getProperties(), userInput.getExclusiveProperties());
                            // if both tests are true, the number's properties are printed
                            if (containsAll && excludesAll) {
                                System.out.println(listPropertiesInRow(number));
                                count++;
                            }
                            number = new Number(number.VALUE + 1).setProperties();
                        }
                    }
                }
            }
        } while (userInput.getNumber() != 0);
    }

    static String listPropertiesVertically(Number number) {
        StringBuilder str = new StringBuilder();

        str.append("Properties of ").append(number.VALUE).append("\n");

        for (Properties value : Properties.values()) {
            str.append(value).append(": ").append(number.getProperties().contains(value)).append("\n");
        }

        return str.toString();
    }

    static String listPropertiesInRow(Number number) {
        StringBuilder properties = new StringBuilder();

        for (Properties property : number.getProperties()) {
            properties.append(property).append(", ");
        }
        properties.deleteCharAt(properties.lastIndexOf(","));
        return number.VALUE + " is " + properties;
    }
}

