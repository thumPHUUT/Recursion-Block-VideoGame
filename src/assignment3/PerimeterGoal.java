package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		Color[][] boardColors = board.flatten();
		Color targetColor = this.targetGoal;
		int counter = 0;

		for(int i =0; i< boardColors.length;i++){
			if(boardColors[i][0] == targetColor) { counter ++;}
			if(boardColors[i][boardColors.length - 1] == targetColor) { counter ++;}
			if(boardColors[0][i] == targetColor) { counter ++;}
			if(boardColors[boardColors.length -1][i] == targetColor) { counter ++;}
		}

		return counter;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
