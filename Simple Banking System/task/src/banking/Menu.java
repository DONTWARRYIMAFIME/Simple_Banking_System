package banking;

public abstract class Menu {

    private final String information;
    private boolean exitMenu = false;
    private boolean exitProgram = false;

    abstract void firstMethod();
    abstract void secondMethod();

    void thirdMethod()  {}
    void fourthMethod() {}
    void fifthMethod()  {}

    public Menu(String information) {
        this.information = information;
    }

    void printInformation() {
        System.out.println(information);
    }

    void setExitMenu() {
        exitMenu = true;
    }

    void setExitProgram() {
        exitProgram = true;
    }

    public boolean isExitMenu() {
        return exitMenu;
    }

    public boolean isExitProgram() {
        return exitProgram;
    }
}