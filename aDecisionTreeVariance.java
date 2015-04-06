
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;

public final class aDecisionTreeVariance implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6164305527716165844L;
	/**
	 * 
	 */
	public aDecisionTreeVariance adt;
	private static ArrayList<String> arrayList;
	public static String line,trainingset,testingset,validationset,preOrderString;
	private static Integer[][] matrix;
	private static Integer[] classValues;
	private static String[] attrNames;
	private static Stack<String> stack=new Stack<String>();
	public static boolean b=true;
	private static TreeMap<String,Integer> temp=new TreeMap<String, Integer>();

	public static int spaces=0,currSpace=0,prevSpace=0,count=0,count1=0,rowCount,rootPos,rootNeg;
	public static Node rootNode,foundNode,rootNodeCopy;
	public aDecisionTreeVariance() throws IOException {
	}
	public aDecisionTreeVariance(boolean b) throws IOException {
		this.b=b;
	}
	public static void init() throws IOException {
		try {
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(trainingset)));
			line=bufferedReader.readLine();
			arrayList=new ArrayList<String>();
			int noOfAttrs=0;
			noOfAttrs = calculateNoOfAttrs(noOfAttrs);
			noOfAttrs++;
			rowCount=-1;
			rowCount = readLines(bufferedReader, rowCount);
			if(b){
				System.out.println("Number of Rows is :"+rowCount);
				System.out.println("Number of attributes is :"+(noOfAttrs-1));
			}
			matrix=new Integer[rowCount][noOfAttrs-1];
			classValues=new Integer[rowCount];
			attrNames=new String[noOfAttrs-1];
			initialiseData(noOfAttrs);
			rootNodeCopy=rootNode=(id3Algo(classValues,matrix,attrNames,-2));
			rootPos=rootNode.posExamples;
			rootNeg=rootNode.negExamples;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static Node id3Algo(Integer[] cV, Integer[][] mtrx,String[] attrsLeft,double parentProb) {
		Node n=new Node();
		Integer posCount=0,negCount=0;
		for (int i = 0; i < cV.length; i++) {
			if(cV[i]==1){
				posCount++;
			}
			else{
				negCount++;
			}
		}
		if(posCount==cV.length){
			n.attributeName="null";
			n.setLabel(new String("1"));
			n.leftNode=n.rightNode=null;
			return n;
		}
		else if(negCount==cV.length){
			n.attributeName="null";
			n.setLabel(new String("0"));
			n.leftNode=n.rightNode=null;
			return n;
		}
		else if(attrsLeft==null ||attrsLeft.length==0){
			if(posCount>negCount){
				n.attributeName="null";
				n.setLabel(new String("1"));
			}
			else{
				n.attributeName="null";
				n.setLabel(new String("0"));
			}
			n.leftNode=n.rightNode=null;
			return n;
		}
		else{
			if(parentProb==-2){//initial case
				parentProb=(double)(posCount*negCount)/(double)((posCount+negCount)*(posCount+negCount));
			}
			String str=getCounts_Entropy_Max(mtrx,cV,attrsLeft,parentProb);
			String maxAttrName=str.substring(0,str.indexOf("/"));
			n.attributeName=maxAttrName;
			n.posExamples=posCount;
			n.negExamples=negCount;
			double leftParentProb=0,rightParentProb=0;
			leftParentProb=Double.parseDouble(str.split("/")[1]);
			rightParentProb=Double.parseDouble(str.split("/")[2]);
			int index=0;
			for (int i = 0; i < attrsLeft.length; i++) {
				if(attrsLeft[i].equalsIgnoreCase(maxAttrName)){
					index=i;
					break;
				}
			}
			int pcount=0,ncount=0;
			for (int i = 0; i < mtrx.length; i++) {
				if(mtrx[i][index]==1){
					pcount++;
				}
				else{
					ncount++;
				}
			}
			Integer[] positivecV=new Integer[pcount];
			Integer[] negativecV=new Integer[ncount];
			Integer[][] positiveMatrix=new Integer[pcount][attrsLeft.length-1];
			Integer[][] negativeMatrix=new Integer[ncount][attrsLeft.length-1];

			int counter=0;

			for (int i = 0; i < cV.length; i++) {
				if(mtrx[i][index]==1){
					positivecV[counter]=cV[i];
					counter++;
				}
			}
			counter=0;

			boolean post=false;
			for (int i = 0; i < mtrx[0].length; i++) {
				if(i==index){
					post=true;
					continue;
				}
				for (int j = 0; j < mtrx.length; j++) {
					////System.out.println(i+"|"+j+"|"+counter);
					if(mtrx[j][index]==1 && post==false){
						positiveMatrix[counter][i]=mtrx[j][i];
						counter++;
					}
					if(mtrx[j][index]==1 && post==true){
						positiveMatrix[counter][i-1]=mtrx[j][i];
						counter++;
					}
				}
				counter=0;
			}
			for (int i = 0; i < cV.length; i++) {
				if(mtrx[i][index]==0){
					negativecV[counter]=cV[i];
					counter++;
				}
			}
			counter=0;
			post=false;
			for (int i = 0; i < mtrx[0].length; i++) {
				if(i==index){
					post=true;
					continue;
				}
				for (int j = 0; j < mtrx.length; j++) {
					if(mtrx[j][index]==0 && post==false){
						negativeMatrix[counter][i]=mtrx[j][i];
						counter++;
					}
					if(mtrx[j][index]==0 && post==true){
						negativeMatrix[counter][i-1]=mtrx[j][i];
						counter++;
					}
				}
				counter=0;
			}
			counter=0;
			if(positiveMatrix==null||positiveMatrix.length==0||positiveMatrix[0].length==0){
				n.setAttributeName(maxAttrName);
				n.leftNode=new Node();
				n.leftNode.label=new String("1");
				n.leftNode.attributeName="null";
				return n;
			}

			if(negativeMatrix==null||negativeMatrix.length==0||negativeMatrix[0].length==0){
				n.setAttributeName(maxAttrName);
				n.rightNode=new Node();
				n.rightNode.label=new String("0");
				n.rightNode.attributeName="null";
				return n;
			}
			else{
				String[] attrLeftNew=new String[attrsLeft.length-1];
				counter=0;
				post=false;
				for (int i = 0; i < attrsLeft.length; i++) {
					if(i==index){
						post=true;
						continue;
					}
					if(post==false){
						attrLeftNew[i]=attrsLeft[i];
					}
					else{
						attrLeftNew[i-1]=attrsLeft[i];
					}
				}
				n.leftNode= id3Algo(positivecV, positiveMatrix, attrLeftNew, leftParentProb);
				n.rightNode=id3Algo(negativecV, negativeMatrix, attrLeftNew, rightParentProb);
				return n;
			}

		}
	}
	private static double calcLog(double i) {
		return -i*Math.log(i)/Math.log(2);
	}
	private static String getCounts_Entropy_Max(Integer[][] mtrx, Integer[] cV,
			String[] attrsLeft,double parentProb) {
		int ppos=0,pneg=0,npos=0,nneg=0;
		HashMap<String, Double> mapper=new HashMap<String, Double>();
		HashMap<String, Double> leftProb=new HashMap<String, Double>();	
		HashMap<String, Double> rightProb=new HashMap<String, Double>();
		for (int i = 0; i < mtrx[0].length; i++) {
			int leftPos=0,leftNeg=0,rightPos=0,rightNeg=0;
			for (int j = 0; j < mtrx.length; j++) {
				if(mtrx[j][i]==1 && cV[j]==1)
					leftPos++;
				else if(mtrx[j][i]==0 && cV[j]==1)
					rightPos++;
				else if(mtrx[j][i]==1 && cV[j]==0)
					leftNeg++;
				else
					rightNeg++;
			}
			double[] d=new double[2];
			d=calculateEntropy(leftPos,leftNeg,rightPos,rightNeg);
			double val=0;
			double parentTotal=leftPos+leftNeg+rightPos+rightNeg;
			val=parentProb-((leftPos+leftNeg)*d[0]/(double)parentTotal)-((rightPos+rightNeg)*d[1]/(double)parentTotal);
			mapper.put(attrsLeft[i],val);
			leftProb.put(attrsLeft[i], d[0]);
			rightProb.put(attrsLeft[i], d[1]);
		}
		return findMax(mapper,attrsLeft,leftProb,rightProb);
	}

	private static String findMax(HashMap<String, Double> mapper,String[] attrsLeft,HashMap<String, Double> leftProb,HashMap<String, Double> rightProb) {	
		String maxAttrName="";
		double max=0,leftEntropy=0,rightEntropy=0;
		for (int i = 0; i < mapper.size(); i++) {
			if((double)(mapper.values().toArray()[i])>max){
				max=(double)(mapper.values().toArray()[i]);
				maxAttrName=(String) mapper.keySet().toArray()[i];
				leftEntropy=(double)leftProb.values().toArray()[i];
				rightEntropy=(double)rightProb.values().toArray()[i];
			}
		}
		String str=maxAttrName+"/"+leftEntropy+"/"+rightEntropy;
		return str;
	}
	private static double[] calculateEntropy(double leftPos,double leftNeg,double rightPos,double rightNeg) {
		double x=0,y=0,x1=0,y1=0;
		double leftEntropy=0,rightEntropy=0;
		if(leftPos==0 && leftNeg==0)
		{
			leftEntropy=0;
		}
		else
			leftEntropy=(leftPos*leftNeg)/(double)((leftPos+leftNeg)*(leftPos+leftNeg));
		if(rightPos==0 && rightNeg==0)
		{
			rightEntropy=0;
		}
		else
			rightEntropy=(rightPos*rightNeg)/(double)((rightPos+rightNeg)*(rightPos+rightNeg));

		double[] ret=new double[2];
		ret[0]=leftEntropy;
		ret[1]=rightEntropy;
		return ret;
	}
	private static void initialiseData(int noOfAttrs) {
		for (int i = 0; i < arrayList.size(); i++) {
			StringTokenizer stringTokenizer=new StringTokenizer(arrayList.get(i),",");
			int j=0;
			while (stringTokenizer.hasMoreElements()) {
				if(i==0){
					String str = (String) stringTokenizer.nextElement();
					if(j!=noOfAttrs-1)
						attrNames[j]=str;
					j++;
				}
				else{
					String val = (String) stringTokenizer.nextElement();
					Integer val1=Integer.parseInt(val);
					if(j!=noOfAttrs-1)
						matrix[i-1][j]=val1;
					else
						classValues[i-1]=val1;
					j++;
				}
			}
		}
	}
	private static int calculateNoOfAttrs(int noOfAttrs) {
		for (int i = 0; i < line.length(); i++) {
			if(line.charAt(i)==',')
				noOfAttrs++;
		}
		return noOfAttrs;
	}
	private static int readLines(BufferedReader bufferedReader, int rowCount)
			throws IOException {
		while(line!=null)
		{
			arrayList.add(line);
			line=bufferedReader.readLine();	
			rowCount++;
		}
		return rowCount;
	}

	public static void numberOfNonLeafNodes(Node t){
		if(t!=null && !t.attributeName.contentEquals("null")){
			count1++;
			t.nodeNum=count1;
			numberOfNonLeafNodes(t.leftNode);
			numberOfNonLeafNodes(t.rightNode);
		}
	}
	public static void getNode(Node t,int nodeNumber){
		if(t!=null ){
			if(t.nodeNum==nodeNumber)
			{
				foundNode=t;
				return;
			}
			else{
				getNode(t.leftNode,nodeNumber);
				getNode(t.rightNode,nodeNumber);
			}
		}
	}
	public static void numberOfNonLeafNodes1(Node t){
		if(t!=null ){
			numberOfNonLeafNodes1(t.leftNode);
			numberOfNonLeafNodes1(t.rightNode);
		}
	}

	public static double calculateAccuracy(){
		Node test=new Node();
		test=rootNode;
		int pos=0,neg=0;
		for (int i = 0; i < rowCount; i++) {
			test=rootNode;
			while(test.label==null)
			{
				int index=0;
				for (int j = 0; j < attrNames.length; j++) {
					if(test.attributeName.contentEquals(attrNames[j]))
					{
						index=j;
						break;
					}
				}
				if(matrix[i][index]==0){
					test=test.rightNode;
				}
				else if(matrix[i][index]==1)
				{
					test=test.leftNode;
				}
			}
			if(Integer.parseInt(test.label)==classValues[i]){
				pos++;
			}
			else{
				neg++;
			}			
		}
		return pos/(double)(pos+neg);
	}
	public static double calculateAccuracy1() throws Exception{
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(testingset)));
		line=bufferedReader.readLine();
		arrayList=new ArrayList<String>();
		int noOfAttrs=0;
		noOfAttrs = calculateNoOfAttrs(noOfAttrs);
		noOfAttrs++;
		rowCount=-1;
		rowCount = readLines(bufferedReader, rowCount);
		matrix=new Integer[rowCount][noOfAttrs-1];
		classValues=new Integer[rowCount];
		attrNames=new String[noOfAttrs-1];
		initialiseData(noOfAttrs);
		Node test=new Node();
		test=rootNode;
		int pos=0,neg=0;
		for (int i = 0; i < rowCount; i++) {
			test=rootNode;
			if(test!=null){
				while(test.label==null)
				{
					int index=0;
					for (int j = 0; j < attrNames.length; j++) {
						if(test.attributeName.contentEquals(attrNames[j]))
						{
							index=j;
							break;
						}
					}
					if(matrix[i][index]==0){
						test=test.rightNode;
					}
					else if(matrix[i][index]==1)
					{
						test=test.leftNode;
					}
				}
				if(Integer.parseInt(test.label)==classValues[i]){
					pos++;
				}
				else{
					neg++;
				}
			}
			else{
				pos=rootPos;
				neg=rootNeg;
			}
		}
		return pos/(double)(pos+neg);
	}

	public static double calculateAccuracy2() throws Exception{
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(validationset)));
		line=bufferedReader.readLine();
		arrayList=new ArrayList<String>();
		int noOfAttrs=0;
		noOfAttrs = calculateNoOfAttrs(noOfAttrs);
		noOfAttrs++;
		rowCount=-1;
		rowCount = readLines(bufferedReader, rowCount);
		matrix=new Integer[rowCount][noOfAttrs-1];
		classValues=new Integer[rowCount];
		attrNames=new String[noOfAttrs-1];
		initialiseData(noOfAttrs);
		Node test=new Node();
		test=rootNode;
		int pos=0,neg=0;
		for (int i = 0; i < rowCount; i++) {
			test=rootNode;
			if(test!=null){
				while(test.label==null)
				{
					int index=0;
					for (int j = 0; j < attrNames.length; j++) {
						if(test.attributeName.contentEquals(attrNames[j]))
						{
							index=j;
							break;
						}
					}
					if(matrix[i][index]==0){
						test=test.rightNode;
					}
					else if(matrix[i][index]==1)
					{
						test=test.leftNode;
					}
				}
				if(Integer.parseInt(test.label)==classValues[i]){
					pos++;
				}
				else{
					neg++;
				}
			}
			else{
				pos=rootPos;
				neg=rootNeg;
			}
		}
		return pos/(double)(pos+neg);
	}
	public static void preOrder1( Node t )
	{	
		if( t != null )
		{	
			if(!t.attributeName.contentEquals("null")){
				for (int i = 0; i < spaces; i++) {
					System.out.print("|");
				}
				temp.put(t.attributeName,spaces);
				spaces++;
				System.out.println(t.attributeName+" = 1 ");
				stack.push(t.attributeName);
			}
			else{
				for (int i = 0; i < spaces; i++) {
					System.out.print("|");
				}
				System.out.println("Class Value : "+t.label);
				if(!stack.isEmpty()){
					String str=stack.pop();
					int n=temp.get(str);
					temp.remove(str);
					for (int i = 0; i < n; i++) {
						System.out.print("|");
					}
					if(stack.isEmpty() && str.contentEquals(rootNode.attributeName))
					{
						spaces=1;
					}
					else if(stack.isEmpty()){
						spaces--;
					}
					System.out.println(str+" = 0 ");
				}
			}
			preOrder1( t.leftNode );
			preOrder1( t.rightNode );
		}
	}


}
