package com.example.chesslibgame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chesslibgame.R
import com.example.chesslibgame.logic.ChessGame
import com.example.chesslibgame.ui.theme.ChessLibGameTheme
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square

@Composable
fun ChessBoardScreen(chessGame: ChessGame = ChessGame()) {
    var selectedSquare by remember { mutableStateOf<Square?>(null) }
    var boardState by remember { mutableStateOf(chessGame.getBoardFEN()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D1B46))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado de jugadores y temporizador
        PlayerHeader()

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
                if (selectedSquare == null) {
                    selectedSquare = square
                } else {
                    val from = selectedSquare!!
                    val to = square
                    if (chessGame.makeMove(from, to)) {
                        boardState = chessGame.getBoardFEN()
                    }
                    selectedSquare = null
                }
            })
        }
        // Botones "Offer Draw" y "Resign"
        Spacer(modifier = Modifier.height(70.dp))
        GameOptions()
    }
}

@Composable
fun PlayerHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Jugador 1
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.player_1), // Imagen del jugador 1
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Text("Player 1", color = Color.White, fontSize = 16.sp)
            Text("Level 2", color = Color.Gray, fontSize = 14.sp)
        }

        // Temporizador
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("00:45", color = Color.White, fontSize = 24.sp)
        }

        // Jugador 2
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.player_2), // Imagen del jugador 2
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Text("Player 2", color = Color.White, fontSize = 16.sp)
            Text("Level 2", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun GameOptions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { /* Acción de Offer Draw */ }) {
            Text("Offer Draw")
        }
        Button(onClick = { /* Acción de Resign */ }) {
            Text("Resign")
        }
    }
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
        ChessBoardScreen(chessGame = ChessGame().apply { resetBoard() })
    }
}
