package bowling.visitor;

import bowling.strategy.NonBonusStrategy;
import bowling.strategy.TenthFrameBonusStrategy;

/**
 * Represents a toss which is an element that can be visited.
 * The score of the toss is generated in the constructor.
 * @author Junjie
 *
 */
public class Toss implements Element {
	public static int FULL_SCORE = 10;
	private int score;

	public Toss(AbstractFrameVisitor currentFrame) {
		if (currentFrame.getBonusStrategy() instanceof NonBonusStrategy) {
			NonBonusStrategy nonBonusStrategy = (NonBonusStrategy) currentFrame.getBonusStrategy();
			this.score = nonBonusStrategy.generateScore();
		} else if (currentFrame.getBonusStrategy() instanceof TenthFrameBonusStrategy) {
			TenthFrameBonusStrategy tenthFrameBonusStrategy = (TenthFrameBonusStrategy) currentFrame.getBonusStrategy();
			this.score = tenthFrameBonusStrategy.generateScore();
		} else {
			throw new IllegalStateException(
					"Error! currentFrame.getBonusStrategy(): "
							+ currentFrame.getBonusStrategy().getClass()
									.getSimpleName()
							+ " currentFrame.getBonusStrategy().getFrameState(): "
							+ currentFrame.getBonusStrategy().getFrameState());
		}
	}

	public int getScore() {
		return score;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
