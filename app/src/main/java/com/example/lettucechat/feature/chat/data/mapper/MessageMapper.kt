package com.example.lettucechat.feature.chat.data.mapper

import com.example.lettucechat.feature.chat.data.model.MessageDto
import com.example.lettucechat.feature.chat.domain.model.Message

fun MessageDto.toDomain(id: String): Message {
    return Message(
        id = id,
        senderId = this.senderId,
        text = this.text,
        timestamp = this.timestamp
    )
}