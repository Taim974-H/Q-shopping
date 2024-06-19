package com.team3.qshopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.qshopping.data.local.models.Address
import com.team3.qshopping.data.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {

    fun addAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            AddressRepository.insert(address)
        }
    }

}