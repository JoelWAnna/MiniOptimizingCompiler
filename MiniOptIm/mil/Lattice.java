package mil;

public class Lattice {
	private latticeNode head;
	public Lattice(Var[] formals) {
		if (formals == null || formals.length == 0)
			return;
		head = new latticeNode(formals[0]);
		System.out.println(formals[0].toString());
		latticeNode current = head;
		for (int i = 1; i < formals.length; ++i) {
			current = current.Next(new latticeNode(formals[i]));

			System.out.println(formals[i].toString());
		}
	}

}

class latticeNode {
	private latticeNode next;
	private Var formal;
	public latticeNode (Var formal)
	{
		this.formal = formal;
	}

	public latticeNode Next() {
		return this.next;
	}
	public latticeNode Next(latticeNode next) {
		if (next != null)
			this.next = next;
		return this.next;
	}
}