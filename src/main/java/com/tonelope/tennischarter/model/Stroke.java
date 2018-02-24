package com.tonelope.tennischarter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stroke extends ScoringObject {

	private Player player;
	private StrokeType strokeType;
	private boolean out;
	private boolean winner;
	
	public boolean isServe() {
		return StrokeType.FIRST_SERVE.equals(this.strokeType) || StrokeType.SECOND_SERVE.equals(this.strokeType);
	}
	
	public boolean isOutRallyShot() {
		return this.out && !this.isServe();
	}
	
	public boolean isDoubleFault() {
		return StrokeType.SECOND_SERVE.equals(strokeType) && this.out;
	}
	
	public boolean isServiceAce() {
		return StrokeType.FIRST_SERVE.equals(strokeType) && this.winner;
	}
}
