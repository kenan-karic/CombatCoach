package io.aethibo.combatcoach.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.aethibo.combatcoach.shared.combo.data.dao.ComboDao
import io.aethibo.combatcoach.shared.combo.data.dao.entity.ComboEntity
import io.aethibo.combatcoach.shared.log.data.dao.WorkoutLogDao
import io.aethibo.combatcoach.shared.log.data.dao.entity.WorkoutLogEntity
import io.aethibo.combatcoach.shared.plan.data.dao.ActivePlanDao
import io.aethibo.combatcoach.shared.plan.data.dao.PlanDao
import io.aethibo.combatcoach.shared.plan.data.dao.entity.ActivePlanEntity
import io.aethibo.combatcoach.shared.plan.data.dao.entity.PlanEntity
import io.aethibo.combatcoach.shared.user.data.dao.entity.UserPrefsEntity
import io.aethibo.combatcoach.shared.workout.data.dao.WorkoutDao
import io.aethibo.combatcoach.shared.workout.data.dao.entity.WorkoutEntity

@Database(
    entities = [
        WorkoutEntity::class,
        ComboEntity::class,
        PlanEntity::class,
        ActivePlanEntity::class,
        WorkoutLogEntity::class,
//        AchievementEntity::class,
        UserPrefsEntity::class,

    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class CombatCoachDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun comboDao(): ComboDao
    abstract fun planDao(): PlanDao
    abstract fun activePlanDao(): ActivePlanDao
    abstract fun workoutLogDao(): WorkoutLogDao

    //    abstract fun achievementDao(): AchievementDao
    abstract fun userPrefsDao(): io.aethibo.combatcoach.shared.user.data.dao.UserPrefsDao

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
