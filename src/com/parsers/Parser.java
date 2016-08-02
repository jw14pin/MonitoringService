package com.parsers;

import java.util.ArrayList;
import java.util.List;

import com.exceptions.InvalidCommandException;
import com.models.ClickCommand;
import com.models.Command;
import com.models.OpenCommand;
import com.models.TypeCommand;
import com.models.VerifyCommand;

public class Parser {
	public static List<Command> parseCommandString(String commands) throws InvalidCommandException {
		ArrayList<Command> returnCommands = new ArrayList<Command>();
		String[] commandList = commands.split(";");
		
		for(String command:commandList) {
			String onlyCommand = command.trim();
			if(onlyCommand.startsWith("open")) {
				String[] parts = onlyCommand.split("\\s+");
				OpenCommand openCommand = new OpenCommand();
				openCommand.setUrl(parts[1]);
				returnCommands.add(openCommand);
			} else if(onlyCommand.startsWith("click")) {
				String[] parts = onlyCommand.split("\\s+");
				ClickCommand clickCommand = new ClickCommand();
				clickCommand.setWord(parts[1]);
				returnCommands.add(clickCommand);
			} else if(onlyCommand.startsWith("type")) {
				String characters = "";
				String[] parts = onlyCommand.split("\\s+");
				TypeCommand typeCommand = new TypeCommand();
				typeCommand.setWord(parts[1]);
				for(int i = 2; i < parts.length; i++) {
					characters += parts[i];
				}
				typeCommand.setCharacters(characters);
				returnCommands.add(typeCommand);
			} else if(onlyCommand.startsWith("verify")) {
				String[] parts = onlyCommand.split("\\s+");
				VerifyCommand verifyCommand = new VerifyCommand();
				verifyCommand.setPhrase(parts[1]);
				System.out.println(onlyCommand);
				returnCommands.add(verifyCommand);
			} else if(onlyCommand.equals("")) {
				
			} else {
				throw new InvalidCommandException();
			}
		}
			
		return returnCommands;
	}
	
	public static String convertToCommandString(List<Command> commands) {
		String allCommands = "";
		for(Command command:commands) {
			String onlyCommand = command.getCommand();
			if(onlyCommand.equals("open")) {
				onlyCommand += " "+((OpenCommand)command).getUrl()+"; ";
			} else if(onlyCommand.equals("click")) {
				onlyCommand += " "+((ClickCommand)command).getWord()+"; ";
			} else if(onlyCommand.equals("type")) {
				onlyCommand += " "+((TypeCommand)command).getWord()+" "+((TypeCommand)command).getCharacters()+"; ";
			} else if(onlyCommand.equals("verify")) {
				onlyCommand += " "+((VerifyCommand)command).getPhrase()+"; ";
			}
			allCommands += onlyCommand;
		}
		return allCommands;
	}
}
