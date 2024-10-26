package me.baggi.schedule.data

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

    suspend fun getLessonTimes(): List<LessonTime> {
        return ApiClient.get("schedule/times")
    }
}
