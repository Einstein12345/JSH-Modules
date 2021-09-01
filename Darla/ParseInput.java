package Darla;

import terra.shell.utils.JProcess;

public class ParseInput extends JProcess {

	private final static String[] questionWords = { "where", "what", "why", "how", "which" };

	private String input;

	public ParseInput(String input) {
		this.input = input.substring(5);
	}

	@Override
	public String getName() {
		return "Darla Input Parser";
	}

	@Override
	public boolean start() {
		// getLogger().print(input);
		return true;
	}

}
