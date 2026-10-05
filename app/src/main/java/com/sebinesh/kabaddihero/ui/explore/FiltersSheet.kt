package com.sebinesh.kabaddihero.ui.explore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersSheet(
    currentFilters: TournamentFilters,
    onApply: (TournamentFilters) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var district by remember { mutableStateOf(currentFilters.selectedDistrict) }
    var category by remember { mutableStateOf(currentFilters.selectedCategory) }
    var gender by remember { mutableStateOf(currentFilters.selectedGender) }
    var verifiedOnly by remember { mutableStateOf(currentFilters.verifiedOnly) }
    var regOpenOnly by remember { mutableStateOf(currentFilters.registrationOpenOnly) }

    val districts = listOf("All", "Madurai", "Tirunelveli", "Thanjavur", "Thoothukudi", "Chennai", "Salem", "Coimbatore")
    val categories = listOf("All", "Open", "Junior", "Sub-Junior")
    val genders = listOf("All", "Men", "Women", "Mixed")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Tournaments",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onReset) {
                    Text(text = "Reset", color = PurplePrimaryBright)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // District
            Text(text = "District", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            FilterChipGroup(items = districts, selected = district, onSelected = { district = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Category
            Text(text = "Category", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            FilterChipGroup(items = categories, selected = category, onSelected = { category = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Gender
            Text(text = "Gender", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            FilterChipGroup(items = genders, selected = gender, onSelected = { gender = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Switches
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Verified Only", fontSize = 14.sp)
                Switch(checked = verifiedOnly, onCheckedChange = { verifiedOnly = it })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Registration Open Only", fontSize = 14.sp)
                Switch(checked = regOpenOnly, onCheckedChange = { regOpenOnly = it })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onApply(
                        currentFilters.copy(
                            selectedDistrict = district,
                            selectedCategory = category,
                            selectedGender = gender,
                            verifiedOnly = verifiedOnly,
                            registrationOpenOnly = regOpenOnly
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
            ) {
                Text(text = "Apply Filters", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipGroup(
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.take(4).forEach { item ->
            FilterChip(
                selected = selected == item,
                onClick = { onSelected(item) },
                label = { Text(item, fontSize = 12.sp) }
            )
        }
    }
}
