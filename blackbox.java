public class blackbox
{
    private int[][] atomGrid;
    //private int[][] userGrid;
    private int numAtoms = 5;
    private int numRaysFired = 0;
    private int numAtomsFound = 0;
    private char [][] userGrid; // for user input
    private int numGuessesLeft = 30;
    
    // default constructor
    public blackbox()
    {
        atomGrid = new int[10][10];
        userGrid = new char[10][10];
        for (int i = 0; i < userGrid.length; i++)
            for (int j = 0; j < userGrid[i].length; j++)
                userGrid[i][j] = '.';
    }
    
    // parameterized constructor
    public blackbox(int n)
    {
        atomGrid = new int[n][n]; // instantiating grid
        userGrid = new char[n][n];
        for (int i = 0; i < userGrid.length; i++)
            for (int j = 0; j < userGrid[i].length; j++)
                userGrid[i][j] = '.';
    }
    
    // not used, but for when the user has a custom grid
    public blackbox(int[][] grid)
    {
        atomGrid = grid;
    }
    
    // not used, for custom grid
    public void addAtom(int i, int j)
    {
        atomGrid[i][j] = 1;
    }
    
    public void fillGrid(int i)
    {
        while (i > 0)
        {
            // random positions
            int a = (int) (Math.random()*atomGrid.length);
            int b = (int) (Math.random()*atomGrid.length);
            
            if (atomGrid[a][b] == 0) // for testing purposes, to be deleted later
            {
                System.out.println(a + ", " + b);
                atomGrid[a][b] = 1;
                i--;
            }
        }
    }
    
    public void printBoard()
    {
        // borders
        int top = 40;
        int bottom = 11;
        int left = 1;
        int right = 30;

        int rows = atomGrid.length;
        int cols = atomGrid[0].length;

        // Print top border (40 → 31)
        System.out.print("   ");
        for (int i = top; i >= top - cols + 1; i--)
            System.out.print(String.format("%3d", i));
        System.out.println();

        // Print side borders with atomGrid content
        for (int i = 0; i < rows; i++) 
        {
            // Left border (1 → 10)
            System.out.print(String.format("%3d", left + i));

            // Inner grid content
            for (int j = 0; j < cols; j++)
                System.out.print(String.format("%3s", userGrid[i][j])); // instead of atomGrid[i][j] bc user cant see board

            // Right border (30 → 21)
            System.out.println(String.format("%3d", right - i));
        }

        // Print bottom border (11 → 20)
        System.out.print("   ");
        for (int i = bottom; i < bottom + cols; i++)
            System.out.print(String.format("%3d", i));
        System.out.println();
    }

    public int[][] getAtomGrid()
    {
        return atomGrid;
    }

    // the board printed at the end of the game
    public void printAtomBoard()
    {
        // borders
        int top = 40;
        int bottom = 11;
        int left = 1;
        int right = 30;

        int rows = atomGrid.length;
        int cols = atomGrid[0].length;

        // Print top border (40 → 31)
        System.out.print("   ");
        for (int i = top; i >= top - cols + 1; i--)
            System.out.print(String.format("%3d", i));
        System.out.println();

        // Print side borders with atomGrid content
        for (int i = 0; i < rows; i++) 
        {
            // Left border (1 → 10)
            System.out.print(String.format("%3d", left + i));

            // Inner grid content
            for (int j = 0; j < cols; j++)
                if (atomGrid[i][j] == 1)
                    System.out.print(String.format("%3s", 'A')); // 'A' for atom
                else
                    System.out.print(String.format("%3s", "."));

            // Right border (30 → 21)
            System.out.println(String.format("%3d", right - i));
        }

        // Print bottom border (11 → 20)
        System.out.print("   ");
        for (int i = bottom; i < bottom + cols; i++)
            System.out.print(String.format("%3d", i));
        System.out.println();
    }

    public char[][] getUserGrid()
    {
        return userGrid;
    }

    public void updateUserGrid(int r, int c, char ch)
    {
        userGrid[r][c] = ch;
    }
    
    public int incrementNumRaysFired(int i)
    {
        numRaysFired+=i;
        return numRaysFired;
    }
    
    public int incrementNumAtomsFound(int i)
    {
        numAtomsFound+=i;
        return numAtomsFound;
    }
    
    public int getNumAtoms()
    {
        return numAtoms;
    }

    public int getNumAtomsFound()
    {
        return numAtomsFound;
    }

    public int getNumGuessesLeft()
    {
        return numGuessesLeft;
    }
    
    public int decrementNumGuessesLeft(int i)
    {
        numGuessesLeft-=i;
        return numGuessesLeft;
    }
    
    public String rayGuessStr(int i)
    {
        if (i <= 10 || (i > 20 && i <= 30))
            return "r";
        return "c";
    }
    
    public int rayGuessInt(int i)
    {
        int row = 11;
        int col = 11;
        
        if (i <= 10)
        {
            row = i-1;
        }
        else if (i <= 20)
        {
            col = -1*((i-1)%10); // to account for difference in indexing
        }
        else if (i <= 30)
        {
            row = -1*(10-(i%10)); // - represents backwards
        }
        else // (i <=40)
        {
            col = (10-(i%10)); //
        }
        if (row == 11) // only one number changes
            return col;
        return row;
    }

    public char checkInteractions(int r, int c, char currentDir) {
        // Check for reflection
        if (currentDir == 'L') {
            if (hasAtom(r-1, c) || hasAtom(r+1, c)) return 'S'; // reflection from left/right
        } else if (currentDir == 'R') {
            if (hasAtom(r-1, c) || hasAtom(r+1, c)) return 'S'; // reflection from right/left
        } else if (currentDir == 'U') {
            if (hasAtom(r, c-1) || hasAtom(r, c+1)) return 'S'; // reflection from top/bottom
        } else if (currentDir == 'D') {
            if (hasAtom(r, c-1) || hasAtom(r, c+1)) return 'S'; // reflection from bottom/top
        }
        
        // Check for deflection (single diagonal atom)
        if (currentDir == 'L') {
            if (hasAtom(r-1, c-1)) return 'D';
            if (hasAtom(r+1, c-1)) return 'U';
        } else if (currentDir == 'R') {
            if (hasAtom(r-1, c+1)) return 'D';
            if (hasAtom(r+1, c+1)) return 'U';
        } else if (currentDir == 'U') {
            if (hasAtom(r-1, c-1)) return 'R';
            if (hasAtom(r-1, c+1)) return 'L';
        } else if (currentDir == 'D') {
            if (hasAtom(r+1, c-1)) return 'R';
            if (hasAtom(r+1, c+1)) return 'L';
        }
        
        return currentDir; // no interaction
    }

    public boolean hasAtom(int r, int c) {
        if (r < 0 || r >= 10 || c < 0 || c >= 10) return false;
        return atomGrid[r][c] == 1;
    }

    public boolean isHit(int r, int c) {
        return atomGrid[r][c] == 1;
    }
    
    public boolean isMiss(String str, int i) // this could also js be a default last condition
    {
        if (str.equals("r"))
        {
            if (i > 0)
            {
                for (int c = 0; c < atomGrid.length; c++)
                    if (atomGrid[i][c] == 1)
                        return false;
            }
            else
            {
                for (int c = atomGrid.length-1; c >= 0; c--)
                    if (atomGrid[Math.abs(i)][c] == 1)
                        return false;
            }
        }
        else // str.equals("c"))
        {
            if (i > 0)
            {
                for (int r = 0; r < atomGrid.length; r++)
                    if (atomGrid[r][i] == 1)
                        return false;
            }
            else
            {
                for (int r = atomGrid.length-1; r >= 0; r--)
                    if (atomGrid[r][Math.abs(i)] == 1)
                        return false;
            }
        }
        return true;
    }
    
}

