package com.example.chesslibgame.ui.screens

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chesslibgame.R
import com.example.chesslibgame.logic.ChessGame
import com.example.chesslibgame.ui.theme.ChessLibGameTheme
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move

@Composable
fun ChessBoardScreen(navController: NavController, chessGame: ChessGame = ChessGame()) {
    val context = LocalContext.current
    var selectedSquare by remember { mutableStateOf<Square?>(null) }
    var boardState by remember { mutableStateOf(chessGame.getBoardFEN()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D1B46))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(70.dp))

        // Tablero de ajedrez con borde redondeado
        Box(
            modifier = Modifier
                .size(360.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF333333)),
            contentAlignment = Alignment.Center
        ) {
            ChessBoard(chessGame, selectedSquare, onSquareClick = { square ->
                val pieceAtSelectedSquare = chessGame.getPieceAt(square)

                // Verificar si la casilla seleccionada está vacía
                if (selectedSquare == null) {
                    // Solo selecciona la casilla si hay una pieza
                    if (pieceAtSelectedSquare != Piece.NONE) {
                        selectedSquare = square
                    }
                } else {
                    val from = selectedSquare!!
                    val to = square

                    // Verificar si hay una pieza en la casilla de origen
                    val piece = chessGame.getPieceAt(from)
                    if (piece != Piece.NONE) {
                        // Lógica adicional para restringir la captura del peón
                        if (piece == Piece.WHITE_PAWN || piece == Piece.BLACK_PAWN) {
                            if (isPawnMoveValid(from, to, chessGame)) {
                                // Realizar el movimiento en el tablero
                                val move = Move(from, to)
                                if (chessGame.board.isMoveLegal(move, true)) {
                                    chessGame.board.doMove(move)
                                    boardState = chessGame.board.fen
                                    playMoveSound(context)
                                }
                            }
                        } else {
                            // Crear un movimiento en ChessLib y verificar si es válido para las demás piezas
                            val move = Move(from, to)
                            if (chessGame.board.isMoveLegal(move, true)) {
                                chessGame.board.doMove(move)
                                boardState = chessGame.board.fen
                                playMoveSound(context)
                            }
                        }
                    }

                    // Desmarcar la casilla seleccionada después del movimiento
                    selectedSquare = null
                }
            })
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}

// Función para verificar si el movimiento del peón es válido (especialmente para capturas diagonales)
fun isPawnMoveValid(from: Square, to: Square, chessGame: ChessGame): Boolean {
    val pieceAtDestination = chessGame.getPieceAt(to)

    // Verificar si es una captura en diagonal
    val isDiagonalCapture = (from.file.ordinal != to.file.ordinal) && (pieceAtDestination != Piece.NONE)

    // Verificar que sea un movimiento hacia adelante para los peones
    val isForwardMove = if (chessGame.getPieceAt(from) == Piece.WHITE_PAWN) {
        to.rank.ordinal == from.rank.ordinal + 1
    } else {
        to.rank.ordinal == from.rank.ordinal - 1
    }

    // Permitimos el movimiento si es una captura diagonal o un movimiento normal hacia adelante
    return (isDiagonalCapture && isForwardMove) || (from.file.ordinal == to.file.ordinal && pieceAtDestination == Piece.NONE && isForwardMove)
}

// Función para reproducir el sonido
fun playMoveSound(context: Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.move_self)
    mediaPlayer?.start()
}

@Composable
fun ChessBoard(
    chessGame: ChessGame,
    selectedSquare: Square?,
    onSquareClick: (Square) -> Unit
) {
    Column {
        for (row in 0 until 8) {
            Row {
                for (col in 0 until 8) {
                    val square = Square.squareAt(row * 8 + col)
                    ChessSquare(
                        square = square,
                        piece = chessGame.getPieceAt(square),
                        isSelected = square == selectedSquare,
                        onClick = { onSquareClick(square) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChessSquare(
    square: Square,
    piece: Piece?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color.Blue else if ((square.rank.ordinal + square.file.ordinal) % 2 == 0) Color(0xFFF0D9B5) else Color(0xFFB58863)

    Box(
        modifier = Modifier
            .size(45.dp)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Mostrar la imagen de la pieza si hay una
        val pieceImage = getPieceImage(piece)
        if (pieceImage != null) {
            Image(
                painter = painterResource(id = pieceImage),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun getPieceImage(piece: Piece?): Int? {
    return when (piece) {
        Piece.WHITE_PAWN -> R.drawable.pawn_piece_w
        Piece.WHITE_ROOK -> R.drawable.rook_piece_w
        Piece.WHITE_KNIGHT -> R.drawable.knight_piece_w
        Piece.WHITE_BISHOP -> R.drawable.bishop_piece_w
        Piece.WHITE_QUEEN -> R.drawable.queen_piece_w
        Piece.WHITE_KING -> R.drawable.king_piece_w
        Piece.BLACK_PAWN -> R.drawable.pawn_piece
        Piece.BLACK_ROOK -> R.drawable.rook_piece
        Piece.BLACK_KNIGHT -> R.drawable.knight_piece
        Piece.BLACK_BISHOP -> R.drawable.bishop_piece
        Piece.BLACK_QUEEN -> R.drawable.queen_piece
        Piece.BLACK_KING -> R.drawable.king_piece
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChessBoardScreen() {
    ChessLibGameTheme {
        ChessBoardScreen(navController = NavController(LocalContext.current), chessGame = ChessGame().apply { resetBoard() })
    }
}
