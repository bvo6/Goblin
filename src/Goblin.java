import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Goblin class is used to implement the Goblin game
 * CS 310-002.
 * @author Bao Vo
 */ 
class Goblin {
    /**
     * userIn is used to scan the user input.
     */ 
    private Scanner userIn;
    
    /**
     * Hold the name of the file that will be used to generate a dictionary.
     */ 
    private String dictFileName;
    
    /**
     * The number of letters of a word.
     */ 
    private int numLetters;
    
    /**
     * The max number of guesses. 
     */ 
    private int numGuesses;
    
    /**
     * A BetterArray of type String that will be used to hold the dictionary.
     */ 
    private BetterArray<String> dictionary;
    
    /**
     * A BetterArray of type Character that is used to hold the guesses from the user.
     */ 
    private BetterArray<Character> guesses;
    
    /**
     * A BetterArray of type Character that is used to hold the current Word that 
     * the user needs to guess.
     */ 
    private BetterArray<Character> currentWord;
    
    /**
     * The current status of user/Check if the user wins or not.
     */ 
    private boolean userWin = false;
    
    /**
     * Get a copy of the dictionary.
     * @return a copy of the current dictionary.
     */
    public BetterArray<String> getWords() { 
        return this.dictionary.clone();
    }
    
    /**
     * Get a copy of the guesses that the user has used.
     * @return a copy of the user's current guesese
     */ 
    public BetterArray<Character> getGuesses() {
        return this.guesses.clone();
    }
    
    /**
     * Get a copy of the current word that the user needs to guess.
     * @return a copy of the letters the user has locked in.
     */ 
    public BetterArray<Character> getCurrentWord() {
        return this.currentWord.clone();
    }
    
    /**
     * Get the number of the remaining guesses.
     * @return the number of wrong guesese the user has left
     */ 
    public int getGuessesRemaining() {
        return this.numGuesses;
    }
    
    /**
     * This method is used to initialize all the fields and generate a dictionary 
     * with words that have the length of the numLetters and have no duplicate words.
     * @exception FileNotFoundException if file is not found from the given name
     * @return false/true if dictionary is generated successfully or not
     */ 
    public boolean init() {
        // Check if the numLetters is valid
        if (this.numLetters < 2) {
            System.out.println("Goblin can't find enough words! You win!");
            return false;
        }
        /* If numLetters is valid, initialize the currentWord with the capacity of numLetters
         * then, append null to the currentWord array.
         */ 
        currentWord = new BetterArray<Character>(numLetters);
        for (int i = 0 ; i < numLetters; i++) {
            currentWord.append(null);
        }
        // Initialize dictionary and guesses.
        dictionary = new BetterArray<String>();
        guesses = new BetterArray<Character>();
        // Create a string word to hold the current word that is being read from file.
        String word = "";
        try {
            File file = new File(this.dictFileName);
            Scanner fileIn = new Scanner(file);
            while(fileIn.hasNextLine()) {
                boolean validWord = true;
                word = fileIn.nextLine();
                // Check if the length of the current word matches the numLetters.
                if (word.length() == numLetters) {
                    // If yes, create conditions to check for duplicate letters.
                    conditions: 
                        for (int i = 0; i < numLetters - 1; i++) {
                        for (int j = i+1; j < numLetters; j++) {
                            if (word.charAt(i) == word.charAt(j)) {
                                validWord = false;
                                break conditions;
                            } 
                        }
                    }
                } else if (word.length() != numLetters) {
                    validWord = false;
                } else {
                    continue;
                }
                // If a valid word is found, append it to the dictionary.
                if (validWord == true) {
                    this.dictionary.append(word);
                }
            }
            // Close the scanner.
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Goblin lost his dictionary! You win!");
            return false;
        }
        if (this.dictionary.size() == 0) {
            System.out.println("Goblin can't find enough words! You win!");
            return false;
        }
        return true;
    }
    
