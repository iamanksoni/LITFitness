package com.litmethod.android.shared

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.litmethod.android.R
import com.litmethod.android.utlis.ConnectionReceiver
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseActivity: AppCompatActivity(), ConnectionReceiver.ReceiverListener {
    var bManager: LocalBroadcastManager? = null
    lateinit var datePickerDialog:DatePickerDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 29) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //checking orientation
        if(resources.getBoolean(R.bool.portrait_only)){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        // create method
        bManager = LocalBroadcastManager.getInstance(this)
        bManager?.registerReceiver(
            ConnectionReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        noInternetDialog()
    }

    private fun noInternetDialog(){
        NoInternetDialogPendulum.Builder(
            this,
            lifecycle
        ).apply {
            dialogProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                    }
                }

                cancelable = false // Optional
                noInternetConnectionTitle = "No Internet" // Optional
                noInternetConnectionMessage =
                    "Check your Internet connection and try again." // Optional
                showInternetOnButtons = true // Optional
                pleaseTurnOnText = "Please turn on" // Optional
                wifiOnButtonText = "Wifi" // Optional
                mobileDataOnButtonText = "Mobile data" // Optional

                onAirplaneModeTitle = "No Internet" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
                pleaseTurnOffText = "Please turn off" // Optional
                airplaneModeOffButtonText = "Airplane mode" // Optional
                showAirplaneModeOffButtons = true // Optional
            }
        }.build()
    }

    override fun onNetworkChange(isConnected: Boolean) {
    }

    override fun onResume() {
        super.onResume()
        ConnectionReceiver.Listener = this
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(ConnectionReceiver())
    }

    open fun intentActivity(thisActivity: Context, cls:Class<*>,passingData:String) {
        val i = Intent(thisActivity, cls)
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if(passingData != ""){
            i.putExtra("pagename",passingData)
        }
        startActivity(i)
        overridePendingTransition(
            R.anim.slide_from_right,
            R.anim.slide_to_left
        )
    }

    open fun intentActivityWithFinish(thisActivity: Context, cls:Class<*>) {
        val i = Intent(thisActivity, cls)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)

        overridePendingTransition(
            R.anim.slide_from_right,
            R.anim.slide_to_left
        )
    }

    open fun checkEmail(m: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(m).matches()
    }

    open class PhoneTextFormatter(private val mEditText: EditText, private val mPattern: String) : TextWatcher {
        private val tag = this.javaClass.simpleName
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            val phone = StringBuilder(s)
            if (count > 0 && !isValid(phone.toString())) {
                for (i in 0 until phone.length) {
                    val c = mPattern[i]
                    if (c != '#' && c != phone[i]) {
                        phone.insert(i, c)
                    }
                }
                mEditText.setText(phone)
                mEditText.setSelection(mEditText.text.length)
            }
        }

        override fun afterTextChanged(s: Editable) {
        }

        private fun isValid(phone: String): Boolean {
            for (i in 0 until phone.length) {
                val c = mPattern[i]
                if (c == '#') continue
                if (c != phone[i]) {
                    return false
                }
            }
            return true
        }

        init {
            val maxLength = mPattern.length
            mEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
    }

    fun CalenderDialog(thisActivity: Context,dateTv:EditText): DatePickerDialog {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(thisActivity,R.style.MyDatePickerStyle,DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
            dateTv.setText("$mdayOfMonth/${mmonth+1}/$myear")
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
        return datePickerDialog
    }

    fun isTablet(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    fun toastMessageShow(msg:String){
        val toast = Toast.makeText(applicationContext,msg, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    open fun isWithinRange(testDate: Date,startDate: Date,endDate:Date): Boolean {
        if((testDate.before(startDate) || testDate.after(endDate))){
            return  false
        }else{
            return  true
        }
    }

    fun String.toDate(): Date{
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    }

    fun getDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        val newFormatter = SimpleDateFormat("yyyy-MM-dd")
        return newFormatter.format(calendar.time)
    }

}