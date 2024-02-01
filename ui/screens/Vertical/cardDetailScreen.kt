package com.example.lexpro_mobile.ui.screens.vertical

import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.RkkData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun rkkListDetails(
    card: RkkData
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${card.rkkNumber}",
                        color = Color.White,
                        fontSize = 18.sp,
                    )
                },
                backgroundColor = Color(0xFF1F6466),
            )
        },
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .padding(top = 5.dp)
                    .padding(bottom = 5.dp),
                border = BorderStroke(0.20.dp, color = Color.Gray),
                shape = RoundedCornerShape(5),
                elevation = 5.dp
            ) {
                Column() {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            card.npaName!!,
                            fontSize = 20.sp,
                            fontWeight = FontWeight(450),
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(1.dp),
                        color = Color.Gray
                    ) {}
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val textModifier = Modifier
                            .padding(horizontal = 5.dp)
                            .padding(top = 3.dp)
                        val textSize = 14.sp

                        Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Номер: ${card.rkkNumber ?: "Empty"}"
                        )

                        val regDate =
                            if (card.registrationDate != null) card.registrationDate?.split("T")
                                ?.get(0)?.split("-") else null
                        if (regDate != null) Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Дата регистрации: ${regDate?.get(2)}.${regDate?.get(1)}.${
                                regDate?.get(0)
                            } "
                        )
                        else Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Дата регистрации: Empty"
                        )

                        val introductionDate =
                            if (card.introductionDate != null) card.introductionDate?.split("T")
                                ?.get(0)?.split("-") else null
                        if (introductionDate != null) Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Дата внесения: ${introductionDate?.get(2)}.${
                                introductionDate?.get(1)
                            }.${introductionDate?.get(0)}"
                        )
                        else Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Дата внесения: Empty"
                        )




                        Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Тип НПА: ${card.npaType?.name?.changeCase()}"
                        )
                        //Text(fontSize = textSize, modifier = textModifier, text = "Номер НПА: ${card.npaType?.id?()}") // Номер нпа нигде не указан

                        val includedInAgenda = card.includedInAgenda?.split("T")?.get(0)?.split("-")

                        Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Включен в повестку: ${includedInAgenda?.get(2) ?: "."}.${includedInAgenda?.get(1) ?: "."}.${includedInAgenda?.get(0) ?: "."}"
                        )
                        Text(
                                fontSize = textSize,
                        modifier = textModifier,
                        text = "Тип НПА: ${card.npaType?.name?.changeCase()}"
                        )
                        val sessionDate =
                            if (card.session?.date != null) card.session.date.split("T")[0].split("-") else null
                        val sessionDateSb: StringBuilder =
                            StringBuilder(if ((card.session != null) && card.session.id != null) "Сессия : ${card.session.id} / " else "Сессия : Empty / ")
                        sessionDateSb.append(if (sessionDate != null) "${sessionDate[2]}.${sessionDate[1]}.${sessionDate[0]}" else "Empty")
                        Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = sessionDateSb.toString()
                        )

                        val deadLine = card.deadline?.split("T")?.get(0)
                            ?.split("-")
                        if (deadLine != null) Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Контрольный срок: ${deadLine?.get(2)}.${deadLine?.get(1)}.${
                                deadLine?.get(0)
                            }"
                        )
                        else Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Контрольный срок: Empty"
                        )

                        Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Подготовлен к сессии: ${if ((card.readyForSession != null) && card.readyForSession) "Да" else "Нет"}"
                        )
                        Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Правительственный час: ${if ((card.governmentHour != null) && card.governmentHour) "Да" else "Нет"}"
                        )
                        Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Стадия: ${card.stage!!.name}"
                        )



                        val publicationDate =
                            if (card.publicationDate != null) card.publicationDate?.split("T")
                                ?.get(0)?.split("-") else null
                        if (publicationDate != null) Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Дата публикации: ${publicationDate?.get(2)}.${
                                publicationDate?.get(1)
                            }.${publicationDate?.get(0)}"
                        )
                        else Text(
                            fontSize = textSize,
                            modifier = textModifier,
                            text = "Дата публикации: Empty"
                        )


                    }
                }
            }
        })
}
