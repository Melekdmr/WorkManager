package com.melekdmr.kotlinworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data=Data.Builder().putInt("intKey",1).build()

        //koşuk belirtme
        val constraints= Constraints.Builder()
         //   .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false) //şarja bağlı olmadan da çalış
            .build()

       /*  val myWorkRequest:WorkRequest= OneTimeWorkRequestBuilder<RefreshDatabase>()
             .setConstraints(constraints)
             .setInputData(data)
             //.setInitialDelay(5,TimeUnit.HOURS)//5 saat sonra başlat
             //.addTag("myTag")//birden fazla workReqest olabilir bunları ayrıştırmak için tag atayabiliriz,kendi zaten id atıyor
             .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)*/

        val  myworkRequest:WorkRequest= PeriodicWorkRequestBuilder<RefreshDatabase>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(myworkRequest)
        //aktivite yaşam döngüsünde gözlemle
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myworkRequest.id).observe(this,
            Observer {
                if (it.state==WorkInfo.State.RUNNING){
                    println("Running")
                } else if(it.state==WorkInfo.State.FAILED){
                    println("failed")
                }else if(it.state==WorkInfo.State.SUCCEEDED){
                    println("succeeded")
                }

            })

        // yaptığımız işlemi iptal etmek istiyorsak(id ye veya etikete göre de iptal edebiliriz
      // WorkManager.getInstance(this).cancelAllWork()

        //Zincirleme :Chaining

       /* val oneTimeRequest:OneTimeWorkRequest= OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        /* ontimerequestle başla ardından yine ontimerequest ,onetimerequesti arka arkaya çalıştır yap
        başka bir request oluşturmadığımı için sadece onetimrequest kullandık
         */
        WorkManager.getInstance(this).beginWith(oneTimeRequest)
            .then(oneTimeRequest)
            .then(oneTimeRequest)
            .enqueue()*/
    }
}