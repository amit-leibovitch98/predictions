package menu;

import facade.EngineFacade;
import simulation.Simulation;
import simulation.SimulationManager;
import simulation.world.detail.environmentvariables.EnvironmentVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private MenuOption menuOption;
    private final EngineFacade engine;
    private final Scanner scanner;
    private SimulationManager simulationManager;

    public Menu() {
        menuOption = MenuOption.INIT_VALUE;
        engine = new EngineFacade();
        scanner = new Scanner(System.in);
        this.simulationManager = SimulationManager.getInstance();
    }

    public void printMenu() {
        int choice = 0;
        while (menuOption != MenuOption.EXIT) {
            System.out.println(menuOption.getDescription());
            System.out.println(MenuOption.READ_FILE.getValue() + ". " + MenuOption.READ_FILE.getDescription());
            System.out.println(MenuOption.SHOW_SIMULATION_DETAILS.getValue() + ". " + MenuOption.SHOW_SIMULATION_DETAILS.getDescription());
            System.out.println(MenuOption.START_SIMULATION.getValue() + ". " + MenuOption.START_SIMULATION.getDescription());
            System.out.println(MenuOption.SHOW_PAST_SIMULATION_RESULT.getValue() + ". " + MenuOption.SHOW_PAST_SIMULATION_RESULT.getDescription());
            System.out.println(MenuOption.EXIT.getValue() + ". " + MenuOption.EXIT.getDescription());
            System.out.println("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                menuOption = MenuOption.fromValue(choice);
                switch (menuOption) {
                    case READ_FILE:
                        readFile();
                        break;
                    case SHOW_SIMULATION_DETAILS:
                        engine.showSimulationDetails();
                        break;
                    case START_SIMULATION:
                        startSimulation();
                        break;
                    case SHOW_PAST_SIMULATION_RESULT:
                        showPastSimulationResult();
                        break;
                    case EXIT:
                        break;
                    default:
                        System.out.println(MenuOption.INVALID.getDescription());
                        menuOption = MenuOption.INIT_VALUE;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }

        }
    }


    private void readFile() {
        String path = "";
        while(!engine.isReadFileFromPathSuccess()) {
            System.out.println("Enter file path: ");
            path = scanner.nextLine();
            try {
                engine.readFile(path);
            } catch (Exception e) {
                System.out.println("Invalid file path, please try again.");
            }
        }
    }

    private void startSimulation() {
        String updateEnvVars = "y";
        while (updateEnvVars.equals("y")) {
            System.out.println("Do you want to update environment variables? (y/n)");
            updateEnvVars = scanner.next();
            if (updateEnvVars.equals("y")) {
                userUpdateEnvVars();
            }
        }
        System.out.println("Starting simulation...");
        printEnvVars();
        Simulation currSimulation = engine.startSimulation();
        System.out.println("Simulation finished.");
        simulationManager.saveResults(currSimulation);
        scanner.nextLine();
    }

    private void printEnvVars() {
        System.out.println("Environment variables:");
        for (EnvironmentVariable envVar : engine.getEnvironmentVariables()) {
            System.out.println(envVar.getName() + ": " + envVar.getValue());
        }
    }

    private void userUpdateEnvVars() {
        EnvironmentVariable envVar;
        System.out.println("Enter the numbe of the environment variable number you want to update: ");
        for (int i = 1; i <= engine.getEnvironmentVariables().size(); i++) {
            envVar = engine.getEnvironmentVariables().get(i - 1);
            System.out.println(i + "." + envVar.getName() + " (" + envVar.getType().toString() + ")" + ":");
        }
        int userChoice = scanner.nextInt();
        while (userChoice < 1 || userChoice > engine.getEnvironmentVariables().size()) {
            System.out.println("Invalid input");
            return;
        }
        System.out.println("Enter new value: ");
        Object newValue = scanner.nextInt();
        while(!engine.getEnvironmentVariables().get(userChoice - 1).isValid(newValue)) {
            System.out.println("Invalid input");
            newValue = scanner.nextInt();
        }
        engine.getEnvironmentVariables().get(userChoice - 1).setValue(newValue);
    }

    private void showPastSimulationResult() {
        String resType, guid, entityName, propertyName;
        guid = getSimulationGuidFromUser();
        System.out.println("What whould you like to see the in results?");
        System.out.println("1. Entities");
        System.out.println("2. Histogram");
        resType = scanner.nextLine();
        while (!resType.equals("1") && !resType.equals("2")) {
            System.out.println("Invalid input, try again");

        }
        if (resType.equals("1")) {
            simulationManager.getSimulationResultByEntities(guid);
        } else if (resType.equals("2")) {
            entityName = getEntityNameFromUser(guid);
            propertyName = getPropertyNameFromUser(guid, entityName);
            Map<Object, Integer> histogram = simulationManager.getSimulationResultByHistogram(guid, entityName, propertyName);
            for (Map.Entry<Object, Integer> entry : histogram.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    private String getSimulationGuidFromUser() {
        List<Simulation> pastSimulationsResults = engine.getPastSimulationResult();
        if(pastSimulationsResults.size() == 0) {
            System.out.println("No past simulations");
            return null;
        }
        for (int i = 1; i <= pastSimulationsResults.size(); i++) {
            System.out.println(i + ". " + pastSimulationsResults.get(i - 1).getGuid());
        }
        System.out.println("Choose simulation: ");
        int userChoice = Integer.parseInt(scanner.nextLine());
        while (userChoice < 1 || userChoice > pastSimulationsResults.size()) {
            System.out.println("Invalid input");
            return null;
        }
    return pastSimulationsResults.get(userChoice - 1).getGuid();
    }

    private String getEntityNameFromUser(String guid) {
        System.out.println("Choose Entity: ");
        int input = -1;
        for (int i = 1; i <= this.simulationManager.getEntitiesNamesForSimulation(guid).size(); i++) {
            System.out.println(i + ". " + this.simulationManager.getEntitiesNamesForSimulation(guid).get(i - 1));
        }
        input = Integer.parseInt(scanner.nextLine());
        while (input < 1 || input > this.simulationManager.getEntitiesNamesForSimulation(guid).size()) {
            System.out.println("Invalid input, try again");
            input = Integer.parseInt(scanner.nextLine());
        }
        return this.simulationManager.getEntitiesNamesForSimulation(guid).get(input - 1);
    }

    private String getPropertyNameFromUser(String guid, String entityName) {
        System.out.println("Choose Entity Property: ");
        int input = -1;
        for (int i = 1; i <= this.simulationManager.getEntityPropertiesNamesForSimulation(guid, entityName).size(); i++) {
            System.out.println(i + ". " + this.simulationManager.getEntityPropertiesNamesForSimulation(guid, entityName).get(i - 1));
        }
        input = Integer.parseInt(scanner.nextLine());
        while (input < 1 || input > this.simulationManager.getEntityPropertiesNamesForSimulation(guid, entityName).size()) {
            System.out.println("Invalid input, try again");
            input = Integer.parseInt(scanner.nextLine());
        }
        return this.simulationManager.getEntityPropertiesNamesForSimulation(guid, entityName).get(input - 1);
    }
}
