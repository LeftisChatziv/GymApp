package com.example.myapplication.notification

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.R
import java.util.Calendar
import kotlin.random.Random

class WorkoutReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val motivationMessages = arrayOf(
            "💪 Ώρα για προπόνηση! Γίνε καλύτερος από χθες!",
            "🔥 Μην τα παρατάς! Το σώμα σου σε ευχαριστεί!",
            "🏋️ Κάθε προπόνηση σε φέρνει πιο κοντά στον στόχο σου!",
            "⚡ Σήκω και κάνε το workout σου τώρα!",
            "🚀 Discipline beats motivation! Πάμε γυμναστήριο!"
        )

        val message = motivationMessages[Random.nextInt(motivationMessages.size)]

        sendNotification(context, "🏋️ Fitness Reminder", message)
    }

    companion object {

        private const val CHANNEL_ID = "fitness_channel"

        // ================= LOGIN NOTIFICATION =================
        fun sendLoginNotification(context: Context) {
            sendNotification(
                context,
                "👋 Καλώς ήρθες!",
                "Συνδέθηκες επιτυχώς στην εφαρμογή 💪"
            )
        }

        // ================= GENERAL NOTIFICATION =================
        private fun sendNotification(
            context: Context,
            title: String,
            message: String
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Fitness Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Workout & Login Notifications"
                }

                val manager = context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

                manager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }

            NotificationManagerCompat.from(context)
                .notify(System.currentTimeMillis().toInt(), builder.build())
        }

        // ================= ALARMS =================
        fun setWorkoutReminders(context: Context) {

            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // ===== 08:00 =====
            val morningIntent = Intent(context, WorkoutReminderReceiver::class.java)

            val morningPendingIntent = PendingIntent.getBroadcast(
                context,
                100,
                morningIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val morningCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            if (morningCalendar.before(Calendar.getInstance())) {
                morningCalendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                morningCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                morningPendingIntent
            )

            // ===== 18:00 =====
            val eveningIntent = Intent(context, WorkoutReminderReceiver::class.java)

            val eveningPendingIntent = PendingIntent.getBroadcast(
                context,
                200,
                eveningIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val eveningCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 18)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            if (eveningCalendar.before(Calendar.getInstance())) {
                eveningCalendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                eveningCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                eveningPendingIntent
            )
        }
    }
}