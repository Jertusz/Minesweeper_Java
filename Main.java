public class Main {
    public static void main(String[] args) {
        // Main game loop only for testing purposes
//        Board gierka = new Board(10, 1);
//        Scanner input = new Scanner(System.in);
//        boolean lost = false;
//        boolean won = false;
//        while (!won && !lost) {
//            gierka.drawBoard();
//            System.out.println("Co chcesz zrobic? \n 1. Odkryj pole \n 2. Zaznacz bombe \n 3. Odznacz bombe");
//            int choice = input.nextInt();
//            System.out.println("Podaj x: ");
//            int x = input.nextInt();
//            System.out.println("Podaj y: ");
//            int y = input.nextInt();
//            switch (choice) {
//                case 1:
//                    lost = gierka.unveilField(x, y);
//                    break;
//                case 2:
//                    gierka.markBomb(x, y);
//                    break;
//                case 3:
//                    gierka.unmarkBomb(x, y);
//                    break;
//            }
//            won = gierka.winCondition();
//        }
        GUI.newGame();

    }
}
