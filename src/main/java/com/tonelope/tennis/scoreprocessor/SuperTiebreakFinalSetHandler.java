package com.tonelope.tennis.scoreprocessor;

/**
 *
 *
 * @author Tony Lopez
 *
 */
class SuperTiebreakFinalSetHandler implements FinalSetHandler {

	public static final int NUM_POINTS_TO_WIN = 10;
	public static final int NUM_POINTS_TO_CONTINUE = NUM_POINTS_TO_WIN - 1;

	private int[] scores = new int[2];

	@Override
	public String getScore() {
		StringBuilder sb = new StringBuilder();
		if (!isComplete()) {
			sb.append("(").append(scores[0]).append(" - ").append(scores[1]).append(")");
		} else {
			int idxWinner = scores[0] > scores[1] ? 0 : 1;
			int idxLoser = idxWinner == 0 ? 1 : 0;
			if (0 == idxWinner) {
				sb.append("1-0(");
			} else {
				sb.append("0-1(");
			}
			sb.append(scores[idxLoser]).append(")");
		}
		return sb.toString();
	}

	@Override
	public void update(int idxWinner) {
		scores[idxWinner]++;
	}

	@Override
	public boolean isComplete() {
		return (scores[0] == NUM_POINTS_TO_WIN && scores[1] < NUM_POINTS_TO_CONTINUE)
				|| (scores[0] < NUM_POINTS_TO_CONTINUE && scores[1] == NUM_POINTS_TO_WIN)
				|| (scores[0] >= NUM_POINTS_TO_CONTINUE && scores[1] >= NUM_POINTS_TO_CONTINUE
						&& (scores[0] > scores[1] + 1 || scores[1] > scores[0] + 1));
	}

}
