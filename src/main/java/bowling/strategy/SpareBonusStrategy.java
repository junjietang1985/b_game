package bowling.strategy;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import bowling.FrameManager;
import bowling.FrameState;
import bowling.visitor.Toss;
/**
 * By using this strategy, a frame can only obtain one additional score as bonus.
 * @author Junjie
 *
 */
public class SpareBonusStrategy extends AbstractBonusStrategy implements
		BonusStrategy {
	private Optional<Integer> firstBonus = Optional.empty();

	public SpareBonusStrategy(int firstToss, int secondToss) {
		this.firstToss = Optional.of(firstToss);
		this.secondToss = Optional.of(secondToss);
		super.setFrameState(FrameState.WAIT_FOR_FIRST_BONUS);
	}

	@Override
	public BonusStrategy update(int score) {
		switch (this.getFrameState()) {
		case WAIT_FOR_FIRST_BONUS:
			this.firstBonus = Optional.of(score);
			this.setFrameState(FrameState.FRAME_SCORE_DETERMINED);
			return this;

		default:
			throw new IllegalStateException(
					"Error! Wrong state of SpareBonusStrategy: "
							+ this.getFrameState());

		}
	}

	@Override
	public Optional<Integer> getFrameScore() {
		return this.getFrameState() == FrameState.FRAME_SCORE_DETERMINED ? Optional
				.of(Toss.FULL_SCORE + this.firstBonus.get()) : Optional.empty();
	}

	@Override
	public void displayThirdRowWithFrameScores() {
		System.out.print("|"
				+ StringUtils.center(
						this.getFirstToss().isPresent() ? String.valueOf(this
								.getFirstToss().get()) : "",
						FrameManager.SIZE_HALF_CELL - 1, "") + "|"
				+ StringUtils.center("/", FrameManager.SIZE_HALF_CELL - 1, "")
				+ "|");
	}

}
