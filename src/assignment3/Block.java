package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth; 
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(4);


 //remove after testing
// public static void main(String[] args) {
//
//  //region flatten test
//  System.out.println("flatten test");
//  Block bA = new Block(0, 3);
//  bA.updateSizeAndPosition(16, 0, 0);
//  bA.printBlock();
//  bA.printColoredBlock();
//
//  System.out.println("------------------");
//  System.out.println();
//  //endregion
//
//
//  //region Basic understanding test
//  Color blue = new GameColors().BLUE;
//  Color red = new GameColors().RED;
//  Color yellow = new GameColors().YELLOW;
//  Color green = new GameColors().GREEN;
//  Block[] empty = new Block[] {};
//  Block LR2blue = new Block(12, 12, 4, 2,2, blue, empty);
//  Block UL2red = new Block(8, 8, 4, 2,2, red, empty);
//  Block LL2yell = new Block(8, 12, 4, 2,2, yellow, empty);
//  Block UR2blue = new Block(12, 8, 4, 2,2, blue, empty);
//  Block[] lower2Array = new Block[] { UR2blue, UL2red, LL2yell, LR2blue};
//
//  Block LR1sub = new Block(8, 8, 8, 1,2, null, lower2Array);
//  Block LL1yell = new Block(0, 8, 8, 1,2, yellow, empty);
//  Block UR1green = new Block(8, 0, 8, 1,2, green, empty);
//  Block UL1red = new Block(0, 0, 8, 1,2, red, empty);
//  Block[] upperArray = new Block[] {UR1green, UL1red, LL1yell, LR1sub};
//  Block mainBlock = new Block(0, 0, 16, 0,2, null, upperArray);
//  //mainBlock.printBlock();
//
//  //endregion
//
//  //region Block(lvl, maxDepth) test
//  Block blockDepth2 = new Block(0,2);
//  blockDepth2.updateSizeAndPosition(16, 0, 0);
//  blockDepth2.printBlock();
//
//  //endregion
//
//  //region getSelectedBlock and reflect test
//  System.out.println("------------------");
//
//  Block blockDepth3 = new Block(0,3);
//  blockDepth3.updateSizeAndPosition(16, 0, 0);
//  blockDepth3.printBlock();
//  System.out.println();
//
//  //Block b1 = blockDepth3.getSelectedBlock(0, -1, 1);
//  Block b1 = blockDepth3.getSelectedBlock(3, 5, 2);
//  b1.printBlock();
//  System.out.println("reflect test");
//  b1.reflect(0);
//  b1.printBlock();
//  System.out.println("-------------");
//  //endregion
// }


 //create helper functions to maintain invariants!! - see document page 9

 /*
  * These two constructors are here for testing purposes. 
  */
 public Block() {}
 
 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }


 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {
  /*
   * ADD YOUR CODE HERE
   */
  int xCoord, yCoord = 0;

  if (lvl == maxDepth) { //base case
   //cannot subdivide - pick color
   makeSolidColorBlock(lvl, maxDepth);
  } else {
   //generate the random value
   double randomValue = gen.nextDouble(); //between 0 and 1
   boolean canSubdivide = randomValue < Math.exp(-0.25* lvl);
   if (canSubdivide) {
    Block UR = new Block(lvl+1, maxDepth);
    Block UL = new Block(lvl+1, maxDepth);
    Block LL = new Block(lvl+1, maxDepth);
    Block LR = new Block(lvl+1, maxDepth);
    Block[] blockArray = new Block[] {UR, UL, LL, LR};

    makeSubdivideBlock(lvl, maxDepth, blockArray);
   } else {
    makeSolidColorBlock(lvl, maxDepth);
   }
  }

 }

 private void makeSubdivideBlock(int lvl, int maxDepth, Block[] blockArray){
  this.level=lvl;
  this.maxDepth = maxDepth;
  this.color = null;
  this.children = blockArray;
 }

 private void makeSolidColorBlock(int lvl, int maxDepth){
  int colorIndex = gen.nextInt(4); //betwen 0 and 4 inclusive
  Block[] empty = new Block[] {};
  this.level=lvl;
  this.maxDepth = maxDepth;
  this.color = GameColors.BLOCK_COLORS[colorIndex];
  this.children = empty;
 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the 
  * blocks. 
  * 
  *  The size is the height and width of the block. (xCoord, yCoord) are the 
  *  coordinates of the top left corner of the block. 
  */
 public void updateSizeAndPosition(int size, int xCoord, int yCoord) {
  /*
   * ADD YOUR CODE HERE
   */

  if (size <=0){ throw new IllegalArgumentException("negative input size"); }
  if(level == 0 && size%(Math.pow(2, maxDepth)) != 0) { throw new IllegalArgumentException("input size must be divisible by 2 until max depth"); }

  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;
  if (children.length == 0 || level == maxDepth) {
   return;
  }


  Block UR = children[0];
  Block UL = children[1];
  Block LL = children[2];
  Block LR = children[3];
  UR.updateSizeAndPosition(size/2, xCoord+size/2, yCoord);
  UL.updateSizeAndPosition(size/2, xCoord, yCoord);
  LL.updateSizeAndPosition(size/2, xCoord, yCoord+size/2);
  LR.updateSizeAndPosition(size/2, xCoord+size/2, yCoord+size/2);
 }

 
 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  /*
   * ADD YOUR CODE HERE
   */
  BlockToDraw colorDrawing = new BlockToDraw(color, xCoord, yCoord, size, 0);
  BlockToDraw borderDrawing = new BlockToDraw(GameColors.FRAME_COLOR, xCoord, yCoord, size, 3);
  ArrayList<BlockToDraw> blockArray = new ArrayList<BlockToDraw>(); //has default initial capacity of 10

  if (children.length != 4){ //when not subdivided - base case
   blockArray.add(colorDrawing);
   blockArray.add(borderDrawing);
  } else { //recursive step
   for (int i =0; i< children.length; i++){
    ArrayList<BlockToDraw> subArray = children[i].getBlocksToDraw();
    if (!subArray.isEmpty()){
     blockArray.addAll(subArray);
    }
   }

  }
  return blockArray;
 }

 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }
 
 
 
 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 
  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
  /*
   * ADD YOUR CODE HERE
   */
  if (lvl < level || lvl > maxDepth){
   throw new IllegalArgumentException("cannot input level larger than max depth or less than current block's level");
  }
  Block ret = this;
  if (level == 0 &&  childIndexFromMouseCoords(x, y) == -1) { //checking if coords inside main block
   return null;
  } //continuative error check happens in recursive step!

  if (lvl == level || children.length == 0){ //base case (at level/the deepest level), return current block!
   return ret;
  } else { //need to go down the child list!
   int childIndex = childIndexFromMouseCoords(x, y);
   if (childIndex == -1) {return null;} //checking if position is out of bounds!
   ret = children[childIndex].getSelectedBlock(x, y, lvl);
  }
  return ret;
 }
