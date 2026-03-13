package io.aethibo.combatcoach.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.aethibo.combatcoach.features.shared.user.data.dao.UserPrefsDao
import io.aethibo.combatcoach.features.shared.user.data.dao.entity.UserPrefsEntity

@Database(
    entities = [
//        WorkoutEntity::class,
//        ComboEntity::class,
//        PlanEntity::class,
//        WorkoutLogEntity::class,
//        AchievementEntity::class,
        UserPrefsEntity::class,
//        ActivePlanEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class CombatCoachDatabase : RoomDatabase() {
//    abstract fun workoutDao(): WorkoutDao
//    abstract fun comboDao(): ComboDao
//    abstract fun planDao(): PlanDao
//    abstract fun workoutLogDao(): WorkoutLogDao
//    abstract fun achievementDao(): AchievementDao
    abstract fun userPrefsDao(): UserPrefsDao
//    abstract fun activePlanDao(): ActivePlanDao

    companion object {
        const val DATABASE_NAME = "combat_coach.db"
    }
}

fun buildDatabase(context: Context): CombatCoachDatabase =
    Room.databaseBuilder(
        context,
        CombatCoachDatabase::class.java,
        CombatCoachDatabase.DATABASE_NAME,
    )
        .fallbackToDestructiveMigrationOnDowngrade(false)
        .build()
