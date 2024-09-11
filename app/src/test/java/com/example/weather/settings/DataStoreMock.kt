package com.example.weather.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DataStoreMock<T>(initialValue: T) : DataStore<T> {

    var stubDataFlowResponse = MutableStateFlow(initialValue)

    override val data: Flow<T>
        get() = stubDataFlowResponse

    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        val newData = transform(stubDataFlowResponse.value)
        stubDataFlowResponse.value = newData
        return newData
    }
}