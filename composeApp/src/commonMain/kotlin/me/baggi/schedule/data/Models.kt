package me.baggi.schedule.data

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class FacultyDTO(val id: Long, val name: String)

@Serializable
data class GroupDTO(val id: Long, val name: String)

@Serializable
data class ScheduleDayDTO(
    val id: Long,
    val intervalId: Long,
    val time: LocalDateTime,
    val lessons: List<LessonDTO>
)

@Serializable
data class LessonDTO(val id: Long, val subject: String, val teacher: String)

@Serializable
data class LessonTime(val id: Long, val name: String, val times: List<String>)

