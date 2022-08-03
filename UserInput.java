package numbers;

import java.util.*;

import static numbers.Number.Properties.*;

public class UserInput {
    private static Scanner scan = new Scanner(System.in);
    private final String requestMessage = "Enter a request: ";
    private String[] values;
    private int size;
    private Long number;
    private int quantity;
    private Set<Number.Properties> inclusiveProperties;
    private Set<Number.Properties> exclusiveProperties;
    private List<String> wrongProperties;
    private List<String> muatualExclusiveProperties;

    public int getSize() {
        return size;
    }

    public Long getNumber() {
        return number;
    }

    public int getQuantity() {
        return quantity;
    }

    public Set<Number.Properties> getInclusiveProperties() {
        return inclusiveProperties;
    }

    public Set<Number.Properties> getExclusiveProperties() {
        return exclusiveProperties;
    }

    /**
     * Scans the user input, splits it and stores it in the String array values.
     * The number entered by the user is stores as Long in the field number, but not validated.
     * @return this
     */
    UserInput scan() {
        System.out.print(requestMessage);
        this.values = scan.nextLine().split(" ");
        size = values.length;
        this.number = Long.parseLong(values[0]);
        return this;
    }

    /**
     * Validates the user input.
     * If the inputs are valid, the method stores the values in the corresponding fields, except the number.
     * @return true, if all user inputs are valid
     */
    
    boolean isValid() {

        if (number < 0) {
            System.out.println("The first parameter should be a natural number or zero.");
            return false;
        }

        // validates the entered quantity and stores it in the corresponding field if valid
        if (values.length > 1) {
            if (Long.parseLong(values[1]) < 1) {
                System.out.println("The second parameter should be a natural number");
                return false;
            } else {
                quantity = Integer.parseInt(values[1]);
            }
        }

        // initiates the collections for the properties entered by the user and generates error messages based on the entered properties
        if (values.length > 2) {
            inclusiveProperties = new HashSet<>();
            exclusiveProperties = new HashSet<>();
            wrongProperties = new ArrayList<>();
            muatualExclusiveProperties = new ArrayList<>();
            String errorMessage;

            sortProperties();

            if (wrongProperties.size() > 0) {
                errorMessage = wrongProperties.size() > 1 ? "The properties %s are wrong.\n" : "The property %s is wrong.\n";
                System.out.printf(errorMessage, wrongProperties);
                System.out.printf("Available properties: %s\n", Arrays.toString(values()));
                return false;
            }

            findMutalExclusive();

            if (muatualExclusiveProperties.size() > 0) {
                System.out.print("The request contains mutually exclusive properties: ");
                System.out.println(muatualExclusiveProperties);
                return false;
            }
        }
        return true;
    }

    /**
     * validates the entered properties and sorts them in different collections: inclusive, exlusive or wrong
     */
    private void sortProperties() {
        boolean exclusive;
        Number.Properties property;
        String value;

        makePropertiesUpperCase();

        for (int i = 2; i < values.length; i++) {

            // if the user entes a property with the prefix "-", the property is exclusive. The variable stores that knowlegde
            exclusive = values[i].charAt(0) == '-' ? true : false;

            // if the property is exclusive, the prefix "-" is removed
            if (exclusive) {
                value = values[i].substring(1);
            } else {
                value = values[i];
            }

            // check if the entered property is a valid property. If not, the value ist stored in the wrong collection, to be processed by the error message.
            // otherwise the value is stored in the corresponding collection (inclusive or exclusive)
            try {
                property = valueOf(value);
                    if (exclusive) {
                        exclusiveProperties.add(property);
                    } else {
                        inclusiveProperties.add(property);
                    }
                } catch (IllegalArgumentException e) {
                    wrongProperties.add(value);
            }
        }
    }

    /**
     * makes all entered properties uppercase. This way, they can be easier used together with the enum Properties.
     */
    private void makePropertiesUpperCase() {
        for (int i = 2; i < values.length; i++) {
            values[i] = values[i].toUpperCase();
        }
    }

    /**
     * searches for mutual exlusive properties in the collections. If a properties are mutual exclusive, they are stored in the collection muatualExclusiveProperties.
     */
    private void findMutalExclusive() {
        for (Number.Properties property : inclusiveProperties) {
            if (property.mutualExclusive != "" && inclusiveProperties.contains(valueOf(property.mutualExclusive))) {
                muatualExclusiveProperties.add(property.toString());
            }
            if (exclusiveProperties.contains(property)) {
                muatualExclusiveProperties.add(property.toString());
                muatualExclusiveProperties.add("-" + property.toString());
            }
        }

        for (Number.Properties property : exclusiveProperties) {
            if (property.mutualExclusive != "" && exclusiveProperties.contains(valueOf(property.mutualExclusive))) {
                muatualExclusiveProperties.add("-" + property.toString());
            }
        }
    }
}
