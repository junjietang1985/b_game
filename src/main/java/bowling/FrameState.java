package bowling;

/**
 * Represents all potential state of a frame
 * 
 * @author Junjie
 *
 */
public enum FrameState {
	WAIT_FOR_FIRST_TOSS, 
	WAIT_FOR_SECOND_TOSS,
	// only applicable for the last frame
	WAIT_FOR_THIRD_TOSS,
	// strike and spare need the first bonus
	WAIT_FOR_FIRST_BONUS,
	// only applicable for strike
	WAIT_FOR_SECOND_BONUS,
	// the last state of all frame, when the frame score can be calulated
	FRAME_SCORE_DETERMINED;
}
