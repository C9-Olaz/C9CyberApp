package com.c9cyber.app.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextPrimary
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.game_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun GameCard(
    game: com.c9cyber.app.domain.model.Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundSecondary),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(game.imageRes ?: Res.drawable.game_placeholder),
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            Text(
                text = game.name,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            )
        }
    }
}
