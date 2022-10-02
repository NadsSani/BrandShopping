package com.nads.shopping.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.PermissionToken
import com.nads.shopping.R
import com.nads.shopping.databinding.PermissionDialogBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

//import com.heronhats.thozhilaliuser.listeners.LiveEvent

/*fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}*/

fun View.show() {
    if (this.visibility == View.GONE || this.visibility == View.INVISIBLE)
        this.visibility = View.VISIBLE
}

fun View.hide() {
    if (this.visibility == View.VISIBLE || this.visibility == View.INVISIBLE)
        this.visibility = View.GONE
}

fun View.invisible() {
    if (this.visibility == View.VISIBLE)
        this.visibility = View.INVISIBLE
}

fun Context.getToken(): String {
    val token: String? = defaultPrefs(this)[PKEY_TOKEN, ""]
    return token.toString()
}

fun Context.getDp(): String {
    val dp: String? = defaultPrefs(this)[PKEY_DP, ""]
    return dp.toString()
}


fun String.createRequestBody(): RequestBody? {
    return getRequestBody("text/plain", this)
}

private fun getRequestBody(type: String, content: String): RequestBody {
    return content.toRequestBody(type.toMediaTypeOrNull())
//            return RequestBody.create(MediaType.parse(type), content!!)
}

fun Uri.getFile(context: Context): File {
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(this, "r", null)
    val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
    val file = File(context.cacheDir, context.contentResolver.getFileName(this))
    val outputStream = FileOutputStream(file)
    outputStream.use { fileOut ->
        inputStream.copyTo(fileOut)
    }
    return file
}

fun ContentResolver.getFileName(fileUri: Uri): String {

    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }

    return name
}

fun Bitmap.getImageUri(context: Activity): Uri {
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            this,
            Calendar.getInstance().time.toString(),
            null
        )
    return Uri.parse(path)
//    return FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", createImageFile())
}

fun Uri.getRealPath(activity: Activity): String {
    val cursor: Cursor? =
        activity.contentResolver.query(this, null, null, null, null)
    cursor?.moveToFirst()
    val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    return cursor?.getString(idx!!).toString()
}

fun getRequest(
    paramName: String, file: File?
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(paramName, file?.name, imageData(file!!))
}

fun imageData(file: File): RequestBody {
    return getImageBody("multipart/form-data", file)
}

fun getImageBody(type: String, file: File): RequestBody {
    return file.asRequestBody(type.toMediaTypeOrNull())
//            return RequestBody.create(MediaType.parse("df"), file)
}

fun View.createSnack(text: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, text, length).setAnchorView(this).show()
}

@SuppressLint("ClickableViewAccessibility")
fun Context.permissionRationaleDialog(token: PermissionToken?, title: String, details: String) {
    var alertDialog: AlertDialog? = null
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    val dialogBinding: PermissionDialogBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.permission_dialog,
            null,
            false
        )
    val dialogView: View = dialogBinding.root
//    dialogBinding.lifecycleOwner = this

    dialogBinding.tvTitle.text = title
    dialogBinding.tvName.text = details

    dialogBinding.btnOk.setOnClickListener {
        alertDialog?.dismiss()
        token?.continuePermissionRequest()
    }
    dialogBinding.btnDismiss.setOnClickListener {
        alertDialog?.dismiss()
    }

    builder.setView(dialogView)
    alertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    alertDialog.show()
}

fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
    var drawable: Drawable? = ContextCompat.getDrawable(this, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable?.intrinsicWidth ?: 0,
        drawable?.intrinsicHeight ?: 0, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap
}

fun Int.getPixels(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}


fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false

    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(format, Locale.UK)
            setText(sdf.format(myCalendar.time))
        }

    setOnClickListener {
        DatePickerDialog(
            context, datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).run {
            maxDate?.time?.also { datePicker.maxDate = it }
            show()
        }
    }
}

private fun Context.getLanguage(): String {
    val lang: String? = defaultPrefs(this)[PREF_KEY_SELECTED_LANGUAGE, "en"]
    return lang.toString()

}

fun Context.isEnglish(): Boolean {
    return this.getLanguage() == "en"
}

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label",text)
    clipboard.setPrimaryClip(clip)
}