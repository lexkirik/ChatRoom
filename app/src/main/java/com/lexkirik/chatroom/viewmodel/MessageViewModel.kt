package com.lexkirik.chatroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.lexkirik.chatroom.data.Message
import com.lexkirik.chatroom.data.MessageRepository
import com.lexkirik.chatroom.data.Result.Error
import com.lexkirik.chatroom.data.Result.Success
import com.lexkirik.chatroom.data.User
import com.lexkirik.chatroom.data.UserRepository
import com.lexkirik.chatroom.Injection
import kotlinx.coroutines.launch

class MessageViewModel: ViewModel() {
    private val messageRepository: MessageRepository
    private val userRepository: UserRepository

    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(FirebaseAuth.getInstance(), Injection.instance())
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomID = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Success -> _currentUser.value = result.data
                is Error -> {

                }
            }
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            if (_roomID != null) {
                messageRepository.getChatMessages(_roomID.value.toString())
                    .collect { _messages.value = it }
            }
        }
    }

    fun sendMessage(text: String) {
        if (_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderID = _currentUser.value!!.email,
                text = text
            )

            viewModelScope.launch {
                when (messageRepository.sendMessage(_roomID.value.toString(), message)) {
                    is Success -> Unit
                    is Error -> {

                    }
                }
            }
        }
    }

    fun setRoomID(roomID: String) {
        _roomID.value = roomID
        loadMessages()
    }
}