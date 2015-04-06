
import java.io.Serializable;

class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4899453172104465681L;
	private double parentProbability;
	public String attributeName;
	public String label;
	public int nodeNum,posExamples,negExamples;
	public Node leftNode,rightNode,otherNode;
	public Node() {
		// TODO Auto-generated constructor stub
	}
	public Node(double parentProbability,String attributeName) {
		// TODO Auto-generated constructor stub
		this.setAttributeName(attributeName);
		this.parentProbability=parentProbability;

	}
	public Node(Node n)
	{
		this.otherNode=n;
	}	
	public Node(String value, Node left, Node right) {
        this.attributeName=value;
        this.leftNode=left;
        this.rightNode=right;
    }
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public Node getRightNode() {
		return rightNode;
	}
	public void setRightNode(Node rightNode) {
		this.rightNode = rightNode;
	}
	public Node getLeftNode() {
		return leftNode;
	}
	public void setLeftNode(Node leftNode) {
		this.leftNode = leftNode;
	}
}
