package jyotti.apexing.apexing_android.ui.activity.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.base.BaseViewModel
import jyotti.apexing.apexing_android.data.repository.SplashRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModelV2 @Inject constructor(
    private val repository: SplashRepository
) : BaseViewModel, ViewModel() {

}