package com.satwik.sbslaunchpad.core.designsystem.components
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.IconPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPlaceholder
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins


@Composable
fun LaunchpadSearchBar(
    modifier:Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder:String,
    fontFamily:FontFamily = poppins,
    leadingButtonOnClick:() -> Unit,
    trailButtonOnClick:() ->Unit,
    @DrawableRes
    leadingButtonIcon:Int = R.drawable.ic_arrow,
    @DrawableRes
    trailButtonIcon:Int = R.drawable.ic_cross,
    autoFocus:Boolean = false
){
    val focusRequester = remember{FocusRequester()}
    if(autoFocus){
        LaunchedEffect(Unit){
            focusRequester.requestFocus()
        }
    }
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),

        value = query,

        onValueChange = onQueryChange,

        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            cursorColor = BrandPrimary,
            focusedIndicatorColor = BrandPrimary,
            unfocusedIndicatorColor = Color.White,
        ),
        singleLine = true,

        textStyle = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, color = Color.White),

        trailingIcon = {
            IconButton(onClick = trailButtonOnClick,
            ) {
                Icon(
                    painter = painterResource(id = trailButtonIcon),
                    contentDescription = null,
                    tint = IconPrimary,
                )
            }
        },
        leadingIcon = {
            IconButton(onClick = leadingButtonOnClick,
            ) {
                Icon(
                    painter = painterResource(id = leadingButtonIcon),
                    contentDescription = null,
                    tint = IconPrimary,
                )
            }
        },

        placeholder = {
            Text(
                text = placeholder,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                fontWeight = FontWeight.Normal,
                color = TextPlaceholder
            )
        },
    )
}




@Preview
@Composable
fun LaunchpadSearchBarPreview(){
    var text by remember { mutableStateOf("") }
    LaunchpadSearchBar(
        query = text,
        onQueryChange = {text = it},
        placeholder = "Search for workspaces",
        trailButtonOnClick = { /*TODO*/ },
        leadingButtonOnClick = { /*TODO*/ })
}