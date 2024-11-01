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
    val time: String,
    val lessons: List<LessonDTO>
)

@Serializable
data class LessonDTO(
    val lessonName: String,
    val teachers: List<String>,
    val cabinets: List<String>
)

@Serializable
data class LessonTime(val id: Long, val name: String, val times: List<String>)

@Serializable
data class AppInfo(
    val lastVersion: String,
    val versionChanges: List<String>
)
