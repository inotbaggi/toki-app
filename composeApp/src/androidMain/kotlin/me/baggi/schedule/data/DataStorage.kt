package me.baggi.schedule.data

object DataStorage {
    var metricParams = mutableMapOf<String, String>()
    var lessonPeriods = mapOf<Long, LessonTime>()
    var cachedWeek = mutableMapOf<Long, ScheduleDayDTO>()

    lateinit var appInfo: AppInfo
}