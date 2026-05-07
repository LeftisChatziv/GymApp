package com.example.myapplication.data.local.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.local.converter.MuscleConverters
import com.example.myapplication.data.local.dao.*
import com.example.myapplication.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Program::class,
        Exercise::class,
        ProgramExerciseCrossRef::class,
        WorkoutHistory::class,
        UserStatsEntity::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(MuscleConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun programDao(): ProgramDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programExerciseDao(): ProgramExerciseDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun userStatsDao(): UserStatsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)

                            INSTANCE?.let { database ->

                                CoroutineScope(Dispatchers.IO).launch {

                                    val exerciseDao = database.exerciseDao()

                                    // ✔ SAFE CHECK
                                    val existing = exerciseDao.getAllExercises()
                                    if (existing.isNotEmpty()) return@launch

                                    val exercises = listOf(

                                        // ================= BODYWEIGHT =================

                                        Exercise(
                                            name = "Push-ups",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("στήθος", 60),
                                                MuscleActivation("τρικέφαλα", 25),
                                                MuscleActivation("ώμοι", 15)
                                            ),
                                            description = "Κλασικές κάμψεις",
                                            difficulty = "Easy"
                                        ),

                                        Exercise(
                                            name = "Plank",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("κορμός", 80),
                                                MuscleActivation("ώμοι", 10),
                                                MuscleActivation("πλάτη", 10)
                                            ),
                                            description = "Σανίδα",
                                            difficulty = "Easy"
                                        ),

                                        Exercise(
                                            name = "Hollow Body",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("κορμός", 90),
                                                MuscleActivation("πόδια", 10)
                                            ),
                                            description = "Hollow body hold",
                                            difficulty = "Easy"
                                        ),

                                        Exercise(
                                            name = "Glute Bridge",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("γλουτοί", 70),
                                                MuscleActivation("πόδια", 20),
                                                MuscleActivation("κορμός", 10)
                                            ),
                                            description = "Γέφυρα γλουτών",
                                            difficulty = "Easy"
                                        ),

                                        Exercise(
                                            name = "Superman",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("πλάτη", 70),
                                                MuscleActivation("κορμός", 30)
                                            ),
                                            description = "Άσκηση πλάτης",
                                            difficulty = "Easy"
                                        ),

                                        Exercise(
                                            name = "Burpees",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("πόδια", 40),
                                                MuscleActivation("στήθος", 30),
                                                MuscleActivation("κορμός", 30)
                                            ),
                                            description = "Burpees",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Mountain Climbers",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("κορμός", 50),
                                                MuscleActivation("πόδια", 30),
                                                MuscleActivation("ώμοι", 20)
                                            ),
                                            description = "Mountain climbers",
                                            difficulty = "Easy"
                                        ),

                                        // ================= WEIGHTS =================

                                        Exercise(
                                            name = "Barbell Bench Press",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("στήθος", 70),
                                                MuscleActivation("τρικέφαλα", 20),
                                                MuscleActivation("ώμοι", 10)
                                            ),
                                            description = "Bench press",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Barbell Back Squat",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("πόδια", 70),
                                                MuscleActivation("γλουτοί", 20),
                                                MuscleActivation("κορμός", 10)
                                            ),
                                            description = "Back squat",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Front Squat",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("πόδια", 75),
                                                MuscleActivation("κορμός", 15),
                                                MuscleActivation("γλουτοί", 10)
                                            ),
                                            description = "Front squat",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Deadlift",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("πλάτη", 50),
                                                MuscleActivation("πόδια", 30),
                                                MuscleActivation("κορμός", 20)
                                            ),
                                            description = "Deadlift",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Standing Overhead Press",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("ώμοι", 70),
                                                MuscleActivation("τρικέφαλα", 20),
                                                MuscleActivation("κορμός", 10)
                                            ),
                                            description = "Shoulder press",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Arnold Press",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("ώμοι", 75),
                                                MuscleActivation("τρικέφαλα", 15),
                                                MuscleActivation("κορμός", 10)
                                            ),
                                            description = "Arnold press",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Lateral Raise",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("ώμοι", 90),
                                                MuscleActivation("τραπεζοειδής", 10)
                                            ),
                                            description = "Lateral raise",
                                            difficulty = "Easy"
                                        ),

                                        Exercise(
                                            name = "Bicep Curl",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("δικέφαλα", 90),
                                                MuscleActivation("πήχεις", 10)
                                            ),
                                            description = "Bicep curls",
                                            difficulty = "Easy"
                                        ),

                                        Exercise(
                                            name = "Triceps Extension",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("τρικέφαλα", 90),
                                                MuscleActivation("ώμοι", 10)
                                            ),
                                            description = "Triceps extensions",
                                            difficulty = "Easy"
                                        ),

                                        // ================= MACHINES =================

                                        Exercise(
                                            name = "Pull-ups",
                                            category = "Όργανα",
                                            muscleGroups = listOf(
                                                MuscleActivation("πλάτη", 65),
                                                MuscleActivation("δικέφαλα", 25),
                                                MuscleActivation("ώμοι", 10)
                                            ),
                                            description = "Pull-ups",
                                            difficulty = "Hard"
                                        ),

                                        Exercise(
                                            name = "Lat Pulldown",
                                            category = "Όργανα",
                                            muscleGroups = listOf(
                                                MuscleActivation("πλάτη", 75),
                                                MuscleActivation("δικέφαλα", 25)
                                            ),
                                            description = "Lat pulldown",
                                            difficulty = "Easy"
                                        )
                                    )

                                    exerciseDao.insertAll(exercises)

                                    database.userStatsDao().insertOrUpdate(
                                        UserStatsEntity(
                                            uid = "demo",
                                            score = 0.0,
                                            streak = 0,
                                            totalWorkouts = 0,
                                            lastWorkoutDate = 0L
                                        )
                                    )
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}