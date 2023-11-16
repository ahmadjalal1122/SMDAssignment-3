package com.example.smdassignment3
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.example.smdassignment3.ui.theme.SMDAssignment3Theme
class MainActivity : ComponentActivity() {

    var isDisplay by mutableStateOf(false)
    var contactNumber by mutableStateOf("")
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            SMDAssignment3Theme{
                LaunchedEffect(true) {
//                    DisplayContactsFromDatabase()
                    Log.d("LaunchEffect","LaunchEffect.")
                }
                // on below line we are specifying background color for our application
                Surface(
                    // on below line we are specifying modifier and color for our app
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        // in scaffold we are specifying the top bar.
                        topBar = {
                            // inside top bar we are specifying background color.
                            TopAppBar(backgroundColor = Color.LightGray,
                                // along with that we are specifying
                                // title for our top bar.
                                title = {
                                    // in the top bar we are specifying
                                    // tile as a text
                                    Text(
                                        // on below line we are specifying
                                        // text to display in top app bar.
                                        text = "CONTACT APP",

                                        // on below line we are specifying
                                        // modifier to fill max width.
                                        modifier = Modifier.fillMaxWidth(),

                                        // on below line we are specifying
                                        // text alignment.
                                        textAlign = TextAlign.Center,

                                        // on below line we are specifying
                                        // color for our text.
                                        color = Color.White
                                    )
                                })
                        }) {

                        // on below line we are calling connection information
                        // method to display UI
//                        contactPicker(
//                            context = LocalContext.current
//                        )
                        contactPicker(context = applicationContext)
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setContent {
            SMDAssignment3Theme{

                // on below line we are specifying background color for our application
                Surface(
                    // on below line we are specifying modifier and color for our app
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
//                    Scaffold(
                        // in scaffold we are specifying the top bar.
//                        topBar = {
//                            // inside top bar we are specifying background color.
//                            TopAppBar(backgroundColor = Color.LightGray,
//                                // along with that we are specifying
//                                // title for our top bar.
//                                title = {
//                                    // in the top bar we are specifying
//                                    // tile as a text
//                                    Text(
//                                        // on below line we are specifying
//                                        // text to display in top app bar.
//                                        text = "CONTACT APP",
//
//                                        // on below line we are specifying
//                                        // modifier to fill max width.
//                                        modifier = Modifier.fillMaxWidth(),
//
//                                        // on below line we are specifying
//                                        // text alignment.
//                                        textAlign = TextAlign.Center,
//
//                                        // on below line we are specifying
//                                        // color for our text.
//                                        color = Color.White
//                                    )
//                                })
//                        }) {
                    Log.d("onres","onres.")

                        contactPicker(context = applicationContext)
//                    DisplayContactsFromDatabase()
//                    }
                }
            }
        }
    }

    // on below line we are calling on activity result method.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // on below line we are checking if result code is ok or not.
        if (resultCode != Activity.RESULT_OK) return

        // on below line we are checking if data is not null.
        if (requestCode === 1 && data != null) {
            // on below line we are getting contact data
            val contactData: Uri? = data.data

            // on below line we are creating a cursor
            val cursor: Cursor = managedQuery(contactData, null, null, null, null)

            // on below line we are moving cursor.
            cursor.moveToFirst()

            // on below line we are getting our
            // number and name from cursor
            val number: String =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val name: String =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

            // on the below line we are setting values.
//            contactName = name
//            contactNumber = number
        }

    }
}

