package com.tonelope.tennis.scoreprocessor.processor;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchRules;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategyResolver;

public class SinglesMatchProcessor extends AbstractMatchProcessor {
	
	public SinglesMatchProcessor(ScoreCompletionStrategyResolver scoreCompletionStrategyResolver) {
		super(scoreCompletionStrategyResolver);
	}
	
	public Match addStrokeToMatch(Match match, Stroke stroke) {
		if (!match.isInProgress() && !match.isNotStarted()) {
			throw new FrameworkException("Updating score when match status is " + match.getStatus() + " is not supported.");
		}
		
		if (match.isNotStarted()) {
			match.setStatus(Status.IN_PROGRESS);
		}
		
		this.validateStroke(match, stroke);
		return this.processStroke(match, stroke);
	}

	private Match processStroke(Match match, Stroke stroke) {
		MatchRules matchRules = match.getMatchRules();
		Set currentSet = this.getLastAndSetInProgress(match.getSets());
		Game currentGame = this.getLastAndSetInProgress(currentSet.getGames());
		Point currentPoint = this.getLastAndSetInProgress(currentGame.getPoints());
		
		currentPoint.getStrokes().add(stroke);
		
		if (this.isComplete(currentPoint, match)) {
			if (this.isComplete(currentGame, match)) {
				if (this.isComplete(currentSet, match)) {
					if (this.isComplete(match, match)) {
						this.onMatchFinish(match);
					} else {
						Player server = this.getOpposingPlayer(currentGame.getServer(), match.getPlayers());
						match.getSets().add(new Set(matchRules, server, currentGame.getServer(), true));
						this.onSetFinish(match);
					}
				} else {
					currentSet.getGames().add(this.createNextGame(matchRules, match));
					this.onGameFinish(match);
				}
			} else {
				currentGame.getPoints().add(new Point(currentGame.getNextServer(), currentGame.getNextReceiver()));
				this.onPointFinish(match);
			}
		}
		
		return match;
	}

	/**
	 * @param match
	 */
	protected void onPointFinish(Match match) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param match
	 */
	protected void onGameFinish(Match match) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param match
	 */
	protected void onSetFinish(Match match) {
		// TODO Auto-generated method stub
	}

	private Game createNextGame(MatchRules matchRules, Match match) {
		Set currentSet = match.getCurrentSet();
		Game currentGame = currentSet.getCurrentGame();
		
		if ((!matchRules.isFinalSetTiebreakDisabled() && match.isCurrentlyInFinalSet()) && currentSet.isNextGameTiebreakEligible() || 
				(!match.isCurrentlyInFinalSet() && currentSet.isNextGameTiebreakEligible())) {
			return new TiebreakGame(currentGame.getReceiver(), currentGame.getServer(), true);
		} else {
			return new Game(currentGame.getReceiver(), currentGame.getServer(), true);
		}
	}
	
	private void onMatchFinish(Match match) {
		// TODO Auto-generated method stub	
	}

	private boolean isComplete(Winnable scoringObject, Match match) {
		return this.scoreCompletionStrategyResolver.resolve(scoringObject, match);
	}

	// TODO Move to validator
	private boolean validateStroke(Match match, Stroke stroke) {
		Game currentGame = match.getCurrentSet().getCurrentGame();
		Point currentPoint = currentGame.getCurrentPoint();
		
		if (currentPoint.getStrokes().isEmpty()) {
			if (!StrokeType.FIRST_SERVE.equals(stroke.getStrokeType())) {
				throw new FrameworkException("First stroke of a point must be a first serve. Found: " + stroke);
			}
			if (!(currentGame instanceof TiebreakGame) && !stroke.getPlayer().equals(currentGame.getServer())) {
				throw new FrameworkException("First stroke server does not match starting server.");
			}
		}
		// TODO - More validations
		return true;
	}
}
