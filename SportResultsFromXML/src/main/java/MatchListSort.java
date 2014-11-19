package main.java;

import java.util.Comparator;

public class MatchListSort {
	
	class MatchListComparator implements Comparator<MatchEntity>{
		 
	    @Override
	    public int compare(MatchEntity e1, MatchEntity e2) {
	        if(e1.getGoal() < e2.getGoal()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }
	}

	class MatchEntity {	

		private String sport;
		private String category;
		private String tournament;
		private String team1Name;
		private String team2Name;
		private String team1Score;
		private String team2Score;
		private int goal;
		
		public MatchEntity(String sport, String category, String tournament,
				String team1Name, String team2Name, String team1Score,
				String team2Score, int goal) {
			
			this.sport = sport;
			this.category = category;
			this.tournament = tournament;
			this.team1Name = team1Name;
			this.team2Name = team2Name;
			this.team1Score = team1Score;
			this.team2Score = team2Score;
			this.goal = goal;
		}
		
		
		public int getGoal() {
			return goal;
		}
		
		public void setGoal(int goal){
			this.goal=goal;
		}

		public String getSport() {
			return sport;
		}

		public void setSport(String sport) {
			this.sport = sport;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getTournament() {
			return tournament;
		}

		public void setTournament(String tournament) {
			this.tournament = tournament;
		}

		public String getTeam1Name() {
			return team1Name;
		}

		public void setTeam1Name(String team1Name) {
			this.team1Name = team1Name;
		}

		public String getTeam2Name() {
			return team2Name;
		}

		public void setTeam2Name(String team2Name) {
			this.team2Name = team2Name;
		}

		public String getTeam1Score() {
			return team1Score;
		}

		public void setTeam1Score(String team1Score) {
			this.team1Score = team1Score;
		}

		public String getTeam2Score() {
			return team2Score;
		}

		public void setTeam2Score(String team2Score) {
			this.team2Score = team2Score;
		}
		public String toString(){
	        return this.sport+ " | " +this.category + " | " + this.tournament + " | " +
	        this.team1Name + " - " + this.team2Name + " : " + this.team1Score + " - " +  
	        this.team2Score;
	    }
	}
	
	class TopListComparator implements Comparator<TopListEntity>{
		 
	    @Override
	    public int compare(TopListEntity e1, TopListEntity e2) {
	        if(e1.getGoal() < e2.getGoal()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }
	}
	
	//Top List Entity Class 
	class TopListEntity {
		String frstLtr;
		int goal;
		
		public TopListEntity(String frstLtr, int goal) {
			this.frstLtr = frstLtr;
			this.goal = goal;
		}

		public String getFrstLtr() {
			return frstLtr;
		}

		public void setFrstLtr(String frstLtr) {
			this.frstLtr = frstLtr;
		}

		public int getGoal() {
			return goal;
		}

		public void setGoal(int goal) {
			this.goal = goal;
		}

		@Override
		public String toString() {
			return this.frstLtr + " : " + this.goal;
		}		
	}
}
