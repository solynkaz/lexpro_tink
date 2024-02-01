package com.example.lexpro_mobile.ui.screens.vertical

import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.RkkData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lexpro_mobile.jsonDTO.RkkFilter
import com.example.lexpro_mobile.viewmodel.RkkFilterEvents
import com.example.lexpro_mobile.viewmodel.RkkFilterState
import com.example.lexpro_mobile.viewmodel.RkkFilterViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.launch


@Composable
fun MainContent(
    rkkFilterViewModel: RkkFilterViewModel = hiltViewModel(),
    onNavToNext: (card: RkkData) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    val state = rkkFilterViewModel.state
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    /*if (!state.isLoading && !state.isLoaded) {
        rkkFilterViewModel.onEvent(
            RkkFilterEvents.RkkFilterEvent(
                RkkFilter(
                    codeStatus = listOf("IN_WORK"),
                    page = "0",
                    size = "25"
                )
            )
        )
    }
     */


    // region Size
    var sizeExpanded by remember { mutableStateOf(false) }
    val sizeOptions = listOf("25", "50", "75")
    var sizeSelectedText by remember { mutableStateOf("25") }
    var sizeTextFieldSize by remember { mutableStateOf(Size.Zero) }
    val sizeIcon = if (sizeExpanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown
    //endregion

    //region Page
    var pageSelectedText by remember { mutableStateOf("1") }
    //endregion

    // region CodeStatus
    var codeStatusExpanded by remember { mutableStateOf(false) }
    val codeStatusOptions = listOf("В работе", "К совещанию", "Принят")
    val codeStatusValue = listOf("IN_WORK", "FOR_MEETING", "ACCEPTED")
    val codeStatus = mutableListOf<String>()
    var codeStatusTextFieldSize by remember { mutableStateOf(Size.Zero) }
    val codeStatusIcon = if (codeStatusExpanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown
    val codeStatusCheckedList =
        remember { listOf(mutableStateOf(true), mutableStateOf(false), mutableStateOf(false)) }
    //endregion

    // region isReadyToPublish
    var isReadyToPublish by remember { mutableStateOf(false) }
    //endregion

    // region isDeleted
    var isDeleted by remember { mutableStateOf(false) }
    //endregion

    // region isReadyForSession
    var isReadyForSession by remember { mutableStateOf(false) }
    //endregion

    // region isPublished
    var isPublished by remember { mutableStateOf(false) }

    //endregion

    // region
    //endregion

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Регистрационные карты",
                        color = Color.White
                    )
                },
                backgroundColor = Color(0xFF1F6466),
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                    Icon(
                        Icons.Filled.Sort,
                        contentDescription = "Сортировка",
                        tint = Color.White,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        },
        content = {
            if (state.isLoaded && !state.isLoading) {
                if (state.rkkFilterList!!.total_count != 0) {
                    LazyColumn(modifier = Modifier.padding(bottom = 1.dp)) {
                        item {
                            var counter = 0
                            for (card in state.rkkFilterList!!.data) {
                                rkkCard(
                                    state = state,
                                    card = card,
                                    npaName = card.npaName,
                                    npaRkkNumber = card.rkkNumber,
                                    npaStatus = card.status!!.name,
                                    npaRegistrationDate = card.registrationDate,
                                    npaType = card.npaType?.name,
                                    visible = state.isLoaded,
                                    onNavToNext = onNavToNext,
                                )
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column() {
                            Text(
                                text = "С данным фильтром данных не найдено.",
                                fontSize = 20.sp,
                                color = Color.Red,
                                fontWeight = FontWeight(400)
                            )
                            Text(
                                text = "Попробуйте изменить настройки",
                                fontSize = 20.sp,
                                color = Color.Red,
                                fontWeight = FontWeight(400)
                            )
                        }
                    }
                }
            } else {
                LazyColumn {
                    item {
                        for (i in 0..10) {
                            rkkCard(
                                state = state,
                                card = null,
                                npaName = "",
                                npaRkkNumber = "",
                                npaStatus = "",
                                npaRegistrationDate = "",
                                npaType = "",
                                visible = state.isLoaded,
                                onNavToNext = onNavToNext,
                            )
                        }
                    }
                }
            }
        },
        drawerContent = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1F6466),
                shape = CutCornerShape(0.dp)
            ) {
                Row() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Фильтрация списка РКК",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight(650)
                        )
                    }
                }
            }
            LazyColumn() {
                item {
                    //Page
                    Column() {
                        OutlinedTextField(
                            value = pageSelectedText,
                            singleLine = true,
                            onValueChange = {
                                if (it.isDigitsOnly() && it != "0") pageSelectedText = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                                .padding(top = 3.dp),
                            label = { Text("Страница") },
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                        )
                    }

                    //Size
                    Column() {
                        OutlinedTextField(
                            readOnly = true,
                            value = sizeSelectedText,
                            onValueChange = { sizeSelectedText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                                .padding(top = 3.dp)
                                .onGloballyPositioned { coordinates ->
                                    //This value is used to assign to the DropDown the same width
                                    sizeTextFieldSize = coordinates.size.toSize()
                                }
                                .clickable {
                                    sizeExpanded = !sizeExpanded
                                },
                            label = { Text("Размер страницы") },
                            trailingIcon = {
                                Icon(sizeIcon, "contentDescription",
                                    Modifier.clickable { sizeExpanded = !sizeExpanded })
                            }
                        )
                        DropdownMenu(
                            expanded = sizeExpanded,
                            onDismissRequest = { sizeExpanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { sizeTextFieldSize.width.toDp() })
                                .clickable { sizeExpanded = !sizeExpanded }
                        ) {
                            sizeOptions.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    focusManager.clearFocus()
                                    sizeSelectedText = label
                                    sizeExpanded = false
                                }) {
                                    Text(text = label)
                                }
                            }
                        }
                    }

                    //CodeStatus
                    Column() {
                        OutlinedTextField(
                            readOnly = true,
                            value = "Состояние РКК",
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                                .padding(top = 8.dp)
                                .onGloballyPositioned { coordinates ->
                                    //This value is used to assign to the DropDown the same width
                                    codeStatusTextFieldSize = coordinates.size.toSize()
                                }
                                .clickable {
                                    codeStatusExpanded = !codeStatusExpanded
                                },
                            trailingIcon = {
                                Icon(codeStatusIcon, "contentDescription",
                                    Modifier.clickable {
                                        codeStatusExpanded = !codeStatusExpanded
                                    })
                            }
                        )

                        DropdownMenu(
                            expanded = codeStatusExpanded,
                            onDismissRequest = { codeStatusExpanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { codeStatusTextFieldSize.width.toDp() })
                                .clickable { codeStatusExpanded = !codeStatusExpanded}
                        ) {
                            Column() {
                                for (i in codeStatusCheckedList.indices) {
                                    Row(
                                        Modifier.fillMaxWidth()
                                            .clickable { codeStatusCheckedList[i].value = !codeStatusCheckedList[i].value}
                                    ) {
                                        Checkbox(
                                            checked = codeStatusCheckedList[i].value,
                                            onCheckedChange = {
                                                codeStatusCheckedList[i].value = it
                                            })
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 14.dp)
                                                .clickable {
                                                    focusManager.clearFocus()
                                                    codeStatusCheckedList[i].value =
                                                        !codeStatusCheckedList[i].value
                                                }, text = codeStatusOptions[i]
                                        )
                                    }
                                }
                                //isDeleted
                                Row(
                                    Modifier.fillMaxWidth()
                                        .clickable { isDeleted = !isDeleted }
                                ) {
                                    Checkbox(
                                        checked = isDeleted,
                                        onCheckedChange = {
                                            isDeleted = it
                                        })
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 14.dp)
                                            .clickable {
                                                focusManager.clearFocus()
                                                isDeleted = !isDeleted
                                            }, text = "Удаленные РКК"
                                    )
                                }
                                //isReadyToPublish
                                Row(
                                    Modifier.fillMaxWidth()
                                        .clickable { isReadyToPublish = !isReadyToPublish }

                                ) {
                                    Checkbox(
                                        checked = isReadyToPublish,
                                        onCheckedChange = {
                                            isReadyToPublish = it
                                        })
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 14.dp)
                                            .clickable {
                                                focusManager.clearFocus()
                                                isReadyToPublish = !isReadyToPublish
                                            }, text = "Готовые к публикации"
                                    )
                                }
                                //isReadyForSession
                                Row(
                                    Modifier.fillMaxWidth()
                                        .clickable { isReadyForSession = !isReadyForSession }

                                ) {
                                    Checkbox(
                                        checked = isReadyForSession,
                                        onCheckedChange = {
                                            isReadyForSession = it
                                        })
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 14.dp)
                                            .clickable {
                                                focusManager.clearFocus()
                                                isReadyForSession = !isReadyForSession
                                            }, text = "Подготовленные к сессии"
                                    )
                                }
                                Row(
                                    Modifier.fillMaxWidth()
                                        .clickable { isPublished = !isPublished }

                                ) {
                                    Checkbox(
                                        checked = isPublished,
                                        onCheckedChange = {
                                            isPublished = it
                                        })
                                    Text(
                                        modifier = androidx.compose.ui.Modifier
                                            .padding(top = 14.dp)
                                            .clickable {
                                                focusManager.clearFocus()
                                                isPublished = !isPublished
                                            }, text = "Опубликованные"
                                    )
                                }
                            }

                        }
                    }
                }
            }
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
            {
                Button(modifier = Modifier.padding(start = 35.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF1F6466),
                        contentColor = Color.LightGray,
                    ),
                    onClick = {
                        focusManager.clearFocus()
                        if (!state.isLoading && state.isLoaded) {
                            if (pageSelectedText == "") pageSelectedText =
                                "1"
                            for (i in codeStatusCheckedList.indices) {
                                if (codeStatusCheckedList[i].value) {
                                    codeStatus.add(codeStatusValue[i])
                                }
                            }
                            if (codeStatus.isEmpty()) {
                                codeStatus.add(codeStatusValue[0])
                                codeStatusCheckedList[0].value =
                                    true
                            }
                            var rkkFilter = RkkFilter(
                                page = (pageSelectedText.toInt() - 1).toString(),
                                size = sizeSelectedText,
                                codeStatus = codeStatus,
                            )
                            if (isDeleted) rkkFilter =
                                rkkFilter.copy(isDeleted = "true")
                            if (isReadyForSession) rkkFilter =
                                rkkFilter.copy(readyForSession = "true")
                            if (isReadyToPublish) rkkFilter =
                                rkkFilter.copy(isReadyToPublish = "true")
                            rkkFilterViewModel.onEvent(
                                RkkFilterEvents.RkkFilterEvent(
                                    rkkFilter
                                )
                            )
                        }
                    }) {
                    Text("Обновить")
                }
            }
        }
    )
}

