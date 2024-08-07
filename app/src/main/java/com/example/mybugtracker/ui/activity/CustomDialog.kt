package com.example.mybugtracker.ui.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mybugtracker.R

@Composable
fun CustomDialog(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    dismissDialog:() -> Unit
){
    Dialog(onDismissRequest = {
        dismissDialog()
    }, properties = DialogProperties(
        usePlatformDefaultWidth = false
    )
        ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.55f)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(15.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
                ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription ="",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .width(30.dp)
                        .clickable { onCameraClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.gallery),
                    contentDescription ="",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .width(30.dp)
                        .clickable { onGalleryClick() }
                )
            }
        }

    }
}