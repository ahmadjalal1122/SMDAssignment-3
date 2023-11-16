package com.example.smdassignment3

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smdassignment3.ui.theme.SMDAssignment3Theme
class ContactDetailActivity : ComponentActivity() {
    var isDel by mutableStateOf(false)
    var isUpdate by mutableStateOf(false)

    var conID by  mutableStateOf(0)
    var Name by  mutableStateOf("")
    var PhoneNumber by  mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SMDAssignment3Theme{
                // on below line we are specifying background color for our application
                Surface(
                    // on below line we are specifying modifier and color for our app
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    var conID = intent.getIntExtra("conID",0)
                    Log.d("CONID","$conID")
                    val displayName = intent.getStringExtra("displayName")
                    val phoneNumber = intent.getStringExtra("phoneNumber")

                    val contact = Contact(displayName = displayName ?: "", phoneNumber = phoneNumber ?: "")

                    ContactDetailUI(contact = contact, onDeleteClick = {
                        // Handle contact deletion from the database
//                        DeleteContact(phoneNumber = contact.phoneNumber)
                        isDel=true
                        // After deletion, navigate back to MainActivity
                        // For now, let's just finish the current activity
                        finish()
                    },

                        onUpdateClick = { updatedName, updatedPhoneNumber ->
                            /* handle update logic with updatedName and updatedPhoneNumber */
                        isUpdate=true
                        Name=updatedName
                        PhoneNumber=updatedPhoneNumber
                        conID=conID

                        })
                    if(isDel)
                    {
                        DeleteContact(phoneNumber = contact.phoneNumber)
                    }
                    if(isUpdate)
                    {
                        updateContact(conID,Name,PhoneNumber)
                    }
                }
            }
        }

    }
}
@Composable
fun ContactDetailUI(
    contact: Contact,
    onDeleteClick: () -> Unit,
    onUpdateClick: (String,String) -> Unit
) {
    val context = LocalContext.current

    // State variables to track edited name and phone number
    var editedName by remember { mutableStateOf(contact.displayName) }
    var editedPhoneNumber by remember { mutableStateOf(contact.phoneNumber) }

    // State variable to track whether the contact is being edited
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Row for Call and Email icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // IconButton for call
                IconButton(
                    onClick = {
                        // Open device dialer with the selected contact's phone number
                        val dialIntent = Intent(Intent.ACTION_DIAL)
                        dialIntent.data = Uri.parse("tel:${contact.phoneNumber}")
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Contact",
                        tint = Color.Green
                    )
                }

                // IconButton for email
                IconButton(
                    onClick = {
                        val messageIntent = Intent(Intent.ACTION_SENDTO)
                        messageIntent.data = Uri.parse("smsto:${contact.phoneNumber}")
                        context.startActivity(messageIntent)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Contact",
                        tint = Color.Green
                    )
                }
            }

            // Delete button at top right corner
            IconButton(
                onClick = { onDeleteClick() },
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Contact",
                    tint = Color.Red
                )
            }
        }

        // Display a dummy image (replace with your logic to load an actual image)
        // Here, I'm using a placeholder image resource
        Image(
            painter = painterResource(id = R.drawable.contacticon),
            contentDescription = "Contact Image",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // Editable text field for name
        OutlinedTextField(
            value = editedName,
            onValueChange = {
                editedName = it
                // Set isEditing to true when name is edited
                isEditing = true
            },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Editable text field for phone number
        OutlinedTextField(
            value = editedPhoneNumber,
            onValueChange = {
                editedPhoneNumber = it
                // Set isEditing to true when phone number is edited
                isEditing = true
            },
            label = { Text("Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Button to edit contact
        Button(
            onClick = {
                // Perform edit action here (e.g., update the contact in the database)
                // Reset isEditing after editing is complete
                onUpdateClick(editedName,editedPhoneNumber)
                isEditing = false
            },
            enabled = isEditing, // Enable button only when editing is in progress
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Edit Contact")
        }

    }
}

