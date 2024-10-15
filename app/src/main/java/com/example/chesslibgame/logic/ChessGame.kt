package com.example.chesslibgame.logic

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.github.bhlangonijr.chesslib.move.MoveException
import com.github.bhlangonijr.chesslib.Piece  // Importa desde el paquete correcto

class ChessGame {

    val board: Board = Board()

    // Resetea el tablero al estado inicial
    fun resetBoard() {
        // Usa directamente la notación FEN para la posición inicial, si STARTING_FEN no está disponible
        val startingFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        board.loadFromFen(startingFen)
    }

    // Realiza un movimiento en el tablero
    fun makeMove(from: Square, to: Square): Boolean {
        return try {
            val move = Move(from, to)
            board.doMove(move)
            true
        } catch (e: MoveException) {
            false
        }
    }

    // Devuelve la pieza en una casilla específica
    fun getPieceAt(square: Square): Piece? {
        return board.getPiece(square)
    }

    // Verifica si el juego está en estado de jaque mate
    fun isCheckmate(): Boolean {
        return board.isMated
    }

    // Verifica si el juego está en estado de tablas
    fun isDraw(): Boolean {
        return board.isDraw
    }

    // Obtiene el tablero actual en formato FEN (para debugging o guardado de estado)
    fun getBoardFEN(): String {
        return board.fen
    }
}
