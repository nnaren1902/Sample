import java.util.LinkedList;
import java.util.ArrayList;
public class Matrix {
	
	public static void main(String[] args) {
		int[][] matrix = {{3, 1, 4}, {1, 5, 9}, {2, 6, 5}};
		
		int rowSize = matrix.length;
		int columnSize = matrix[0].length;
		
		ArrayList<ArrayList<LinkedList<Integer>>> minCostPath = new ArrayList<>();
		int[][] minCost = new int[rowSize][columnSize];
		
		for(int i=0; i<rowSize; i++) {
			ArrayList<LinkedList<Integer>> currentRow = new ArrayList<>();
			for(int j=0; j<columnSize; j++) {
				currentRow.add(j, new LinkedList<>());
			}
			minCostPath.add(i, currentRow);
		}
		
		if(findMinCostPath(matrix, minCost, minCostPath, rowSize, columnSize)) {
			//after building the min cost path matrix, parse the first row and find the minimum element and the corresponding path
			int min = minCost[0][0];
			int minColumn = 0;
			for(int col=1; col<columnSize; col++) {
				if(minCost[0][col] < min) {
					min = minCost[0][col];
					minColumn = col;
				}
			}
			System.out.println(min);
			System.out.print("And the path is :  ");
			for(int a : minCostPath.get(0).get(minColumn)) {
				System.out.print(a + "  ");
			}
		}
	}
	
	private static boolean findMinCostPath(int[][] matrix, int[][] minCost, 
			ArrayList<ArrayList<LinkedList<Integer>>> minCostPath, int rowSize, int columnSize) {
		
		if(matrix == null)
			return false;
		
		//for the last row, the min cost is the value of the element itself and the path consists of the node itself
		for(int col=0; col<columnSize; col++) {
			minCost[rowSize-1][col] = matrix[rowSize-1][col];
			minCostPath.get(rowSize-1).get(col).add(matrix[rowSize-1][col]); 
		}
		
		for(int row=rowSize-2; row >= 0; row--) {
			for(int col=0; col<columnSize; col++) {
				
				LinkedList<Integer> currentElementPath = minCostPath.get(row).get(col);
				currentElementPath.add(matrix[row][col]);
				
				if(col == 0) {
					//if the element is in first column, then we can go down or down right and keep track of the path 
					if(minCost[row+1][col] < minCost[row+1][col+1]) {
						//if down lesser than down right, take down path
						minCost[row][col] = matrix[row][col] + minCost[row+1][col];
						currentElementPath.addAll(minCostPath.get(row+1).get(col));
					} else {
						//go down right
						minCost[row][col] = matrix[row][col] + minCost[row+1][col+1];
						currentElementPath.addAll(minCostPath.get(row+1).get(col+1));
					}
					 
				} else if(col == columnSize-1) {
					//if the element is in the last column, then we can go down or down left and keep track of the path
					if(minCost[row+1][col] < minCost[row+1][col-1]) {
						//if down is lesser than down left, go down
						minCost[row][col] = matrix[row][col] + minCost[row+1][col];
						currentElementPath.addAll(minCostPath.get(row+1).get(col));
					} else {
						//go down left
						minCost[row][col] = matrix[row][col] + minCost[row+1][col-1];
						currentElementPath.addAll(minCostPath.get(row+1).get(col-1));
					}
				} else {
					//else, we can go either down, down left or down right and keep track of the path
					int down = minCost[row+1][col];
					int downLeft = minCost[row+1][col-1];
					int downRight = minCost[row+1][col+1];
					switch(findMinimumAmongstThreePaths(down, downRight, downLeft)) {
						case 0 : //go down and remember path 
							minCost[row][col] = matrix[row][col] + down;
							currentElementPath.addAll(minCostPath.get(row+1).get(col));
							break;
						case -1 : //go down left and remember path 
							minCost[row][col] = matrix[row][col] + downLeft;
							currentElementPath.addAll(minCostPath.get(row+1).get(col-1));
							break;
						case 1 : //go down rightand remember path 
							minCost[row][col] = matrix[row][col] + downRight;
							currentElementPath.addAll(minCostPath.get(row+1).get(col+1));
							break;
					}
					
				}
			}
		}
		
		return true;//successful
	}
	
	//returns 0 if should go down, -1 if should go down left or +1 if should go down right
	private static int findMinimumAmongstThreePaths(int down, int downRight, int downLeft) {
		if(down < downLeft
				&& down < downRight) {
			return 0;
		} else if(downLeft < down 
				&& downLeft < downRight) {
			return -1;
		} else {
			return 1;
		}
	}
}
