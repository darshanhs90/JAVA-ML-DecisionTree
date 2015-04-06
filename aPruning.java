
import java.io.Serializable;
import java.util.Random;


public class aPruning implements Serializable{
	/**
	 * write the best to the file and read from the file
	 */
	private static final long serialVersionUID = -1494599345565410873L;
	/**
	 * 
	 */

	public   final aDecisionTree tree;
	public static double maxVal=0;
	public aDecisionTree tree2;
	public aPruning(aDecisionTree aDecisionTree) throws Exception {
		tree=aDecisionTree;
	}

	public  aDecisionTree pruner(Integer l,Integer k) throws Exception{
		System.out.println("Pruning the tree now.Wait!!!!!!");
		aDecisionTree adBest = null;//=new aDecisionTree();
		adBest.b=false;
		adBest=new aDecisionTree(false);
		adBest.init();
		adBest=tree;
		aDecisionTree adDash=null;
		Random randM=new Random();
		Random randP=new Random();
		boolean rn=false;
		double accuracy=0;
		int m=0;
		aDecisionTree[] arrVal=new aDecisionTree[l+1];
		Node[] nodeVal=new Node[l+1];
		
		Double[] accrcy=new Double[l+1];
		arrVal[0]=(aDecisionTree) ObjectCloner.deepCopy(adBest);
		accrcy[0]=adBest.calculateAccuracy2();
		nodeVal[0]=(Node) ObjectCloner.deepCopy(adBest.rootNode);
		//System.out.println("l is :"+l);
		for (int i = 1; i <= l; i++) {
			//System.out.println("i is:"+i);
			System.out.print(".");
			if(i==l/4){
				System.out.println();
				System.out.println("25% pruning done");
			}
			if(i==l/2){System.out.println();
				System.out.println("50% pruning done");
			}
			if(i==(3*l/4)){System.out.println();
				System.out.println("75% pruning done");
			}
			if(i+1>l){
				System.out.println();
				System.out.println("100% pruning done");
			}
			adBest=new aDecisionTree(false);
			adBest.init();
			if(adDash==null){
				 adDash=new aDecisionTree(false);
				adDash.init();
				accuracy=adDash.calculateAccuracy2();
			}
			m=randM.nextInt(k);
			for (int j = 1; j <= m; j++) {
				adDash.count1=0;
				adDash.numberOfNonLeafNodes(adDash.rootNode);
				int n=adDash.count1;
				int p=0;
				if(n!=0 && n!=1){
					p=randP.nextInt(n);
					while(p==0){
						p=randP.nextInt(n);
					}
				}
				if(n==1)
					p=1;
				if(n==0){
					continue;
				}
				if(p>n)
					continue;

				if(p!=1){
					adDash.getNode(adDash.rootNode,p);
					adDash.foundNode.leftNode=null;
					adDash.foundNode.rightNode=null;
					adDash.foundNode.attributeName="null";
					if(adDash.foundNode.posExamples>adDash.foundNode.negExamples){
						adDash.foundNode.label=new String("1");
					}
					else{
						adDash.foundNode.label=new String("0");
					}
				}
				else{
					rn=true;
					break;
				}
			}	
			if(rn==false){
				if(adBest.calculateAccuracy2()>adDash.calculateAccuracy2()){
					accuracy=adBest.calculateAccuracy2();
					accrcy[i]=accuracy;
					adBest.count1=0;
					nodeVal[i]=(Node) ObjectCloner.deepCopy(adBest.rootNode);
					adBest.numberOfNonLeafNodes(adBest.rootNode);
					arrVal[i]=(aDecisionTree) ObjectCloner.deepCopy(adBest);

					//System.out.println("adbest is greater"+adBest.count1+accuracy+nodeVal[i]+"||"+arrVal[i]);
					//System.out.println("adbest is greater compare"+adBest.count1+accuracy+adBest.rootNode+"||"+adBest);
					
				}
				else{
					adBest=null;
					accrcy[i]=adDash.calculateAccuracy2();
					adDash.count1=0;
					adDash.numberOfNonLeafNodes(adDash.rootNode);
					nodeVal[i]=(Node) ObjectCloner.deepCopy(adDash.rootNode);
					arrVal[i]=(aDecisionTree) ObjectCloner.deepCopy(adDash);
					//System.out.println("addash is greater"+adDash.count1+accrcy[i]+nodeVal[i]+"||"+arrVal[i]);
					//System.out.println("addash is greater compare"+adDash.count1+accrcy[i]+adDash.rootNode+"||"+adDash);
					
				}
			}
			else{
				double accuracy_new=adDash.rootNode.posExamples/(double)(adBest.rootNode.posExamples+adBest.rootNode.negExamples);
				adDash.rootNode=null;
				if(adBest.calculateAccuracy2()<accuracy_new){
					//adBest=adDash;
					accrcy[i]=accuracy_new;
					arrVal[i]=(aDecisionTree) ObjectCloner.deepCopy(adDash);
					nodeVal[i]=(Node) ObjectCloner.deepCopy(adDash.rootNode);
					rn=false;
				}
				else{
					arrVal[i]=(aDecisionTree) ObjectCloner.deepCopy(adBest);
					accrcy[i]=adBest.calculateAccuracy2();
					nodeVal[i]=(Node) ObjectCloner.deepCopy(adBest.rootNode);
					rn=false;
				}
			}
			//arrVal[i]=adBest;
			adDash=null;
		}
		for (int j = 0; j < arrVal.length; j++) {
			arrVal[j].count1=0;
			arrVal[j].numberOfNonLeafNodes(arrVal[j].rootNode);
			//System.out.println(arrVal[j].count1);
			arrVal[j].count1=0;
			/*System.out.print(arrVal[j]);
			System.out.println("||"+accrcy[j]);*/
		}
		double max=0;
		int index=0;
		for (int j = 0; j < accrcy.length; j++) {
			if(accrcy[j]>max)
			{
				index=j;
				max=accrcy[j];
			}
		}
		arrVal[index].count1=0;
		arrVal[index].numberOfNonLeafNodes(nodeVal[index]);
		//System.out.println("Count val is:"+arrVal[index].count1);
		arrVal[index].rootNode=nodeVal[index];
		//arrVal[index].preOrder1(nodeVal[index]);
		
		maxVal=accrcy[index];
		return arrVal[index];
	}
}
