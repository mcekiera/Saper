package JUnit;


import Game.Board;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class BoardTest {
    Board board;


    @Before
    public void before() throws Exception {
        board = new Board();
        board.setBoard();
    }

    @Test
    public void testAddMines() throws Exception {
        int count = 0;
        for(int i = 0; i < board.getBoardSize();i++){
            if(board.getCell(i).getValue() == -1) count++;
        }
        assertTrue(count == 10);
    }

    @Test
    public void testGetMinesLoc() throws Exception {
        assertEquals(10,board.getMinesLoc(10).size());
        assertEquals(15,board.getMinesLoc(15).size());
        assertEquals(0,board.getMinesLoc(0).size());
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

    /**
     *
     * Method: getCell(int id)
     *
     */
    @Test
    public void testGetCell() throws Exception {

    }

    /**
     *
     * Method: fail()
     *
     */
    @Test
    public void testFail() throws Exception {

    }


} 
