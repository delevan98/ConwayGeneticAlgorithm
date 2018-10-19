//Created by: Mike Delevan

import java.io.*;
import java.util.*;

public class OptimizeConway
{
   public int[][] initialBoard;
   public int[][] board;
   public int[][] nextBoard;
   public final int LIVE = 1;
   public final int DEAD = 0;
   public int rows = 32;
   public int columns = 32;
   public int generation = 0;
   public int bestFitness;
   public int[][] bestInitial;
   public int[][] bestEnd;
   
   public OptimizeConway()
   {
      board = new int[rows][columns];
      nextBoard = new int[rows][columns];
      initialBoard = new int[rows][columns];
      bestInitial = new int[rows][columns];
      bestEnd = new int[rows][columns];    
   }
    
    //Gets the playing board from a 8x8 array from a file and places
    //it in the middle of the playing board
    public void getBoard()
    {
      Random random = new Random();

      for (int i = 12; i < 20 ; i++)
      {
         for (int j = 12; j < 20; j++) 
         {
            if (random.nextDouble() < 0.5) 
            {
               board[i][j] = 1;
            } 
            else 
            {
               board[i][j] = 0;
            }
         }
      }
       initialBoard = board;
    }
    
    //Generates the next generation of the playing board
    //using the rules of Conways and places them in the nextBoard array
    public void nextGeneration()
    {
      for(int i = 1; i < rows - 1; i++)
      {
         for(int j = 1; j < columns - 1; j++)
         {
            int neighbors = neighborCount(i,j);
            
            if((board[i][j] == LIVE) && (neighbors < 2))
            {
               nextBoard[i][j] = DEAD;
            }
            
            else if((board[i][j] == LIVE) && (neighbors > 3))
            {
               nextBoard[i][j] = DEAD;
            }
            
            else if((board[i][j] == DEAD) && (neighbors == 3))
            {
               nextBoard[i][j] = LIVE;
            }
            
            else
            {
               nextBoard[i][j] = board[i][j];
            }
          }
        }
        
        board = nextBoard;
        nextBoard = new int[rows][columns];
        generation++;
    }
    
    //Returns the amount of neighbors a certain cell has
    public int neighborCount(int row, int col)
    {
      int numNeighbors = 0;
      
      for(int i = -1; i <= 1; i++)
      {
         for(int j = -1; j <= 1; j++)
         {
            if(i != 0 || j != 0)
            {
               if(board[row+i][col+j] == LIVE)
               {
                  numNeighbors++;
               }
            }
          }
      }      
      return numNeighbors;
    }
    
    //Prints in nice formatting
    public void printNice()
    {       
       System.out.println("\nPrinting Initial Board");
       for(int i = 0; i < rows; i++)
       {
         for(int j = 0; j < columns; j++)
         {
            if(bestInitial[i][j] == LIVE)
            {
               System.out.print("X");
            }
            
            else
            {
               System.out.print("*");
            }
          }
          System.out.println();
        }
        
        System.out.println("\nPrinting End Board");
        for(int i = 0; i < rows; i++)
       {
         for(int j = 0; j < columns; j++)
         {
            if(bestEnd[i][j] == LIVE)
            {
               System.out.print("X");
            }
            
            else
            {
               System.out.print("*");
            }
          }
          System.out.println();
        }
    }
    
    public void printHex()
    {
      System.out.println("\nPrinting Compact Form:");
      for(int i = 12; i < 20; i++)
      {
         int row = 0;
         for(int j = 12; j < 20; j++)
         {
            row = (row<<1) | bestInitial[i][j];
         }      
         System.out.printf("%02X\n", row);
      }
    }
    
    //Returns the amount of live cells at the end of the 1000th iteration
    public int getFitness()
    {
      int EndLiveCells = 0;
      for(int i = 0; i <1000; i++)
      {
         nextGeneration();
      }
      
      for(int i = 0; i < rows; i++)
      {
        for(int j = 0; j < columns; j++)
        {
            if(board[i][j] == LIVE)
            {
               EndLiveCells++;
            }
        }
      }     
      return EndLiveCells;
    }
    
    public void optimize()
    {
      int count = 0;
      int fitness = 0;
      
      while(count < 100)
      {
         getBoard(); //set initial board
         
         fitness = getFitness();
         
         if(fitness > bestFitness)
         {
            bestFitness = fitness;
            bestInitial = new int[rows][columns]; //Clear both to avoid mixing
            bestInitial = initialBoard;           //data from the past bestInitial
            bestEnd = new int[rows][columns];     //and bestEnd boards
            bestEnd = board;
            
            System.out.println("\nBetter solution found on iteration: " + count);
            System.out.println("New better fitness is: " + bestFitness);
         }
         
         count++;
         initialBoard = new int[rows][columns];
         board = new int[rows][columns];
     }
     
     printNice();
     System.out.println("\nBest fitness is: " + bestFitness);
     printHex();
   }
       
    public static void main(String[] args)
    { 
            OptimizeConway game = new OptimizeConway();                
            game.optimize();                       
    }
}