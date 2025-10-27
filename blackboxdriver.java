import java.util.*;

public class blackboxdriver // OK EAT THIS WORKS
{
    public static void main(String[] args)
    {
        System.out.println("Welcome to Blackbox Game!");
        System.out.println("Find all 5 hidden atoms in the grid.");
        System.out.println("Enter a border number (1-40) to shoot a ray from that position");
        System.out.println("or enter 0 to guess an atom location.");
        System.out.println("You have 30 guesses to find all atoms. Good luck!");
        
        blackbox game = new blackbox();
        game.fillGrid(5);

        String prevMoves = "";
        String wrongAtomGuesses = "";
        
        Scanner scan = new Scanner(System.in);

        while (game.getNumAtomsFound() < game.getNumAtoms() && game.getNumGuessesLeft() > 0) // need to come up w a better condition
        {
            System.out.println("------------------------------------------\nCurrent Board:");
            game.printBoard(); // print the board
            System.out.println("\nScore: " + game.getNumGuessesLeft());
            System.out.println("\nAtoms found: " + game.getNumAtomsFound() + "/" + game.getNumAtoms());
            System.out.println("\nPrevious moves: " + prevMoves);
            System.out.println("\nWrong atom guesses: " + wrongAtomGuesses);
            System.out.print("\nEnter border position (1-40) or 0 to guess: ");
            
            try {
                if (!scan.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scan.next(); // clear invalid input
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scan.next(); // clear invalid input
                continue;
            }

            int position = scan.nextInt();
            boolean hit = false;

            if (position >= 1 && position <= 40)
            {
                System.out.println("Firing a ray!"); // fire a ray
                game.decrementNumGuessesLeft(1);
                // Initialize starting position and direction
                int r = -1, c = -1;
                char dir = ' ';
                
                // Convert border position to starting coordinates and direction
                if (position <= 10) { // Left border (1-10)
                    r = position - 1;
                    c = -1;
                    dir = 'R'; // moving right
                } else if (position <= 20) { // Bottom border (11-20)
                    r = 10;
                    c = position - 11;
                    dir = 'U'; // moving up
                } else if (position <= 30) { // Right border (21-30)
                    r = 30 - position;
                    c = 10;
                    dir = 'L'; // moving left
                } else { // Top border (31-40)
                    r = -1;
                    c = 40 - position;
                    dir = 'D'; // moving down
                }
                
                // Simulate ray movement
                while (true) {
                    // Move one step in current direction
                    switch (dir) {
                        case 'R': c++; break;
                        case 'L': c--; break;
                        case 'D': r++; break;
                        case 'U': r--; break;
                    }
                    
                    // Check if ray exited grid
                    if (r < 0 || r >= 10 || c < 0 || c >= 10) {
                        break;
                    }
                    
                    // Check for interactions
                    if (game.isHit(r, c)) {
                        hit = true;
                        System.out.println("Ray did not exit the grid");
                        prevMoves += "\nIn: " + position + " Absorbed";
                        break;
                    }
                    
                    // Check and handle reflections/deflections
                    char newDir = game.checkInteractions(r, c, dir);
                    if (newDir == 'S') {
                        hit = true;
                        prevMoves += "\nIn: " + position + " Out: " + position + " ";
                        break; // Ray reflected, stop processing
                    } else if (newDir != dir) {
                        dir = newDir; // Update direction if it changed
                        continue;
                    }
                }

                // Calculate exit position
                if (!hit)
                {
                    System.out.println("Ray exited at position: (" + r + ", " + c + ") with direction " + dir);

                    if (r < 0) { // exited top (row -1)
                        System.out.println(String.valueOf(40 - c)); // 31-40 right to left
                        prevMoves += "\nIn: " + position + " Out: " + (40 - c) + " ";
                    } else if (r >= 10) { // exited bottom
                        System.out.println(String.valueOf(11 + c)); // 11-20 left to right
                        prevMoves += "\nIn: " + position + " Out: " + (11 + c) + " ";
                    } else if (c < 0) { // exited left
                        System.out.println(String.valueOf(r + 1)); // 1-10 top to bottom
                        prevMoves += "\nIn: " + position + " Out: " + (r + 1) + " ";
                    } else { // exited right
                        System.out.println(String.valueOf(30 - r)); // 21-30 bottom to top
                        prevMoves += "\nIn: " + position + " Out: " + (30 - r) + " ";
                    }
                }
            }
            if (position == 0)
            {
                System.out.println("Enter the row number of your guessed atom position (1-10)");
                int row = scan.nextInt();
                System.out.println("Enter the column number of your guessed atom position (11-20)");
                int col = scan.nextInt();
                if ((game.getAtomGrid())[row-1][(col-1)%10] == 1) // to account for differences in indexing
                {
                    if (game.incrementNumAtomsFound(1) == game.getNumAtoms())
                    {
                        System.out.println("------------------------------------------");
                        System.out.println("CONGRATS! You have found all atoms!!!");
                        game.updateUserGrid(row-1, (col-1)%10, 'A'); // update user grid with found atom
                        System.out.println("Your final score is: " + game.getNumGuessesLeft());
                        break;
                    }
                    else
                    {
                        System.out.println("Congrats! You have found an atom!");
                        game.updateUserGrid(row-1, (col-1)%10, 'A'); // update user grid with found atom
                    }
                }
                else
                {
                    System.out.println("Unfortunately, your guess was wrong. You have " + game.decrementNumGuessesLeft(1) + " guesses left. Try again next time!");
                    wrongAtomGuesses += "\nRow: " + row + " Col: " + col + " ";
                }
            }
        }
        System.out.println("The final board is:"); // user seees board at the end no matter if they win or lose
        game.printAtomBoard(); // print the final board
        System.out.println("Game over. See you next time!");
        scan.close();
        
    }
}