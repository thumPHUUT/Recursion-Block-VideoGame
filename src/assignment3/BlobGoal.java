package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		Color[][] colorArr = board.flatten();
		int len = colorArr.length;
		boolean[][] boolArr = new boolean[len][len];
		int maxBlobSize = 0;


		for (int i = 0; i< len; i++){
			for(int j = 0; j<len; j++){
				if(!boolArr[i][j] && colorArr[i][j] == targetGoal){
					int temp = undiscoveredBlobSize(i, j, colorArr, boolArr);
					maxBlobSize = Math.max(maxBlobSize, temp);
				}
			}
		}

		return maxBlobSize;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		/*
		 * ADD YOUR CODE HERE
		 */
		if (i < 0 || i >= visited.length || j < 0 || j >= visited[0].length) {return 0;}

		Color targetColor = this.targetGoal;
		if(unitCells[i][j] != targetColor || visited[i][j]) {return 0;}

		int counter = 1;
		visited[i][j] = true;

		counter += undiscoveredBlobSize(i-1, j, unitCells, visited);
		counter += undiscoveredBlobSize(i+1, j, unitCells, visited);
		counter += undiscoveredBlobSize(i, j-1, unitCells, visited);
		counter += undiscoveredBlobSize(i, j+1, unitCells, visited);
		return counter;
	}

}
