import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
@Composable
fun WeeklyVolumeChart(data: List<Float>) {

    val safeData =
        if (data.size == 7) data
        else (data + List(7 - data.size) { 0f }).take(7)

    val maxValue = (safeData.maxOrNull() ?: 1f).coerceAtLeast(1f)

    Column {

        Text("Weekly Volume", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            val w = size.width
            val h = size.height
            val step = w / 6f

            drawLine(Color.Gray, Offset(0f, h), Offset(w, h), 3f)
            drawLine(Color.Gray, Offset(0f, 0f), Offset(0f, h), 3f)

            val path = Path()

            safeData.forEachIndexed { i, v ->

                val x = i * step
                val y = h - (v / maxValue) * h

                if (i == 0) path.moveTo(x, y)
                else path.lineTo(x, y)

                drawCircle(Color.Blue, 8f, Offset(x, y))
            }

            drawPath(
                path = path,
                color = Color.Blue,
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
                .forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
    }
}