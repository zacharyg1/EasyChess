package com.github.zacharygriggs.chess;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.helper.MovementHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

class MovementHelperTest {

    @Test
    public void testRookMovements() {
        // Valid moves.
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("a1"), new ChessCoordinate("a8")));
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("a2"), new ChessCoordinate("a3")));
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("b6"), new ChessCoordinate("d6")));
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("e6"), new ChessCoordinate("f6")));
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("a8"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("a3"), new ChessCoordinate("a2")));
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("d6"), new ChessCoordinate("b6")));
        Assert.assertTrue(MovementHelper.validRookMove(new ChessCoordinate("f6"), new ChessCoordinate("e6")));
        // Invalid moves.
        Assert.assertFalse(MovementHelper.validRookMove(new ChessCoordinate("a1"), new ChessCoordinate("b2")));
        Assert.assertFalse(MovementHelper.validRookMove(new ChessCoordinate("a1"), new ChessCoordinate("b2")));
        Assert.assertFalse(MovementHelper.validRookMove(new ChessCoordinate("b2"), new ChessCoordinate("b2")));
        Assert.assertFalse(MovementHelper.validRookMove(new ChessCoordinate("b2"), new ChessCoordinate("a1")));
        Assert.assertFalse(MovementHelper.validRookMove(new ChessCoordinate("a1"), new ChessCoordinate("b3")));
    }

    @Test
    public void testBishopMovements() {
        // Valid moves.
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("a1"), new ChessCoordinate("b2")));
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("a1"), new ChessCoordinate("c3")));
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("a1"), new ChessCoordinate("d4")));
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("b2"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("c3"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("d4"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("a2"), new ChessCoordinate("b3")));
        Assert.assertTrue(MovementHelper.validBishopMove(new ChessCoordinate("b3"), new ChessCoordinate("a2")));
        // Invalid moves.
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("b2"), new ChessCoordinate("b2")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("a1"), new ChessCoordinate("a8")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("a2"), new ChessCoordinate("a3")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("b6"), new ChessCoordinate("d6")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("e6"), new ChessCoordinate("f6")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("a8"), new ChessCoordinate("a1")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("a3"), new ChessCoordinate("a2")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("d6"), new ChessCoordinate("b6")));
        Assert.assertFalse(MovementHelper.validBishopMove(new ChessCoordinate("f6"), new ChessCoordinate("e6")));
    }

    @Test
    public void testKnightMovements() {
        // Valid moves.
        Assert.assertTrue(MovementHelper.validKnightMove(new ChessCoordinate("a1"), new ChessCoordinate("b3")));
        Assert.assertTrue(MovementHelper.validKnightMove(new ChessCoordinate("a1"), new ChessCoordinate("c2")));
        Assert.assertTrue(MovementHelper.validKnightMove(new ChessCoordinate("b3"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validKnightMove(new ChessCoordinate("c2"), new ChessCoordinate("a1")));
        // Invalid moves.
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("b2"), new ChessCoordinate("b2")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("a1"), new ChessCoordinate("a8")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("a2"), new ChessCoordinate("a3")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("b6"), new ChessCoordinate("d6")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("e6"), new ChessCoordinate("f6")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("a8"), new ChessCoordinate("a1")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("a3"), new ChessCoordinate("a2")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("d6"), new ChessCoordinate("b6")));
        Assert.assertFalse(MovementHelper.validKnightMove(new ChessCoordinate("f6"), new ChessCoordinate("e6")));
    }

    @Test
    public void testQueenMovements() {
        // Valid moves.
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a1"), new ChessCoordinate("a8")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a2"), new ChessCoordinate("a3")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("b6"), new ChessCoordinate("d6")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("e6"), new ChessCoordinate("f6")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a8"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a3"), new ChessCoordinate("a2")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("d6"), new ChessCoordinate("b6")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("f6"), new ChessCoordinate("e6")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a1"), new ChessCoordinate("b2")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a1"), new ChessCoordinate("c3")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a1"), new ChessCoordinate("d4")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("b2"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("c3"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("d4"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("a2"), new ChessCoordinate("b3")));
        Assert.assertTrue(MovementHelper.validQueenMove(new ChessCoordinate("b3"), new ChessCoordinate("a2")));
        // Invalid moves.
        Assert.assertFalse(MovementHelper.validQueenMove(new ChessCoordinate("b2"), new ChessCoordinate("b2")));
        Assert.assertFalse(MovementHelper.validQueenMove(new ChessCoordinate("a1"), new ChessCoordinate("b3")));
        Assert.assertFalse(MovementHelper.validQueenMove(new ChessCoordinate("a1"), new ChessCoordinate("c2")));
        Assert.assertFalse(MovementHelper.validQueenMove(new ChessCoordinate("b3"), new ChessCoordinate("a1")));
        Assert.assertFalse(MovementHelper.validQueenMove(new ChessCoordinate("c2"), new ChessCoordinate("a1")));
    }

    @Test
    public void testKingMovement() {
        // Valid moves.
        Assert.assertTrue(MovementHelper.validKingMove(new ChessCoordinate("a1"), new ChessCoordinate("a2")));
        Assert.assertTrue(MovementHelper.validKingMove(new ChessCoordinate("a1"), new ChessCoordinate("b2")));
        Assert.assertTrue(MovementHelper.validKingMove(new ChessCoordinate("a1"), new ChessCoordinate("b1")));
        Assert.assertTrue(MovementHelper.validKingMove(new ChessCoordinate("a2"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validKingMove(new ChessCoordinate("b2"), new ChessCoordinate("a1")));
        Assert.assertTrue(MovementHelper.validKingMove(new ChessCoordinate("b1"), new ChessCoordinate("a1")));
        // Invalid moves.
        Assert.assertFalse(MovementHelper.validKingMove(new ChessCoordinate("b2"), new ChessCoordinate("b2")));
        Assert.assertFalse(MovementHelper.validKingMove(new ChessCoordinate("a1"), new ChessCoordinate("a3")));
        Assert.assertFalse(MovementHelper.validKingMove(new ChessCoordinate("a1"), new ChessCoordinate("c3")));
        Assert.assertFalse(MovementHelper.validKingMove(new ChessCoordinate("a1"), new ChessCoordinate("b3")));
        Assert.assertFalse(MovementHelper.validKingMove(new ChessCoordinate("a3"), new ChessCoordinate("a1")));
        Assert.assertFalse(MovementHelper.validKingMove(new ChessCoordinate("c3"), new ChessCoordinate("a1")));
        Assert.assertFalse(MovementHelper.validKingMove(new ChessCoordinate("c1"), new ChessCoordinate("a1")));
    }

    @Test
    public void testBetween() {
        // Diagonal between
        List<ChessCoordinate> coords = MovementHelper.spacesBetween(new ChessCoordinate("a1"), new ChessCoordinate("c3"));
        Assert.assertEquals(coords.size(), 1);
        Assert.assertEquals(coords.get(0), new ChessCoordinate("b2"));

        coords = MovementHelper.spacesBetween(new ChessCoordinate("a1"), new ChessCoordinate("d4"));
        Assert.assertEquals(coords.size(), 2);
        Assert.assertEquals(coords.get(0), new ChessCoordinate("b2"));
        Assert.assertEquals(coords.get(1), new ChessCoordinate("c3"));

        // Vertical between
        coords = MovementHelper.spacesBetween(new ChessCoordinate("a1"), new ChessCoordinate("a5"));
        Assert.assertEquals(coords.size(), 3);
        Assert.assertEquals(coords.get(0), new ChessCoordinate("a2"));
        Assert.assertEquals(coords.get(1), new ChessCoordinate("a3"));
        Assert.assertEquals(coords.get(2), new ChessCoordinate("a4"));

        // Horizontal between
        coords = MovementHelper.spacesBetween(new ChessCoordinate("a1"), new ChessCoordinate("d1"));
        Assert.assertEquals(coords.size(), 2);
        Assert.assertEquals(coords.get(0), new ChessCoordinate("b1"));
        Assert.assertEquals(coords.get(1), new ChessCoordinate("c1"));
    }
}