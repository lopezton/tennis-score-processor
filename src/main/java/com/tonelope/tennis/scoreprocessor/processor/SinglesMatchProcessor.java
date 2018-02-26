package com.tonelope.tennis.scoreprocessor.processor;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategyResolver;
import com.tonelope.tennis.scoreprocessor.utils.ListUtils;

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
						match.getSets().add(new Set(server, currentGame.getServer(), true));
					}
				} else {
					Game nextGame;
					if (!match.isCurrentlyInFinalSet()) {
						if (currentSet.getGames().size() != (2 * match.getMatchRules().getNumberOfGamesPerSet())) {
							nextGame = new Game(currentGame.getReceiver(), currentGame.getServer(), true);
						} else {
							nextGame = new TiebreakGame(currentGame.getReceiver(), currentGame.getServer(), true);
						}
					} else {
						if (match.getMatchRules().isFinalSetTiebreakDisabled()) {
							nextGame = new Game(currentGame.getReceiver(), currentGame.getServer(), true);
						} else {
							if (currentSet.getGames().size() != (2 * match.getMatchRules().getNumberOfGamesPerSet())) {
								nextGame = new Game(currentGame.getReceiver(), currentGame.getServer(), true);
							} else {
								nextGame = new TiebreakGame(currentGame.getReceiver(), currentGame.getServer(), true);
							}
						}
					}
					currentSet.getGames().add(nextGame);
				}
			} else {
				currentGame.getPoints().add(new Point(currentGame.getNextServer(), currentGame.getNextReceiver()));
			}
		}
		
		return match;
	}

	private void onMatchFinish(Match match) {
		// TODO Auto-generated method stub	
	}

	private boolean isComplete(Winnable scoringObject, Match match) {
		return this.scoreCompletionStrategyResolver.resolve(scoringObject, match);
	}

	private boolean validateStroke(Match match, Stroke stroke) {
		Set currentSet = ListUtils.getLast(match.getSets());
		Game currentGame = ListUtils.getLast(currentSet.getGames());
		Point currentPoint = ListUtils.getLast(currentGame.getPoints());
		
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
