class Problem {
    public static void main(String[] args) {

        int firstNum = Integer.parseInt(args[1]);
        int secondNum = Integer.parseInt(args[2]);

        switch (args[0]) {
            case "+":
                System.out.println(firstNum + secondNum);
                break;
            case "-":
                System.out.println(firstNum - secondNum);
                break;
            case "*":
                System.out.println(firstNum * secondNum);
                break;
            default:
                System.out.println("Unknown operator");
                break;
        }

    }
}