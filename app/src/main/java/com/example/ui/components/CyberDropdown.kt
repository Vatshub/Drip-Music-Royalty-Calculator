package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberError
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberKey
import com.example.ui.theme.CyberKeyText
import com.example.ui.theme.CyberMuted
import com.example.ui.theme.CyberPurple

@Composable
fun CyberDropdown(
    placeholder: String,
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    testTag: String,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        val borderColor = when {
            isError -> CyberError
            expanded -> CyberGreen
            else -> CyberPurple.copy(alpha = 0.6f)
        }
        val borderWidth = if (isError) 1.5.dp else 1.dp

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(999.dp))
                .background(
                    if (isError) CyberError.copy(alpha = 0.12f) else CyberKey
                )
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(999.dp)
                )
                .drawBehind {
                    if (isError) {
                        drawCircle(
                            color = CyberError.copy(alpha = 0.22f),
                            radius = size.maxDimension,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    } else if (expanded) {
                        drawCircle(
                            color = CyberGreen.copy(alpha = 0.12f),
                            radius = size.maxDimension,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }
                }
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .testTag(testTag),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val displayText = when {
                selectedValue.isNotEmpty() -> selectedValue
                isError -> "$placeholder *"
                else -> placeholder
            }

            Text(
                text = displayText,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = if (isError) FontWeight.SemiBold else FontWeight.Normal,
                    color = when {
                        isError -> CyberError
                        selectedValue.isEmpty() -> CyberMuted
                        else -> CyberKeyText
                    },
                    shadow = if (isError) {
                        Shadow(color = CyberError.copy(alpha = 0.6f), blurRadius = 6f)
                    } else if (selectedValue.isNotEmpty()) {
                        Shadow(color = CyberPurple.copy(alpha = 0.4f), blurRadius = 4f)
                    } else null
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "$placeholder dropdown",
                tint = if (isError) CyberError else CyberGreen,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(CyberKey)
                .border(1.dp, CyberPurple.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = if (option == selectedValue) CyberGreen else CyberKeyText,
                                fontWeight = if (option == selectedValue) FontWeight.SemiBold else FontWeight.Normal
                            )
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = CyberKeyText
                    )
                )
            }
        }
    }
}