fun String.changeCase(): String {
    return if (!this.isEmpty()) {
        val array = this.toLowerCase().toCharArray()
        array[0] = array[0].uppercaseChar()
        String(array)
    } else {
        this
    }
}

@Composable
fun rkkCard(
    state: RkkFilterState,
    card: RkkData?,
    modifier: Modifier = Modifier,
    npaName: String?,
    npaRkkNumber: String?,
    npaStatus: String?,
    npaType: String?,
    npaRegistrationDate: String?,
    visible: Boolean,
    onNavToNext: (card: RkkData) -> Unit,
) {
    val visiblePlaceHolder = remember { mutableStateOf(visible) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .padding(top = 2.dp)
            .placeholder(
                visible = !visiblePlaceHolder.value,
                color = Color.Gray,
                shape = RoundedCornerShape(15),
                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
            )
            .clickable {
                if (!state.isLoading) {
                    onNavToNext(card!!)
                }
            },
        border = BorderStroke(0.20.dp, color = Color.Gray),
        shape = RoundedCornerShape(15),
        elevation = 5.dp
    ) {
        Column() {
            Row(modifier = Modifier.fillMaxWidth()) {
                if (npaType != null) {
                    Text(
                        text = npaType.changeCase(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        fontSize = 22.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight(350)
                    )
                } else {
                    Text(
                        text = "Empty",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        fontSize = 22.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight(350)
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(0.5.dp),
                color = Color.Gray
            ) {

            }
            Row(modifier.fillMaxWidth()) {
                if (npaName != null) {
                    Text(
                        npaName,
                        modifier = Modifier
                            .weight(5f)
                            .padding(top = 3.dp)
                            .padding(start = 8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight(350),
                        fontSize = 18.sp,
                    )
                } else {
                    Text(
                        text = "Empty",
                        modifier = Modifier
                            .weight(5f)
                            .padding(top = 3.dp)
                            .padding(start = 8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight(350),
                        fontSize = 18.sp,
                    )
                }
                Spacer(Modifier.weight(1f))
                Card(
                    backgroundColor =
                    when (npaStatus) {
                        "ПРИНЯТ" -> Color.Green.copy(alpha = 0.3f)
                        "В РАБОТЕ" -> Color.Yellow.copy(alpha = 0.3f)
                        "ОТКЛОНЕН" -> Color.Red.copy(alpha = 0.3f)
                        else -> {
                            Color.Black.copy(alpha = 0.3f)
                        }
                    },
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .padding(top = 3.dp)
                        .wrapContentWidth(),
                    shape = RoundedCornerShape(50),
                    elevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(horizontal = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (npaStatus != null) {
                            Text(
                                npaStatus,
                                modifier = Modifier
                                    .padding(vertical = 3.dp),
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight(350),
                                fontSize = 16.sp

                            )
                        } else {
                            Text(
                                "Empty",
                                modifier = Modifier
                                    .padding(vertical = 3.dp),
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight(350),
                                fontSize = 16.sp

                            )
                        }
                    }
                }
            }
            Row(modifier.fillMaxWidth()) {
                if (npaRkkNumber != null) {
                    Text(
                        npaRkkNumber,
                        modifier = Modifier
                            .weight(5f)
                            .padding(top = 5.dp)
                            .padding(start = 8.dp)
                            .padding(bottom = 6.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight(300),
                        color = Color.DarkGray,
                        fontSize = 18.sp
                    )
                } else {
                    Text(
                        "Empty",
                        modifier = Modifier
                            .weight(5f)
                            .padding(top = 5.dp)
                            .padding(start = 8.dp)
                            .padding(bottom = 6.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight(300),
                        color = Color.DarkGray,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                if (npaRegistrationDate != null) {
                    Text(
                        text = npaRegistrationDate.split("T")[0],
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .padding(end = 8.dp)
                            .padding(bottom = 6.dp),
                        fontWeight = FontWeight(300),
                        color = Color.DarkGray.copy(alpha = 0.5f),
                        fontSize = 18.sp
                    )
                } else {
                    Text(
                        text = "Empty",
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .padding(end = 8.dp)
                            .padding(bottom = 6.dp),
                        fontWeight = FontWeight(300),
                        color = Color.DarkGray.copy(alpha = 0.5f),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}