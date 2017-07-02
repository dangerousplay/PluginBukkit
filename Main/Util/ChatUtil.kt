package Main.Util


import net.minecraft.server.v1_12_R1.*
import org.bukkit.inventory.ItemStack

import java.util.ArrayList

object ChatUtil {

    fun chatHover(texto: String, msgHover: String): IChatBaseComponent {
        val chatB = ChatMessage(texto)
        chatB.chatModifier = ChatModifier()
        chatB.chatModifier.setChatHoverable(ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT,ChatMessage(msgHover)))
        val chatBClick = IChatBaseComponent.ChatSerializer.a(chatB)
        val chat = IChatBaseComponent.ChatSerializer.a("" + chatBClick)
        return chat!!
    }

    fun chatHover(Inicio: String, Fim: String, texto: String, msgHover: String): IChatBaseComponent {
        val chatB = ChatMessage(texto)
        chatB.chatModifier = ChatModifier()
        chatB.chatModifier.setChatHoverable(ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, ChatMessage(msgHover)))
        val chatBClick = IChatBaseComponent.ChatSerializer.a(chatB)
        val chat = IChatBaseComponent.ChatSerializer.a(Inicio + chatBClick + Fim)
        return chat!!
    }

    fun chatComando(texto: String, msgHover: String, comando: String): IChatBaseComponent {
        val chatB = ChatMessage(texto)
        chatB.chatModifier = ChatModifier()
        chatB.chatModifier.setChatClickable(ChatClickable(ChatClickable.EnumClickAction.RUN_COMMAND, comando))
        chatB.chatModifier.setChatHoverable(ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, ChatMessage(msgHover)))
        val chatBClick = IChatBaseComponent.ChatSerializer.a(chatB)
        val chat = IChatBaseComponent.ChatSerializer.a(chatBClick)
        return chat!!
    }

    fun ShowItem(Item: ItemStack): IChatBaseComponent {

        var Lore: List<String> = ArrayList()

        if (Item.hasItemMeta() && Item.itemMeta.hasLore()) {
            Lore = Item.itemMeta.lore
        }

        val SB = StringBuffer()
        Lore.forEach { P -> SB.append(P).append("") }

        val chatB = ChatMessage("SHOW")
        chatB.chatModifier = ChatModifier()
        chatB.chatModifier.setChatHoverable(ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, ChatMessage(SB.toString())))
        val chatBClick = IChatBaseComponent.ChatSerializer.a(chatB)
        val chat = IChatBaseComponent.ChatSerializer.a(chatBClick)
        return chat!!
    }



}