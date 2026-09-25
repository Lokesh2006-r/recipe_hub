package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmBorder

@Composable
fun InstructionStepRow(
    stepNumber: Int,
    instruction: String,
    isCompleted: Boolean,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleCompleted)
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Step Number Badge or Checkmark if completed
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = if (isCompleted) Color(0xFF2E7D32) else ForestGreen,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Step completed",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = "$stepNumber",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = instruction,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                lineHeight = 22.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
