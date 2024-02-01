package com.example.lexpro_mobile.ui.screens.horizontal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lexpro_mobile.R
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import com.example.lexpro_mobile.jsonDTO.RkkFilter
import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.RkkData
import com.example.lexpro_mobile.ui.patterns.horizontal.HorizontalListCard
import com.example.lexpro_mobile.viewmodel.RkkFilterEvents
import com.example.lexpro_mobile.viewmodel.RkkFilterViewModel
import kotlinx.coroutines.launch
import java.util.*

enum class DrawerState {
    Collapsed,
    Expanded
}

enum class ListSortStates {
    DateFromNewToOld,
    DateFromOldToNew
}

val rkkFilter = RkkFilter()


@Composable
fun rkkFilterHorizontal(
    rkkFilterViewModel: RkkFilterViewModel = hiltViewModel(),
    onNavToNext: (card: RkkData) -> Unit = {},
) {
    val state = rkkFilterViewModel.state

    val filterIconSize = Modifier.size(25.dp)
    //TODO Анимация появления элементов дроплиста, анимация клика по элементу + Анимация появления левой панели фильтра по иконки кнопки в топбаре
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val drawerSize = remember { mutableStateOf(0.22f) }
    var currentDrawerState by remember { mutableStateOf(DrawerState.Expanded) }
    val checkedStatesBoxes = remember {
        listOf(
            listOf(
                mutableStateOf(false) //Мои ЗИ
            ),
            listOf(
                mutableStateOf(false) //Я докладчик
            ),
            listOf(
                mutableStateOf(false) //Мой комитет
            ),
        )
    }
    val listSortStates = remember {
        mutableStateOf(ListSortStates.DateFromNewToOld)
    }
    val listSortItems = listOf("От старых к новым", "От новых к старым")
    val listSortArrowState = remember { mutableStateOf(false) }
    val listSortSelected = remember { mutableStateOf(0)}


    val checkedStatesList = remember {
        listOf(
            mutableStateOf(false), //Принят
            mutableStateOf(false), //В работе
            mutableStateOf(false), //К заседанию
            mutableStateOf(false), //Отклонен
            mutableStateOf(false)  //Отозван
        )
    }
    val checkedStateRadioSession = remember { mutableStateOf(-1) }
    val filterFolderCheckedState = remember { mutableStateOf(0) }
    val filterSearchValue = remember { mutableStateOf("") }

    if (!state.isLoading && !state.isLoaded) {
        rkkFilterViewModel.onEvent(
            RkkFilterEvents.RkkFilterEvent(
                RkkFilter(
                    codeStatus = listOf("IN_WORK","ACCEPTED","FOR_MEETING"),
                    page = "1",
                    size = "25"
                )
            )
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Регистрационные карты",
                            color = Color.White,
                            fontWeight = FontWeight(400)
                        )
                    }
                },
                backgroundColor = Color(0xFF1992AF),
                modifier = Modifier.wrapContentHeight()
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            currentDrawerState = when (currentDrawerState) {
                                DrawerState.Collapsed -> {
                                    DrawerState.Expanded
                                }
                                DrawerState.Expanded -> {
                                    DrawerState.Collapsed
                                }
                            }
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
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.White,
                modifier = Modifier.border(
                    BorderStroke(
                        0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                )
            ) {
                Row(Modifier.fillMaxSize()) {
                    Column(Modifier.weight(0.22f)) {
                        FilterNavigationList(
                            elements = listOf(
                                MenuSection(
                                    onClick = {
                                        //todo работа кнопки выбора окна
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id =
                                                if (filterFolderCheckedState.value == 0) R.drawable.ic_folder_bold
                                                else R.drawable.ic_folder_with_files_light
                                            ), contentDescription = "folder",
                                            tint = Color.Black,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    },
                                    title = "РКК",
                                )
                            ), selected = filterFolderCheckedState
                        )
                    }
                    Column(Modifier.weight(0.78f)) {
                        FilterList(
                            filterItems = listOf(
                                FilterListSection(
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id =
                                                if (!checkedStatesList[0].value) R.drawable.ic_check_circle
                                                else R.drawable.ic_check_circle_bold
                                            ), contentDescription = "check_circle",
                                            tint = Color.Black,
                                            modifier = filterIconSize
                                        )
                                    },
                                    title = "Принят",
                                    value = "ACCEPTED",
                                    //TODO Добавление валью фильтра в запрос
                                    onClick = {
                                        checkedStatesList[0].value = !checkedStatesList[0].value
                                    },
                                    checkedState = checkedStatesList[0]
                                ),
                                FilterListSection(
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id =
                                                if (!checkedStatesList[1].value) R.drawable.ic_clock
                                                else R.drawable.ic_clock_bold
                                            ), contentDescription = "folder",
                                            tint = Color.Black,
                                            modifier = filterIconSize
                                        )
                                    },
                                    title = "В работе",
                                    value = "IN_WORK",
                                    //TODO Добавление валью фильтра в запрос
                                    //TODO Иконка болд полностью черная
                                    onClick = {
                                        checkedStatesList[1].value = !checkedStatesList[1].value
                                    },
                                    checkedState = checkedStatesList[1]
                                ),
                                FilterListSection(
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id =
                                                if (!checkedStatesList[2].value) R.drawable.ic_calendar
                                                else R.drawable.ic_calendar_bold
                                            ), contentDescription = "folder",
                                            tint = Color.Black,
                                            modifier = filterIconSize
                                        )
                                    },
                                    title = "К заседанию",
                                    value = "TEMP", //TODO Добавить описание
                                    //TODO Добавление валью фильтра в запрос
                                    onClick = {
                                        checkedStatesList[2].value = !checkedStatesList[2].value
                                    },
                                    checkedState = checkedStatesList[2]
                                ),
                                FilterListSection(
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id =
                                                if (!checkedStatesList[3].value) R.drawable.ic_trash
                                                else R.drawable.ic_trash_bold
                                            ), contentDescription = "folder",
                                            tint = Color.Black,
                                            modifier = filterIconSize
                                        )
                                    },
                                    title = "Отклонен",
                                    value = "TEMP", //TODO Добавить описание
                                    //TODO Добавление валью фильтра в запрос
                                    onClick = {
                                        checkedStatesList[3].value = !checkedStatesList[3].value
                                    },
                                    checkedState = checkedStatesList[3]
                                ),
                                FilterListSection(
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id =
                                                if (!checkedStatesList[4].value) R.drawable.ic_history
                                                else R.drawable.ic_history_bold
                                            ), contentDescription = "folder",
                                            tint = Color.Black,
                                            modifier = filterIconSize
                                        )
                                    },
                                    title = "Отозван",
                                    value = "TEMP", //TODO Добавить описание
                                    onClick = {
                                        //TODO Добавление валью фильтра в запрос
                                        checkedStatesList[4].value = !checkedStatesList[4].value
                                    },
                                    checkedState = checkedStatesList[4]
                                ),
                            )
                        )
                    }
                }
            }
        },
        content = {
            padding ->
            Row(Modifier.fillMaxSize()) {
                when (currentDrawerState) {
                    DrawerState.Expanded -> {
                        //Открывающаяся колонка
                        Column(
                            Modifier
                                .weight(drawerSize.value)
                                .fillMaxHeight()
                                .border(
                                    BorderStroke(
                                        0.5.dp,
                                        color = Color.Black.copy(alpha = 0.5f)
                                    )
                                )
                        ) {
                            TextField(
                                value = filterSearchValue.value,
                                onValueChange = { letter -> filterSearchValue.value = letter },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_magnifier),
                                        contentDescription = "Search icon",
                                        tint = Color.Black,
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                textStyle = TextStyle.Default.copy(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(370)
                                ),
                                shape = MaterialTheme.shapes.small,
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFFD9D9D9),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = Color.Black.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 10.dp,
                                        horizontal = 8.dp
                                    ),
                            )
                            CollapsableLazyColumnBoxes(
                                sections = listOf(
                                    CollapsableSection(
                                        title = "Сотрудники",
                                        rows = listOf("Мои ЗИ"),
                                        titleIcon = "User"
                                    ),
                                    CollapsableSection(
                                        title = "Докладчики",
                                        rows = listOf("Я докладчик"),
                                        titleIcon = "Notebook"
                                    ),
                                    CollapsableSection(
                                        title = "Комитеты",
                                        rows = listOf(
                                            "Мой комитет"
                                        ),
                                        titleIcon = "Users_Group"
                                    ),
                                ),
                                checkedStates = checkedStatesBoxes
                            )
                            CollapsableLazyColumnRadio(
                                sections = listOf(
                                    CollapsableSection(
                                        title = "Сессия",
                                        rows = listOf(
                                            "Сегодня",
                                            "Эта неделя",
                                            "Этот месяц",
                                        ),
                                        titleIcon = "Calendar"
                                    )
                                ), checkedState = checkedStateRadioSession
                            )
                        }

                    }
                    DrawerState.Collapsed -> {
                    }
                }
                Column(Modifier.weight(1f - drawerSize.value)) {
                    Row(
                        modifier = Modifier
                            .weight(0.05f)
                            .fillMaxSize()
                            .border(
                                border = BorderStroke(
                                    0.5.dp,
                                    color = Color.Black.copy(alpha = 0.5f)
                                )
                            ),
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        Text(
                            "Сортировка по дате регистрации (${
                                listSortItems[listSortSelected.value].lowercase(
                                    Locale.ROOT
                                )
                            })",
                            modifier = Modifier.padding(start = 5.dp, end = 4.dp)
                        )
                        Column {
                            Icon(
                                painter = painterResource(
                                    id =
                                    if (listSortArrowState.value) R.drawable.ic_arrow_up
                                    else R.drawable.ic_arrow_down
                                ), contentDescription = "arrowListSort",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(25.dp)
                                    .noRippleClickable {
                                        listSortArrowState.value = !listSortArrowState.value
                                    }
                            )
                            DropdownMenu(
                                expanded = listSortArrowState.value,
                                onDismissRequest = { listSortArrowState.value = false },
                                modifier = Modifier
                                    //.width(with(LocalDensity.current) { sizeTextFieldSize.width.toDp() })
                                    .clickable {
                                        //TODO работа со списком в зависимости от сортировки
                                        listSortArrowState.value = !listSortArrowState.value
                                    }
                            ) {
                                listSortItems.forEachIndexed { i, label ->
                                    DropdownMenuItem(onClick = {
                                        listSortSelected.value = i
                                        listSortArrowState.value = false
                                    }) {
                                        Text(text = label)
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.95f)
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        //region Main Content
                        if (state.isLoaded && !state.isLoading) {
                            if (state.rkkFilterList != null && state.rkkFilterList.total_count != 0) {
                                LazyColumn(modifier = Modifier.padding(bottom = 1.dp)) {
                                    item {
                                        for (card in state.rkkFilterList.data) {
                                            HorizontalListCard(
                                                state = state,
                                                card = card,
                                                onNavToNext = onNavToNext
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Box(contentAlignment = Center) {
                                Text(text = "Loading...")
                                //TODO Zaglushka()
                            }

                        }
                     /*
                        LazyColumn {
                            item {
                                for (i in 0..10) {
                                    HorizontalListCard(
                                        state = state,
                                        card = null,
                                        npaName = "О проекте закона Республики Бурятия О внесении изменений в отдельные законодательныеы акты Республики Бурятия",
                                        npaRkkNumber = "45.01-06-05-в2137",
                                        npaStatus = "Принят",
                                        npaRegistrationDate = "25.07.2021",
                                        npaType = "",
                                        visible = true,
                                        onNavToNext = onNavToNext,
                                    )
                                }
                            }
                        }

                         */
                        //endregion
                    }
                }
            }
        })

/*
    if (!state.isLoading && !state.isLoaded) {
        rkkFilterViewModel.onEvent(
            RkkFilterEvents.RkkFilterEvent(
                RkkFilter(
                    codeStatus = listOf("IN_WORK"),
                    page = "0",
                    size = "25"
                )
            )
        )
    }*/
}

fun String.changeCase(): String {
    return if (this.isNotEmpty()) {
        val array = this.lowercase().toCharArray()
        array[0] = array[0].uppercaseChar()
        String(array)
    } else {
        this
    }
}

data class CollapsableSection(
    val title: String,
    val rows: List<String>,
    val titleIcon: String,
)

data class MenuSection(
    val title: String,
    val icon: (@Composable () -> Unit)? = null,
    val onClick: () -> Unit,
)

data class FilterListSection(
    val title: String,
    val value: String,
    val icon: (@Composable () -> Unit)? = null,
    val onClick: () -> Unit,
    val checkedState: MutableState<Boolean>
)

@Composable
fun FilterList(
    filterItems: List<FilterListSection>,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.09f))
        filterItems.forEachIndexed { i, dataItem ->
            Column(
                modifier = Modifier
                    .weight(0.1f)
                    .noRippleClickable {
                        dataItem.onClick()
                    }
                    .padding(bottom = 0.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                if (dataItem.icon != null) dataItem.icon.let { it() }
                Text(
                    text = dataItem.title,
                    fontWeight = if (dataItem.checkedState.value) {
                        FontWeight(485)
                    } else {
                        FontWeight(350)
                    },
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.09f))
    }
}

@Composable
fun FilterNavigationList(
    elements: List<MenuSection>,
    selected: MutableState<Int>
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        elements.forEachIndexed { i, dataItem ->
            Column(
                Modifier
                    .wrapContentSize()
                    .noRippleClickable {
                        dataItem.onClick()
                    }
                    .weight(1f)
                    .padding(vertical = 1.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (dataItem.icon != null) dataItem.icon.let { it() }
                Text(
                    text = dataItem.title,
                    fontSize = 17.sp,
                    fontWeight =
                    if (i == selected.value) FontWeight(500)
                    else FontWeight(350),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun CollapsableLazyColumnRadio(
    sections: List<CollapsableSection>,
    modifier: Modifier = Modifier,
    checkedState: MutableState<Int>
) {
    val collapsedState = remember(sections) { sections.map { false }.toMutableStateList() }

    LazyColumn(modifier) {
        sections.forEachIndexed { i, dataItem ->
            val collapsed = collapsedState[i]
            item(key = "header_$i") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .noRippleClickable {
                            collapsedState[i] = !collapsed
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            id = when (dataItem.titleIcon) {
                                "Calendar" -> R.drawable.ic_calendar
                                else -> {
                                    R.drawable.ic_square
                                }
                            }
                        ),
                        contentDescription = "Icon description",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(horizontal = 5.dp)
                    )
                    Text(
                        dataItem.title,
                        fontWeight = FontWeight(350),
                        modifier = Modifier
                            .padding(top = 3.dp, bottom = 4.dp)
                            .weight(1f),
                        fontSize = 21.sp
                    )
                    Icon(
                        painter = painterResource(
                            id = when (collapsed) {
                                true -> {
                                    R.drawable.ic_arrow_down
                                }
                                false -> {
                                    R.drawable.ic_arrow_up
                                }
                            }
                        ),
                        contentDescription = "Icon description",
                        modifier = Modifier
                            .size(33.dp)
                            .padding(horizontal = 5.dp)//
                    )
                }
                Divider(
                    startIndent = 30.dp,
                    thickness = 0.5.dp,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = when (collapsedState[i]) {
                        true -> {
                            Modifier.padding(bottom = 10.dp)
                        }
                        false -> {
                            Modifier.padding(bottom = 0.dp)
                        }
                    }
                )
            }
            if (!collapsed) {
                dataItem.rows.forEachIndexed { k, row ->
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .noRippleClickable {
                                    checkedState.value = k
                                }
                                .padding(top = 3.dp, bottom = 2.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(Modifier.padding(start = 30.dp))
                            Icon(
                                painter = painterResource(
                                    id = when (k == checkedState.value) {
                                        true -> {
                                            R.drawable.ic_circle_check
                                        }
                                        false -> {
                                            R.drawable.ic_circle_empty
                                        }
                                    }
                                ),
                                contentDescription = "Unchecked square",
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(start = 4.dp, end = 3.dp, top = 3.dp)
                            )
                            Text(
                                row,
                                fontWeight = FontWeight(330),
                                fontSize = 19.sp
                            )
                        }
                        Divider(
                            startIndent = 58.dp,
                            thickness = 0.5.dp,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = when (collapsedState[i]) {
                                true -> {
                                    Modifier.padding(bottom = 10.dp)
                                }
                                false -> {
                                    Modifier.padding(bottom = 0.dp)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CollapsableLazyColumnBoxes(
    sections: List<CollapsableSection>,
    modifier: Modifier = Modifier,
    checkedStates: List<List<MutableState<Boolean>>>
) {
    val collapsedState = remember(sections) { sections.map { false }.toMutableStateList() }
    LazyColumn(modifier) {
        sections.forEachIndexed { i, dataItem ->
            val collapsed = collapsedState[i]
            item(key = "header_$i") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .noRippleClickable {
                            collapsedState[i] = !collapsed
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            id = when (dataItem.titleIcon) {
                                "User" -> R.drawable.ic_user
                                "Users_Group" -> R.drawable.ic_users_group
                                "Notebook" -> R.drawable.ic_notebook
                                else -> {
                                    R.drawable.ic_square
                                }
                            }
                        ),
                        contentDescription = "Icon description",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(horizontal = 5.dp)
                    )
                    Text(
                        dataItem.title,
                        fontWeight = FontWeight(350),
                        modifier = Modifier
                            .padding(top = 3.dp, bottom = 4.dp)
                            .weight(1f),
                        fontSize = 21.sp
                    )
                    Icon(
                        painter = painterResource(
                            id = when (collapsed) {
                                true -> {
                                    R.drawable.ic_arrow_down
                                }
                                false -> {
                                    R.drawable.ic_arrow_up
                                }
                            }
                        ),
                        contentDescription = "Icon description",
                        modifier = Modifier
                            .size(33.dp)
                            .padding(horizontal = 5.dp)
                    )
                }
                Divider(
                    startIndent = 30.dp,
                    thickness = 0.5.dp,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = when (collapsedState[i]) {
                        true -> {
                            Modifier.padding(bottom = 10.dp)
                        }
                        false -> {
                            Modifier.padding(bottom = 0.dp)
                        }
                    }
                )
            }
            if (!collapsed) {
                dataItem.rows.forEachIndexed { k, row ->
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .noRippleClickable {
                                    checkedStates[i][k].value =
                                        !checkedStates[i][k].value
                                }
                                .padding(top = 3.dp, bottom = 1.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(Modifier.padding(start = 30.dp))
                            Icon(
                                painter = painterResource(
                                    id = when (checkedStates[i][k].value) {
                                        true -> {
                                            R.drawable.ic_check_square
                                        }
                                        false -> {
                                            R.drawable.ic_square
                                        }
                                    }
                                ),
                                contentDescription = "Unchecked square",
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(start = 4.dp, end = 3.dp, top = 3.dp)
                            )
                            Text(
                                row,
                                fontWeight = FontWeight(330),
                                fontSize = 19.sp
                            )
                        }
                        Divider(
                            startIndent = 58.dp,
                            thickness = 0.5.dp,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = when (collapsedState[i]) {
                                true -> {
                                    Modifier.padding(bottom = 10.dp)
                                }
                                false -> {
                                    Modifier.padding(bottom = 0.dp)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}