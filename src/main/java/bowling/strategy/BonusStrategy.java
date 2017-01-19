package bowling.strategy;

import java.util.Optional;

import bowling.FrameState;

public interface BonusStrategy {
	public Optional<Integer> getFirstToss();
	public Optional<Integer> getSecondToss();	
	public FrameState getFrameState();
	public BonusStrategy update(int score);
	public Optional<Integer> getFrameScore();
	public void displayThirdRowWithFrameScores();
}
