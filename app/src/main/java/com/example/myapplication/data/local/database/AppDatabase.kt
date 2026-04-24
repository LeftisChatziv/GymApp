package com.example.myapplication.data.local.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.local.converter.MuscleConverters
import com.example.myapplication.data.local.dao.ExerciseDao
import com.example.myapplication.data.local.dao.ProgramDao
import com.example.myapplication.data.local.dao.WorkoutHistoryDao
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

                            // ⚠️ IMPORTANT: μην ξανακαλείς getDatabase εδώ
                            INSTANCE?.let { database ->

                                CoroutineScope(Dispatchers.IO).launch {

                                    val programDao = database.programDao()
                                    val exerciseDao = database.exerciseDao()

                                    // ✅ PROGRAMS
                                    programDao.insertProgram(
                                        Program(name = "Beginner", difficulty = "Easy")
                                    )

                                    programDao.insertProgram(
                                        Program(name = "Advanced", difficulty = "Hard")
                                    )

                                    // ✅ EXERCISES
                                    val exercises = listOf(

                                        // 🏠 ΣΩΜΑ (ΣΠΙΤΙ / BODYWEIGHT)
                                        Exercise(
                                            name = "Push-ups",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Στήθος", 60),
                                                MuscleActivation("Τρικέφαλα", 25),
                                                MuscleActivation("Ώμοι", 15)
                                            ),
                                            description = "Κλασικές κάμψεις",
                                            programId = 1
                                        ),

                                        Exercise(
                                            name = "Squats",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Πόδια", 70),
                                                MuscleActivation("Γλουτοί", 25),
                                                MuscleActivation("Κορμός", 5)
                                            ),
                                            description = "Καθίσματα σώματος",
                                            programId = 1
                                        ),

                                        Exercise(
                                            name = "Plank",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Κορμός", 80),
                                                MuscleActivation("Ώμοι", 10),
                                                MuscleActivation("Πλάτη", 10)
                                            ),
                                            description = "Στατική άσκηση κορμού",
                                            programId = 1
                                        ),

                                        Exercise(
                                            name = "Lunges",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Πόδια", 65),
                                                MuscleActivation("Γλουτοί", 30),
                                                MuscleActivation("Κορμός", 5)
                                            ),
                                            description = "Προβολές",
                                            programId = 1
                                        ),

                                        Exercise(
                                            name = "Burpees",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Όλο το σώμα", 100)
                                            ),
                                            description = "Full body cardio",
                                            programId = 1
                                        ),

                                        Exercise(
                                            name = "Mountain Climbers",
                                            category = "Σώμα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Κορμός", 50),
                                                MuscleActivation("Πόδια", 30),
                                                MuscleActivation("Ώμοι", 20)
                                            ),
                                            description = "Cardio core exercise",
                                            programId = 1
                                        ),

                                        // 🏋️ ΒΑΡΑΚΙΑ / DUMBBELLS / BARBELL
                                        Exercise(
                                            name = "Bench Press",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("Στήθος", 70),
                                                MuscleActivation("Τρικέφαλα", 20),
                                                MuscleActivation("Ώμοι", 10)
                                            ),
                                            description = "Πιέσεις πάγκου",
                                            programId = 2
                                        ),

                                        Exercise(
                                            name = "Dumbbell Curl",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("Δικέφαλα", 90),
                                                MuscleActivation("Πήχεις", 10)
                                            ),
                                            description = "Κάμψεις δικεφάλων",
                                            programId = 2
                                        ),

                                        Exercise(
                                            name = "Shoulder Press",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("Ώμοι", 70),
                                                MuscleActivation("Τρικέφαλα", 20),
                                                MuscleActivation("Κορμός", 10)
                                            ),
                                            description = "Πιέσεις ώμων",
                                            programId = 2
                                        ),

                                        Exercise(
                                            name = "Deadlift",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("Πλάτη", 50),
                                                MuscleActivation("Πόδια", 30),
                                                MuscleActivation("Κορμός", 20)
                                            ),
                                            description = "Άρση θανάτου",
                                            programId = 2
                                        ),

                                        Exercise(
                                            name = "Barbell Row",
                                            category = "Βαράκια",
                                            muscleGroups = listOf(
                                                MuscleActivation("Πλάτη", 70),
                                                MuscleActivation("Δικέφαλα", 20),
                                                MuscleActivation("Κορμός", 10)
                                            ),
                                            description = "Κωπηλατική",
                                            programId = 2
                                        ),

                                        // 🏋️ ΟΡΓΑΝΑ / MACHINES
                                        Exercise(
                                            name = "Lat Pulldown",
                                            category = "Όργανα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Πλάτη", 70),
                                                MuscleActivation("Δικέφαλα", 20),
                                                MuscleActivation("Ώμοι", 10)
                                            ),
                                            description = "Έλξεις στο μηχάνημα",
                                            programId = 2
                                        ),

                                        Exercise(
                                            name = "Leg Press",
                                            category = "Όργανα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Πόδια", 80),
                                                MuscleActivation("Γλουτοί", 20)
                                            ),
                                            description = "Πρέσα ποδιών",
                                            programId = 2
                                        ),

                                        Exercise(
                                            name = "Chest Fly Machine",
                                            category = "Όργανα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Στήθος", 85),
                                                MuscleActivation("Ώμοι", 15)
                                            ),
                                            description = "Άνοιγμα στήθους",
                                            programId = 2
                                        ),

                                        Exercise(
                                            name = "Cable Row",
                                            category = "Όργανα",
                                            muscleGroups = listOf(
                                                MuscleActivation("Πλάτη", 75),
                                                MuscleActivation("Δικέφαλα", 25)
                                            ),
                                            description = "Κωπηλατική με τροχαλία",
                                            programId = 2
                                        )
                                    )
                                    exerciseDao.insertAll(exercises)
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