package com.example.smdassignment3

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Contract class for defining table structure and queries
object ContactsContract {
    object ContactEntry : BaseColumns {
        const val TABLE_NAME = "CONTACTS"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_PHONE_NUMBER = "phoneNumber"
        const val COLUMN_NAME_IMAGE = "image"
    }
}

// SQLiteOpenHelper for managing database creation and version management.
class ContactsDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contacts.db"
        const val DATABASE_VERSION = 1
    }

    // SQL statement to create the contacts table
    private val SQL_CREATE_ENTRIES = """
    CREATE TABLE ${ContactsContract.ContactEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${ContactsContract.ContactEntry.COLUMN_NAME_NAME} TEXT,
        ${ContactsContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER} TEXT,
        ${ContactsContract.ContactEntry.COLUMN_NAME_IMAGE} BLOB
    )
""".trimIndent()


    // SQL statement to delete the contacts table
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ContactsContract.ContactEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    // Add this function to your ContactsDbHelper
    fun readContacts(): List<ContactHolder> {
        val contacts = mutableListOf<ContactHolder>()

        val db = readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            ContactsContract.ContactEntry.COLUMN_NAME_NAME,
            ContactsContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER,
        )

        val cursor = db.query(
            ContactsContract.ContactEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val conID = it.getInt(it.getColumnIndexOrThrow(BaseColumns._ID))
//                Log.d("conIDinDBhelper::","$conID")
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.ContactEntry.COLUMN_NAME_NAME))
                val phoneNumber = it.getString(it.getColumnIndexOrThrow(ContactsContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER))

                // Adding contact to the list
                contacts.add(ContactHolder(conID,name, phoneNumber))
            }
        }

        return contacts
    }
}
fun insertContact(contact: Contact, db: SQLiteDatabase, imageByteArray: ByteArray? = null) {
    // Check if the phone number already exists in the database
    if (!isPhoneNumberExists(db, contact.phoneNumber)) {
        val values = ContentValues().apply {
            put(ContactsContract.ContactEntry.COLUMN_NAME_NAME, contact.displayName)
            put(ContactsContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER, contact.phoneNumber)
            // Uncomment the line below if you want to store image data
            // put(ContactsContract.ContactEntry.COLUMN_NAME_IMAGE, imageByteArray)
        }

        db.insert(ContactsContract.ContactEntry.TABLE_NAME, null, values)
    } else {
        // Handle the case where the phone number already exists (e.g., show a message)
        // You can customize this part based on your requirements
        Log.d("ContactExists", "Contact with phone number ${contact.phoneNumber} already exists.")
    }
}

private fun isPhoneNumberExists(db: SQLiteDatabase, phoneNumber: String): Boolean {
    val projection = arrayOf(BaseColumns._ID)
    val selection = "${ContactsContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER} = ?"
    val selectionArgs = arrayOf(phoneNumber)

    val cursor = db.query(
        ContactsContract.ContactEntry.TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        null
    )

    val phoneNumberExists = cursor.count > 0
    cursor.close()

    return phoneNumberExists
}




// In your Composable function
@Composable
fun DisplayContactsFromDatabase() {
    val dbHelper = ContactsDbHelper(LocalContext.current)
    val contacts = dbHelper.readContacts()
    Log.d("Contacts:::","$contacts")
    DisplayContacts(contacts)
}

//@Composable
//fun ContactItem(contact: ContactHolder) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Image on the left
//        Image(
//            painter = painterResource(id = R.drawable.contacticon), // Replace with your image resource
//            contentDescription = "Contact Image",
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .background(Color.Transparent)
//        )
//
//        // Spacer to create some space between the image and text
//        Spacer(modifier = Modifier.width(16.dp))
//
//        // Column for name and phone number
//        Column {
//            // Display name
//            androidx.compose.material.Text(text = contact.displayName, fontWeight = FontWeight.Bold)
//
//            // Display phone number below the name
//            androidx.compose.material.Text(text = contact.phoneNumber)
//        }
//    }
//}
@Composable
fun ContactItem(contact: ContactHolder, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image on the left
        Image(
            painter = painterResource(id = R.drawable.contacticon), // Replace with your image resource
            contentDescription = "Contact Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
        )

        // Spacer to create some space between the image and text
        Spacer(modifier = Modifier.width(16.dp))

        // Column for name and phone number
        Column {
            // Display name
            androidx.compose.material.Text(text = contact.displayName, fontWeight = FontWeight.Bold)

            // Display phone number below the name
            androidx.compose.material.Text(text = contact.phoneNumber)
        }
    }
}

//@Composable
//fun DisplayContacts(contacts: List<ContactHolder>) {
//    LazyColumn {
//        items(contacts) { contact ->
//            // Display each contact using the ContactItem composable
//            ContactItem(contact = contact)
//            Divider() // Optional: Add a divider between contacts
//        }
//    }
//}
@Composable
fun DisplayContacts(contacts: List<ContactHolder>) {
    val context = LocalContext.current

    LazyColumn {
        items(contacts) { contact ->
            ContactItem(contact = contact) {
                // Create an Intent to navigate to the detail screen
                val intent = Intent(context, ContactDetailActivity::class.java).apply {
                    putExtra("conID", contact.contactId)
                    putExtra("displayName", contact.displayName)
                    putExtra("phoneNumber", contact.phoneNumber)
                }
                context.startActivity(intent)
            }
            Divider() // Optional: Add a divider between contacts
        }
    }
}


@Composable
fun DeleteContact(phoneNumber: String) {
    val dbHelper = ContactsDbHelper(LocalContext.current)

    val db = dbHelper.writableDatabase

    val whereClause = "${ContactsContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER} = ?"
    val whereArgs = arrayOf(phoneNumber)

    // Delete the contact from the database
    db.delete(
        ContactsContract.ContactEntry.TABLE_NAME,
        whereClause,
        whereArgs
    )

    // Close the database
    db.close()
}
@Composable
fun updateContact(conID: Int, newName: String, newPhoneNumber: String) {
    val dbHelper = ContactsDbHelper(LocalContext.current)

    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put(ContactsContract.ContactEntry.COLUMN_NAME_NAME, newName)
        put(ContactsContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER, newPhoneNumber)
    }

    val selection = "${BaseColumns._ID} = ?"
    val selectionArgs = arrayOf(conID.toString())
    Log.d("updateContact","$conID : $newName : $newPhoneNumber")
    db.update(
        ContactsContract.ContactEntry.TABLE_NAME,
        values,
        selection,
        selectionArgs
    )
}
data class ContactHolder(val contactId:Int,val displayName: String, val phoneNumber: String)