/**
 *  Contains the main method for running the Goblin
 *  Game for CS310 Fall 2019.
 *  
 *  @author K. Raven Russell
 */
class GoblinGame {
	/**
	 *  Handles command line input and the game loop.
	 *  Also does debugging output for the user.
	 *  
	 *  @param args standard command line args
	 */
	public static void main(String[] args) {
		String usage = "Usage: Goblin [dictionary] [numLetters] [numGuesses] [debug]";
		
		if(args.length != 3 && (args.length != 4 || !args[3].equals("debug"))) {
			System.out.println(usage);
			return;
		}
		
		boolean debug = false;
		int numLetters = 0, numGuesses = 0;
		try {
			numLetters = Integer.parseInt(args[1]);
			numGuesses = Integer.parseInt(args[2]);
			debug = (args.length == 4);
		}
		catch(NumberFormatException e) {
			System.out.println(usage);
		}
		
		Goblin nog = new Goblin(args[0], numLetters, numGuesses);
		//standard game loop
		if(nog.init()) {
			if(debug) printDebug(nog);
			while(nog.step()) if(debug) printDebug(nog);
			if(debug) printDebug(nog);
			nog.finish();
		}
	}
	
	/**
	 *  Prints the list of words the given Goblin
	 *  is currently working from.
	 *  
	 *  @param g a goblin with some words
	 */
	private static void printDebug(Goblin g) {
		System.out.println("--------------------------------------------------");
		System.out.println(g.getWords().toString());
		System.out.println("--------------------------------------------------");
	}
}