//@Composable
//fun contactPicker(
//    context: Context,
//    contactName: String,
//    contactNumber: String,
//) {
//    // on below line we are creating variable for activity.
//    val activity = LocalContext.current as Activity
//
//    // on below line we are creating a column,
//    Column(
//        // on below line we are adding a modifier to it,
//        modifier = Modifier
//            .fillMaxSize()
//            // on below line we are adding a padding.
//            .padding(all = 30.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//    ) {
//        // on below line we are adding a text for heading.
//        Text(
//            // on below line we are specifying text
//            text = "Contact in Android",
//            // on below line we are specifying
//            // text color, font size and font weight
//            color = Color.Green, fontSize = 20.sp, fontWeight = FontWeight.Bold
//        )
//
//        // on below line adding a spacer
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // on below line creating a text for contact name
//        Text(text = contactName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//
//        // on below line adding a spacer
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // on below line creating a text for contact number.
//        Text(text = contactNumber, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//
//        // on below line adding a spacer
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // on below line creating a button to pick contact.
//        Button(
//            // on below line adding on click for button.
//            onClick = {
//                // on below line checking if permission is granted.
//                if (hasContactPermission(context)) {
//                    // if permission granted open intent to pick contact/
//                    val intent = Intent(Intent.ACTION_GET_CONTENT)
//                    intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
//                    startActivityForResult(activity, intent, 1, null)
//                } else {
//                    // if permission not granted requesting permission .
//                    requestContactPermission(context, activity)
//                }
//            },
//            // adding padding to button.
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        ) {
//            // displaying text in our button.
//            Text(text = "Pick Contact")
//
//        }
//    }
//}
@Composable
fun contactPicker(
    context: Context,
) {
    // on below line we are creating variable for activity.
    val dbHelper = ContactsDbHelper(context)
    val activity = LocalContext.current as Activity
    var contacts: List<Contact>
    var isDisplay by remember {mutableStateOf(false)}
    // on below line we are creating a column,
    Column(
        // on below line we are adding a modifier to it,
        modifier = Modifier
            .fillMaxSize()
            // on below line we are adding a padding.
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // on below line we are adding a text for heading.

        Button(
            // on below line adding on click for button.
            onClick = {
                // on below line checking if permission is granted.
                if (hasContactPermission(context)) {
                    isDisplay=true

                } else {
                    // if permission not granted requesting permission .
                    requestContactPermission(context, activity)
                }
            },
            // adding padding to button.
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // displaying text in our button.
            Text(text = "Import Contacts")
        }
        Text(
            // on below line we are specifying text
            text = "Contacts Fetched",
            // on below line we are specifying
            // text color, font size and font weight
            color = Color.LightGray, fontSize = 20.sp, fontWeight = FontWeight.Bold
        )
        // on below line adding a spacer
        Spacer(modifier = Modifier.height(20.dp))
        DisplayContactsFromDatabase()

        if(isDisplay){
            contacts = getAllContacts(context)
            Log.d("contacts","$contacts")

            // Store contacts in the database
            with(dbHelper.writableDatabase) {
                contacts.forEach { contact ->
                    insertContact(contact, this)
                }
            }
            DisplayContactsFromDatabase()

//            DisplayContacts(contacts)
        }
    }
}


private fun getAllContacts(context: Context): List<Contact> {
    val contacts = mutableListOf<Contact>()

    // Using ContentResolver to query contacts
    val cursor: Cursor? = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    cursor?.use {
        val displayNameColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val phoneNumberColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            // Check if the column index is valid before retrieving data
            val displayName = if (displayNameColumnIndex != -1) {
                it.getString(displayNameColumnIndex)
            } else {
                null
            }

            val phoneNumber = if (phoneNumberColumnIndex != -1) {
                it.getString(phoneNumberColumnIndex)
            } else {
                null
            }

            // Adding contact to the list if both display name and phone number are not null
            if (displayName != null && phoneNumber != null) {
                contacts.add(Contact(displayName, phoneNumber))
            }
        }
    }

    return contacts
}


// Data class to represent a contact
data class Contact(val displayName: String, val phoneNumber: String)

fun hasContactPermission(context: Context): Boolean {
    // on below line checking if permission is present or not.
    return ContextCompat.checkSelfPermission(context,Manifest.permission.READ_CONTACTS) ==
// return ContextCompat.checkSelfPermission(context,READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED;
}

fun requestContactPermission(context: Context, activity: Activity) {
    // on below line if permission is not granted requesting permissions.
    if (!hasContactPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), 1)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SMDAssignment3Theme {
//        MyApp()
    }
}