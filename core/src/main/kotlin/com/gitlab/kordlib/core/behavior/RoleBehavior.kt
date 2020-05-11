package com.gitlab.kordlib.core.behavior

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.rest.builder.role.RoleModifyBuilder
import com.gitlab.kordlib.rest.builder.role.RolePositionsModifyBuilder
import com.gitlab.kordlib.core.cache.data.RoleData
import com.gitlab.kordlib.core.entity.Entity
import com.gitlab.kordlib.core.entity.Role
import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.indexOfFirstOrNull
import com.gitlab.kordlib.core.sorted
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import java.util.*


/**
 * The behavior of a [Discord Role](https://discordapp.com/developers/docs/topics/permissions#role-object) associated to a [guild].
 */
interface RoleBehavior : Entity {
    /**
     * The id of the guild this channel is associated to.
     */
    val guildId: Snowflake

    /**
     * The guild behavior this channel is associated to.
     */
    val guild: GuildBehavior get() = GuildBehavior(guildId, kord)

    /**
     * The raw mention of this entity.
     */
    val mention: String get() = "<@&${id.value}>"

    /**
     * Requests to change the [position] of this role.
     *
     * This request will execute regardless of the consumption of the return value.
     *
     * @return The roles in of this [guild] in updated order
     */
    suspend fun changePosition(position: Int): Flow<Role> {
        val response = kord.rest.guild.modifyGuildRolePosition(guildId.value) {
            move(id to position)
        }
        return response.asFlow().map { RoleData.from(guildId.value, it) }.map { Role(it, kord) }.sorted()
    }

    /**
     * Requests to get the position of this role in the role list of this guild.
     */
    suspend fun getPosition(): Int = guild.roles.sorted().indexOfFirstOrNull { it.id == id }!!

    /**
     * Requests to delete this role.
     */
    suspend fun delete() {
        kord.rest.guild.deleteGuildRole(guildId = guildId.value, roleId = id.value)
    }


    companion object {
        internal operator fun invoke(guildId: Snowflake, id: Snowflake, kord: Kord): RoleBehavior = object : RoleBehavior {
            override val guildId: Snowflake = guildId
            override val id: Snowflake = id
            override val kord: Kord = kord

            override fun hashCode(): Int = Objects.hash(id, guildId)

            override fun equals(other: Any?): Boolean = when(other) {
                is RoleBehavior -> other.id == id && other.guildId == guildId
                else -> false
            }
        }
    }
}

/**
 * Requests to edit this role.
 *
 * @return The edited [Role].
 */
@Suppress("NAME_SHADOWING")
suspend inline fun RoleBehavior.edit(builder: RoleModifyBuilder.() -> Unit): Role {
    val response = kord.rest.guild.modifyGuildRole(guildId = guildId.value, roleId = id.value, builder = builder)
    val data = RoleData.from(id.value, response)

    return Role(data, kord)
}