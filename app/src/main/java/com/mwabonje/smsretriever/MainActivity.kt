package com.mwabonje.smsretriever

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mwabonje.smsretriever.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SmsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SmsAdapter(mutableListOf())
        binding.rvSms.adapter = adapter

        if (!hasReadSmsPermission()) {
            requestReadSmsPermission()
            return
        }

        readSms()
    }

    private fun readSms() {
        val numberCol = Telephony.TextBasedSmsColumns.ADDRESS
        val textCol = Telephony.TextBasedSmsColumns.BODY
        val typeCol = Telephony.TextBasedSmsColumns.TYPE

        val projection = arrayOf(numberCol, textCol, typeCol)

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val numberColIdx = cursor!!.getColumnIndex(numberCol)
        val textColIdx = cursor.getColumnIndex(textCol)

        val list = mutableListOf<String>()

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)

            if (number.equals("MPESA")) {
                list.add(text)
            }
        }

        binding.progressCircular.visibility = View.GONE
        binding.rvSms.visibility = View.VISIBLE
        adapter.setSmsList(list)
        cursor.close()
    }

    /**
     * Runtime permission shenanigans
     */
    private fun hasReadSmsPermission() = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED

    private fun requestReadSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_SMS)
        ) {
            Log.d("MainActivity",
                "shouldShowRequestPermissionRationale(), no permission requested")
            return
        }
        ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
            123)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                readSms()
            } else {
                finish()
            }
            return
        }
    }
}