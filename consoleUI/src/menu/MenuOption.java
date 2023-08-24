package menu;

public enum MenuOption {
    INIT_VALUE(0, "Select option:"),
    READ_FILE(1, "Read file"),
    SHOW_SIMULATION_DETAILS(2, "Show simulation details"),
    START_SIMULATION(3, "Start simulation"),
    SHOW_PAST_SIMULATION_RESULT(4, "Show past simulation result"),
    EXIT(9, "Exit"),
    INVALID(-1, "Invalid input");

    private final int value;
    private final String description;

    MenuOption(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static MenuOption fromValue(int value) {
        for (MenuOption option : MenuOption.values()) {
            if (option.value == value) {
                return option;
            }
        }
        return INVALID;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
