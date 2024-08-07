package com.example.mybugtracker.ui.activity

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybugtracker.data.model.BugItemResponse

@Composable
fun ShowButItemScree(bugItemList : List<BugItemResponse>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(10.dp),
        content = {
            items(bugItemList){
                BugListItem(bugItem = it)
            }
        })
}