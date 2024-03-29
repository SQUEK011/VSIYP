package com.example.vsiyp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vsiyp.ui.mediaeditor.VideoClipsActivity
import com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.CLIPS_VIEW_TYPE

class CameraActivity : AppCompatActivity() {
    private val VIDEO_CAPTURE = 101
    private val VIEW_CAMERA = 2
    private val TAG = "CameraActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera(){
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_CAPTURE)
    }

   private fun getRealPathFromURI(uri: Uri) : String {
       var path = ""
       if (contentResolver != null){
           val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
           if (cursor != null){
               cursor.moveToFirst()
               val idx: Int = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
               path = cursor.getString(idx)
               cursor.close()
           }
       }
       return path
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val videoUri = data?.data

        if (requestCode == VIDEO_CAPTURE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(this, "Video saved to:\n"
                            + videoUri, Toast.LENGTH_LONG).show()
                    val editIntent = Intent(this, VideoClipsActivity::class.java)

                    val filePath = videoUri?.let { getRealPathFromURI(it) }
                    Log.d(TAG,filePath.toString())
                    editIntent.putExtra("videoPath",filePath)
                    editIntent.putExtra(CLIPS_VIEW_TYPE, VIEW_CAMERA)
                    startActivity(editIntent)
                    finish()

                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show()
                    val returnIntent = Intent(this, MainActivity::class.java)
                    startActivity(returnIntent)
                    finish()

                }
                else -> {
                    Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show()
                    val returnIntent = Intent(this, MainActivity::class.java)
                    startActivity(returnIntent)
                    finish()

                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}