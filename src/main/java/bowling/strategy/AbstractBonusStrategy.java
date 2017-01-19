package bowling.strategy;

import java.util.Optional;

import bowling.FrameState;

public abstract class AbstractBonusStrategy implements BonusStrategy {
	protected Optional<Integer> firstToss = Optional.empty();
	protected Optional<Integer> secondToss= Optional.empty();
	protected FrameState frameState;

	@Override
	public Optional<Integer> getFirstToss() {
		return this.firstToss;
	}

	@Override
	public Optional<Integer> getSecondToss() {
		return this.secondToss;
	}	

	@Override
	public FrameState getFrameState() {
		return this.frameState;
	}
	
	public void setFrameState(FrameState frameState){
		this.frameState = frameState;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " firstToss: "
				+ firstToss + " secondToss: " + secondToss;
	}

}
