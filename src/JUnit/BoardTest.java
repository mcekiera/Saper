package JUnit;


import Game.Board;
import Game.Main;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;


public class BoardTest {
    Board board;
    Main main;


    @Before
    public void before() throws Exception {
        //board = new Board();
       // board.setBoard();
    }

    @Test
    public void testAddMines() throws Exception {
        int count = 0;
        for(int i = 0; i < 100;i++){
            if(board.getCell(i).getValue() == -1) count++;
        }
        assertTrue(count == 10);
    }

    @Test
    public void testGetMinesLoc() throws Exception {
        assertEquals(10,board.generateMinesLocation(10).size());
        assertEquals(15,board.generateMinesLocation(15).size());
        assertEquals(0,board.generateMinesLocation(0).size());
    }

    @Test
    public void testScanAreaForEmpty() throws Exception {
    }

    @Test
    public void testGetID() throws Exception {
        assertTrue(board.getID() == 100);
        assertTrue(board.getID() == 101);
        assertTrue(board.getID() == 102);
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
    public void testFail() throws Exception {
        board.fail();
        for(int i = 0; i < 100; i++){
            assertFalse(board.getCell(i).getButton().isEnabled());
        }
    }


} 
