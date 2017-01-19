package bowling.visitor;

import java.util.Optional;
import bowling.strategy.BonusStrategy;

public abstract class AbstractFrameVisitor implements Visitor,
		Comparable<AbstractFrameVisitor> {
	protected int id;
	protected BonusStrategy bonusStrategy;
	protected Optional<Integer> frameScore = Optional.empty();

	public AbstractFrameVisitor(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public BonusStrategy getBonusStrategy() {
		return bonusStrategy;
	}

	public void setBonusStrategy(BonusStrategy bonusStrategy) {
		this.bonusStrategy = bonusStrategy;
	}

	public Optional<Integer> getFrameScore() {
		return frameScore;
	}

	public boolean isFrameScoreDetermined() {
		return frameScore.isPresent();
	}

	@Override
	public void visit(Toss toss) {
		this.bonusStrategy = this.bonusStrategy.update(toss.getScore());
		this.frameScore = this.bonusStrategy.getFrameScore();
	}

	@Override
	public int compareTo(AbstractFrameVisitor another) {
		return this.id - another.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractFrameVisitor other = (AbstractFrameVisitor) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public abstract boolean isFinished();

	public abstract void displayFirstRow();

	public abstract void displaySecondRow();

	public abstract void displayThirdRowWithFrameScores();

	public abstract void displayForthRow();

	public abstract void displayFifthRowWithFrameScore(Optional<Integer> totalScore);

}
