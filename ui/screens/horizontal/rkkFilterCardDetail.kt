package com.example.lexpro_mobile.ui.screens.horizontal

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lexpro_mobile.R
import com.example.lexpro_mobile.jsonDTO.RkkFilterAttachment
import com.example.lexpro_mobile.jsonDTO.RkkFilterMailingList
import com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment.AttachmentList
import com.example.lexpro_mobile.jsonDTO.responseJSON.MailingList.MailingList
import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.RkkData
import com.example.lexpro_mobile.viewmodel.RkkFilterEvents
import com.example.lexpro_mobile.viewmodel.RkkFilterViewModel
import java.util.*

enum class CardDetailMenuEnum {
    Passport,
    Attachments,
    Visa,
    ChangeProtocol,
    Mailing
}

@Composable
fun RkkFilterCardDetail(
    card: RkkData,
    rkkFilterViewModel: RkkFilterViewModel = hiltViewModel(),
    onNavBack: () -> Unit = {}

) {
    var AttachmentListState = rkkFilterViewModel.attachmentListState
    var MailingListState = rkkFilterViewModel.mailingListState
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val menuIconSize = Modifier.size(25.dp)
    val screenState = remember { mutableStateOf(CardDetailMenuEnum.Passport) }

    val mailingThemeValue = remember { mutableStateOf("") }
    val mailingMessageValue = remember { mutableStateOf("") }
    val mailingDateValue = remember { mutableStateOf("") }

    val mailingListChecked = remember { mutableMapOf<Int, Boolean>() }
    val attachmentListChecked = remember { mutableMapOf<Int, Boolean>() }

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
                            text = card.npaNumber ?: "Не назначен",
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
                        onNavBack()
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "Back Button",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
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
                        CardDetailMenu(
                            menuItems = listOf(
                                MenuDetail(
                                    title = "Паспорт",
                                    enabled = screenState.value == CardDetailMenuEnum.Passport,
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id = if (screenState.value == CardDetailMenuEnum.Passport) R.drawable.document_text_bold
                                                else R.drawable.document_text
                                            ), contentDescription = "Passport icon",
                                            tint = Color.Black,
                                            modifier = menuIconSize
                                        )
                                    },
                                    onClick = {
                                        screenState.value = CardDetailMenuEnum.Passport
                                    }
                                ),
                                MenuDetail(
                                    title = "Вложения",
                                    enabled = screenState.value == CardDetailMenuEnum.Attachments,
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id = if (screenState.value == CardDetailMenuEnum.Attachments) R.drawable.paperclip_bold
                                                else R.drawable.paperclip
                                            ), contentDescription = "Attachment icon",
                                            tint = Color.Black,
                                            modifier = menuIconSize
                                        )
                                    },
                                    onClick = {
                                        screenState.value = CardDetailMenuEnum.Attachments
                                    }
                                ),
                                MenuDetail(
                                    title = "Виза",
                                    enabled = screenState.value == CardDetailMenuEnum.Visa,
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id = if (screenState.value == CardDetailMenuEnum.Visa) R.drawable.clipboard_check_bold
                                                else R.drawable.clipboard_check
                                            ), contentDescription = "Visa icon",
                                            tint = Color.Black,
                                            modifier = menuIconSize
                                        )
                                    },
                                    onClick = {
                                        screenState.value = CardDetailMenuEnum.Visa
                                    }
                                ),
                                MenuDetail(
                                    title = "Протокол правки",
                                    enabled = screenState.value == CardDetailMenuEnum.ChangeProtocol,
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id = if (screenState.value == CardDetailMenuEnum.ChangeProtocol) R.drawable.pen_bold
                                                else R.drawable.pen
                                            ), contentDescription = "Change protocol icon",
                                            tint = Color.Black,
                                            modifier = menuIconSize
                                        )
                                    },
                                    onClick = {
                                        screenState.value = CardDetailMenuEnum.ChangeProtocol
                                    }
                                ),
                                MenuDetail(
                                    title = "Рассылка",
                                    enabled = screenState.value == CardDetailMenuEnum.Mailing,
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                id = if (screenState.value == CardDetailMenuEnum.Mailing) R.drawable.letter_opened_bold
                                                else R.drawable.letter_opened
                                            ), contentDescription = "Distribution icon",
                                            tint = Color.Black,
                                            modifier = menuIconSize
                                        )
                                    },
                                    onClick = {
                                        screenState.value = CardDetailMenuEnum.Mailing
                                    }
                                ),
                            )
                        )
                    }
                }
            }
        },
        content = { padding ->
            when (screenState.value) {
                CardDetailMenuEnum.Passport -> {
                    Passport(card = card, padding)
                }
                CardDetailMenuEnum.Attachments -> {
                    if (!AttachmentListState.isAttachmentLoading && !AttachmentListState.isAttachmentLoaded) {
                        AttachmentListState = AttachmentListState.copy(isAttachmentLoading = true)
                        rkkFilterViewModel.onEvent(
                            RkkFilterEvents.RkkAttachmentEvent(
                                RkkFilterAttachment(card.id)
                            )
                        )
                    }
                    if (AttachmentListState.isAttachmentLoaded && !AttachmentListState.isAttachmentLoading) {
                        if (AttachmentListState.rkkID != card.id) AttachmentListState =
                            AttachmentListState.copy(
                                isAttachmentLoading = false,
                                isAttachmentLoaded = false
                            )
                        else {
                            if (AttachmentListState.rkkAttachmentList!!.isNotEmpty()) {
                                Attachments(
                                    attachmentList = AttachmentListState.rkkAttachmentList!!,
                                    paddingValues = padding
                                )
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Пусто", fontSize = 26.sp, fontWeight = FontWeight(350))
                                }
                            }
                        }
                    }
                }

                CardDetailMenuEnum.Visa -> {

                }
                CardDetailMenuEnum.ChangeProtocol -> {

                }
                CardDetailMenuEnum.Mailing -> {
                    if ((!MailingListState.isMailingLoading && !MailingListState.isMailingLoaded) || MailingListState.rkkID != card.id) {
                        MailingListState = MailingListState.copy(isMailingLoading = true)
                        rkkFilterViewModel.onEvent(
                            RkkFilterEvents.RkkMailingEvent(
                                RkkFilterMailingList(card.id)
                            )
                        )
                    }
                    if ((!AttachmentListState.isAttachmentLoading && !AttachmentListState.isAttachmentLoaded) || AttachmentListState.rkkID != card.id) {
                        AttachmentListState = AttachmentListState.copy(isAttachmentLoading = true)
                        rkkFilterViewModel.onEvent(
                            RkkFilterEvents.RkkAttachmentEvent(
                                RkkFilterAttachment(card.id)
                            )
                        )
                    }
                    if ((MailingListState.isMailingLoaded && !MailingListState.isMailingLoading) && (AttachmentListState.isAttachmentLoaded && !AttachmentListState.isAttachmentLoading)) {
                        if (MailingListState.rkkID == card.id && AttachmentListState.rkkID == card.id) {
                            Mailing(
                                mailingList = MailingListState.rkkMailingList,
                                attachmentList = AttachmentListState.rkkAttachmentList,
                                paddingValues = padding,
                                themeValue = mailingThemeValue,
                                messageValue = mailingMessageValue,
                                dateValue = mailingDateValue,
                                checkedMailing = mailingListChecked,
                                checkedAttachment = attachmentListChecked
                            )
                        } else {
                            AttachmentListState =
                                AttachmentListState.copy(isAttachmentLoaded = false)
                            MailingListState = MailingListState.copy(isMailingLoaded = false)
                        }
                    }
                }
            }
        })
}

