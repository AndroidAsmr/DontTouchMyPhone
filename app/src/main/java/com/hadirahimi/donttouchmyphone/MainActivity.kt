package com.hadirahimi.donttouchmyphone

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.hadirahimi.donttouchmyphone.databinding.ActivityMainBinding
import kotlin.jvm.internal.Intrinsics.Kotlin

class MainActivity : AppCompatActivity(),SensorEventListener
{
    private lateinit var binding : ActivityMainBinding
    private lateinit var sensorManager : SensorManager
    private var acceleration = 0f
    private var accelerationCurrent = 0f
    private var accelerationLast = 0f
    private val threshold = 10.0f
    private lateinit var mediaPlayer : MediaPlayer
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setFullScreen
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //initMediaPlayer
        mediaPlayer = MediaPlayer.create(this,R.raw.alarmpolice)
        mediaPlayer.isLooping = true
        //initSensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI)
            
        }
    }
    
    override fun onSensorChanged(event : SensorEvent?)
    {
        val x = event?.values?.get(0) ?:0f
        val y = event?.values?.get(1) ?:0f
        val z = event?.values?.get(2) ?:0f
        
        accelerationLast = accelerationCurrent
        accelerationCurrent = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = kotlin.math.abs(accelerationCurrent - accelerationLast)
        acceleration = acceleration * 0.7f + delta
        
        if (acceleration > threshold)
        {
            binding.apply {
                tvMotionAlert.text = getString(R.string.motion_detected)
                tvMotionAlert.setTextColor(ActivityCompat.getColor(this@MainActivity,R.color.yellow))
                
                mediaPlayer.start()
                animationView.setAnimation(R.raw.danger)
                animationView.playAnimation()
            }
        }
        
    }
    
    override fun onAccuracyChanged(p0 : Sensor? , p1 : Int)
    {
    
    }
}