    /**
     * Generate the best partition of dictionary based on the user's guess.
     * @param guess the user's current guess
     * @return the position of the letter with the chosen guess or -1 for the bestPartition of no chosen guess
     */ 
    public int bestPartition(char guess) {
        /* Create a temporary generic BetterArray with type String inside another generic BetterArray with the capacity of numLetters + 1
         * which will be used to hold the words according to their index from the chosen guess.
         */ 
        BetterArray<BetterArray<String>> tempDict = new BetterArray<>(numLetters + 1);
        // Initialize the inner BetterArray.
        for (int i = 0; i < tempDict.capacity(); i++) {
            tempDict.append(new BetterArray<String>());
        }
        // This loop is used to add the words from the dictionary to the inner BetterArray by its appropriate index.
        for (int i = 0; i < dictionary.size(); i++) {
            // Create a boolean to check if the chosen guess matches the word or not.
            // If yes, add the word with the index matching the chosen guess to the index of the inner array.
            // If the current word does not contain the chosen guess, add that word to the first sublist of tempArray.
            boolean hasLetter = false;
            for (int j = 0; j < numLetters; j++) {
                if (dictionary.get(i).charAt(j) == guess) {
                    hasLetter = true;
                    tempDict.get(j+1).append(dictionary.get(i));
                }
            }
            if (hasLetter == false) {
                tempDict.get(0).append(dictionary.get(i));
            }
        }
        // Create a variable largestSize and set it to the size of the first sublist of tempDict .
        int largestSize = tempDict.get(0).size();
        // Create a variable temp to hold the current index.
        int temp = 0;
        // Loop through the list of tempDict.
        for (int i = 0; i < tempDict.size(); i++) {
            // Check if the size of the current sublist is greater than the largestSize or not.
            if (tempDict.get(i).size() > largestSize) {
                // If yes, set that size to largestSize and set temp to the index of that sublist.
                largestSize = tempDict.get(i).size();
                temp = i;
            }
        }
        // Set the dictioanry to the sublist that has the best partition.
        this.dictionary = tempDict.get(temp);
        // If Goblin picks the partition with the chosen guess, return the index of that letter which matches the guess.
        if (temp != 0) {
            return temp - 1;
        }
        // Return -1, if Goblin picks the partition without the chosen guess.
        return -1; 
    }
    
    /**
     * This helper method is used to check if the user has unlocked all the letters or not.
     * @return false if the user still have not unlocked all the letters or true if the user wins/unlocked all the letters
     */ 
    private boolean win() {
        boolean check = true;
        for (int i = 0; i < currentWord.capacity(); i++) {
            if (currentWord.get(i) == null) {
                check = false;
                break;
            }
        }
        return check;
    }
    