data class MenuDetail(
    val title: String,
    val enabled: Boolean,
    val icon: (@Composable () -> Unit)? = null,
    val onClick: () -> Unit,
)


@Composable
fun Mailing(
    mailingList: MailingList?,
    attachmentList: List<AttachmentList>?,
    paddingValues: PaddingValues,
    themeValue: MutableState<String>,
    messageValue: MutableState<String>,
    dateValue: MutableState<String>,
    checkedMailing: MutableMap<Int, Boolean>,
    checkedAttachment: MutableMap<Int, Boolean>
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
            ) {
                Text("Тема", fontSize = 24.sp, fontWeight = FontWeight(400))
                OutlinedTextField(
                    value = themeValue.value,
                    onValueChange = { themeValue.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.13f),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                Text(
                    "Сообщение",
                    fontSize = 24.sp,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.padding(top = 15.dp)
                )
                OutlinedTextField(
                    value = messageValue.value,
                    onValueChange = { messageValue.value = it },
                    modifier = Modifier
                        .weight(0.74f)
                        .fillMaxSize()
                        .padding(bottom = 15.dp),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                Text("Дата рассылки", fontSize = 24.sp, fontWeight = FontWeight(400))
                OutlinedTextField(
                    value = dateValue.value,
                    onValueChange = { dateValue.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.13f)
                        .fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 18.sp)

                )
            }
        }
        Row(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxSize()
        ) {
            Column() {
                Column(modifier = Modifier.weight(0.45f)) {
                    LazyColumn(modifier = Modifier) {
                        item {
                            if (mailingList != null) {
                                if (mailingList.destinationList!!.isNotEmpty()) {
                                    for (destinationSubject in mailingList.destinationList) {
                                        Row() {

                                        }
                                        Text(
                                            destinationSubject.employee.name,
                                            Modifier.noRippleClickable {
                                                if (!checkedMailing.containsKey(destinationSubject.employee.id)) {
                                                    checkedMailing[destinationSubject.employee.id] =
                                                        true
                                                    Log.d(
                                                        "Mailing",
                                                        "Добавлено в checkedMailing: ID: ${destinationSubject.employee.id} with value: ${checkedMailing[destinationSubject.employee.id]}"
                                                    )
                                                } else {
                                                    checkedMailing[destinationSubject.employee.id] =
                                                        !checkedMailing[destinationSubject.employee.id]!!
                                                    Log.d(
                                                        "Mailing",
                                                        "Изменено в checkedMailing: ID: ${destinationSubject.employee.id} to value: ${checkedMailing[destinationSubject.employee.id]}"
                                                    )
                                                }
                                            })
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "Пусто",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight(350)
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Пусто", fontSize = 26.sp, fontWeight = FontWeight(350))
                                }
                            }
                        }
                    }
                    //endregion
                }
                Column(modifier = Modifier.weight(0.45f)) {
                    LazyColumn(modifier = Modifier) {
                        item {
                            if (attachmentList != null) {
                                if (attachmentList.isNotEmpty()) {
                                    for (attachment in attachmentList) {
                                        Text(
                                            attachment.originalFileName ?: "",
                                            modifier = Modifier.noRippleClickable {
                                                if (!checkedAttachment.containsKey(attachment.id)) {
                                                    checkedAttachment[attachment.id] = true
                                                } else {
                                                    checkedAttachment[attachment.id] =
                                                        !checkedAttachment[attachment.id]!!
                                                }
                                            })
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "Пусто",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight(350)
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Пусто", fontSize = 26.sp, fontWeight = FontWeight(350))
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.weight(0.1f)) {
                    Button(onClick = { /*TODO*/ }) {
                        Text("Отправить")
                    }
                }
            }
        }
    }
}

@Composable
fun CardDetailMenu(menuItems: List<MenuDetail>) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.09f))
        menuItems.forEachIndexed { i, dataItem ->
            Column(
                modifier = Modifier
                    .weight(0.1f)
                    .noRippleClickable {
                        dataItem.onClick()
                    }
                    .padding(bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (dataItem.icon != null) dataItem.icon.let { it() }
                Text(
                    text = dataItem.title,
                    fontWeight = if (dataItem.enabled) {
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
fun Passport(card: RkkData, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
    ) {
        val fieldModifier = Modifier.weight(1f)
        val dataRowModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        Text(
            text = card.npaName ?: "",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            modifier = Modifier.padding(top = 5.dp, bottom = 15.dp)
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            //region User, Speaker, Cospeaker, Status
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier.padding(end = 6.dp)) {
                    Text(
                        text = "Ответственное лицо",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = if (card.user != null) card.user.fullName else "Не назначено",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(400),
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(horizontal = 6.dp)) {
                    Text(text = "Докладчик", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(
                        text = if (card.speaker != null) card.speaker.fullName else "Не назначено",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(horizontal = 6.dp)) {
                    Text(text = "Содокладчик", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(text = if (card.coSpeaker != null) card.coSpeaker.fullName else "Не назначен", fontSize = 22.sp)
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(start = 6.dp)) {
                    Text(text = "Статус", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(text = if (card.status != null) card.status.name.lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } else "Не назначено",
                        fontSize = 22.sp)
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion
            //region Responsible Organization
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier) {
                    Text(
                        text = "Ответственные комитеты",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = if (card.responsibleOrganization != null) card.responsibleOrganization.fullName else "Не назначено",
                        fontSize = 22.sp
                    ) //TODO Раскрывающимся списком
                    Divider(
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion
            //region NPA Number, Npa Type
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier.padding(end = 6.dp)) {
                    Text(text = "№ РКК", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(text = card.npaNumber ?: "Не назначено", fontSize = 22.sp)
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(horizontal = 6.dp)) {
                    Text(text = "Тип НПА", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(
                        text = if (card.npaType?.name != null) card.npaType.name.lowercase()
                            .capitalize(
                                Locale.ROOT
                            )
                            ?: "Не назначено" else "Не назначено", fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion
            //region Reg Date, Introduction date
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier.padding(end = 6.dp)) {
                    Text(text = "Дата регистрации", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(
                        text = if (card.registrationDate != null) dateOperate(card.registrationDate) else "Не назначено",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(horizontal = 6.dp)) {
                    Text(text = "Дата внесения", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(
                        text = if (card.introductionDate != null) dateOperate(card.introductionDate) else "Не назначено",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion
            //region Stage
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier) {
                    Text(
                        text = "Стадия",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = if (card.stage != null) card.stage.name else "Не назначено",
                        fontSize = 22.sp
                    ) //TODO Раскрывающимся списком
                    Divider(
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion

            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            //Session
            Text(
                "Сессия",
                fontSize = 22.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(bottom = 5.dp)
            )

            //region Session, Agenda Include
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier.padding(end = 6.dp)) {
                    Text(
                        text = "Сессия",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = if (card.session?.date != null) dateOperate(card.session.date) else "Не назначено",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(horizontal = 6.dp)) {
                    Text(
                        text = "Включен в повестку",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = if (card.includedInAgenda != null) dateOperate(card.includedInAgenda) else "Не назначено",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion
            //region Agenda number, control date
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier.padding(end = 6.dp)) {
                    Text(
                        text = "Номер по повестке",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(text = card.agendaNumber ?: "Не назначено", fontSize = 22.sp)
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(start = 6.dp)) {
                    Text(text = "Контрольный срок", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(
                        text = if (card.deadline != null) dateOperate(card.deadline) else "Не назначено",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion

            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            //Signing
            Text(
                "Подписание",
                fontSize = 22.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(bottom = 5.dp)
            )

            //region Sign Date, Publish Date
            Row(modifier = dataRowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = fieldModifier.padding(end = 6.dp)) {
                    Text(
                        text = "Дата подписания Главой",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = if (card.headSignature != null) dateOperate(card.headSignature) else "Не подписано",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
                Column(modifier = fieldModifier.padding(horizontal = 6.dp)) {
                    Text(text = "Дата публикации", fontSize = 18.sp, fontWeight = FontWeight.Light)
                    Text(
                        text = if (card.publicationDate != null) dateOperate(card.publicationDate) else "Не опубликовано",
                        fontSize = 22.sp
                    )
                    Divider(
                        //startIndent = 30.dp,
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.5f),
                    )
                }
            }
            //endregion
        }
    }
}

@Composable
fun Attachments(attachmentList: List<AttachmentList>, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
    ) {
        LazyColumn(modifier = Modifier.padding(bottom = 1.dp)) {
            item {
                for (attachment in attachmentList) {
                    AttachmentField(attachment = attachment)
                }
            }
        }
    }
}

@Composable
fun AttachmentField(attachment: AttachmentList?) {
    if (attachment != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 5.dp)
        ) {
            Icon(
                painter = painterResource(
                    id =
                    when (attachment.fileExtension) {
                        ".doc", ".docx" -> {
                            R.drawable.ic_file_text
                        }
                        ".rar", ".7zip", ".zip" -> {
                            R.drawable.ic_zip_file
                        }
                        else -> {
                            R.drawable.ic_file
                        }
                    }
                ),
                contentDescription = "File icon",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 5.dp)
            )
            Row(Modifier.weight(0.9f)) {
                Column() {
                    if (attachment.originalFileName != null) {
                        Text(
                            text = attachment.originalFileName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight(400)
                        )
                    }
                    Row() {
                        if (attachment.docDateAttachment != null) {
                            Text(
                                text = dateOperate(attachment.docDateAttachment),
                                fontSize = 18.sp,
                                fontWeight = FontWeight(325),
                                modifier = Modifier.padding(end = 7.dp)
                            )
                        }
                        if (attachment.pageCount != 0 && attachment.pageCount != null) {
                            Text(
                                text = "${attachment.pageCount} стр.",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(325),
                                modifier = Modifier.padding(end = 7.dp)
                            )
                        }
                        if (attachment.type != null) {
                            Text(
                                text = attachment.type.name.lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                                    ?: "",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(325),
                                modifier = Modifier.padding(end = 7.dp)
                            )
                        }
                        if (attachment.group != null) {
                            Text(
                                text = attachment.group.name
                                    ?: "",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(325),
                                modifier = Modifier.padding(end = 7.dp)
                            )
                        }
                    }
                }
            }
            Row(Modifier.weight(0.1f), horizontalArrangement = Arrangement.End) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_file_download
                    ),
                    contentDescription = "File icon",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            //TODO Добавить скачивание файла
                        }
                )
            }
        }
    }
}

fun dateOperate(date: String): String {
    val temp = date.lowercase().split("t")[0].split("-")
    return "${temp[2]}.${temp[1]}.${temp[0]}"
}