/*
GAME SETUP
- board
  - 2d array grid 🗸
  - 2d array grid for outer edges, user input 🗸
- place hidden atoms within the grid 🗸

MECHANISMS
- hit (absorbed): a ray hits an atom directly and does not exit the box 🗸
- reflection: a ray reflects backs the way it came due to an atom nearby 🗸
- deflection: a ray bends at an angle, exiting the box at a different point 🗸
- miss: a ray does not come in contact with any atom and goes straight through 🗸
- repeat detection of redirected rays until no more redirections are possible -- todo! 🗸
  - a ray can reflect off an atom, which can then deflect it, which can then reflect it again, etc.

PLAYER INTERACTION
- fire a ray (accepts index locations) 🗸
- make a guess (accepts index locations) 🗸
- display already guessed spots 🗸

LOGISTICS
- store number of rays fired and guesses left 🗸
- store number of atoms found 🗸
- check if all atoms are correctly identified 🗸

OTHER
- scoring system 🗸
- difficulty levels (prob not)
- gui (prob not)

UI (prob not anymore)
- display grid
- show ray paths

WHAT THE USER SEES

  40 39 38 37 36 35 34 33 32 31 
1                              30
2                              29
3                              28
4                              27
5                              26
6                              25
7                              24
8                              23
9                              22
10                             21
  11 12 13 14 15 16 17 18 19 20

1-10: 1-10 rows
11-20: 1-10 cols, bottom up
21-30: 10-1 rows, right to left
31-40: 10-1 col, right to left


Welcome to Blackbox Game!
Find all 5 hidden atoms in the grid.
Enter a border number (1-40) to shoot a ray from that position
or enter 0 to guess an atom location.

Current Board:
    40  39  38  37  36  35  34  33  32  31  
31   .   .   .   .   .   .   .   .   .   .   28 
32   .   .   .   .   .   .   .   .   .   .   27
33   .   .   .   .   .   .   .   .   .   .   26 
34   .   .   .   .   .   .   .   .   .   .   25
35   .   .   .   .   .   .   .   .   .   .   24
36   .   .   .   .   .   .   .   .   .   .   23
37   .   .   .   .   .   .   .   .   .   .   22
38   .   .   .   .   .   .   .   .   .   .   21
    11  12  13  14  15  16  17  18  19  20

Enter border position (1-40) or 0 to guess:

*/

// r3dH4Tprogramming!