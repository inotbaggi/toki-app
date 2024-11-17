package me.baggi.schedule.web

import io.ktor.client.statement.*
import me.baggi.schedule.data.*

object Repository {
    suspend fun getFaculties(): List<FacultyDTO> {
        return ApiClient.get("schedule/faculty/list")
    }

    suspend fun getGroups(facultyId: Long): List<GroupDTO> {
        return ApiClient.get("schedule/faculty/$facultyId/groups")
    }

    suspend fun getScheduleForGroup(groupId: Long): List<ScheduleDayDTO> {
        return ApiClient.get("schedule/group/$groupId")
    }

    suspend fun getScheduleToday(groupId: Long): ScheduleDayDTO? {
        return ApiClient.get("schedule/group/$groupId/today")
    }

    suspend fun getLessonTimes(): List<LessonTime>? {
        return ApiClient.get("schedule/times")
    }

    suspend fun getAppInfo(): AppInfo? {
        return ApiClient.get("app/info")
    }

    suspend fun getLastApplication(): HttpResponse? {
        return ApiClient.get("app/download")
    }
}