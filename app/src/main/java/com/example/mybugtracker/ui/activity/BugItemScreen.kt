package com.example.mybugtracker.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mybugtracker.data.model.BugItemResponse

@Composable
fun BugListItem(bugItem : BugItemResponse){
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(8.dp)
            .height(130.dp),
        onClick = {

        }
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = bugItem.imageUrl,
                contentDescription = "",
                modifier = Modifier.weight(0.8f),
                contentScale = ContentScale.FillWidth
                )
            Spacer(modifier = Modifier
                .height(6.dp)
                .fillMaxWidth())
            Text(
                text = bugItem.title,
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .weight(0.2f),
                color = Color.Blue
            )
        }
    }
}
