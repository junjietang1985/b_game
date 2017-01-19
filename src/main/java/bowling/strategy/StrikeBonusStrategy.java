package bowling.strategy;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import bowling.FrameManager;
import bowling.FrameState;
import bowling.visitor.Toss;

/**
 * By using this strategy, a frame can obtain two additional scores as bonus.
 * @author Junjie
 *
 */
public class StrikeBonusStrategy extends AbstractBonusStrategy implements
		BonusStrategy {
	private Optional<Integer> firstBonus = Optional.empty();
	private Optional<Integer> secondBonus = Optional.empty();

	public StrikeBonusStrategy() {
		firstToss = Optional.of(Toss.FULL_SCORE);
		super.setFrameState(FrameState.WAIT_FOR_FIRST_BONUS);
	}

	@Override
	public BonusStrategy update(int score) {
		switch (this.getFrameState()) {
		case WAIT_FOR_FIRST_BONUS:
			this.firstBonus = Optional.of(score);
			this.setFrameState(FrameState.WAIT_FOR_SECOND_BONUS);
			return this;
		case WAIT_FOR_SECOND_BONUS:
			this.secondBonus = Optional.of(score);
			this.setFrameState(FrameState.FRAME_SCORE_DETERMINED);
			return this;
		default:
			throw new IllegalStateException(
					"Error! Wrong state of StrikeBonusStrategy: "
							+ this.getFrameState());
		}
	}

	@Override
	public Optional<Integer> getFrameScore() {
		return this.getFrameState() == FrameState.FRAME_SCORE_DETERMINED ? Optional
				.of(Toss.FULL_SCORE + this.firstBonus.get()
						+ this.secondBonus.get()) : Optional.empty();
	}

	@Override
	public void displayThirdRowWithFrameScores() {
		System.out.print("|"
				+ StringUtils.center("", FrameManager.SIZE_HALF_CELL - 1, "")
				+ "|"
				+ StringUtils.center("X", FrameManager.SIZE_HALF_CELL - 1, "")
				+ "|");
	}

}
