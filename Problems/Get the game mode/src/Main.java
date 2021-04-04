class Problem {
    public static void main(String[] args) {

        String gameMode = "default";

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("mode")) {
                gameMode = args[i + 1];
                break;
            }
        }

        System.out.println(gameMode);
    }
}