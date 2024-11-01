package me.baggi.schedule.data

object CacheRepository {
    var metricParams = mutableMapOf<String, String>()
    var lessonPeriods = mapOf<Long, LessonTime>()

    lateinit var appInfo: AppInfo
}