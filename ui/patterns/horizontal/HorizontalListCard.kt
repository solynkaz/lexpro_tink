package com.example.lexpro_mobile.ui.patterns.horizontal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lexpro_mobile.R
import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.RkkData
import com.example.lexpro_mobile.ui.screens.horizontal.dateOperate
import com.example.lexpro_mobile.ui.screens.horizontal.noRippleClickable
import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.rkkFilterJSON
import com.example.lexpro_mobile.viewmodel.RkkFilterState
import java.util.*

@Composable
fun HorizontalListCard(
    state: RkkFilterState,
    card: RkkData?,
    onNavToNext: (card: RkkData) -> Unit,
) {
    if (card != null) {
        Column(modifier = Modifier
            .wrapContentSize()
            .noRippleClickable {
                onNavToNext(card)
            }) {
            //Название РКК и статус с цветом
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                //TODO Добавить бордеры
                Text(
                    text =
                    card.npaName ?: "",
                    modifier = Modifier
                        .weight(0.85f)
                        .padding(start = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 24.sp,
                    fontWeight = FontWeight(450)
                )
                if (card.status != null) {
                    Row(
                        modifier = Modifier
                            .background(
                                color =
                                when (card.status.id) {
                                    1 -> Color.Green.copy(alpha = 0.3f)
                                    2 -> Color.Red.copy(alpha = 0.3f)
                                    3 -> Color.Yellow.copy(alpha = 0.3f)
                                    else -> Color.Gray.copy(0.3f)
                                }
                            )
                            .weight(0.15f)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (card.status.name.lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
                                ?: "",
                            modifier = Modifier.padding(start = 3.dp, end = 4.dp),
                            fontSize = 24.sp,
                            fontWeight = FontWeight(350)
                        )
                        Icon(
                            painter = painterResource(
                                id = when (card.status.id) {
                                    1 -> R.drawable.ic_check_circle
                                    2 -> R.drawable.ic_clock
                                    3 -> R.drawable.ic_calendar
                                    4 -> R.drawable.ic_trash
                                    5 -> R.drawable.ic_history
                                    else -> {
                                        R.drawable.ic_user
                                    }
                                }
                            ),
                            contentDescription = "Icon description",
                            modifier = Modifier
                                .size(22.dp)
                        )
                    }
                }
            }
//            Divider(
//                thickness = 0.5.dp,
//                color = Color.Black.copy(alpha = 0.5f),
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom) {
                Column(
                    Modifier
                        .weight(0.4f)
                        .padding(start = 5.dp)) {
                    cardMainText(text = "Номер РКК: ${card.npaNumber ?: ""}")
                    cardMainText(text = if (card.session?.date != null) "Сессия: ${dateOperate(card.session.date)}" else "Не назначено")
                    cardMainText(text = if (card.readyForSession == true) "Подготовлен к сессии" else "Не подготовлен к сессии")
                    cardMainText(text = "Номер по повестке: ${if (card.agendaNumber != null) card.agendaNumber ?: "" else "не назначен"}")
                }
                Spacer(Modifier.weight(0.3f))
                Column(
                    Modifier
                        .weight(0.3f)
                        .padding(end = 5.dp), horizontalAlignment = Alignment.End) {
                    cardSubText(text = if (card.user != null) card.user.fullName else "Отвественное лицо не назначено")
                    cardSubText(text = if (card.registrationDate != null) "Дата регистрации: ${dateOperate(card.registrationDate)}" else "Дата регистрации не указана")
                    cardSubText(text = if( card.introductionDate != null) "Дата внесения: ${dateOperate(card.introductionDate)}" else "Дата внесения не указана")
                }
            }
        }
    }
    Divider(
        thickness = 0.5.dp,
        color = Color.Black.copy(alpha = 0.5f),
    )
}



@Composable
fun cardMainText(text: String) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 20.sp,
        fontWeight = FontWeight(350),
        modifier = Modifier.padding(bottom = 5.dp)
    )
}

@Composable
fun cardSubText(text: String) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 16.sp,
        fontWeight = FontWeight(285)
    )
}