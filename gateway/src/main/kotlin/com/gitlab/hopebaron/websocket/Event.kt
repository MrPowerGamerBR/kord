package com.gitlab.hopebaron.websocket

import com.gitlab.hopebaron.websocket.entity.*
import kotlinx.serialization.*
import kotlinx.serialization.internal.BooleanDescriptor
import kotlinx.serialization.internal.LongDescriptor


@Serializable
sealed class Event

object HeartbeatACK : Event()
object Reconnect : Event()
@Serializable
data class Hello(
        @SerialName("heartbeat_interval")
        val heartbeatInterval: Long,
        @SerialName("_trace")
        val traces: List<String>
) : Event()

@Serializable
data class Ready(
        @SerialName("v")
        val version: Int,
        val user: User,
        @SerialName("private_channels")
        val privateChannels: List<Channel>, //TODO("Add DM Channel.")
        val guilds: List<UnavailableGuild>,
        @SerialName("session_id")
        val sessionId: String,
        @SerialName("_trace")
        val traces: List<String>,
        val shard: Shard?
) : Event()

@Serializable
data class Heartbeat(val data: Long) : Event() {
    @Serializer(Heartbeat::class)
    companion object : DeserializationStrategy<Heartbeat> {
        override val descriptor: SerialDescriptor
            get() = LongDescriptor.withName("HeartbeatEvent")
        override fun deserialize(decoder: Decoder) = Heartbeat(decoder.decodeLong())
    }
}

@Serializable
data class Resumed(
        @SerialName("_traces")
        val traces: List<String>
) : Event()

@Serializable
data class InvalidSession(val resumable: Boolean) : Event() {
    @Serializer(InvalidSession::class)
    companion object : DeserializationStrategy<InvalidSession> {
        override val descriptor: SerialDescriptor
            get() = BooleanDescriptor.withName("InvalidSession")
        override fun deserialize(decoder: Decoder) = InvalidSession(decoder.decodeBoolean())
    }
}


data class ChannelCreate(val channel: Channel) : Event()
data class ChannelUpdate(val channel: Channel) : Event()
data class ChannelDelete(val channel: Channel) : Event()
data class ChannelPinsUpdate(val pins: PinsUpdateData) : Event()

data class TypingStart(val data: Typing) : Event()
data class GuildCreate(val guild: Guild) : Event()
data class GuildUpdate(val guild: Guild) : Event()
data class GuildDelete(val guild: UnavailableGuild) : Event()
data class GuildBanAdd(val ban: GuildBan) : Event()
data class GuildBanRemove(val ban: GuildBan) : Event()
data class GuildEmojisUpdate(val emoji: UpdatedEmojis) : Event()
data class GuildIntegrationsUpdate(val integrations: GuildIntegrations) : Event()
data class GuildMemberAdd(val member: AddedGuildMember) : Event()
data class GuildMemberRemove(val member: RemovedGuildMember) : Event()
data class GuildMemberUpdate(val member: UpdatedGuildMember) : Event()
data class GuildRoleCreate(val role: GuildRole) : Event()
data class GuildRoleUpdate(val role: GuildRole) : Event()
data class GuildRoleDelete(val role: DeletedGuildRole) : Event()
data class GuildMembersChunk(val data: GuildMembersChunkData) : Event()

data class MessageCreate(val message: Message) : Event()
data class MessageUpdate(val message: Message) : Event()
data class MessageDelete(val message: DeletedMessage) : Event()
data class MessageDeleteBulk(val messageBulk: BulkDeleteData) : Event()
data class MessageReactionAdd(val reaction: MessageReaction) : Event()
data class MessageReactionRemove(val reaction: MessageReaction) : Event()
data class MessageReactionRemoveAll(val reactions: AllRemovedMessageReactions) : Event()

data class PresenceUpdate(val presence: PresenceUpdateData) : Event()
data class UserUpdate(val user: User) : Event()
data class VoiceStateUpdate(val voiceState: VoiceState) : Event()
data class VoiceServerUpdate(val voiceServerUpdateData: VoiceServerUpdateData) : Event()
data class WebhooksUpdate(val webhooksUpdateData: WebhooksUpdateData) : Event()
