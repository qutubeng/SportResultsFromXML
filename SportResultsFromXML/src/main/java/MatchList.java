package main.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MatchList extends MatchListSort{
	
	ArrayList<String> matchListGoalWise = new ArrayList<String>();
	List<MatchEntity> list = new ArrayList<MatchEntity>();
	List<TopListEntity> topList = new ArrayList<TopListEntity>();
	
	public void parseXMLfile(String fileName) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(fileName);
		Element docElement = dom.getDocumentElement();
		NodeList sportNodeList = docElement.getElementsByTagName("Sport");
		String sportValue = "";
		if(sportNodeList != null && sportNodeList.getLength() > 0) {
			for(int s = 0 ; s < sportNodeList.getLength(); s++) {				
				Element sportElement = (Element)sportNodeList.item(s);	
				sportValue = elementValue(sportElement, "Name");				
				
				// this is for the value of Category
				NodeList categoryNodeList = sportElement.getElementsByTagName("Category");
				String categoryValue = "";
				if(categoryNodeList != null && categoryNodeList.getLength() > 0) {
					for(int c=0; c < categoryNodeList.getLength(); c++) {
						Element categoryElement = (Element)categoryNodeList.item(c);
						categoryValue = elementValue(categoryElement, "Name");
						
						// this for the Tournament
						
						NodeList tournamentNodeList = categoryElement.getElementsByTagName("Tournament");
						String tournamentValue ="";
						if(tournamentNodeList != null && tournamentNodeList.getLength() > 0) {
							for(int t=0; t < tournamentNodeList.getLength(); t++) {	
								Element tournamentElement = (Element)tournamentNodeList.item(t);
								tournamentValue =  elementValue(tournamentElement, "Name");								
								
								// Match info
								NodeList matchNodeList = tournamentElement.getElementsByTagName("Match");
								if(matchNodeList.getLength() > 0) {
									for(int m=0; m < matchNodeList.getLength(); m++) {
										Element matchElement = (Element)matchNodeList.item(m);
										
										// get info for team 1 
										NodeList team1NodeList = matchElement.getElementsByTagName("Team1");
										String team1Value = "";
										if(team1NodeList != null && team1NodeList.getLength() > 0) {
											Element team1Element = (Element)team1NodeList.item(0);
											team1Value = elementValue(team1Element, "Name");
										}
										
										//get info for team 2 
										NodeList team2NodeList = matchElement.getElementsByTagName("Team2");
										String team2Value = "";
										if(team2NodeList != null && team2NodeList.getLength() > 0) {
											Element team2Element = (Element)team2NodeList.item(0);
											team2Value = elementValue(team2Element, "Name");
										}
										
										//Status checking whether game has started or not
										NodeList statusNodeList = matchElement.getElementsByTagName("Status");
										String team1Score = "";
										String team2Score = "";
										if(statusNodeList != null && statusNodeList.getLength() > 0) {
											Element statusElement = (Element) statusNodeList.item(0);
											//Checking game is not in "Not Started" Mode
											if(!attributeValue(statusElement, "Code").equals("0")){
												NodeList scoreNodeList = matchElement.getElementsByTagName("Score");
												if(scoreNodeList != null && scoreNodeList.getLength() > 0) {
													for(int sc=0; sc<scoreNodeList.getLength(); sc++) {
														Element scoreElement = (Element)scoreNodeList.item(sc);
														//getting current score data
														if(attributeValue(scoreElement, "type").equals("Current")){
															team1Score = elementValue(scoreElement, "Team1");
															team2Score = elementValue(scoreElement, "Team2");
															
															int teamGoal = Integer.parseInt(team1Score) + Integer.parseInt(team2Score);
															
															list.add(new MatchEntity(sportValue, categoryValue, tournamentValue, team1Value,
																	team2Value, team1Score, team2Score, teamGoal));
															
															goalListPrepare(team1Value.trim(), team1Score.trim());
															goalListPrepare(team2Value.trim(), team2Score.trim());
														
														}
													}
												}
											}
										}										
									}
								}
							}
						}				
					}
				}
			}
		}
		//preparing data to write on file
		dataPrepare(matchListGoalWise, list);		
		System.out.println("\nPlease find the generated files in \"/src/main/generatedFiles/\" directory of current porject!");
	}
	
	//Preparing data for top goal wise team list (Team list by first character of team name)
	private void goalListPrepare(String teamName, String goal) {
		
		if(Integer.parseInt(goal) > 0) {
			String fstLtr = teamName.substring(0, 1);
			
			if(matchListGoalWise != null && matchListGoalWise.contains(fstLtr)) {
				for(int i = 0; i < matchListGoalWise.size(); i++) {
					if(matchListGoalWise.get(i).equals(fstLtr)) {
						int oldGoal = Integer.parseInt(matchListGoalWise.get(i+1));
						int updateGoal = oldGoal + Integer.parseInt(goal);
						matchListGoalWise.set(i, fstLtr);
						matchListGoalWise.set(i+1, updateGoal + "");
					}
				}
			} else {
				matchListGoalWise.add(fstLtr);
				matchListGoalWise.add(goal);
			}
		}
	}
	
	//Preparing data according to the requirement
	private void dataPrepare(ArrayList<String> goalWisematchList, List<MatchEntity> list) throws IOException {
				
		//Match list is sorted on number of goals
		Collections.sort(list, new MatchListComparator());
		
		//Converting List in ArrayList to use this ArrayList in writeOnTextFile method
		ArrayList<String> mList = new ArrayList<String>();
		for(MatchEntity e:list){
            mList.add(e.toString());
        }
		//Calling file writing method with file name and ArrayList data 
		writeOnTextFile("big-matchlist-goalsort.txt", mList);
		
		//Alphabetically ordering
		Collections.sort(mList);
		//Writing alphabetically sorted match list in text file
		writeOnTextFile("big-matchlist-alphasort.txt", mList);	
		
		//preparing top list in a list 
		for(int i = 0; i < goalWisematchList.size()-1; i=i+2) {
			topList.add(new TopListEntity(goalWisematchList.get(i).trim(), Integer.parseInt(goalWisematchList.get(i+1).trim())));
		}
		
		//sorting top goal list
		Collections.sort(topList, new TopListComparator());
		//Store elements in ArrayList 
		ArrayList<String> golArrayList = new ArrayList<String>();
		for(TopListEntity e:topList) {
			golArrayList.add(e.toString());
		}
		//Writing sorted top list in text file
		writeOnTextFile("big-toplist.txt", golArrayList);
	}
	
	//Write data in Text file
	private void writeOnTextFile(String fileName, ArrayList<String> matchList) throws IOException {
		//Get working directory path
		String directoryPath = getProjectDirectory();
		directoryPath = directoryPath + "/src/main/generatedFiles/";
		String filePath = directoryPath + fileName;
		
		try {			
			//If old file is exist, then delete this file.
			File file = new File(filePath);
			if(file.exists()) {
				file.delete();
			}
			
			FileWriter writer = new FileWriter(filePath, true);
			BufferedWriter out = new BufferedWriter(writer);
						
			for(int i = 0; i < matchList.size(); i++) {				
				out.write(matchList.get(i)+"\n");
			}
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("\"" +fileName + "\"" + " text file has been created!");
	}
	
	//Returns Tag element value
	private String elementValue(Element elements, String elementName) {
		String nameValue = prepareElementValue(elements,elementName);
		return nameValue;
	}	
	//Return Tag attribute type value
	private String attributeValue(Element elements, String attributeName) {
		String attributeValue = elements.getAttribute(attributeName);
		return attributeValue;
	}
	
	//prepare element value according tag element type or without element type 
	private String prepareElementValue(Element element, String tagName) {
		
		String elementValue = null;
		NodeList nodeList = element.getElementsByTagName(tagName);
		if(nodeList != null && nodeList.getLength() > 0) {			
			//to check Value is in English or not
			// in the 1th position
			Element el = (Element)nodeList.item(0);
			if(el.getAttribute("language").equals("en")) {
				elementValue = el.getFirstChild().getNodeValue();
			}
			else if(el.getAttribute("language").equals("se")) {
				// in the 2th position
				Element el1 = (Element)nodeList.item(1);
				if(el1.getAttribute("language").equals("en")) {
					elementValue = el1.getFirstChild().getNodeValue();
				}
			}
			
			//if attribute type is not there 
			else {
				Element el3 = (Element)nodeList.item(0);
				elementValue = el3.getFirstChild().getNodeValue();
			}
		}
		return elementValue;
	}
	
	public String getProjectDirectory() {
		String path = new File("").getAbsolutePath();
		path = path.replace("\\", "/");
		return path;
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        
    	MatchList parser = new MatchList();
    	String directoryPath = parser.getProjectDirectory();
        parser.parseXMLfile(directoryPath+ "/src/main/resources/big-data.xml");
    }	
}