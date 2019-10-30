package com.webcheckers.model;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.ui.BoardView;
import com.webcheckers.ui.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class ReplayGameTest {
    private PlayerServices p1;
    private PlayerServices p2;
    private GameCenter gameCenter;
    private Board board;
    private Map<String, Object> vm;
    private Map<String, Object> modeOptions;
    private Gson gson;
    private ReplayGame CuT;

    /**
     * Setup new object for each test.
     */
    @BeforeEach
    public void setup() {
        gameCenter = new GameCenter();
        p1 = new PlayerServices("Spongebob", gameCenter);
        p2 = new PlayerServices("Patrick", gameCenter);
        gameCenter.createGame(p1.Id(), p2.Id());
        board = p1.getCurrentGame().getBoard();

        this.gson = new Gson();

        modeOptions = new HashMap<>();
        vm = new HashMap<>();
        this.modeOptions.put("isGameOver", false);

        CuT = new ReplayGame(p1, p2, board);

        assertNotNull(CuT);
    }

    @Test
    void getAttributes() {
        Map<String, Object> vm2 = new HashMap<>();
        Map<String, Object> vm3 = new HashMap<>();
        Map<String, Object> mO2 = new HashMap<>(modeOptions);
        Map<String, Object> mO3 = new HashMap<>(modeOptions);
        Map resultCuT;
        //before getAttr
        assertEquals(0, CuT.getCurrConfigIndex());

        //1st config
        vm.put("PlayerServices", p1);
        vm.put("Player1", p1);
        vm.put("Player2", p2);
        vm.put("viewMode", "REPLAY");
        modeOptions.put("hasNext", false);
        modeOptions.put("hasPrevious", false);
        vm.put("activeColor", board.getActiveColor());
        BoardView bView1 = new BoardView(board);
        vm.put("board", bView1);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));

        resultCuT = CuT.getAttributes(p1);
        assertEquals(((PlayerServices) vm.get("PlayerServices")).Id(),
                ((PlayerServices) resultCuT.get("PlayerServices")).Id());
        assertEquals(((PlayerServices) vm.get("Player1")).Id(),
                ((PlayerServices) resultCuT.get("Player1")).Id());
        assertEquals(((PlayerServices) vm.get("Player2")).Id(),
                ((PlayerServices) resultCuT.get("Player2")).Id());
        assertEquals(((String) vm.get("viewMode")), ((String) resultCuT.get("viewMode")));
        assertEquals(((Piece.Color) vm.get("activeColor")), ((Piece.Color) resultCuT.get("activeColor")));
        assertEquals(((BoardView) vm.get("board")).getBoard().toString(),
                ((BoardView) resultCuT.get("board")).getBoard().toString());
        assertEquals(((boolean) modeOptions.get("isGameOver")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("isGameOver")));
        assertEquals(((boolean) modeOptions.get("hasNext")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("hasNext")));
        assertEquals(((boolean) modeOptions.get("hasPrevious")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("hasPrevious")));
        assertEquals(0, CuT.getCurrConfigIndex());

        //2nd config
        vm2.put("PlayerServices", p2);
        vm2.put("Player1", p1);
        vm2.put("Player2", p2);
        vm2.put("viewMode", "REPLAY");

        Board b1 = new Board();
        Board b2 = new Board();
        b1.addPieceTypeToBoard(3, 6, Piece.Color.WHITE, Piece.Type.KING);
        b2.addPieceTypeToBoard(3, 0, Piece.Color.RED, Piece.Type.KING);
        CuT.addConfiguration(b1);
        CuT.addConfiguration(b2);
        CuT.setButton("next");

        mO2.put("hasNext", true);
        mO2.put("hasPrevious", true);
        vm2.put("activeColor", b1.getActiveColor());
        BoardView bView2 = new BoardView(b1);
        bView2.flip();
        vm2.put("board", bView2);
        vm2.put("modeOptionsAsJSON", gson.toJson(mO2));

        resultCuT = CuT.getAttributes(p2);
        assertEquals(((PlayerServices) vm2.get("PlayerServices")).Id(),
                ((PlayerServices) resultCuT.get("PlayerServices")).Id());
        assertEquals(((PlayerServices) vm2.get("Player1")).Id(),
                ((PlayerServices) resultCuT.get("Player1")).Id());
        assertEquals(((PlayerServices) vm2.get("Player2")).Id(),
                ((PlayerServices) resultCuT.get("Player2")).Id());
        assertEquals(((String) vm2.get("viewMode")), ((String) resultCuT.get("viewMode")));
        assertEquals(((Piece.Color) vm2.get("activeColor")), ((Piece.Color) resultCuT.get("activeColor")));
        assertEquals(((BoardView) vm2.get("board")).getBoard().toString(),
                ((BoardView) resultCuT.get("board")).getBoard().toString());
        assertEquals(((boolean) mO2.get("isGameOver")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("isGameOver")));
        assertEquals(((boolean) mO2.get("hasNext")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("hasNext")));
        assertEquals(((boolean) mO2.get("hasPrevious")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("hasPrevious")));
        assertEquals(1, CuT.getCurrConfigIndex());

        //last config
        vm3.put("PlayerServices", p1);
        vm3.put("Player1", p1);
        vm3.put("Player2", p2);
        vm3.put("viewMode", "REPLAY");

        Board b3 = new Board();
        Board b4 = new Board();
        b3.addPieceTypeToBoard(3, 2, Piece.Color.WHITE, Piece.Type.KING);
        b4.addPieceTypeToBoard(4, 1, Piece.Color.RED, Piece.Type.KING);
        CuT.addConfiguration(b3);
        CuT.addConfiguration(b4);
        CuT.setCurrConfigIndex(1);
        CuT.setButton("previous");

        mO3.put("hasNext", true);
        mO3.put("hasPrevious", false);
        vm3.put("activeColor", board.getActiveColor());
        BoardView bView3 = new BoardView(board);
        vm3.put("board", bView3);
        vm3.put("modeOptionsAsJSON", gson.toJson(mO3));

        resultCuT = CuT.getAttributes(p1);
        assertEquals(((PlayerServices) vm3.get("PlayerServices")).Id(),
                ((PlayerServices) resultCuT.get("PlayerServices")).Id());
        assertEquals(((PlayerServices) vm3.get("Player1")).Id(),
                ((PlayerServices) resultCuT.get("Player1")).Id());
        assertEquals(((PlayerServices) vm3.get("Player2")).Id(),
                ((PlayerServices) resultCuT.get("Player2")).Id());
        assertEquals(((String) vm3.get("viewMode")), ((String) resultCuT.get("viewMode")));
        assertEquals(((Piece.Color) vm3.get("activeColor")), ((Piece.Color) resultCuT.get("activeColor")));
        assertEquals(((BoardView) vm3.get("board")).getBoard().toString(),
                ((BoardView) resultCuT.get("board")).getBoard().toString());
        assertEquals(((boolean) mO3.get("isGameOver")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("isGameOver")));
        assertEquals(((boolean) mO3.get("hasNext")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("hasNext")));
        assertEquals(((boolean) mO3.get("hasPrevious")),
                ((boolean) gson.fromJson((String) resultCuT.get("modeOptionsAsJSON"), Map.class).get("hasPrevious")));
        assertEquals(0, CuT.getCurrConfigIndex());
    }

    @Test
    void hasNextConfigurationsTest() {
        assertFalse(CuT.hasNextConfigurations());

        Board board = new Board();
        board.addPieceTypeToBoard(3, 6, Piece.Color.WHITE, Piece.Type.KING);
        CuT.addConfiguration(board);
        assertTrue(CuT.hasNextConfigurations());
    }

    @Test
    void hasPreviousConfigurationsTest() {
        assertFalse(CuT.hasPreviousConfigurations());

        Board board = new Board();
        board.addPieceTypeToBoard(3, 6, Piece.Color.WHITE, Piece.Type.KING);
        CuT.addConfiguration(board);
        CuT.setCurrConfigIndex(1);
        assertTrue(CuT.hasPreviousConfigurations());
    }

    @Test
    void getPlayerVsPlayerTest() {
        String expected = p1.Id() + " vs. " + p2.Id();

        assertNotNull(CuT.getPlayerVsPlayer());
        assertEquals(expected, CuT.getPlayerVsPlayer());
    }

    @Test
    void tryNextReplayMoveTest() {
        assertTrue(CuT.tryNextReplayMove());
        assertEquals("next", CuT.getButton());
    }

    @Test
    void addConfigurationTest() {
        Board board = new Board();
        board.addPieceTypeToBoard(3, 6, Piece.Color.WHITE, Piece.Type.KING);

        CuT.addConfiguration(board);
        assertTrue(CuT.hasNextConfigurations());
    }

    @Test
    void tryPreviousReplayMoveTest() {
        assertTrue(CuT.tryPreviousReplayMove());
        assertEquals("previous", CuT.getButton());
    }

    @Test
    void setCurrConfigIndexTest() {
        assertEquals(0, CuT.getCurrConfigIndex());
        CuT.setCurrConfigIndex(10);
        assertEquals(10, CuT.getCurrConfigIndex());
        CuT.setCurrConfigIndex(-100);
        assertEquals(-100, CuT.getCurrConfigIndex());
    }

    @Test
    void getCurrConfigIndexTest() {
        assertEquals(0, CuT.getCurrConfigIndex());
        CuT.setCurrConfigIndex(10);
        assertEquals(10, CuT.getCurrConfigIndex());
        CuT.setCurrConfigIndex(-100);
        assertEquals(-100, CuT.getCurrConfigIndex());
    }

    @Test
    void getButtonTest() {
        assertNotNull(CuT.getButton());
        assertEquals("none", CuT.getButton());
        CuT.setButton("next");
        assertEquals("next", CuT.getButton());
        CuT.setButton("previous");
        assertEquals("previous", CuT.getButton());
    }

    @Test
    void setButtonTest() {
        assertEquals("none", CuT.getButton());
        assertTrue(CuT.setButton("next"));
        assertEquals("next", CuT.getButton());
        assertFalse(CuT.setButton("RLRLUDUDABStart"));
        assertEquals("next", CuT.getButton());
        assertTrue(CuT.setButton("previous"));
        assertEquals("previous", CuT.getButton());
        assertTrue(CuT.setButton("none"));
        assertEquals("none", CuT.getButton());
    }
}