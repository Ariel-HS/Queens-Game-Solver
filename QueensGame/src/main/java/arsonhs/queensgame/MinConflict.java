/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arsonhs.queensgame;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author ariel
 */
public class MinConflict {
    private static final Random rand = new Random(); 
    
    public static Pair<Boolean,Integer[]> test(String piece, String[][] boardStr, Integer nRows, Integer nCols) {
        Integer[] pieceLocation = {1,3,0,2};
        
//        System.out.println(noConflict(boardStr, pieceLocation));
        
        return new Pair(false,pieceLocation);
    }
    
    public static Pair<Boolean,Integer[]> solve(String piece, String[][] boardStr, Integer nRows, Integer nCols) {
        // stores in which col is piece at row-i placed
        Integer[] pieceLocation = new Integer[nRows];
        Arrays.fill(pieceLocation, -1);
        
        Integer currRow = rand.nextInt(nRows);
        Integer cycle = 0;
        // while has conflict or not all placed
        while (cycle < 50000 && (notAllPlaced(pieceLocation) || !noConflict(piece, boardStr, pieceLocation))) {           
            Integer minConflictNum = nRows + 1;
            Integer minConflictCol = 0;

            // try all col in row
            for (int i = 0; i < nCols; i++) {
                Integer conflictNum = nRows+1;
                switch (piece) { // i, pieceLocation[i]
                    case "knight" -> conflictNum = numConflictKnight(boardStr, pieceLocation, currRow, i);
                    case "rook" -> conflictNum = numConflictRook(boardStr, pieceLocation, currRow, i);
                    case "bishop" -> conflictNum = numConflictBishop(boardStr, pieceLocation, nCols, currRow, i);
                    case "queen" -> conflictNum = numConflictQueen(boardStr, pieceLocation, currRow, i);
                }

                if (conflictNum < minConflictNum) {
                    minConflictCol = i;
                    minConflictNum = conflictNum;
                } else if (conflictNum.compareTo(minConflictNum) == 0 && i != pieceLocation[currRow]) {
                    minConflictCol = i;
                }
            }
            pieceLocation[currRow] = minConflictCol;
            cycle++;
            
            currRow = rand.nextInt(nRows);
        }
        
        if (!noConflict(piece, boardStr, pieceLocation) || !checkRegion(boardStr, pieceLocation)) {
            return new Pair(false, pieceLocation);
        } else {
            return new Pair(true, pieceLocation);
        }
    }
    
    // num of queens conflicting with coordinate (row,col)
    private static Integer numConflictQueen(String[][] boardStr, Integer[] pieceLocation, Integer row, Integer col) {
        Integer nRows = pieceLocation.length;
        Integer ctr = 0;
        for (int i = 0; i < nRows; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            // conflicts adjacently
            if ((i == row+1 || i == row-1) && pieceLocation[i] >= col-1 && pieceLocation[i] <= col+1) {
                ctr++;
                continue;
            }
            
            // conflicts by column
            Integer colLocation = pieceLocation[i];
            if (colLocation.compareTo(col) == 0) {
                ctr++;
                continue;
            }
            
            // conflicts by region
            if (boardStr[row][col].equals(boardStr[i][colLocation])) {
                ctr++;
            }
            
        }
        return ctr;
    }
    
