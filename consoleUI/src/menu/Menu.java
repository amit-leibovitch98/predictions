package menu;

import facade.EngineFacade;
import java.util.Scanner;

public class Menu {
    private MenuOption menuOption;
    private final EngineFacade engine;
    private final Scanner scanner;

    public Menu() {
        menuOption = MenuOption.INIT_VALUE;
        engine = new EngineFacade();
        scanner = new Scanner(System.in);
    }

    public void printMenu() {
        while (menuOption != MenuOption.EXIT) {
            System.out.println(menuOption.getDescription());
            System.out.println(MenuOption.READ_FILE.getValue() + ". " + MenuOption.READ_FILE.getDescription());
            System.out.println(MenuOption.SHOW_SIMULATION_DETAILS.getValue() + ". " + MenuOption.SHOW_SIMULATION_DETAILS.getDescription());
            System.out.println(MenuOption.START_SIMULATION.getValue() + ". " + MenuOption.START_SIMULATION.getDescription());
            System.out.println(MenuOption.SHOW_PAST_SIMULATION_RESULT.getValue() + ". " + MenuOption.SHOW_PAST_SIMULATION_RESULT.getDescription());
            System.out.println(MenuOption.EXIT.getValue() + ". " + MenuOption.EXIT.getDescription());
            System.out.println("Enter your choice: ");
            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
            menuOption = MenuOption.fromValue(choice);
            switch (menuOption) {
                case READ_FILE:
                    readFile();
                    break;
                case SHOW_SIMULATION_DETAILS:
                    engine.ShowSimulationDetails();
                    break;
                case START_SIMULATION:
                    engine.StartSimulation();
                    break;
                case SHOW_PAST_SIMULATION_RESULT:
                    engine.ShowPastSimulationResult();
                    break;
                case EXIT:
                    break;
                case INVALID:
                    System.out.println(MenuOption.INVALID.getDescription());
                default:
                    System.out.println(MenuOption.INVALID.getDescription());
            }
        }
    }
    private void readFile(){
        System.out.println("Enter file path: ");
        String path = scanner.nextLine();
        engine.ReadFile(path);
    }
}
