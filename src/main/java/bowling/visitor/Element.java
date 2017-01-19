package bowling.visitor;

/**
 * The Element interface of the visitor pattern.
 * @author Junjie
 *
 */
public interface Element {
	public void accept(Visitor v);
}
