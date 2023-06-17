package org.d3if3118.hitungbmr.network

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.d3if3118.hitungbmr.MainActivity
import org.d3if3118.hitungbmr.R

class UpdateWorker(
    context: Context, workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val notificationId = 44

    override fun doWork(): Result {
        Log.d("Worker", "Menjalankan proses background..")

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val builder = NotificationCompat.Builder(applicationContext, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(applicationContext.getString(R.string.notif_title))
            .setContentText(applicationContext.getString( R.string.notif_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val manager = NotificationManagerCompat.from(applicationContext)
        manager.notify(notificationId, builder.build())

        return Result.success()
    }
}