     private static Integer numConflictRook(String[][] boardStr, Integer[] pieceLocation, Integer row, Integer col) {
        Integer nRows = pieceLocation.length;
        Integer ctr = 0;
        for (int i = 0; i < nRows; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            } 
            
            // conflicts by column
            Integer colLocation = pieceLocation[i];
            if (colLocation.compareTo(col) == 0) {
                ctr++;
                continue;
            }
            
            // conflicts by region
            if (boardStr[row][col].equals(boardStr[i][colLocation])) {
                ctr++;
            }
            
        }
        return ctr;
    }
     
    private static Integer numConflictBishop(String[][] boardStr, Integer[] pieceLocation, Integer nCols, Integer row, Integer col) {
        Integer nRows = pieceLocation.length;
        Integer ctr = 0;
        for (int i = 0; i < nRows; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            Integer colLocation = pieceLocation[i];
            // conflicts diagonally
            if (i+colLocation == row+col) {
                ctr++;
                continue;
            }
            
            if (i+nCols-colLocation-1 == row+nCols-col-1) {
                ctr++;
                continue;
            }
            
            // conflicts by region
            if (boardStr[row][col].equals(boardStr[i][colLocation])) {
                ctr++;
            }
            
        }
        return ctr;
    }
    
    private static Integer numConflictKnight(String[][] boardStr, Integer[] pieceLocation, Integer row, Integer col) {
        Integer nRows = pieceLocation.length;
        Integer ctr = 0;
        for (int i = 0; i < nRows; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            // conflict by knight movement
            Integer colLocation = pieceLocation[i];
            if ((i == row-2 && (colLocation == col-1 || colLocation == col+1)) ||
                    i == row-1 && (colLocation == col-2 || colLocation == col+2)) {
                ctr++;
                continue;
            }
            
            // conflicts by region
            if (boardStr[row][col].equals(boardStr[i][colLocation])) {
                ctr++;
            }
            
        }
        return ctr;
    }
    
    private static Boolean noConflict(String piece, String[][] boardStr, Integer[] pieceLocation) {
        for (int i = 0; i < pieceLocation.length; i++) {
            if (pieceLocation[i] == -1) {
                continue;
            }

            Integer nCols = boardStr[0].length;
            Boolean hasConflict = false;
            switch (piece) {
                case "knight" -> hasConflict = hasConflictKnight(boardStr, pieceLocation, i, pieceLocation[i]);
                case "rook" -> hasConflict = hasConflictRook(boardStr, pieceLocation, i, pieceLocation[i]);
                case "bishop" -> hasConflict = hasConflictBishop(boardStr, pieceLocation, nCols, i, pieceLocation[i]);
                case "queen" -> hasConflict = hasConflictQueen(boardStr, pieceLocation, i, pieceLocation[i]);
            }
            
            if (hasConflict) {
                return false;
            }
        }
        
        return true;
    }
    
    private static Boolean hasConflictQueen(String[][] boardStr, Integer[] pieceLocation, Integer row, Integer col) {
        return conflictColumn(pieceLocation, row, col) || conflictAdjacent(pieceLocation, row, col) ||
                conflictRegion(boardStr, pieceLocation, row, boardStr[row][col]);
    }
    
    private static Boolean hasConflictRook(String[][] boardStr, Integer[] pieceLocation, Integer row, Integer col) {
        return conflictColumn(pieceLocation, row, col) || conflictRegion(boardStr, pieceLocation, row, boardStr[row][col]);
    }
    
    private static Boolean hasConflictBishop(String[][] boardStr, Integer[] pieceLocation, Integer nCols, Integer row, Integer col) {
        return conflictLeftDiagonal(pieceLocation, row, col) || conflictRightDiagonal(pieceLocation, nCols, row, col)
                || conflictRegion(boardStr, pieceLocation, row, boardStr[row][col]);
    }
    
    private static Boolean hasConflictKnight(String[][] boardStr, Integer[] pieceLocation, Integer row, Integer col) {
        return conflictKnight(pieceLocation, row, col) || conflictRegion(boardStr, pieceLocation, row, boardStr[row][col]);
    }
    
    private static Boolean conflictLeftDiagonal(Integer[] pieceLocation, Integer row, Integer col) {
        for (int i = 0; i < pieceLocation.length; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            Integer colLocation = pieceLocation[i];
            if (i+colLocation == row+col) {
                return true;
            }
        }

        return false;
    } 
    
    private static Boolean conflictRightDiagonal(Integer[] pieceLocation, Integer nCols, Integer row, Integer col) {
        for (int i = 0; i < pieceLocation.length; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            Integer colLocation = pieceLocation[i];
            if (i+nCols-colLocation-1 == row+nCols-col-1) {
                return true;
            }
        }

        return false;
    } 
    
    private static Boolean conflictKnight(Integer[] pieceLocation, Integer row, Integer col) {
        for (int i = 0; i < pieceLocation.length; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            Integer colLocation = pieceLocation[i];
            if ((i == row-2 && (colLocation == col-1 || colLocation == col+1)) ||
                    i == row-1 && (colLocation == col-2 || colLocation == col+2)) {
                return true;
            }
        }

        return false;
    } 
    
    private static Boolean conflictColumn(Integer[] pieceLocation, Integer row, Integer col) {
        for (int i = 0; i < pieceLocation.length; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
//            System.out.println("TEST-2 " +  pieceLocation[i]);
            if (pieceLocation[i].compareTo(col) == 0) {
                return true;
            }
        }
        
        return false;
    } 
    
    private static Boolean conflictAdjacent(Integer[] pieceLocation, Integer row, Integer col) {
        Integer nRows = pieceLocation.length;
        for (int i = 0; i < nRows; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            if ((i == row+1 || i == row-1)  && pieceLocation[i] >= col-1 && pieceLocation[i] <= col+1) {
                return true;
            }
        }
        
        return false;
    } 
    
    private static Boolean conflictRegion(String[][] boardStr, Integer[] pieceLocation, Integer row, String symbol) {
        for (int i = 0; i < pieceLocation.length; i++) {
            if (i == row || pieceLocation[i] == -1) {
                continue;
            }
            
            if (boardStr[i][pieceLocation[i]].equals(symbol)) {
                return true;
            }
        }
        
        return false;
    }
    
    private static Boolean notAllPlaced(Integer[] pieceLocation) {
        for (Integer i: pieceLocation) {
            if (i == -1) {
                return true;
            }
        }
        
        return false;
    }
    
    private static Boolean checkRegion(String[][] boardStr, Integer[] pieceLocation) {
        HashMap<String, Boolean> regionMap = new HashMap<>();
        
        for (String[] row : boardStr) {
            for (String symbol : row) {
                if (regionMap.get(symbol) != null) {
                    regionMap.put(symbol, false);
                }
            }
        }
        
        for (int i = 0; i < pieceLocation.length; i++) {
            regionMap.put(boardStr[i][pieceLocation[i]], true);
        }
        
        for (Map.Entry<String, Boolean> entry : regionMap.entrySet()) {
            if (entry.getValue() == false) {
                return false;
            }
        }
        
        return true;
    }
}
