package com.tonelope.tennis.scoreprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import com.tonelope.tennis.scoreprocessor.AbstractPoint.Team;

/**
 *
 *
 * @author Tony Lopez
 *
 */
public class TennisScoreProcessor {

	public enum FinalSet {

		/**
		 * A 7-point tiebreaker is played at 6-6 in the final set of the match.
		 */
		TIEBREAK,

		/**
		 * A 10-point tiebreaker is played at 6-6 in the final set of the match.
		 */
		TEN_POINT_TIEBREAK,
		
		/**
		 * The final set is win by 2 games up until 12-12. A 7-point tiebreaker is
		 * played at 12-12 in the final set of the match.
		 */
		TIEBREAK_AT_12_ALL,

		/**
		 * A 10-point tiebreaker is played in lieu of a final set of the match.
		 */
		SUPER_TIEBREAK,

		/**
		 * The final set is win by 2 games.
		 */
		WIN_BY_2;
	}

	public static class TennisScoreProcessorBuilder {

		String homeTeam = "Player 1";
		String visitorTeam = "Player 2";
		int gamesPerSet = 6;
		int numberOfSets = 3;
		int pointsPerSetTiebreak = 7;
		boolean adScoring = true;
		FinalSet finalSet = FinalSet.TIEBREAK;

		public TennisScoreProcessorBuilder adScoring() {
			this.adScoring = true;
			return this;
		}

		public TennisScoreProcessor build() {
			return new TennisScoreProcessor(this);
		}

		public TennisScoreProcessorBuilder finalSet(FinalSet type) {
			this.finalSet = type;
			return this;
		}

		public TennisScoreProcessorBuilder gamesPerSet(int gamesPerSet) {
			this.gamesPerSet = gamesPerSet;
			return this;
		}

		public TennisScoreProcessorBuilder homeTeam(String homeTeam) {
			this.homeTeam = homeTeam;
			return this;
		}

		public TennisScoreProcessorBuilder noAdScoring() {
			this.adScoring = false;
			return this;
		}

		public TennisScoreProcessorBuilder numberOfSets(int numberOfSets) {
			this.numberOfSets = numberOfSets;
			return this;
		}

		public TennisScoreProcessorBuilder visitorTeam(String visitorTeam) {
			this.visitorTeam = visitorTeam;
			return this;
		}
	}

	protected static final Map<Integer, String> GAME_SCORE_MAP = new HashMap<>();
	static {
		GAME_SCORE_MAP.put(0, "0");
		GAME_SCORE_MAP.put(1, "15");
		GAME_SCORE_MAP.put(2, "30");
		GAME_SCORE_MAP.put(3, "40");
	}
	public static final int DEUCE_POINT = 3;
	public static final int AD_POINT = 4;
	public static final int GAME_POINT = 5;

	private final String team1;
	private final String team2;
	private final int gamesPerSet;
	private final int numberOfSets;
	private final int numSetsToWin;
	private final int pointsPerSetTiebreak;
	private final boolean adScoring;
	private final FinalSet finalSet;
	private final FinalSetHandler finalSetHandler;

	private int[] gameScores = new int[2];
	private int[][] tiebreakScores;
	private int[][] setScores;
	private int[][] matchScores;
	private int[] setsWonScores;
	private int currentSetNumber = 0;

	public TennisScoreProcessor() {
		this(new TennisScoreProcessorBuilder());
	}

	public TennisScoreProcessor(TennisScoreProcessorBuilder builder) {
		this.team1 = builder.homeTeam;
		this.team2 = builder.visitorTeam;
		this.gamesPerSet = builder.gamesPerSet;
		this.numberOfSets = builder.numberOfSets;
		this.numSetsToWin = (builder.numberOfSets / 2) + 1;
		this.pointsPerSetTiebreak = builder.pointsPerSetTiebreak;
		this.adScoring = builder.adScoring;
		this.finalSet = builder.finalSet;

		this.tiebreakScores = new int[this.numberOfSets][2];
		this.setScores = new int[this.numberOfSets][2];
		this.matchScores = new int[this.numberOfSets][2];
		this.setsWonScores = new int[this.numberOfSets];
		this.currentSetNumber = 0;
		
		if (this.finalSet == FinalSet.SUPER_TIEBREAK) {
			this.finalSetHandler = new SuperTiebreakFinalSetHandler();
		} else {
			this.finalSetHandler = null;
		}
	}

	private String getGameScore() {
		if (isDeuce()) {
			return "Deuce";

		} else if (isAdvantage()) {
			String leadingTeam = gameScores[0] > gameScores[1] ? this.team1 : this.team2;
			return "Ad. " + leadingTeam;

		}
		return GAME_SCORE_MAP.get(gameScores[0]) + " - " + GAME_SCORE_MAP.get(gameScores[1]);
	}

