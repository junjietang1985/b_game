package bowling.visitor;

/**
 * The Visitor interface of the visitor pattern.
 * @author Junjie
 *
 */
public interface Visitor {
	public void visit(Toss toss);
}
