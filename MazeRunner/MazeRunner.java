
/*
 * Created by: Atul Mohan
 * My solution to the MazeRunner problem. Please refer ProblemStatement.txt for complete details on the problem statement 
 * 
 * Usage:  java MazeRunner <Task Number> <stdin>
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MazeRunner {
	private class Element {
		int row;
		int col;

		Element(int r, int c) {
			this.row = r;
			this.col = c;
		}

		protected boolean isEqual(Element e) {
			if (this.row == e.row && this.col == e.col)
				return true;
			else
				return false;
		}
	}

	/*
	 * Accepts a two-dimensional maze matrix and returns the validity of the maze
	 */
	protected boolean isValid(char[][] inputGrid) {
		int sCount = 0, dCount = 0, colCount = 0;
		
		if (inputGrid.length <= 0){
			return false;
		}
		else{
			colCount = inputGrid[0].length;
		}
		
		for (int i = 0; i < inputGrid.length; i++) {
			
			if (inputGrid[i].length <= 0 || inputGrid[i].length != colCount) {
				return false;
			}
			for (int j = 0; j < inputGrid[i].length; j++) {

				if (inputGrid[i][j] != 'S' && inputGrid[i][j] != 'D' && inputGrid[i][j] != '#'
						&& inputGrid[i][j] != '.' && inputGrid[i][j] <'1' && inputGrid[i][j]>'9' ){
					return false;
				}
				if (inputGrid[i][j] == 'S'){
					sCount++;
				}
				if (inputGrid[i][j] == 'D'){
					dCount++;
				}
			}
		}
		
		if (sCount == 0 || dCount == 0 || sCount > 1 || dCount > 1){
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("NO");
			return;
		}
		int taskNumber = Integer.parseInt(args[0]);
		MazeRunner mz = new MazeRunner();
		char mazeChar;
		int readChar;
		List<char[]> inputList = new ArrayList<char[]>(50);
		StringBuffer sbuf = new StringBuffer();
		BufferedReader bs = new BufferedReader(new InputStreamReader(System.in));

		// Read in the maze one character at a time
		while ((readChar = bs.read()) != -1) {
			
			mazeChar = (char) readChar;
			if (mazeChar == '\n'||mazeChar=='\r') {
				String mazeStr = sbuf.toString();
				inputList.add(mazeStr.toCharArray());
				sbuf.setLength(0);
				if(mazeChar=='\r'){
					bs.read();
				}

			} 
			else {
				sbuf.append(mazeChar);
			}
		}

		char grid[][] = new char[inputList.size()][];
		inputList.toArray(grid);
		
		if (!(sbuf.toString().isEmpty())) {
			System.out.println("NO12");
			return;
		}
			if (mz.isValid(grid)) {
				if (taskNumber == 1) {
					System.out.println("YES");
					return;
				}
			}
			else {
				System.out.println("NO1");
				return;
			}
			
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					System.out.print(grid[i][j] + " ");
				}
				System.out.println("\n");
			}
		
		mz.getPath(grid, taskNumber);

	}

	/*
	 * Driver function to check if valid path exists and identify optimal path
	 */
	protected void getPath(char[][] mazeArray, int task) {

		int sx = 0, sy = 0, ex = 0, ey = 0;
		int gridRows = mazeArray.length;
		int gridCols = mazeArray[0].length;
		int[][] levelMatrix = new int[gridRows][gridCols];
		int disx[] = { 0, 0, -1, 1 };  // For navigation along rows
		int disy[] = { 1, -1, 0, 0 };  // For navigation along columns
		HashMap<Character, Element[]> teleportMap = new HashMap<Character, Element[]>(10); // For tracking teleports
		char directions[] = { 'L', 'R', 'D', 'U' };
		StringBuffer pathBuffer = new StringBuffer();
		
		/*
		 * Assign distance as zero in case of a path, else store as -1
		 */
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridCols; j++) {
				if (mazeArray[i][j] == '.') {
					levelMatrix[i][j] = 0;
				} 
				else {
					if (mazeArray[i][j] == 'S') {
						sx = i;
						sy = j;
					} 
					else if (mazeArray[i][j] == 'D') {
						ex = i;
						ey = j;
					} 
					else if (mazeArray[i][j] >= '1' && mazeArray[i][j] <= '9') {
						
						if (teleportMap.containsKey(mazeArray[i][j])) {
							Element[] teleportElement = teleportMap.get(mazeArray[i][j]);
							Element t = new Element(i, j);
							teleportElement[1] = t;
							teleportMap.put(mazeArray[i][j], teleportElement);
						} 
						else {
							Element[] teleportElement = new Element[2];
							Element t = new Element(i, j);
							teleportElement[0] = t;
							teleportMap.put(mazeArray[i][j], teleportElement);
						}
						levelMatrix[i][j] = -1;
					} else {
						levelMatrix[i][j] = -1;
					}
				}
			}
		}
		
		
		Element startCell = new Element(sx, sy);
		Element endCell = new Element(ex, ey);
		Queue<Element> pathQueue = new LinkedList<Element>();
		boolean isPathExists=false;
		
		pathQueue.add(startCell);
		levelMatrix[startCell.row][startCell.col] = 1;
		
		while (pathQueue.peek() != null) {
			Element pathElement = pathQueue.poll();
			for (int k = 0; k < 4; k++) {

				if (pathElement.row + disx[k] >= 0 && pathElement.row + disx[k] < gridRows
						&& pathElement.col + disy[k] >= 0 && pathElement.col + disy[k] < gridCols) {
					int xk = pathElement.row + disx[k];
					int yk = pathElement.col + disy[k];

					if (mazeArray[xk][yk] == 'D') {
						isPathExists=true;
						break;
					}
					if (mazeArray[xk][yk] >= '1' && mazeArray[xk][yk] <= '9' && levelMatrix[xk][yk] == -1) {
						Element[] result = teleportMap.get(mazeArray[xk][yk]);
						//If a teleport element is encountered, take the corresponding pair from the map and add to queue
						if (result[0].row == xk && result[0].col == yk) {
							pathQueue.add(result[1]);
							levelMatrix[xk][yk] = levelMatrix[pathElement.row][pathElement.col] + 1;
							levelMatrix[result[1].row][result[1].col] = levelMatrix[xk][yk];

						} else {
							pathQueue.add(result[0]);
							levelMatrix[xk][yk] = levelMatrix[pathElement.row][pathElement.col] + 1;
							levelMatrix[result[0].row][result[0].col] = levelMatrix[xk][yk];
						}
					}
					if (levelMatrix[xk][yk] == 0) {
						Element compElement = new Element(xk, yk);
						pathQueue.add(compElement);
						levelMatrix[xk][yk] = levelMatrix[pathElement.row][pathElement.col] + 1;
					}

				}
			}
		}
		for (int i = 0; i < levelMatrix.length; i++) {
			for (int j = 0; j < levelMatrix[i].length; j++) {
				System.out.print(levelMatrix[i][j] + " ");
			}
			System.out.println("\n");
		}
			if(!isPathExists){
				System.out.println("NO5");
				return;
			}
			if(task==2){
				System.out.println("YES");
				return;
			}
		
		int currentLevel = 0, rx = endCell.row, ry = endCell.col,direct=0;
		
		//From the destination cell, identify the cell with smallest distance
		
		for (int k = 0; k < 4; k++) {
			if (endCell.row + disx[k] >= 0 && endCell.row + disx[k] < gridRows && endCell.col + disy[k] >= 0
					&& endCell.col + disy[k] < gridCols) {
				int xk = endCell.row + disx[k];
				int yk = endCell.col + disy[k];

				if (levelMatrix[xk][yk] > 0) {
					if (currentLevel <= 0 || levelMatrix[xk][yk] < currentLevel) {
						currentLevel = levelMatrix[xk][yk];
						rx = xk;
						ry = yk;
						direct=k;
					}

				}
			}
		}
		pathBuffer.append(directions[direct]);

		
		//Backtrack till starting cell
		while (currentLevel != 1) {
			for (int k = 0; k < 4; k++) {
				if (rx + disx[k] >= 0 && rx + disx[k] < gridRows && ry + disy[k] >= 0 && ry + disy[k] < gridCols) {
					if (mazeArray[rx][ry] >= '1' && mazeArray[rx][ry] <= '9') {
						Element[] result = teleportMap.get(mazeArray[rx][ry]);
						Element link = new Element(rx, ry);
						if (result[0].isEqual(link)) {
							rx = result[1].row;
							ry = result[1].col;
							currentLevel = levelMatrix[rx][ry];

						} else {
							rx = result[0].row;
							ry = result[0].col;
							currentLevel = levelMatrix[rx][ry];

						}
					}
					int xk = rx + disx[k];
					int yk = ry + disy[k];
					if (levelMatrix[xk][yk] == currentLevel - 1) {
						pathBuffer.append(directions[k]);
						currentLevel--;
						rx = xk;
						ry = yk;
						break;
					}
				}
			}
		}

		System.out.println(pathBuffer.reverse().toString());

	}
}
