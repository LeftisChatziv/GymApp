import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WeeklyVolumeChart(data: List<Float>) {

    val maxValue = (data.maxOrNull() ?: 1f)

    Column {

        Text("Weekly Volume", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            val widthStep = size.width / 6f
            val height = size.height

            // ================= AXES =================
            drawLine(
                color = Color.Gray,
                start = Offset(0f, height),
                end = Offset(size.width, height),
                strokeWidth = 4f
            )

            drawLine(
                color = Color.Gray,
                start = Offset(0f, 0f),
                end = Offset(0f, height),
                strokeWidth = 4f
            )

            // ================= LINE GRAPH =================
            for (i in 0 until data.lastIndex) {

                val x1 = i * widthStep
                val y1 = height - (data[i] / maxValue) * height

                val x2 = (i + 1) * widthStep
                val y2 = height - (data[i + 1] / maxValue) * height

                drawLine(
                    color = Color.Blue,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 5f
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // ================= DAYS LABELS =================
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach {
                Text(it)
            }
        }
    }
}