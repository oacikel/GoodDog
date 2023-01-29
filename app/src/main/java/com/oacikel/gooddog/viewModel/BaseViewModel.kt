package com.oacikel.gooddog.viewModel

import androidx.lifecycle.ViewModel
import com.oacikel.gooddog.util.SingleLiveEvent
import com.oacikel.gooddog.util.Status
import com.oacikel.gooddog.util.default

open class BaseViewModel : ViewModel() {
    internal open var status = SingleLiveEvent<Status>()
    internal open var errorMessage = SingleLiveEvent<String>().default(
        "An unexpected error has occurred please try again later"
    )
}