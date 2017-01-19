package bowling.strategy;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import bowling.FrameManager;
import bowling.FrameState;
import bowling.ScoreGenerator;
import bowling.visitor.Toss;

/**
 * A specific strategy for the last frame.
 * @author Junjie
 *
 */
public class TenthFrameBonusStrategy extends AbstractBonusStrategy implements
		BonusStrategy {
	private ScoreGenerator generator;
	private Optional<Integer> thirdToss = Optional.empty();

	public TenthFrameBonusStrategy(ScoreGenerator generator) {
		super.setFrameState(FrameState.WAIT_FOR_FIRST_TOSS);
		this.generator = generator;
	}

	public Optional<Integer> getThirdToss() {
		return thirdToss;
	}

	@Override
	public Optional<Integer> getFrameScore() {
		return this.getFrameState() == FrameState.FRAME_SCORE_DETERMINED ? Optional
				.of(this.firstToss.get()
						+ this.secondToss.get()
						+ (this.thirdToss.isPresent() ? this.thirdToss.get()
								: 0)) : Optional.empty();
	}

	@Override
	public BonusStrategy update(int score) {
		switch (this.getFrameState()) {
		case WAIT_FOR_FIRST_TOSS:
			this.firstToss = Optional.of(score);
			this.setFrameState(FrameState.WAIT_FOR_SECOND_TOSS);
			return this;
		case WAIT_FOR_SECOND_TOSS:
			this.secondToss = Optional.of(score);
			// the third toss only happen when achieving strike or spare
			if (getFirstToss().get().intValue() == Toss.FULL_SCORE
					|| getFirstToss().get().intValue()
							+ getSecondToss().get().intValue() == Toss.FULL_SCORE) {
				this.setFrameState(FrameState.WAIT_FOR_THIRD_TOSS);
			} else {
				this.setFrameState(FrameState.FRAME_SCORE_DETERMINED);
			}
			return this;
		case WAIT_FOR_THIRD_TOSS:
			this.thirdToss = Optional.of(score);
			this.setFrameState(FrameState.FRAME_SCORE_DETERMINED);
			return this;
		default:
			throw new IllegalStateException(
					"Error! Wrong state of StrikeBonusStrategy: "
							+ this.getFrameState());
		}
	}

	public int generateScore() {
		int score, maxScore;
		switch (getFrameState()) {
		case WAIT_FOR_FIRST_TOSS:
			// 0 <= first toss <= full score
			score = generator.generateScore(Toss.FULL_SCORE);
			if (score <= Toss.FULL_SCORE && score >= 0) {
				return score;
			}
			throw new IllegalStateException("Error! first toss is invalid: "
					+ score);
		case WAIT_FOR_SECOND_TOSS:
			// if the first toss is not strike
			// 0 <= second toss <= full score - first toss
			if (getFirstToss().get() != Toss.FULL_SCORE) {
				maxScore = Toss.FULL_SCORE - getFirstToss().get();
				score = generator.generateScore(maxScore);
				if (score >= 0 && score <= maxScore) {
					return score;
				}
				throw new IllegalStateException(
						"Error! second toss is invalid, firstToss: "
								+ getFirstToss().get() + " secondToss: "
								+ score);
			}
			// otherwise
			// 0 <= second toss <= full score
			return generator.generateScore(Toss.FULL_SCORE);
		case WAIT_FOR_THIRD_TOSS:
			// if the first toss and the second toss are strike
			// or the two tosses make it as spare
			// 0 <= third toss <= full score
			if (getFirstToss().get() == Toss.FULL_SCORE
					&& getSecondToss().get() == Toss.FULL_SCORE
					|| getFirstToss().get() != Toss.FULL_SCORE) {
				score = generator.generateScore(Toss.FULL_SCORE);
				if (score <= Toss.FULL_SCORE && score >= 0) {
					return score;
				}
				throw new IllegalStateException(
						"Error! third toss is invalid: " + score);
			}
			// otherwise
			// 0 <= third toss <= full score - second toss
			maxScore = Toss.FULL_SCORE - getSecondToss().get();
			score = generator.generateScore(maxScore);
			if (score >= 0 && score <= maxScore) {
				return score;
			}
			throw new IllegalStateException(
					"Error! third toss is invalid, firstToss: "
							+ getFirstToss().get() + " secondToss: "
							+ getSecondToss().get() + " thirdToss: " + score);
		default:
			throw new IllegalStateException("Error! getFrameState: "
					+ getFrameState());
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " firstToss: " + firstToss
				+ " secondToss: " + secondToss
				+ (thirdToss.isPresent() ? " thirdToss: " + thirdToss : "");
	}

	@Override
	public void displayThirdRowWithFrameScores() {
		System.out.print("|"
				+ StringUtils.center(getFirstCellForDisplay(),
						FrameManager.SIZE_HALF_CELL - 1, "")
				+ "|"
				+ StringUtils.center(getSecondCellForDisplay(),
						FrameManager.SIZE_HALF_CELL - 1, "")
				+ "|"
				+ StringUtils.center(getThirdCellForDisplay(),
						FrameManager.SIZE_HALF_CELL - 1, "") + "|");
	}

	private String getFirstCellForDisplay() {
		return this.getFirstToss().isPresent() ? this.getFirstToss().get() == Toss.FULL_SCORE ? "X"
				: String.valueOf(this.getFirstToss().get())
				: "";
	}

	private String getSecondCellForDisplay() {
		return this.getSecondToss().isPresent() ? this.firstToss.get() != Toss.FULL_SCORE
				&& this.getFirstToss().get() + this.getSecondToss().get() == Toss.FULL_SCORE ? "/"
				: this.getSecondToss().get() == Toss.FULL_SCORE ? "X" : String
						.valueOf(this.getSecondToss().get())
				: "";
	}

	private String getThirdCellForDisplay() {
		return this.getThirdToss().isPresent() ? this.firstToss.get() == Toss.FULL_SCORE
				&& this.secondToss.get() != Toss.FULL_SCORE
				&& this.getSecondToss().get() + this.getThirdToss().get() == Toss.FULL_SCORE ? "/"
				: this.getThirdToss().get() == Toss.FULL_SCORE ? "X" : String
						.valueOf(this.getThirdToss().get())
				: "";
	}
}