//first of all I need to identify which quarter the cursor is in, can make a helper function for this

 private int childIndexFromMouseCoords(int x, int y){
  //return int indicates the index of the cursor relative to the children nodes
  //0 - UR, 1 - UL, 2 - LL, 3 - LR
  boolean xBigger = x > children[3].xCoord;
  boolean yBigger = y > children[3].yCoord;
  int ret=0;

  if (x < xCoord || x > xCoord+size || y < yCoord || y > yCoord+size ){
    return -1;
  }


  if (xBigger && yBigger){
   ret = 3;
  } else if (!xBigger && yBigger){
   ret = 2;
  } else if (xBigger && !yBigger){
   ret = 0;
  } else if (!xBigger && !yBigger){
   ret = 1;
  }

  return ret;
 }
 

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagated, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */
 public void reflect(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  if (! (direction == 0 || direction == 1) ) {
   throw new IllegalArgumentException("input must be either 1 or 0");
  }
  //x - axis == 0, y axis == 1
  boolean xAxisReflection = direction == 0; //i had the initial reasoning inverted. this fixes it
  if (children.length == 0) {
   return;
  }
//  Block UR = children[0];
//  Block UL = children[1];
//  Block LL = children[2];
//  Block LR = children[3];

  if (xAxisReflection){
   //switch UR with LR and UL with LL. propogate
   swapChildrenIndices(0, 3);
   swapChildrenIndices(1, 2);
  } else {
   //switch UR with UL and LR with LL. propogate
   swapChildrenIndices(0, 1);
   swapChildrenIndices(2, 3);
  }
  updateSizeAndPosition(size, xCoord, yCoord);

  children[0].reflect(direction);
  children[1].reflect(direction);
  children[2].reflect(direction);
  children[3].reflect(direction);
 }