	public String getScore() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < currentSetNumber; i++) {
			sb.append(getSetScore(i)).append(", ");
		}
		
		if (null != finalSetHandler) {
			sb.append(finalSetHandler.getScore());
			
		} else {
			sb.append(getSetScore(currentSetNumber));
			if (!isTiebreak()) {
				if (isGameStarted()) {
					sb.append(" ");
					sb.append(getGameScore());
				}
			} else {
				sb.append("(").append(tiebreakScores[currentSetNumber][0]).append(" - ")
						.append(tiebreakScores[currentSetNumber][1]).append(")");
			}
		}
		return sb.toString();
	}

	private String getSetScore(int setNumber) {
		final int NUM_GAMES_TO_WIN_SET = this.gamesPerSet;
		final int NUM_GAMES_TO_WIN_BY_2 = this.gamesPerSet + 1;
		int[] setScore = setScores[setNumber];
		StringBuilder sb = new StringBuilder();
		sb.append(setScore[0]).append("-").append(setScore[1]);
		if ((setScore[0] == NUM_GAMES_TO_WIN_BY_2 && setScore[1] == NUM_GAMES_TO_WIN_SET)
				|| (setScore[0] == NUM_GAMES_TO_WIN_SET && setScore[1] == NUM_GAMES_TO_WIN_BY_2)) {
			int[] matchScore = matchScores[setNumber];
			int loserIdx = IntStream.range(0, matchScores[setNumber].length).filter(j -> matchScore[j] == 0).findFirst()
					.getAsInt();
			sb.append("(").append(tiebreakScores[setNumber][loserIdx]).append(")");
		}
		return sb.toString();
	}

	public boolean isAdvantage() {
		return isAdvantage(0) || isAdvantage(1);
	}

	private boolean isAdvantage(int idxPlayer) {
		int idxOpposingPlayer = idxPlayer == 0 ? 1 : 0;
		return gameScores[idxPlayer] == AD_POINT && gameScores[idxOpposingPlayer] == DEUCE_POINT;
	}

	public boolean isDeuce() {
		return gameScores[0] == DEUCE_POINT && gameScores[1] == DEUCE_POINT;
	}

	public boolean isGameStarted() {
		return gameScores[0] != 0 || gameScores[1] != 0;
	}

	public boolean isMatchComplete() {
		return setsWonScores[0] == this.numSetsToWin || setsWonScores[1] == this.numSetsToWin;
	}

	public boolean isTiebreak() {
		return setScores[currentSetNumber][0] == this.gamesPerSet && setScores[currentSetNumber][1] == this.gamesPerSet;
	}

	public void update(AbstractPoint... points) {
		for (AbstractPoint point : points) {
			int idx = point.getWinner() == Team.HOME ? 0 : 1;
			updatePointWonFor(idx);
		}
	}

	private void updateGameScore(int idxWinner) {
		final int idxLoser = idxWinner == 0 ? 1 : 0;
		gameScores[idxWinner]++;

		if (adScoring) {
			if (gameScores[idxWinner] == AD_POINT && gameScores[idxLoser] < DEUCE_POINT
					|| gameScores[idxWinner] == GAME_POINT) {
				setScores[currentSetNumber][idxWinner]++;
				gameScores[idxWinner] = 0;
				gameScores[idxLoser] = 0;
			} else if (gameScores[0] == AD_POINT && gameScores[1] == AD_POINT) {
				gameScores[0] = DEUCE_POINT;
				gameScores[1] = DEUCE_POINT;
			}

		} else {
			if (gameScores[idxWinner] == AD_POINT) {
				setScores[currentSetNumber][idxWinner]++;
				gameScores[idxWinner] = 0;
				gameScores[idxLoser] = 0;
			}
		}
	}

	private void updatePointWonFor(int idxWinner) {
		final int NUM_GAMES_TO_WIN_BY_2 = this.gamesPerSet + 1;
		final int NUM_GAMES_NEEDED_FOR_TIEBREAK = this.gamesPerSet - 1;
		final int idxLoser = idxWinner == 0 ? 1 : 0;
		
		if (isFinalSet() && null != finalSetHandler) {
			finalSetHandler.update(idxWinner);
		} else {
			if (!isTiebreak()) {
				updateGameScore(idxWinner);
	
				if ((setScores[currentSetNumber][idxWinner] == this.gamesPerSet
						&& setScores[currentSetNumber][idxLoser] < NUM_GAMES_NEEDED_FOR_TIEBREAK)
						|| setScores[currentSetNumber][idxWinner] == NUM_GAMES_TO_WIN_BY_2) {
					matchScores[currentSetNumber][idxWinner]++;
					setsWonScores[idxWinner]++;
	
					if (!isMatchComplete()) {
						currentSetNumber++;
					}
				}
			} else {
				updateTiebreakScore(idxWinner);
			}
		}
	}
	
	public boolean isFinalSet() {
		return currentSetNumber + 1 == this.numberOfSets;
	}

	private void updateTiebreakScore(int idxWinner) {
		final int NUM_POINTS_TO_WIN_TIEBREAK = this.pointsPerSetTiebreak;
		final int NUM_POINTS_NEEDED_FOR_TIEBREAK = this.pointsPerSetTiebreak - 1;
		final int idxLoser = idxWinner == 0 ? 1 : 0;
		int[] scores = tiebreakScores[currentSetNumber];
		scores[idxWinner]++;
		if ((scores[idxWinner] == NUM_POINTS_TO_WIN_TIEBREAK && scores[idxLoser] < NUM_POINTS_NEEDED_FOR_TIEBREAK)
				|| (scores[idxWinner] >= NUM_POINTS_NEEDED_FOR_TIEBREAK
						&& scores[idxLoser] >= NUM_POINTS_NEEDED_FOR_TIEBREAK
						&& scores[idxWinner] > (scores[idxLoser] + 1))) {
			setScores[currentSetNumber][idxWinner]++;
			matchScores[currentSetNumber][idxWinner]++;
			setsWonScores[idxWinner]++;

			if (!isMatchComplete()) {
				currentSetNumber++;
			}
		}
	}
}