    /**
     * This helper method is used to check for the valid guess
     * a guess that has not been used.
     * @param guess the current guess
     * @return true for an invalid guess (a guess that has been used) or false for a valid guess (a guess that has not been used)
     */ 
    private boolean checkForGuesses(char guess) {
        for (int i = 0; i < guesses.size(); i++) {
            if (guesses.get(i) == guess) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method is used to set up the game and get a guess from the user.
     * @return true if the game should continue false if the game should stop
     */ 
    public boolean step() {
        // Create variables to hold the input from the user, then convert that input to guess with type char.
        String input = "";
        char guess = ' ';
        int index = 0;
        System.out.print("Goblin says \"Guess a letter\": ");
        input = userIn.nextLine();
        // Will prompt the user for a valid input with 1 letter at a time.
        while (input.length() != 1) {
            System.out.print("Goblin says \"One letter! Guess a single letter\":");
            input = userIn.nextLine();
        }
        // Assign guess to hold the letter from the user input.
        guess = input.charAt(0);
        // Use the while loop to check for a valid guess using the helper method checkForGueses.
        while (checkForGuesses(guess) == true) {
            System.out.print("Goblin says \"You already guessed: " + getGuesses().toString() + "\nGuess a new letter\": ");
            input = userIn.nextLine();
            while (input.length() != 1) {
                System.out.print("Goblin says \"One letter! Guess a single letter\":");
                input = userIn.nextLine();
            }
            guess = input.charAt(0);
            checkForGuesses(guess);
        }
        // Append the current guess to the array guesses after checking for the valid guess.
        guesses.append(guess);
        // Check if the current dictionary's size is greater than 1 which contain more than 1 word.
        if (dictionary.size() > 1) {
            // If yes, assign the index to get the bestPartition with the current guess.
            index = bestPartition(guess);
            // If the current dictioanry's size is 1 which only contains 1 word now.
        } else {
            // Create conditions, which will check if the current guess matches any letter from the word in the dictionary or not.
            conditions:
                for (int i = 0; i < dictionary.size(); i++) {
                for (int j = 0; j < numLetters; j++) {
                    if (dictionary.get(i).charAt(j) == guess) {
                        index = j;
                        break conditions;
                    }
                }
                // If not match is found, index will be -1 (the index which the guess has no match in the current dictionary).
                index = -1;
            }
        }
        // Check if index != a letter is unlocked.
        if (index != -1) {
            // Replace the guess with the position of the index.
            currentWord.replace(index, guess);
            // Print the appropriate message
            System.out.println("Goblin says \"Yeah, yeah, it's like this: "+ getCurrentWord().toString().replaceAll("null","-").replaceAll("[ ,]","")+"\"");
        } 
        // Check if index = -1 (no letter is matched).
        if (index == -1) {
            // Decrement numGuesses by 1 and print the appropriate message.
            this.numGuesses--;
            System.out.println("Goblin says \"No dice! "+getGuessesRemaining()+" wrong guesses left...\"");
        }
        // Check if the user wins or not.
        this.userWin = win();
        // Check if the game should stop or continue.
        if ((userWin == true) || (numGuesses == 0)) {
            return false;
        }
        return true;
    }
    
    /**
     * This method will be called after step() return false which means a winner 
     * is found.
     */ 
    public void finish() {
        /*
         This will be called after step() returns false. Print the appropriate
         win/lose message here.
         */
        if (userWin == true) {
            System.out.println("Goblin says \"You win... here's your stuff. But I'll get you next time!\"");
        } else {
            System.out.println("Goblin says \"I win! I was thinking of the word '"+getWords().get(0)+"'");
            System.out.println("Your stuff is all mine... I'll come back for more soon!\"");
        }
    }
    
    /**
     * Constructor for the Goblin class which is used to initialize all fields.
     * @param dictFileName the name of the file
     * @param numLetters the number of letters of a word
     * @param numGuesses the max number of wrong guesses
     */ 
    public Goblin(String dictFileName, int numLetters, int numGuesses) {
        this.userIn = new Scanner(System.in);
        this.dictFileName = dictFileName;
        this.numLetters = numLetters;
        this.numGuesses = numGuesses;
    }
    
    /**
     * This main method is used to test all other methods in the Goblin class.
     * @param args the command line arguments
     */ 
    public static void main(String[] args) {
        Goblin g1 = new Goblin("../dictionary-mini.txt", 3, 10);
        Goblin g2 = new Goblin("../dictionary-mini.txt", 6, 6);
        Goblin g3 = new Goblin("../dictionary.txt", 1, 6);
        Goblin g4 = new Goblin("banana.txt", 3, 3);
        
        if(g1.init() && g2.init() && !g3.init() && !g4.init()) {
            System.out.println("Yay 1");
        }
        
        if(g1.getGuessesRemaining() == 10 && g1.getWords().size() == 1
               && g2.getGuessesRemaining() == 6 && g2.getWords().size() == 5
               && g1.getGuesses().size() == 0 && g2.getCurrentWord().size() == 6) {
            System.out.println("Yay 2");
        }
        
        BetterArray<Character> g1word = g1.getCurrentWord();
        if(g1word.get(0) == null  && g1word.get(1) == null && g1word.get(2) == null) {
            System.out.println("Yay 3");
        }
        
        //remember what == does on objects... not the same as .equals()
        if(g1.getWords() != g1.getWords() && g1.getGuesses() != g1.getGuesses()
               && g1.getCurrentWord() != g1.getCurrentWord()) {
            System.out.println("Yay 4");
        }
        
        if(g2.bestPartition('l') == -1 && g2.getWords().size() == 3
               && g2.bestPartition('p') == 0 && g2.getWords().size() == 2
               && g2.bestPartition('n') == -1 && g2.getWords().size() == 1) {
            System.out.println("Yay 5");
        }
        
        //can't test step() or finish() this way... requires user input!
        //maybe you need to test another way...
    }
}