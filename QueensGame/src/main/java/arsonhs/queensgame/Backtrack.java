/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arsonhs.queensgame;

import java.util.Arrays;
import java.util.HashMap;
    
/**
 *
 * @author ariel
 */
public class Backtrack {    
    private static HashMap<String, Boolean> regionMap;
    
    private static Integer getMaxNumPieces(Integer[] pieceLocation) {
        for (int i = 0; i < pieceLocation.length; i++) {
            if (pieceLocation[i] == -1) {
                return i;
            }
        }
        
        return pieceLocation.length;
    }
    
    private static Boolean canPutKnight(String[][] boardStr, Integer[] pieceLocation, Integer row, Integer col) {
        // assert piece is safe based on knight movement
        if ((row > 1 && (pieceLocation[row-2] == col-1 || pieceLocation[row-2] == col+1)) ||
            (row > 0 && (pieceLocation[row-1] == col-2 || pieceLocation[row-1] == col+2))
            ) 
            return false;
        
        String region = boardStr[row][col];
        return regionMap.get(region) == null || regionMap.get(region) == false;
    }
    
    private static Boolean canPutBishop(String[][] boardStr, Boolean[] rightDiagonalState, Boolean[] leftDiagonalState, 
                                        Integer nCols, Integer row, Integer col) {
        // assert no piece is located at the same diagonal
        if (rightDiagonalState[row+nCols-col-1] == true || 
                leftDiagonalState[row+col] == true) 
            return false;
        
        String region = boardStr[row][col];
        return regionMap.get(region) == null || regionMap.get(region) == false;
    }
    
    private static Boolean canPutRook(String[][] boardStr, Boolean[] colState, Integer row, Integer col) {
        // assert no piece is located at the same col
        if (colState[col] == true) 
            return false;
        
        String region = boardStr[row][col];
        return regionMap.get(region) == null || regionMap.get(region) == false;
    }
    
    private static Boolean canPutQueen(String[][] boardStr, Integer[] pieceLocation, Boolean[] colState, Integer row, Integer col) {
        // assert no piece is located at the same col or adjacent to piece or in the same region 
        if (colState[col] == true || 
            (row > 0 && (pieceLocation[row-1] >= col-1 && pieceLocation[row-1] <= col+1))) {
            return false;
        }
        
        String region = boardStr[row][col];
        return regionMap.get(region) == null || regionMap.get(region) == false;
    }
    
    private static Pair<Boolean,Integer[]> backtrack(String piece, String[][] boardStr, Integer[] pieceLocation, 
                                                        Boolean[] rightDiagonalState, Boolean[] leftDiagonalState, 
                                                        Boolean[] colState, Integer row) {
        Integer nRows = pieceLocation.length;
        Integer nCols = colState.length;
        
        // if all location checks out, return pieceLocation
        if (row >= nRows) {
            return new Pair(true,pieceLocation);
        }
        
        // stores pieceLocation with the max number of pieces placed in case of no solution
        Integer[] pieceLocationMax = new Integer[nRows];
        Integer maxNumPieces = 0;
        Arrays.fill(pieceLocationMax, -1);
        
        // try all cols in row
        for (int i=0; i<nCols; i++) {
            // if can put piece
            Boolean isSafe = false;
            switch (piece) {
                case "knight" -> isSafe = canPutKnight(boardStr, pieceLocation, row, i);
                case "rook" -> isSafe = canPutRook(boardStr, colState, row, i);
                case "bishop" -> isSafe = canPutBishop(boardStr, rightDiagonalState, leftDiagonalState, nCols, row, i);
                case "queen" -> isSafe = canPutQueen(boardStr, pieceLocation, colState, row, i);
            }
            
            if (isSafe) {
                pieceLocation[row] = i;
                colState[i] = true;
                String region = boardStr[row][i];
                regionMap.put(region, true);
                
                if (piece.equals("bishop")) {
                    rightDiagonalState[row+nCols-i-1] = true;
                    leftDiagonalState[row+i] = true;
                }
                
                // try for next row
                Pair<Boolean,Integer[]> result = backtrack(piece, boardStr, pieceLocation, rightDiagonalState, 
                                                            leftDiagonalState, colState, row+1);
                if (result.getKey() == true) {
                    return new Pair(true, pieceLocation);
                }
                
                // before reset, set pieceLocationMax
                Integer[] tempLoc = result.getValue().clone();
                Integer temp = getMaxNumPieces(tempLoc);
                if (temp > maxNumPieces) {
                    pieceLocationMax = tempLoc;
                    maxNumPieces = temp;
                }
                
                // if cant put, reset state
                pieceLocation[row] = -1;
                colState[i] = false;
                regionMap.replace(region, false);
                
                if (piece.equals("bishop")) {
                    rightDiagonalState[row+nCols-i-1] = false;
                    leftDiagonalState[row+i] = false;
                }
            }
        }

        if (maxNumPieces == 0) {
            return new Pair(false, pieceLocation);
        } else {
            return new Pair(false, pieceLocationMax);
        }
    }
            
    public static Pair<Boolean,Integer[]> solve(String piece, String[][] boardStr, Integer nRows, Integer nCols) {
        // stores in which col is piece at row-i placed
        Integer[] pieceLocation = new Integer[nRows];
        // stores whether a col already has a piece
        Boolean[] colState = new Boolean[nCols];
        // stores whether a right diagonal already has a piece
        // with the formula i = row + nCols - col - 1
        Boolean[] rightDiagonalState = new Boolean[nRows+nCols-1];
        // stores whether a left diagonal already has a piece
        // with the formula i = row + col
        Boolean[] leftDiagonalState = new Boolean[nRows+nCols-1];
        // initialize boardState
        Arrays.fill(pieceLocation, -1);
        Arrays.fill(colState, false);
        Arrays.fill(rightDiagonalState, false);
        Arrays.fill(leftDiagonalState, false);
        regionMap = new HashMap<>();

        return backtrack(piece, boardStr, pieceLocation, rightDiagonalState, leftDiagonalState, colState, 0);
    }
}
