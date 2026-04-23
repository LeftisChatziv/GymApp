package com.example.myapplication.data.local.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.local.converter.MuscleConverters
import com.example.myapplication.data.local.dao.ExerciseDao
import com.example.myapplication.data.local.dao.ProgramDao
import com.example.myapplication.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Program::class,
        Exercise::class,
        WorkoutHistory::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MuscleConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun programDao(): ProgramDao
    abstract fun exerciseDao(): ExerciseDao

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

                                val exerciseDao = database.exerciseDao()
                                val programDao = database.programDao()

                                // ✅ PROGRAMS (πρέπει να μπουν πρώτα)
                                programDao.insertProgram(
                                    Program(id = 1, name = "Beginner", difficulty = "Easy")
                                )

                                programDao.insertProgram(
                                    Program(id = 2, name = "Advanced", difficulty = "Hard")
                                )

                                // ✅ EXERCISES (ΣΩΣΤΟ INSERT)
                                val exercises = listOf(

                                    Exercise(
                                        name = "Push-ups",
                                        muscleGroups = listOf(
                                            MuscleActivation("Στήθος", 60),
                                            MuscleActivation("Τρικέφαλα", 25),
                                            MuscleActivation("Ώμοι", 15)
                                        ),
                                        description = "Κλασικές κάμψεις σώματος",
                                        programId = 1
                                    ),

                                    Exercise(
                                        name = "Squats",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πόδια", 70),
                                            MuscleActivation("Γλουτοί", 25),
                                            MuscleActivation("Κορμός", 5)
                                        ),
                                        description = "Καθίσματα για πόδια",
                                        programId = 1
                                    ),

                                    Exercise(
                                        name = "Lunges",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πόδια", 65),
                                            MuscleActivation("Γλουτοί", 30),
                                            MuscleActivation("Κορμός", 5)
                                        ),
                                        description = "Προβολές",
                                        programId = 1
                                    ),

                                    Exercise(
                                        name = "Plank",
                                        muscleGroups = listOf(
                                            MuscleActivation("Κορμός", 80),
                                            MuscleActivation("Ώμοι", 10),
                                            MuscleActivation("Πλάτη", 10)
                                        ),
                                        description = "Στατική άσκηση κορμού",
                                        programId = 1
                                    ),

                                    Exercise(
                                        name = "Glute Bridge",
                                        muscleGroups = listOf(
                                            MuscleActivation("Γλουτοί", 70),
                                            MuscleActivation("Πόδια", 20),
                                            MuscleActivation("Κορμός", 10)
                                        ),
                                        description = "Ενεργοποίηση γλουτών",
                                        programId = 1
                                    ),

                                    Exercise(
                                        name = "Pull-ups",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πλάτη", 65),
                                            MuscleActivation("Δικέφαλα", 25),
                                            MuscleActivation("Ώμοι", 10)
                                        ),
                                        description = "Έλξεις",
                                        programId = 2
                                    ),

                                    Exercise(
                                        name = "Bench Press",
                                        muscleGroups = listOf(
                                            MuscleActivation("Στήθος", 70),
                                            MuscleActivation("Τρικέφαλα", 20),
                                            MuscleActivation("Ώμοι", 10)
                                        ),
                                        description = "Πιέσεις πάγκου",
                                        programId = 2
                                    ),

                                    Exercise(
                                        name = "Deadlift",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πλάτη", 50),
                                            MuscleActivation("Πόδια", 30),
                                            MuscleActivation("Κορμός", 20)
                                        ),
                                        description = "Άρση θανάτου",
                                        programId = 2
                                    ),

                                    Exercise(
                                        name = "Shoulder Press",
                                        muscleGroups = listOf(
                                            MuscleActivation("Ώμοι", 70),
                                            MuscleActivation("Τρικέφαλα", 20),
                                            MuscleActivation("Κορμός", 10)
                                        ),
                                        description = "Πιέσεις ώμων",
                                        programId = 2
                                    ),

                                    Exercise(
                                        name = "Barbell Row",
                                        muscleGroups = listOf(
                                            MuscleActivation("Πλάτη", 70),
                                            MuscleActivation("Δικέφαλα", 20),
                                            MuscleActivation("Κορμός", 10)
                                        ),
                                        description = "Κωπηλατική",
                                        programId = 2
                                    )
                                )

                                exerciseDao.insertAll(exercises)
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