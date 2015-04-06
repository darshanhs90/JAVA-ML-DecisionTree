
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;


public class aTester implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7467326745444246899L;

	/**
	 * 
	 */


	public  static void main(String[] args) throws Exception {

		/*FOR NORMAL DECISION TREE
		 */
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
		String str=bufferedReader.readLine();
		String[] strArr=str.split(" ");
		/*for (int i = 0; i < strArr.length; i++) {
			System.out.println(strArr[i]);
		}*/
		int l=0,k=0;
		String strCheck;
		if(strArr[0].contentEquals(".\\aDTree")){
			aDecisionTree D=new aDecisionTree(true);
			l=Integer.valueOf(strArr[1]);
			k=Integer.valueOf(strArr[2]);
			D.trainingset=strArr[3];
			D.testingset=strArr[4];
			D.validationset=strArr[5];
			D.init();
			strCheck=strArr[6];
			if(strCheck.contentEquals("yes")){
				D.preOrder1(D.rootNode);
			}
			D.spaces=0;
			System.out.println("TrainingSet Accuracy:"+D.calculateAccuracy()*100);
			System.out.println("TestSetAccuracy:"+D.calculateAccuracy1()*100);
			System.out.println("ValidationSetAccuracy Before pruning:"+D.calculateAccuracy2()*100);
			System.out.println("Pruning now");
			aPruning aPr=new aPruning(D);
			aDecisionTree new_adt1=aPr.pruner(l,k);
			System.out.println("ValidationSetAccuracy After pruning:"+aPr.maxVal*100);
			//new_adt1.preOrder1(new_adt1.rootNode);
			System.out.println(new_adt1.calculateAccuracy2());
			if(strCheck.contentEquals("yes")){
				new_adt1.preOrder1(new_adt1.rootNode);
			}
		}
		else if(strArr[0].contentEquals(".\\aDTreeVariance")){
			/*FOR VARIANCE DECISION TREE
			 */
			aDecisionTreeVariance D=new aDecisionTreeVariance(true);
			l=Integer.valueOf(strArr[1]);
			k=Integer.valueOf(strArr[2]);
			D.trainingset=strArr[3];
			D.testingset=strArr[4];
			D.validationset=strArr[5];
			D.init();
			strCheck=strArr[6];
			if(strCheck.contentEquals("yes")){
				D.preOrder1(D.rootNode);
			}
			D.spaces=0;
			System.out.println("TrainingSet Accuracy:"+D.calculateAccuracy()*100);
			System.out.println("TestSetAccuracy:"+D.calculateAccuracy1()*100);
			System.out.println("ValidationSetAccuracy Before pruning:"+D.calculateAccuracy2()*100);
			System.out.println("Pruning now");
			aPruningVariance aPr=new aPruningVariance(D);
			aDecisionTreeVariance new_adt1=aPr.pruner( l,k);
			System.out.println(new_adt1.calculateAccuracy2());
			if(strCheck.contentEquals("yes")){
				new_adt1.preOrder1(new_adt1.rootNode);
			}
		}


	}

}
