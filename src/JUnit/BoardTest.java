package JUnit;


import Game.Board;
import Game.Main;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;


public class BoardTest {
    Board board;
    Main main;


    @Before
    public void before() throws Exception {
        main = new Main();
        main.buildGUI();
        board = main.getBoard();
    }

    @Test
    public void testAddMines() throws Exception {
        int count = 0;
        for(int i = 0; i < 64;i++){
            if(board.getCell(i).isMine()) count++;
        }
        assertTrue(count == 10);
    }

    @Test
    public void testScanAreaForEmptyTrue() throws Exception {
        if((!board.getCell(0).isMine()) && (board.getCell(0).getValue() == 0)){
            board.scanForEmptyCells(board.getIDsFromArea(0));
            assertTrue(board.getCell(1).isChecked());
            assertTrue(board.getCell(8).isChecked());
            assertTrue(board.getCell(9).isChecked());
        }
    }
    @Test public void testScanAreaForEmptyFalse() throws Exception {
    if((!board.getCell(0).isMine()) && (board.getCell(0).getValue() > 0)){
        assertFalse(board.getCell(1).isChecked() && (!board.getCell(1).isMine()));
        assertFalse(board.getCell(8).isChecked() && (!board.getCell(1).isMine()));
        assertFalse(board.getCell(9).isChecked() && (!board.getCell(1).isMine()));
        }
        assertTrue(true);
    }

    @Test
    public void testGetCell() throws Exception {
        board.getCell(1).setValue(-3);
        assertTrue(board.getCell(1).getValue() == -3);
        board.getCell(1).setValue(7);
        assertTrue(board.getCell(1).getValue() == 7);
        board.getCell(1).setValue(67);
        assertTrue(board.getCell(1).getValue() == 67);
    }

    @Test
    public void testGetID() throws Exception {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(8);
        list.add(9);
        List<Integer> test = board.getIDsFromArea(0);
        for(int a : list){
            assertTrue(test.contains(a));
        }
    }

    @Test
    public void testFinish(){
        board.finish(true);
        assertTrue(board.isSuccess());
        assertFalse(!board.isSuccess());
        board.finish(false);
        assertTrue(!board.isSuccess());
        assertFalse(board.isSuccess());
    }

    @Test
    public void testCounter(){
        board.incrementCounter();
        assertEquals(11, board.getCounterValue());
        board.decrementCounter();
        assertEquals(10, board.getCounterValue());
    }
} 