private void swapChildrenIndices(int i, int j){
 //updating coords
 //swapping array locations
 Block temp = children[i];
 children[i] = children[j];
 children[j] = temp;
}

//alternative swap function, not in use currently
private void fieldwiseSwap(int i , int j){
  Color tempColor = children[i].color;
  Block[] blockArr = children[i].children;
  children[i].color = children[j].color;
  children[i].children = children[j].children;
  children[j].color = tempColor;
  children[j].children = blockArr;
}
 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  if (! (direction == 0 || direction == 1) ) {
   throw new IllegalArgumentException("input must be either 1 or 0");
  }
  //x - axis == 0, y axis == 1
  boolean counterClockwiseRotation = direction == 0; //i had the initial reasoning inverted. this fixes it
  if (children.length == 0) {
   return;
  }
  if (counterClockwiseRotation){
   //switching is slightly complicated. see notebook for explanatio/figure it out visually
   swapChildrenIndices(0, 1);
   swapChildrenIndices(0, 2);
   swapChildrenIndices(0, 3);
  } else { //clockwise
   swapChildrenIndices(0, 1);
   swapChildrenIndices(1, 2);
   swapChildrenIndices(2, 3);
  }
  updateSizeAndPosition(size, xCoord, yCoord);

  children[0].rotate(direction);
  children[1].rotate(direction);
  children[2].rotate(direction);
  children[3].rotate(direction);
 }
 


 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
  /*
   * ADD YOUR CODE HERE
   */
  boolean canSmash = level > 0 && level < maxDepth; //if at maxdepth, do nothing anyway

  if (canSmash) {
   Block UR = new Block(level+1, maxDepth);
   Block UL = new Block(level+1, maxDepth);
   Block LL = new Block(level+1, maxDepth);
   Block LR = new Block(level+1, maxDepth);
   Block[] blockArray = new Block[] {UR, UL, LL, LR};
   children = blockArray;
   updateSizeAndPosition(size, xCoord, yCoord);
  }

  return canSmash;
 }
 
 
 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  * 
  * Return and array arr where, arr[i] represents the unit cells in row i, 
  * arr[i][j] is the color of unit cell in row i and column j.
  * 
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
  /*
   * ADD YOUR CODE HERE
   */
  int numUnitCellsPerSideLength = (int) Math.pow(2, maxDepth);
  int unitCellSize = size/numUnitCellsPerSideLength;
  Color[][] colorArr = new Color[numUnitCellsPerSideLength][numUnitCellsPerSideLength];
  //use getSelectedBlock to find color of block (at lowest depth). Need to move x and y coord - scaled by unitCellSize

  for (int i =0; i< numUnitCellsPerSideLength; i++){
   for (int j = 0; j< numUnitCellsPerSideLength; j++){
    Block block = getSelectedBlock(j * unitCellSize+1 + xCoord, i * unitCellSize+1 + yCoord, maxDepth);
    if (block == null) {System.out.println(i+", "+j); }
    colorArr[i][j] = block.color;
   }
  }
  return colorArr;
 }



 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
