package com.hydbest.baseandroid.activity.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hydbest.baseandroid.R

class ComposeFirstActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Text("Hello world!")
            test()
        }
    }

    @Preview
    @Composable
    fun test() {
        content()
    }

    @Composable
    fun content() {
        MaterialTheme {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(
                        R.drawable.a
                    ),
                    contentDescription = null,
                    modifier = Modifier.height(300.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    "cai shuzha is a good good good good good good good good enginner! ok" +
                    " are the best!", style = MaterialTheme.typography.h2, maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text("bbbb", style = MaterialTheme.typography.body2)
                Text("cccc", style = MaterialTheme.typography.body2)
            }
        }
    }
}