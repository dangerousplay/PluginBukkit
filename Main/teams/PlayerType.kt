package Main.teams

enum class PlayerType constructor(chat: String) {
    Assasino("§4[A]§r"), Detetive("§1[D]§r"), Vitima("§4[V]§r");

    val chats = chat

    fun getChat() : String = chats

}
