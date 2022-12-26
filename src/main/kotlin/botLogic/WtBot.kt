package botLogic

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import weatherInfo.WeatherRepository
import weatherInfo.apiKey
import java.util.logging.Filter

private const val botAnswerTime = 20
private const val botToken = "5938695131:AAGf3dXaKbyLZXE8tL6zq_RxeI84aFuxB0s"

class WtBot(private val weatherRepository: WeatherRepository) {

    private var _chatId: ChatId? = null
    private val chatId by lazy { requireNotNull(_chatId) }
    private  lateinit var city: String

    fun createBot(): Bot {
        return bot{
            timeout = botAnswerTime
            token = botToken
            logLevel = LogLevel.Error

            dispatch {
                setUpCommands()
                setUpCallbacks()
            }

        }
    }

    private fun Dispatcher.setUpCallbacks() {

        callbackQuery(callbackData = "getLocation") {
            bot.sendMessage(
                chatId = chatId,
                text = "Щя подскажу(кинь локацию)"
            )
            location {
                CoroutineScope(Dispatchers.IO).launch {
                    city = weatherRepository .getCoordinateCity(
                        location.latitude.toString(), location.longitude.toString(), "json"
                    ).address.city

                    val keyboard =  InlineKeyboardMarkup.create(
                        listOf(
                            InlineKeyboardButton.CallbackData(
                                text = "Ураа",
                                callbackData = "agreement"
                            )
                        )
                    )
                    bot.sendMessage(
                        chatId=chatId,
                        text = "Ты вот тут!!! : : ${city} ",
                        replyMarkup =  keyboard
                    )
                }
            }
        }
        callbackQuery(callbackData = "getCity") {
            bot.sendMessage(
                chatId = chatId,
                text = "Ну вводи город, умник"
            )
            message(com.github.kotlintelegrambot.extensions.filters.Filter.Text) {

                city = message.text.toString()
                val keyboardButton =  InlineKeyboardMarkup.create(
                    listOf(
                        InlineKeyboardButton.CallbackData(
                            text = "И правда",
                            callbackData = "agreement"
                        )
                    )
                )
                bot.sendMessage(
                    chatId=chatId,
                    text = "Выбранное село : ${city}",
                    replyMarkup =  keyboardButton
                )


            }
        }

        callbackQuery(callbackData = "agreement") {
            bot.apply {
                sendAnimation(
                    chatId = chatId,
                    animation = TelegramFile.ByUrl("https://media.tenor.com/AzEW3sXMqqYAAAAd/бэбра-харош.gif")
                )
                sendMessage(
                    chatId= chatId,
                    text = "Щя все будет, братик"
                )
            }
            CoroutineScope(Dispatchers.IO).launch {
                val currWeather = weatherRepository.getCurrentWeather(
                    apiKey, city, "no"
                )
                bot.sendMessage(
                    chatId = chatId,
                    text = """ 
                        Темпа: ${currWeather.current.temp_c}
                        Ощущения: ${currWeather.current.feelslike_c}
                        ВлАААгААА: ${currWeather.current.humidity}
                        Давление груза обтветственности: ${currWeather.current.pressure_in }
                        """.trimIndent()

                )

                val keyboardMarkup = InlineKeyboardMarkup.create(
                    listOf(
                        InlineKeyboardButton.CallbackData(
                            text = "Да",
                            callbackData = "cancel"
                        )
                    )
                )
                bot.sendMessage(
                    chatId = chatId,
                    text = "Даволен?",
                    replyMarkup = keyboardMarkup
                )
            }

        }
        callbackQuery(callbackData = "disagreement") {
            bot.sendMessage(
                chatId = chatId,
                text = "Жаль..."
            )
        }
        callbackQuery(callbackData = "cancel") {

        }

    }


    private fun Dispatcher.setUpCommands() {
        command("start"){
            _chatId = ChatId.fromId(message.chat.id)
            bot.sendMessage(
                chatId = chatId,
                text = "Это бот для погодки, здарова)"
            )
        }



        command("weather"){
            val keyboardMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Где я?!",
                        callbackData = "getLocation"
                    )
                ),
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Ручками 8-)",
                        callbackData = "getCity"
                    )
                )
            )
            bot.sendMessage(
                chatId = chatId,
                text = "Как желаете указать город, сэээр?",
                replyMarkup = keyboardMarkup
            )

        }
        command("anekdot"){
            bot.sendMessage(
                chatId = chatId,
                text = "Появился как-то в Зоне чёрный сталкер."+
                        "В лагере ночью повидался ходить и там суёт значит руку в палатку и говорит:" +
                        "Водички! Воды!\" А если не даш фляжку или из палатки полезешь - пришибёт!"+
                        " И вот решил один сталкер поприкалываться:" +
                        "вышел ночью из палатки, надел кожаную перчатку и пошёл к соседям."+
                        " Жалобным голосом: \"Водички! Водички попить!\" "+
                        "Тут из этой же палатки высовывается рука, хватает того за горло и сиплый голосок отзывается тихонько:"+
                        " \"А тебе моя водичка зачем нужна?!\""
            )
        }
    }

}