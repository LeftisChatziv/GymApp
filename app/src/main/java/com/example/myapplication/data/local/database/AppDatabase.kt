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
        WorkoutHistory::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(MuscleConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun programDao(): ProgramDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programExerciseDao(): ProgramExerciseDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao

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

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {

                                val database = INSTANCE ?: return@launch
                                val dao = database.exerciseDao()

                                val exercises = listOf(

                                    // 🏠 EASY (ΣΩΜΑ)
                                    Exercise(
                                        name = "Push-ups",
                                        category = "Σώμα",
                                        muscleGroups = listOf(
                                            MuscleActivation("Στήθος", 60),
                                            MuscleActivation("Τρικέφαλα", 25),
                                            MuscleActivation("Ώμοι", 15)
                                        ),
                                        description = "Κλασικές κάμψεις",
                                        difficulty = "Easy"
                                    ),

                                    Exercise(
                                        name = "Plank",
                                        category = "Σώμα",
                                        muscleGroups = listOf(
                                            MuscleActivation("Κορμός", 80),
                                            MuscleActivation("Ώμοι", 10),
                                            MuscleActivation("Πλάτη", 10)
                                        ),
                                        description = "Σανίδα",
                                        difficulty = "Easy"
                                    ),

                                    Exercise(
                                        name = "Hollow Body",
                                        category = "Σώμα",
                                        muscleGroups = listOf(
                                            MuscleActivation("Κορμός", 90),
                                            MuscleActivation("Πόδια", 10)
                                        ),
                                        description = "Hollowman",
                                        difficulty = "Easy"
                                    ),

                                    Exercise(
                                        name = "Glute Bridge",
                                        category = "Σώμα",
                                        muscleGroups = listOf(
                                            MuscleActivation("Γλουτοί", 70),
                                            MuscleActivation("Πόδια", 20),
                                            MuscleActivation("Κορμός", 10)
                                        ),
                                        description = "Γέφυρα",
                                        difficulty = "Easy"
                                    ),

                                    Exercise(
                                        name = "Superman",
                                        category = "Σώμα",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πλάτη", 70),
                                            MuscleActivation("Κορμός", 30)
                                        ),
                                        description = "Superman",
                                        difficulty = "Easy"
                                    ),

                                    // 🏋️ HARD (ΒΑΡΑΚΙΑ)
                                    Exercise(
                                        name = "Barbell Bench Press",
                                        category = "Βαράκια",
                                        muscleGroups = listOf(
                                            MuscleActivation("Στήθος", 70),
                                            MuscleActivation("Τρικέφαλα", 20),
                                            MuscleActivation("Ώμοι", 10)
                                        ),
                                        description = "Bench press",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Barbell Back Squat",
                                        category = "Βαράκια",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πόδια", 70),
                                            MuscleActivation("Γλουτοί", 20),
                                            MuscleActivation("Κορμός", 10)
                                        ),
                                        description = "Squat",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Front Squat",
                                        category = "Βαράκια",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πόδια", 75),
                                            MuscleActivation("Κορμός", 15),
                                            MuscleActivation("Γλουτοί", 10)
                                        ),
                                        description = "Front squat",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Deadlift",
                                        category = "Βαράκια",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πλάτη", 50),
                                            MuscleActivation("Πόδια", 30),
                                            MuscleActivation("Κορμός", 20)
                                        ),
                                        description = "Deadlift",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Standing Overhead Press",
                                        category = "Βαράκια",
                                        muscleGroups = listOf(
                                            MuscleActivation("Ώμοι", 70),
                                            MuscleActivation("Τρικέφαλα", 20),
                                            MuscleActivation("Κορμός", 10)
                                        ),
                                        description = "Shoulder press",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Arnold Press",
                                        category = "Βαράκια",
                                        muscleGroups = listOf(
                                            MuscleActivation("Ώμοι", 75),
                                            MuscleActivation("Τρικέφαλα", 15),
                                            MuscleActivation("Κορμός", 10)
                                        ),
                                        description = "Arnold press",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Lateral Raise",
                                        category = "Βαράκια",
                                        muscleGroups = listOf(
                                            MuscleActivation("Ώμοι", 90),
                                            MuscleActivation("Τραπεζοειδής", 10)
                                        ),
                                        description = "Lateral raise",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Pull-ups",
                                        category = "Όργανα",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πλάτη", 65),
                                            MuscleActivation("Δικέφαλα", 25),
                                            MuscleActivation("Ώμοι", 10)
                                        ),
                                        description = "Pull ups",
                                        difficulty = "Hard"
                                    ),

                                    Exercise(
                                        name = "Leg Press",
                                        category = "Όργανα",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πόδια", 80),
                                            MuscleActivation("Γλουτοί", 20)
                                        ),
                                        description = "Leg press",
                                        difficulty = "Hard"
                                    )
                                )

                                dao.insertAll(exercises)
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